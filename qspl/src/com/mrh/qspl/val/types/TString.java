package com.mrh.qspl.val.types;

import com.mrh.qspl.val.ValueType;

public class TString implements ValueType<String>{

	final String s;
	
	public TString(String v) {
		s = v;
	}
	
	public TString(TString v) {
		s = v.get();
	}
	
	@Override
	public String get() {
		return s;
	}
	
	@Override
	public String toString() {
		return get();//"'"+get()+"'";
	}

	@Override
	public ValueType add(ValueType v) {
		if(v instanceof TNumber) 
			return new TString(s+v.get());
		if(v instanceof TString) 
			return new TString(s+v.get());
		return null;
	}

	@Override
	public ValueType sub(ValueType v) {
		if(v instanceof TNumber) 
			return new TString(s.replace((String)v.get(), ""));
		if(v instanceof TString) 
			return new TString(s.replace((String)v.get(), ""));
		return null;
	}

	@Override
	public ValueType multi(ValueType v) {
		return null;
	}

	@Override
	public ValueType div(ValueType v) {
		if(v instanceof TString) 
			return new TString(s.replaceAll((String)v.get(), ""));
		return null;
	}

	@Override
	public ValueType mod(ValueType v) {
		return null;
	}

	@Override
	public ValueType pow(ValueType v) {
		return null;
	}

	@Override
	public ValueType root() {
		return null;
	}
	
	@Override
	public boolean bool() {
		return s.length() > 0;
	}

	@Override
	public boolean equals(ValueType v) {
		return v.get().equals(s);
	}

	@Override
	public int compare(ValueType v) {
		return ((String)v.get()).compareTo(s);
	}

	@Override
	public ValueType duplicate() {
		return new TString(s);
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
		return Types.STRING;
	}
	
	@Override
	public boolean isUndefined() {
		return false;
	}

	@Override
	public ValueType accessor(ValueType[] v) {
		if(v.length == 1)
			return new TString(s.charAt((int)Math.round((double)v[0].get()))+"");
		if(v.length >= 2)
			return new TString(s.substring((int)Math.round((double)v[0].get()), (int)Math.round((double)v[1].get()))+"");
		return null;
	}
}
