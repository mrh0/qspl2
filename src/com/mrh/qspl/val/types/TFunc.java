package com.mrh.qspl.val.types;

import java.util.ArrayList;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.vm.Scope;
import com.mrh.qspl.vm.VM;

public class TFunc implements ValueType{

	public ValueType execute(ArrayList<ValueType> args, VM vm) {
		return new TNumber(25);//new TNumber((double)args.get(0).get()*(double)args.get(1).get());
	}
	
	@Override
	public ValueType add(ValueType v) {
		return null;
	}

	@Override
	public ValueType sub(ValueType v) {
		return null;
	}

	@Override
	public ValueType multi(ValueType v) {
		return null;
	}

	@Override
	public ValueType div(ValueType v) {
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
		// TODO Auto-generated method stub
		return null;
	}
}
