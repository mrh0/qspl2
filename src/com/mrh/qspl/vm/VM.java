package com.mrh.qspl.vm;

import java.util.Stack;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.func.Common;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.var.Var;

public class VM {
	protected Stack<Scope> scopeStack;
	protected Scope rootScope;
	public VM() {
		scopeStack = new Stack<Scope>();
		rootScope = createNewScope("root");
		Common.defineCommons(rootScope);
	}
	
	public Scope getCurrentScope() {
		return scopeStack.peek();
	}
	
	public Scope createNewScope(String name) {
		scopeStack.push(new Scope(name));
		return getCurrentScope();
	}
	
	public void popScope() {
		scopeStack.pop();
	}
	
	protected Var getVar(String name) {
		Var v;
		for(Scope s : scopeStack) {
			v = s.getVariable(name);
			if(v != null) 
				return v;
		}
		//System.out.println(name + " is Undefined");
		v = new Var(TUndefined.getInstance(), name.toUpperCase().equals(name));
		scopeStack.peek().setVariable(name, v);
		return v;
	}
	
	public ValueType getValue(String name) {
		return getVar(name).get();
	}
	
	public void setValue(String name, ValueType v) {
		getVar(name).set(v);
	}
}
