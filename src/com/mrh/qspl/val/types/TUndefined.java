package com.mrh.qspl.val.types;

import com.mrh.qspl.val.ValueType;

public class TUndefined implements ValueType<Object>{
	
	protected static TUndefined instance = null;
	
	protected TUndefined() {
		instance = this;
	}
	
	public static TUndefined getInstance() {
		if(instance == null)
			new TUndefined();
		return instance;
	}
	
	@Override
	public String toString() {
		return "UNDEFINED";
	}

	@Override
	public ValueType add(ValueType v) {
		return v;
	}

	@Override
	public ValueType sub(ValueType v) {
		return v;
	}

	@Override
	public ValueType multi(ValueType v) {
		return new TNumber(0);
	}

	@Override
	public ValueType div(ValueType v) {
		return new TNumber(0);
	}

	@Override
	public ValueType mod(ValueType v) {
		return new TNumber(0);
	}

	@Override
	public ValueType pow(ValueType v) {
		return new TNumber(0);
	}

	@Override
	public ValueType root() {
		return getInstance();
	}

	@Override
	public boolean bool() {
		return false;
	}

	@Override
	public boolean equals(ValueType v) {
		return false;
	}

	@Override
	public int compare(ValueType v) {
		return 0;
	}

	@Override
	public Object get() {
		return getInstance();
	}

	@Override
	public ValueType duplicate() {
		return new TUndefined();
	}

	@Override
	public boolean contains(ValueType v) {
		return false;
	}

	@Override
	public ValueType childObject(ValueType v) {
		return getInstance();
	}

	@Override
	public ValueType[] childObjects(ValueType v) {
		return new ValueType[0];
	}

	@Override
	public int getType() {
		return Types.UNDEFINED;
	}

	@Override
	public boolean isUndefined() {
		return true;
	}

	@Override
	public ValueType accessor(ValueType[] v) {
		return getInstance();
	}
}