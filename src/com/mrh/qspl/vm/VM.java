package com.mrh.qspl.vm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import com.mrh.qspl.internal.common.Common;
import com.mrh.qspl.syntax.parser.Block;
import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.val.Value;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.val.types.TUserFunc;
import com.mrh.qspl.var.Var;

public class VM {
	protected Stack<Scope> scopeStack;
	protected Scope rootScope;
	
	private ExpressionEvaluator ev;
	
	public VM(Tokenizer t) {
		Common.initPrototypes();
		ev = new ExpressionEvaluator(this, t);
		scopeStack = new Stack<Scope>();
		rootScope = createNewScope("root");
		rootScope.setVariable("this", new Var("this", TUndefined.getInstance()));
		Common.defineCommons(rootScope);
	}
	
	public Value evalBlock(Block b) {
		ev.exitCalledStack.push(null);
		Value r = ev.walkThrough(b);
		ev.exitCalledStack.pop();
		return r;//ev.vals.peek();
	}
	
	public Scope getCurrentScope() {
		return scopeStack.peek();
	}
	
	public Scope createNewScope(String name) {
		scopeStack.push(new Scope(name));
		return getCurrentScope();
	}
	
	public Scope createNewScope(String name, boolean lock) {
		scopeStack.push(new Scope(name, lock));
		return getCurrentScope();
	}
	
	public Scope popScope() {
		return scopeStack.pop();
	}
	
	public Value executeFunction(TFunc func, ArrayList<Value> args, Value _this) {
		this.createNewScope("func:"+func);
		Value rv = ((TFunc) func).execute(args, this, _this);
		/*if(rv != null)
			ev.vals.push(rv);
		else {
			ev.walkThrough(((TUserFunc) func).getRefBlock());
			ev.exitCalled = false;
		}*/
		this.popScope();
		return rv;//ev.vals.pop();
	}
	
	private Var getVar(String name, boolean checkLock) {
		Var v;
		ListIterator<Scope> it = scopeStack.listIterator(scopeStack.size());
		while(it.hasPrevious()) {
			Scope s = it.previous();
			v = s.getVariable(name);
			if(s.isLocked() && checkLock)
				break;
			if(s.isLocked())
				continue;
			
			if(v != null) 
				return v;
		}
		v = new Var(name, TUndefined.getInstance(), name.toUpperCase().equals(name));
		getCurrentScope().setVariable(name, v);
		return v;
	}
	
	protected Var getVar(String name) {
		return getVar(name, false);
	}
	
	public Value getValue(String name) {
		return getVar(name).get();
	}
	
	public void setValue(String name, Value v) {
		getVar(name, true).set(v);
	}
	
	public void eval() {
		ev.eval();
	}
}
