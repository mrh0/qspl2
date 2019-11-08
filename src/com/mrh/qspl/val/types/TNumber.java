package com.mrh.qspl.val.types;

import com.mrh.qspl.val.ValueType;

public class TNumber implements ValueType<Double>{//, Arithmetic<TString, TString>

	final double value;
	
	public TNumber(double v) {
		value = v;
		//System.out.println("TN:"+v);
	}
	
	public TNumber(TNumber v) {
		value = v.get();
		//System.out.println("TN:"+v.get());
	}
	
	public TNumber(ValueType v) {
		value = (double) v.get();
	}
	
	@Override
	public String toString() {
		return value+"";
	}

	@Override
	public Double get() {
		return value;
	}
	
	@Override
	public ValueType add(ValueType v) {
		if(v instanceof TNumber) 
			return new TNumber(value + (double)v.get());
		if(v instanceof TString) 
			return new TString(value+((String)v.get()));
		return null;
	}

	@Override
	public ValueType sub(ValueType v) {
		if(v instanceof TNumber) 
			return new TNumber(value - (double)v.get());
		if(v instanceof TString)
			System.err.println("Cannot subtract string from number");
		return null;
	}

	@Override
	public ValueType multi(ValueType v) {
		if(v instanceof TNumber) 
			return new TNumber(value * (double)v.get());
		if(v instanceof TString)
			System.err.println("Cannot multiply string with a number");
		return null;
	}

	@Override
	public ValueType div(ValueType v) {
		if(v instanceof TNumber) 
			return new TNumber(value / (double)v.get());
		if(v instanceof TString)
			System.err.println("Cannot multiply string with a number");
		return null;
	}

	@Override
	public ValueType mod(ValueType v) {
		if(v instanceof TNumber) 
			return new TNumber(value % (double)v.get());
		if(v instanceof TString)
			System.err.println("Cannot multiply string with a number");
		return null;
	}

	@Override
	public ValueType pow(ValueType v) {
		if(v instanceof TNumber) 
			return new TNumber(Math.pow(value, (double)v.get()));
		if(v instanceof TString)
			System.err.println("Cannot multiply string with a number");
		return null;
	}

	@Override
	public ValueType root() {
		return new TNumber(Math.sqrt(value));
	}

	@Override
	public boolean bool() {
		return value > 0;
	}

	@Override
	public boolean equals(ValueType v) {
		return v.get().equals(value);
	}
	
	@Override
	public int compare(ValueType v) {
		return (((Double)value).compareTo((Double)v.get()));
	}

	@Override
	public ValueType duplicate() {
		return new TNumber(value);
	}

	@Override
	public boolean contains(ValueType v) {
		return false;
	}

	@Override
	public ValueType childObject(ValueType v) {
		return TUndefined.getInstance();
	}

	@Override
	public ValueType[] childObjects(ValueType v) {
		return new ValueType[0];
	}

	@Override
	public int getType() {
		return Types.NUMBER;
	}
	
	@Override
	public boolean isUndefined() {
		return false;
	}

	@Override
	public ValueType accessor(ValueType[] v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		return 0;
	}
}
