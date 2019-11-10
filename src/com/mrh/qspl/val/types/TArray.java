package com.mrh.qspl.val.types;

import java.util.ArrayList;
import java.util.List;

import com.mrh.qspl.val.ValueType;

public class TArray implements ValueType, Comparable<ValueType>{
	private ArrayList<ValueType> values;
	
	public TArray() {
		values = new ArrayList<ValueType>();
	}
	
	public TArray(ArrayList<ValueType> v) {
		values = v;
	}
	
	public TArray(ValueType[] v) {
		values = new ArrayList<ValueType>();
		for(ValueType k : v)
			values.add(k);
	}
	
	public TArray(String[] v) {
		values = new ArrayList<ValueType>();
		for(String k : v)
			values.add(new TString(k));
	}
	
	public TArray(List<ValueType> v) {
		values = new ArrayList<ValueType>();
		for(ValueType k : v)
			values.add(k);
	}
	
	public ValueType find(ValueType v) {
		for(ValueType k : values)
			if(k.equals(v))
				return k;
		return TUndefined.getInstance();
	}

	@Override
	public ValueType add(ValueType v) {
		if(v.getType() == Types.ARRAY)
			values.addAll(((TArray)v).getAll());
		else
			values.add(v);
		return this;
	}

	@Override
	public ValueType sub(ValueType v) {
		ValueType vt = find(v);
		if(!vt.isUndefined())
			values.remove(vt);
		return this;
	}

	@Override
	public ValueType multi(ValueType v) {
		for(int i = 0; i < getSize(); i++) {
			values.set(i, values.get(i).multi(v));
		}
		return this;
	}

	@Override
	public ValueType div(ValueType v) {
		int index  = (int)Math.round((double)v.get());
		if(index < getSize() && index >= 0)
			values.remove(index);
		return this;
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
		return getSize() > 0;
	}

	@Override
	public boolean equals(ValueType v) {
		if(!(v instanceof TArray))
			return false;
		if(getSize() != v.getSize())
			return false;
		TArray va = (TArray)v;
		for(int i = 0; i < getSize(); i++) {
			if(!va.getIndex(i).equals(getIndex(i)))
				return false;
		}
		return true;
	}

	@Override
	public int compare(ValueType v) {
		return 0;
	}

	@Override
	public boolean contains(ValueType v) {
		for(int i = 0; i < getSize(); i++) {
			if(v.equals(getIndex(i)))
				return true;
		}
		return false;
	}

	@Override
	public ValueType childObject(ValueType v) {
		return null;
	}

	@Override
	public ValueType[] childObjects(ValueType v) {
		return null;
	}

	@Override
	public int getType() {
		return Types.ARRAY;
	}

	@Override
	public Object get() {
		return this;
	}

	@Override
	public ValueType duplicate() {
		return new TArray(values);
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
			return values.get((int)Math.round((double)v[0].get()));
		if(v.length == 2) {
			return new TArray(values.subList((int)Math.round((double)v[0].get()), (int)Math.round((double)v[1].get()+1)));
		}
		return TUndefined.getInstance();
	}
	
	@Override
	public String toString() {
		String r = "[";
		for(int i = 0; i < values.size(); i++) {
			r += values.get(i);
			if(i+1 < values.size())
				r += ",";
		}
		return r+"]";
	}

	@Override
	public int getSize() {
		return values.size();
	}
	
	public ValueType getIndex(int i) {
		return values.get(i);
	}
	
	public ValueType setIndex(int i, ValueType v) {
		values.set(i, v);
		return this;
	}
	
	public ArrayList<ValueType> getAll(){
		return values;
	}
	
	public double sum() {
		double d = 0;
		for(ValueType vt : values) {
			if(vt.getType() == Types.ARRAY)
				d += ((TArray)vt).sum();
			if(vt.getType() == Types.NUMBER)
				d += ((TNumber)vt).get();
		}
		return d;
	}

	@Override
	public ValueType toType(int type) {
		if(type == Types.ARRAY)
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
	
	public static TArray merge(ValueType v1, ValueType v2) {
		TArray a = new TArray();
		a.add(v1);
		a.getAll().add(v2);
		System.out.println("new array" + a.getAll());
		return a;
	}
	
	public static TArray from(ValueType v) {
		if(v instanceof TArray)
			return (TArray)v;
		System.err.println(v + " is not an array.");
		return null;
	}
}
