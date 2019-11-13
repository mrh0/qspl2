package com.mrh.qspl.val.types;

import java.util.HashMap;
import java.util.Map;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.var.Var;
import com.mrh.qspl.vm.Scope;

public class TObject implements ValueType{
	
	private Map<String, ValueType> map;
	private static Map<String, ValueType> prototype = new HashMap<String, ValueType>();
	
	public TObject() {
		map = new HashMap<>();
	}
	
	public TObject(Map<String, ValueType> m) {
		map = m;
	}

	@Override
	public ValueType add(ValueType v) {
		return TUndefined.getInstance();
	}

	@Override
	public ValueType sub(ValueType v) {
		return TUndefined.getInstance();
	}

	@Override
	public ValueType multi(ValueType v) {
		return TUndefined.getInstance();
	}

	@Override
	public ValueType div(ValueType v) {
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
		return !map.isEmpty();
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
	public boolean contains(ValueType v) {
		for(ValueType vv : map.values()) {
			if(v.equals(vv))
				return true;
		}
		return false;
	}

	@Override
	public ValueType childObject(ValueType v) {
		return map.getOrDefault(TString.from(v).get(), TUndefined.getInstance());
	}

	@Override
	public ValueType[] childObjects(ValueType v) {
		return map.values().toArray(new ValueType[0]);
	}
	
	public static Map<String, ValueType> getPrototype(){
		return prototype;
	}
	
	public static void addPrototype(Var v){
		prototype.put(v.getName(), v.get());
	}

	@Override
	public ValueType accessor(ValueType[] v) {
		if(v.length == 0)
			return new TNumber(map.size());
		if(v.length == 1) {
			String key = TString.from(v[0]).get();
			return map.getOrDefault(key, getPrototype().getOrDefault(key, TUndefined.getInstance()));
		}
		TObject o = new TObject();
		for(ValueType vv : v) {
			String k = TString.from(vv).get();
			o.set(k, map.getOrDefault(k, TUndefined.getInstance()));
		}
		return o;
	}

	@Override
	public int getType() {
		return Types.OBJECT;
	}

	@Override
	public int getSize() {
		return map.size();
	}

	@Override
	public ValueType toType(int type) {
		return TUndefined.getInstance();//convert into json if string
	}

	@Override
	public Object get() {
		return map;
	}

	@Override
	public ValueType duplicate() {
		return TUndefined.getInstance();
	}

	@Override
	public boolean isUndefined() {
		return false;
	}

	@Override
	public int intValue() {
		return 0;
	}

	public static TObject from(ValueType v) {
		if(v instanceof TObject)
			return (TObject)v;
		System.err.println(v + " is not an object.");
		return null;
	}
	
	public TObject set(String key, ValueType v) {
		map.put(key, v);
		return this;
	}
	
	public ValueType get(String key) {
		map.getOrDefault(key, TUndefined.getInstance());
		return this;
	}
	
	public String[] getKeys() {
		return map.keySet().toArray(new String[0]);
	}
	
	public ValueType[] getValues() {
		return map.values().toArray(new ValueType[0]);
	}
	
	public Map<String, ValueType> getMap() {
		return map;
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
}
