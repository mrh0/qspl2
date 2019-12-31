package com.mrh.qspl.vm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.mrh.qspl.debug.Debug;
import com.mrh.qspl.io.console.Console;
import com.mrh.qspl.io.files.FileIO;
import com.mrh.qspl.io.files.Include;
import com.mrh.qspl.syntax.parser.Block;
import com.mrh.qspl.syntax.parser.Statement;
import com.mrh.qspl.syntax.parser.StatementEndType;
import com.mrh.qspl.syntax.tokenizer.Token;
import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.syntax.tokenizer.Tokens;
import com.mrh.qspl.syntax.tokenizer.Tokens.TokenType;
import com.mrh.qspl.val.Value;
import com.mrh.qspl.val.types.TArray;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.val.types.TNumber;
import com.mrh.qspl.val.types.TObject;
import com.mrh.qspl.val.types.TString;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.val.types.TUserFunc;
import com.mrh.qspl.val.types.Types;
import com.mrh.qspl.var.Var;
import com.mrh.qspl.vm.stacks.BracketItem;
import com.mrh.qspl.vm.stacks.BracketStack;
import com.mrh.qspl.vm.stacks.ValStack;

/**
 * Expression Evaluator.
 * @author MRH0
 */

public class ExpressionEvaluator {
	
	private VM vm;
	private Tokenizer tokens;
	private boolean breakCalled;
	private boolean continueCalled;
	private boolean inCalled;
	private boolean ofCalled;
	protected Stack<Boolean> exitCalledStack;
	
	public ExpressionEvaluator(VM vm, Tokenizer tokens) {
		this.vm = vm;
		this.tokens = tokens;
		breakCalled  = false;
		exitCalledStack = new Stack<Boolean>();
		inCalled = false;
		ofCalled = false;
		continueCalled = false;
	}
	
	public void eval() {
		this.vm.evalBlock(this.tokens.getRootBlock());
	}
	
