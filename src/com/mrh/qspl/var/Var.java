package com.mrh.qspl.var;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.types.TUndefined;

public class Var {
	private ValueType value;
	private boolean constant = false;
	
	public Var(ValueType v) {
		this.value = v;
	}
	
	public Var() {
		this.value = TUndefined.getInstance();
	}
	
	public Var(ValueType v, boolean constant) {
		this.value = v;
		this.constant = constant;
	}
	
	public void set(ValueType v) {
		if(!constant || this.value instanceof TUndefined)
			this.value = v;
		else
			System.err.println("Tried to set constant value");
	}
	
	public ValueType get() {
		return value;
	}
}
