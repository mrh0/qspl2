package com.mrh.qspl.vm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.func.Common;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.val.types.TUserFunc;
import com.mrh.qspl.var.Var;

public class VM {
	protected Stack<Scope> scopeStack;
	protected Scope rootScope;
	
	ExpressionEvaluator ev;
	
	public VM(Tokenizer t) {
		ev = new ExpressionEvaluator(this, t);
		scopeStack = new Stack<Scope>();
		rootScope = createNewScope("root");
		rootScope.setVariable("this", new Var("this", TUndefined.getInstance()));
		Common.defineCommons(rootScope);
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
	
	public ValueType executeFunction(TFunc func, ArrayList<ValueType> args, ValueType _this) {
		this.createNewScope("func:"+func);
		ValueType rv = ((TFunc) func).execute(args, this, _this);
		if(rv != null)
			ev.vals.push(rv);
		else {
			ev.walkThrough(((TUserFunc) func).getRefBlock());
			ev.exitCalled = false;
		}
		this.popScope();
		return ev.vals.pop();
	}
	
	private Var getVar(String name, boolean checkLock) {
		Var v;
		ListIterator<Scope> it = scopeStack.listIterator(scopeStack.size());
		while(it.hasPrevious()) {
			Scope s = it.previous();
			v = s.getVariable(name);
			if(s.isLocked() && checkLock) {
				System.out.println(name+" LOCKED");
				break;
			}
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
	
	public ValueType getValue(String name) {
		return getVar(name).get();
	}
	
	public void setValue(String name, ValueType v) {
		getVar(name, true).set(v);
	}
	
	public void eval() {
		ev.eval();
	}
}
