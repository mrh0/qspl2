package com.mrh.qspl.vm.stacks;

import java.util.Stack;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.var.Var;
import com.mrh.qspl.vm.ExpressionEvaluator;

public class ValStack{
	private class ValVarItem{
		public ValVarItem(ValueType val, Var var) {
			this.val = val;
			this.var = var;
		}
		public ValueType val;
		public Var var;
	}
	private Stack<ValVarItem> vals;
	private ExpressionEvaluator ee;
	public ValStack(ExpressionEvaluator ee) {
		this.vals = new Stack<ValVarItem>();
		this.ee = ee;
	}
	
	public void push(ValueType item) {
		System.out.println("PUSH:  " + item);
		this.vals.push(new ValVarItem(item, null));
	}
	
	public void push(ValueType item, Var var) {
		System.out.println("PUSH: VAR " + item);
		this.vals.push(new ValVarItem(item, var));
	}
	
	public boolean isEmpty() {
		return this.vals.isEmpty();
	}
	
	public int size() {
		return this.vals.size();
	}
	
	public ValueType pop() {
		if(isEmpty()) {
			System.out.println("POP: Empty UNDEF");
			return TUndefined.getInstance();
		}
		if(this.vals.peek().var != null) {
			ee.popVar();
		}
		System.out.println("POP: " + this.vals.peek().val);
		return this.vals.pop().val;
	}
	
	public ValueType peek() {
		if(isEmpty())
			return null;
		return vals.peek().val;
	}
}