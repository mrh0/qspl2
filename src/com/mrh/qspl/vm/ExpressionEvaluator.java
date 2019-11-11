package com.mrh.qspl.vm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.mrh.qspl.debug.Debug;
import com.mrh.qspl.syntax.parser.Block;
import com.mrh.qspl.syntax.parser.Statement;
import com.mrh.qspl.syntax.parser.StatementEndType;
import com.mrh.qspl.syntax.tokenizer.Token;
import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.syntax.tokenizer.Tokens;
import com.mrh.qspl.syntax.tokenizer.Tokens.TokenType;
import com.mrh.qspl.val.ValueType;
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
	private Stack<String> ops;
	protected ValStack vals;
	private Stack<Var> vars;
	private Stack<TokenType> types;
	private BracketStack brackets;
	private VM vm;
	private Tokenizer tokens;
	private boolean breakCalled;
	private boolean continueCalled;
	private boolean inCalled;
	protected boolean exitCalled;
	
	public ExpressionEvaluator(VM vm, Tokenizer tokens) {
		this.vm = vm;
		this.tokens = tokens;
		breakCalled  = false;
		exitCalled = false;
		inCalled = false;
		continueCalled = false;
	}
	
	public void eval() {
		walkThrough(this.tokens.getRootBlock());
		if(!Debug.noResult) {
			if(vals != null && !vals.isEmpty() && vals.peek() != null) {
				System.out.println("[RESULT:"+vals.size()+"]: "+vals.pop().get());
			}
			else
				System.err.println("Finished with empty stack.");
		}
	}
	
	public void popVar() {
		vars.pop();
	}
	
	private boolean evalStatement(Statement statement, boolean oneliner, ValueType previousResult) {
		ops = new Stack<String>();
		vals = new ValStack(this);
		vars = new Stack<Var>();
		types = new Stack<TokenType>();
		brackets = new BracketStack();
		Token prev = null;
		
		boolean outCalled = false;
		boolean funcDefine = false;
		boolean onceFunc = false;
		
		ArrayList<String> funcArgNames = new ArrayList<String>();
		
		//vals.push(new TNumber(0));
		for(Token token : statement.getTokens()) {
			String s = token.getToken();
			TokenType t = token.getType();
			
			if(Debug.enabled())
				System.out.println("T: " + s);
			
			if(funcDefine) {
				if(t == TokenType.identifier) {
					funcArgNames.add(s);
					continue;
				}
				if(s.equals(","))
					continue;
				if(t != TokenType.identifier) {
					funcDefine = false;
					vals.push(new TUserFunc(statement.getNext(), funcArgNames.toArray(new String[0])));
				}
			}
			if(s.equals("[")) {
				ValueType vt = (Tokens.isNewSymbol(prev.getToken())?null:vals.pop());
				brackets.push(new BracketItem('[', vt));
			}
			else if(s.equals("{")) {
				ValueType vt = (Tokens.isNewSymbol(prev.getToken())?null:vals.pop());
				brackets.push(new BracketItem('{', vt) );
				vm.createNewScope("object", true);
			}
			else if(s.equals(",")) {
				if(!brackets.isEmpty())
					brackets.peek().add(vals.pop());
			}
			else if(s.equals("}")) {
				Scope oScope = vm.popScope();
				if(!brackets.isEmpty()) {
					BracketItem bi = brackets.pop();
					if(!prev.getToken().equals("}")) 
						bi.add(vals.pop());
					vals.push(new TObject(oScope.getAllValues()));
					
				}
			}
			else if(s.equals("]")) {
				if(!brackets.isEmpty()) {
					BracketItem bi = brackets.pop();
					if(!prev.getToken().equals("[")) 
						bi.add(vals.pop());
					if(bi.getPrev() == null) {
						vals.push(new TArray(bi.getParameters()));
					}
					else {
						ValueType prevp = bi.getPrev();
						if(prevp.getType() == Types.FUNC) {
							ValueType _this = vals.isEmpty()?TUndefined.getInstance():vals.pop();
							ValueType vt = ((TFunc) prevp).execute(bi.getParameters(), vm, _this);
							vals.push(vt);
							if(Debug.enabled())
								System.out.println("EXEC:" + _this + " par:" + bi.getParameters() + " ret:" + vals.peek());
						}
						else {
							ValueType vt = prevp.accessor(bi.getParameters().toArray(new ValueType[0]));
							vals.push(vt);
						}
					}
				}
			}
			else if(t == TokenType.operator) {
				ops.push(s);
				if(!ops.isEmpty()) {
					String op = ops.pop();
					ValueType v = vals.pop();
					/*if(!ops.isEmpty())
						System.out.println("OP: "+vals.peek().get()+" "+s+" "+v);
					else
						System.out.println("OP: "+s+" "+v);*/
					if (op.equals("+")) {
						v = vals.pop().add(v);
					}
					else if (op.equals("-")) {
						v = vals.pop().sub(v);
					}
					else if (op.equals("*")) 
						v = vals.pop().multi(v);
					else if (op.equals("/")) 
						v = vals.pop().div(v);
					else if (op.equals("%")) 
						v = vals.pop().mod(v);
					else if (op.equals("==")) 
						v = new TNumber(vals.pop().equals(v)?1:0);
					else if (op.equals("!=")) 
						v = new TNumber(vals.pop().equals(v)?0:1);
					else if (op.equals("<"))
						v = new TNumber(vals.pop().compare(v)==-1?1:0);
					else if (op.equals(">")) 
						v = new TNumber(vals.pop().compare(v)==1?1:0);
					else if (op.equals("<=")) 
						v = new TNumber(vals.pop().compare(v)<=0?1:0);
					else if (op.equals(">=")) 
						v = new TNumber(vals.pop().compare(v)>=0?1:0);
					else if (op.equals("!")) 
						v = new TNumber(v.bool()?0:1);
					else if (op.equals("&&")) 
						v = new TNumber(v.bool() && vals.pop().bool()?1:0);
					else if (op.equals("||")) 
						v = new TNumber(v.bool() || vals.pop().bool()?1:0);
					else if (op.equals("&"))
						v = new TNumber(v.intValue() & vals.pop().intValue());
					else if (op.equals("|"))
						v = new TNumber(v.intValue() | vals.pop().intValue());
					else if (op.equals("^")) 
						v = new TNumber(v.intValue() ^ vals.pop().intValue());
					else if (op.equals("?")) //Contains
						v = new TNumber(vals.pop().contains(v)?1:0);
					else if (op.equals("is")) //is type
						v = new TNumber((v.getType() == vals.pop().getType())?1:0);
					else if (op.equals("as")) //as type
						v = vals.pop().toType(v.getType());
					else if (op.equals("--"))
						v = TNumber.from(v).decriment(1);
					else if (op.equals("++"))
						v = TNumber.from(v).incriment(1);
					else if (op.equals("=")) {
						Var k = vars.peek();
						System.out.println("Set var: " + k + " to " + v);
						vm.setValue(k.getName(), v); //k.set(v);
						vals.pop();
						v = k.get();
					}
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
				else if(s.equals("func")) {
					funcDefine = true;
					onceFunc = true;
				}
				else if(s.equals("exit")) {
					exitCalled = true;
				}
				else if(s.equals("prev")) {
					vals.push(previousResult);
				}
				else if(s.equals("else")) {
					vals.push(new TNumber(previousResult.bool()?0:1));
				}
				else if(s.equals("break")) {
					breakCalled = true;
					return false;
				}
				else if(s.equals("continue")) {
					continueCalled = true;
					return false;
				}
				else if(s.equals("in")) {
					inCalled = true;
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
				System.err.println("Unexpected token: '" + s + "'");
			types.push(t);
			prev = token;
		}
		if(outCalled) {
			if(!vals.isEmpty())
				System.out.println("[OUT]: "+vals.peek());
		}
		return statement.getEndType() != StatementEndType.END && !vals.isEmpty() && vals.peek().bool() && !onceFunc; //sub statement
	}
	
	private boolean isFirst() {
		return vals.isEmpty() && vars.isEmpty() && ops.isEmpty();
	}

	protected void walkThrough(Block b) {
		
		Iterator iter = null;
		ValueType previousResult = TUndefined.getInstance();
		ArrayList<Statement> a = b.getAll();
		for(int i = 0; i < a.size(); i++) {
			Statement s = a.get(i);
			boolean ss = false;
			ss = evalStatement(s, false, previousResult);
			previousResult = vals.peek();
			boolean didIter = false;
			if(inCalled && iter == null) {
				inCalled = false;
				iter = TArray.from(vals.pop()).getAll().iterator();
			}
			if(iter != null) {
				while(iter.hasNext() && !breakCalled) {
					vm.setValue(s.getTokens()[0].getToken(), (ValueType)iter.next());
					walkThrough(s.getNext());
				}
				breakCalled = false;
				iter = null;
				didIter = true;
			}
			else if(ss && s.hasNext())
				walkThrough(s.getNext());
			
			if(s.getEndType() == StatementEndType.WHILE && ss && !exitCalled && !didIter) {
				if(!breakCalled)
					i--;
				breakCalled = false;
			}
			if(continueCalled || breakCalled || exitCalled) {
				continueCalled = false;
				return;
			}
		}
	}
}