	private StatementResult evalStatement(Statement statement, Value previousResult) {
		Stack<String> ops;
		ValStack vals;
		Stack<Var> vars;
		Stack<TokenType> types;
		BracketStack brackets;
		ops = new Stack<String>();
		vals = new ValStack(this);
		vars = new Stack<Var>();
		types = new Stack<TokenType>();
		brackets = new BracketStack();
		Token prev = null;
		Token beforeprev = null;
		
		boolean thisCalledExit = false;
		
		boolean outCalled = false;
		boolean errorCalled = false;
		boolean includeCalled = false;
		boolean includeFromCalled = false;
		boolean exportCalled = false;
		boolean deleteCalled = false;

		boolean onceFunc = false;
		
		ArrayList<String> funcArgNames = new ArrayList<String>();
		if(Debug.enabled())
			System.out.println("ln:"+statement.getLine());
		
		Console.g.setCurrentLine(statement.getLine());
		try {
		for(Token token : statement.getTokens()) {
			String s = token.getToken();
			TokenType t = token.getType();
			if(s.equals("[")) {
				Value vt = (Tokens.isNewSymbol(prev.getToken())?null:vals.pop(vars));
				brackets.push(new BracketItem('[', vt, beforeprev!= null && beforeprev.getToken().equals("#")));
			}
			else if(s.equals("{")) {
				Value vt = (Tokens.isNewSymbol(prev.getToken())?null:vals.pop(vars));
				brackets.push(new BracketItem('{', vt, beforeprev!= null && prev.getToken().equals("#")) );
				vm.createNewScope("object", true);
			}
			else if(s.equals(",")) {
				if(!brackets.isEmpty())
					brackets.peek().add(vals.pop(vars));
			}
			else if(s.equals("}")) {
				Scope oScope = vm.popScope();
				if(!brackets.isEmpty()) {
					BracketItem bi = brackets.pop();
					if(!prev.getToken().equals("{"))
						bi.add(vals.pop(vars));
					vals.push(new TObject(oScope.getAllValues(), oScope.getKeyOrder()));
				}
			}
			else if(s.equals("]")) {
				if(!brackets.isEmpty()) {
					BracketItem bi = brackets.pop();
					if(!prev.getToken().equals("[")) 
						bi.add(vals.pop(vars));
					if(bi.getPrev() == null) {
						vals.push(new TArray(bi.getParameters()));
					}
					else {
						Value prevp = bi.getPrev();
						if(prevp.getType() == Types.FUNC) {
							Value _this = bi.isSubOp()?vals.pop(vars):TUndefined.getInstance();
							Value vt = ((TFunc) prevp).execute(bi.getParameters(), vm, _this);
							vals.push(vt);
							if(vt == null && Debug.enabled())
								System.out.println("RESULT NULL");
							if(Debug.enabled())
								System.out.println("EXEC:" + prevp.toString() + ":" + _this + " par:" + bi.getParameters() + " ret:" + vals.peek()+" sub: " + bi.isSubOp());
						}
						else {
							//vals.push(prevp); //Used as this value if function needs it. Will increase stack (KEEP IN MIND)
							Value vt = prevp.accessor(bi.getParameters().toArray(new Value[0]));
							vals.push(vt);
						}
					}
				}
			}
			else if(t == TokenType.operator) {
				ops.push(s);
				if(!ops.isEmpty()) {
					String op = ops.pop();
					Value v = vals.pop(vars);
					if (op.equals("+")) {
						v = vals.pop(vars).add(v);
					}
					else if (op.equals("-")) {
						v = vals.pop(vars).sub(v);
					}
					else if (op.equals("*")) 
						v = vals.pop(vars).multi(v);
					else if (op.equals("/")) 
						v = vals.pop(vars).div(v);
					else if (op.equals("%")) 
						v = vals.pop(vars).mod(v);
					else if (op.equals("==")) 
						v = new TNumber(vals.pop(vars).equals(v)?1:0);
					else if (op.equals("!=")) 
						v = new TNumber(vals.pop(vars).equals(v)?0:1);
					else if (op.equals("<"))
						v = new TNumber(vals.pop(vars).compare(v)==-1?1:0);
					else if (op.equals(">")) 
						v = new TNumber(vals.pop(vars).compare(v)==1?1:0);
					else if (op.equals("<=")) 
						v = new TNumber(vals.pop(vars).compare(v)<=0?1:0);
					else if (op.equals(">=")) 
						v = new TNumber(vals.pop(vars).compare(v)>=0?1:0);
					else if (op.equals("!")) 
						v = new TNumber(v.bool()?0:1);
					else if (op.equals("&&")) 
						v = new TNumber(v.bool() && vals.pop(vars).bool()?1:0);
					else if (op.equals("||")) 
						v = new TNumber(v.bool() || vals.pop(vars).bool()?1:0);
					else if (op.equals("&"))
						v = new TNumber(vals.pop(vars).intValue() & v.intValue());
					else if (op.equals("|"))
						v = new TNumber(vals.pop(vars).intValue() | v.intValue());
					else if (op.equals("^")) 
						v = new TNumber(vals.pop(vars).intValue() ^ v.intValue());
					else if (op.equals("?")) //Contains
						v = new TNumber(vals.pop(vars).contains(v)?1:0);
					else if (op.equals("is")) //is type
						v = new TNumber((v.getType() == vals.pop(vars).getType())?1:0);
					else if (op.equals("as")) //as type
						v = vals.pop(vars).toType(v.getType());
					else if (op.equals("--"))
						v = TNumber.from(v).decrement(1);
					else if (op.equals("++"))
						v = TNumber.from(v).increment(1);
					else if (op.equals("=")) {
						Var k = vars.peek();
						vm.setValue(k.getName(), v);
						vals.pop(vars);
						v = k.get();
					}
					else if (op.equals("+=")) {
						Var k = vars.peek();
						vm.setValue(k.getName(), vm.getValue(k.getName()).add(v));
						vals.pop(vars);
						v = k.get();
					}
					else if (op.equals("-=")) {
						Var k = vars.peek();
						vm.setValue(k.getName(), vm.getValue(k.getName()).sub(v));
						vals.pop(vars);
						v = k.get();
					}
					else if (op.equals("*=")) {
						Var k = vars.peek();
						vm.setValue(k.getName(), vm.getValue(k.getName()).multi(v));
						vals.pop(vars);
						v = k.get();
					}
					else if (op.equals("/=")) {
						Var k = vars.peek();
						vm.setValue(k.getName(), vm.getValue(k.getName()).div(v));
						vals.pop(vars);
						v = k.get();
					}
					else if (op.equals("%=")) {
						Var k = vars.peek();
						vm.setValue(k.getName(), vm.getValue(k.getName()).mod(v));
						vals.pop(vars);
						v = k.get();
					}
					if(v != null)
						vals.push(v);
				}
			}
			else if(t.equals(TokenType.literal))
				vals.push(new TNumber(Double.parseDouble(s)));
			else if(t.equals(TokenType.string))
				vals.push(new TString(s));
			else if(t.equals(TokenType.keyword)) {
				if(s.equals("out")) {
					outCalled = true;
				}
				else if(s.equals("error")) {
					errorCalled = true;
				}
				else if(s.equals("import")) {
					if(includeFromCalled) {  
						TString from = TString.from(vals.pop(vars));
						TObject toInclude = TObject.from(vals.pop(vars));
						//System.out.println("INCLUDE: " + toInclude + " FROM: " + from);
						TObject included = Include.fromFile(from.get());
						for(String key : toInclude.getMap().keySet()) {
							if(key.equals("ALL")) {
								for(String akey : included.getKeys()) {
									vm.setValue(akey, included.get(akey));
								}
								break;
							}
							Value v = included.get(key);
							if(v == null || v == TUndefined.getInstance())
								Console.g.err("Undefined import '" + key + "' from " + from);
							vm.setValue(key, v);
						}
					}
					else {
						TObject obj = TObject.from(vals.pop(vars));
						System.out.println("INCLUDE: " + obj + " FROM: INTERNAL");
					}
					includeFromCalled = false;
					Console.g.setScope(vm.getCurrentScope()); // return to origin scope;
				}
				else if(s.equals("export")) {
					exportCalled = true;
				}
				else if(s.equals("from")) {
					includeFromCalled = true;
				}
				else if(s.equals("delete")) {
					deleteCalled = true;
				}
				else if(s.equals("func")) {
					TObject o = TObject.from(vals.pop(vars));
					vals.push(new TUserFunc(statement.getNext(), o, o.getSpecialOrder()));//funcArgNames.toArray(new String[0]))
					onceFunc = true;
				}
				else if(s.equals("exit")) {
					exitCalledStack.push(true);
					thisCalledExit = true;
				}
				else if(s.equals("prev")) {
					vals.push(previousResult);
				}
				else if(s.equals("else")) {
					vals.push(new TNumber(previousResult.bool()?0:1));
				}
				else if(s.equals("break")) {
					breakCalled = true;
					return new StatementResult(false, vals, vars, null);
				}
				else if(s.equals("continue")) {
					continueCalled = true;
					return new StatementResult(false, vals, vars, null);
				}
				else if(s.equals("in")) {
					inCalled = true;
				}
				else if(s.equals("of")) {
					ofCalled = true;
				}
			}
			else if(t.equals(TokenType.identifier)) {
				Var k = vm.getVar(s);
				if(Debug.enabled())
					System.out.println("Pushed var " + s + ":" + k);
				vars.push(k);
				vals.push(k.get(), k);
			}
			else
				Console.g.err("Unexpected token: '" + s + "'");
			types.push(t);
			beforeprev = prev;
			prev = token;
		}
		if(outCalled) {
			if(!vals.isEmpty())
				Console.g.log(vals.peek());
		}
		if(errorCalled) {
			if(!vals.isEmpty())
				Console.g.err(vals.peek());
		}
		if(exportCalled) {
			if(!vars.isEmpty())
				vm.getCurrentScope().export(vars.peek());
			else
				Console.g.err("Attempting export on non variable.");
		}
		if(deleteCalled) {
			if(!vars.isEmpty())
				vm.deleteVar(vars.peek());
			else
				Console.g.err("Attempting delete on non variable.");
		}
		}
		catch(Exception e) {
			Console.g.err(e.getMessage());
			e.printStackTrace(); //Debug Info
		}

		boolean pass = statement.getEndType() != StatementEndType.END && !vals.isEmpty() && vals.peek().bool() && !onceFunc;//do sub statement?
		return new StatementResult(pass, vals, vars, thisCalledExit?vals.peek():null);
	}

