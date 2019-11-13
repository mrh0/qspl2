package com.mrh.qspl.val.types;

import com.mrh.qspl.val.ValueType;

public class TString implements ValueType<String>, Comparable<ValueType>{

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
		return "'"+get()+"'";
	}

	@Override
	public ValueType add(ValueType v) {
		if(v instanceof TNumber) 
			return new TString(s+v.get());
		if(v instanceof TString) 
			return new TString(s+v.get());
		return new TString(s+v.toString());
	}

	@Override
	public ValueType sub(ValueType v) {
		if(v instanceof TNumber) 
			return new TString(s.replaceAll((String)v.get(), ""));
		if(v instanceof TString) 
			return new TString(s.replaceAll((String)v.get(), ""));
		if(v instanceof TArray) {
			String r = s;
			TArray a = (TArray) v;
			for(ValueType k : a.getAll()) {
				r = r.replaceAll((String)k.toString(), "");
			}
			return new TString(r);
		}
		return TUndefined.getInstance();
	}

	@Override
	public ValueType multi(ValueType v) {
		return TUndefined.getInstance();
	}

	@Override
	public ValueType div(ValueType v) {
		if(v instanceof TString) {
			String[] k = s.split((String)v.get());
			ValueType[] j = new ValueType[k.length];
			for(int i = 0; i < k.length; i++)
				j[i] = new TString(k[i]);
			return new TArray(j);
		}
		return TUndefined.getInstance();
	}

	@Override
	public ValueType mod(ValueType v) {
		return TUndefined.getInstance();
	}

	@Override
	public ValueType pow(ValueType v) {
		return TUndefined.getInstance();
	}

	@Override
	public ValueType root() {
		return TUndefined.getInstance();
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
		if(v instanceof TString)
			return s.indexOf((String)v.get())!=-1;
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
		if(v.length == 0)
			return new TNumber(getSize());
		if(v.length == 1)
			return new TString(s.charAt((int)Math.round((double)v[0].get()))+"");
		if(v.length >= 2)
			return new TString(s.substring((int)Math.round((double)v[0].get()), (int)Math.round((double)v[1].get()+1))+"");
		return null;
	}

	@Override
	public int getSize() {
		return s.length();
	}
	
	@Override
	public ValueType toType(int type) {
		if(type == Types.STRING)
			return this;
		if(type == Types.NUMBER)
			return new TNumber(Double.parseDouble(s));
		return TUndefined.getInstance();
	}

	@Override
	public int intValue() {
		return -1;
	}

	@Override
	public int compareTo(ValueType o) {
		return compare(o);
	}
	
	public static TString from(ValueType v) {
		if(v instanceof TString)
			return (TString)v;
		System.err.println(v + " is not a string.");
		return null;
	}
}
