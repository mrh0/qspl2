package com.mrh.qspl.var;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.types.TUndefined;

public class Var {
	private ValueType value;
	private String name = "";
	private boolean constant = false;
	
	public Var(String name, ValueType v) {
		this.value = v;
		this.name = name;
	}
	
	public Var(String name) {
		this.name = name;
		this.value = TUndefined.getInstance();
	}
	
	public Var(String name, ValueType v, boolean constant) {
		this.value = v;
		this.constant = constant;
		this.name = name;
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
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "("+name+":"+value.toString()+")";
	}
}