	protected Value walkThrough(Block b) {
		Value retv = null;
		Iterator iter = null;
		StatementResult sr = new StatementResult(false, null, null, TUndefined.getInstance());
		Value previousResult = TUndefined.getInstance();
		ArrayList<Statement> a = b.getAll();
		for(int i = 0; i < a.size(); i++) {
			Statement s = a.get(i);
			sr = evalStatement(s, previousResult);
			if(sr.ret != null)
				retv = sr.ret;
			previousResult = sr.vals.peek();
			boolean didIter = false;
			if(iter == null) {
				if(inCalled) { //Values
					inCalled = false;
					Value p = sr.vals.pop(sr.vars);
					if(p.getType() == Types.ARRAY)
						iter = TArray.from(p).getAll().iterator();
					if(p.getType() == Types.OBJECT)
						iter = TObject.from(p).getMap().values().iterator();
				}
				if(ofCalled) { //Keys
					ofCalled = false;
					Value p = sr.vals.pop(sr.vars);
					if(p.getType() == Types.ARRAY)
						iter = TArray.from(p).getAll().iterator(); //fix should be index
					if(p.getType() == Types.OBJECT)
						iter = TObject.from(p).keyIterator();
				}
				if(sr.pass && s.hasNext()) { // if iter != null else if(...)
					Value y = walkThrough(s.getNext());
					if(y != null)
						retv = y;
				}
			}
			else { //if iter != null
				while(iter.hasNext() && !breakCalled) {
					vm.setValue(s.getTokens()[0].getToken(), (Value)iter.next());
					Value y = walkThrough(s.getNext());
					if(y != null)
						retv = y;
				}
				breakCalled = false;
				iter = null;
				didIter = true;
			}
			
			if(Debug.enabled() && exitCalledStack.peek() != null)
				System.out.println("EXIT CALLED: " + sr.vals.peek());
			if(s.getEndType() == StatementEndType.WHILE && sr.pass && !(exitCalledStack.peek() != null) && !didIter) {
				if(!breakCalled)
					i--;
				breakCalled = false;
			}
			if(continueCalled || breakCalled || exitCalledStack.peek() != null)
				continueCalled = false;
		}
		return retv;
	}
}
