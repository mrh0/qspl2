package com.mrh.qspl.val.types;

import java.util.ArrayList;

import com.mrh.qspl.val.ValueType;

public class TArray implements ValueType{
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
	
	public ValueType find(ValueType v) {
		for(ValueType k : values)
			if(k.equals(v))
				return k;
		return TUndefined.getInstance();
	}

	@Override
	public ValueType add(ValueType v) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueType div(ValueType v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueType mod(ValueType v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueType pow(ValueType v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueType root() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean bool() {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueType[] childObjects(ValueType v) {
		// TODO Auto-generated method stub
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
		return null;
	}

	@Override
	public boolean isUndefined() {
		return false;
	}

	@Override
	public ValueType accessor(ValueType[] v) {
		if(v.length == 0)
			return new TNumber(getSize());
		return values.get((int)Math.round((double)v[0].get()));
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
}
