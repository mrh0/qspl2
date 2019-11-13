package com.mrh.qspl.val.types;

import com.mrh.qspl.val.ValueType;

public class TUndefined implements ValueType<Object>{
	
	private static TUndefined instance = null;
	
	private TUndefined() {
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
		return getInstance();
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

	@Override
	public int getSize() {
		return 0;
	}
	
	@Override
	public ValueType toType(int type) {
		if(type == Types.UNDEFINED)
			return this;
		if(type == Types.STRING)
			return new TString(this.toString());
		return TUndefined.getInstance();
	}

	@Override
	public int intValue() {
		return -1;
	}

	@Override
	public int compareTo(ValueType o) {
		return 0;
	}
}
