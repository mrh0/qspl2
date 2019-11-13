package com.mrh.qspl.val.types;

import java.util.ArrayList;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.func.IFunc;
import com.mrh.qspl.vm.Scope;
import com.mrh.qspl.vm.VM;

public class TFunc implements ValueType{
	
	protected String[] paramaterList = {};
	
	public String[] getParameters() {
		return paramaterList;
	}

	private final IFunc internal;
	
	public TFunc() {
		internal = null;
	}
	
	public TFunc(IFunc f) {
		internal = f;
	}
	
	public TFunc(IFunc f, String...p) {
		internal = f;
		paramaterList = p;
	}
	
	public ValueType execute(ArrayList<ValueType> args, VM vm, ValueType pThis) {
		if(internal != null)
			return internal.execute(args, vm, pThis);
		return TUndefined.getInstance();
	}
	
	@Override
	public String toString() {
		String r = "";
		for(int i = 0; i < paramaterList.length; i++) {
			r += paramaterList[i];
			if(i+1 < paramaterList.length)
				r += ",";
		}
		return "internal:func("+r+")";
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
		return true;
	}

	@Override
	public boolean equals(ValueType v) {
		return this == v;
	}

	@Override
	public int compare(ValueType v) {
		return 0;
	}

	@Override
	public Object get() {
		return this;
	}

	@Override
	public ValueType duplicate() {
		return new TFunc();
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
		return Types.FUNC;
	}

	@Override
	public boolean isUndefined() {
		return false;
	}

	@Override
	public ValueType accessor(ValueType[] v) {
		return null;
	}

	@Override
	public int getSize() {
		return 0;
	}
	
	@Override
	public ValueType toType(int type) {
		if(type == Types.FUNC)
			return this;
		if(type == Types.STRING)
			return new TString(this.toString());
		return TUndefined.getInstance();
	}

	@Override
	public int intValue() {
		return -1;
	}
	
	public static TFunc from(ValueType v) {
		if(v instanceof TFunc)
			return (TFunc)v;
		System.err.println(v + " is not a function.");
		return null;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
