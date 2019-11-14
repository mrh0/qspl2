package com.mrh.qspl.val;

public interface Value<T> extends Arithmetic, Comparable<Value> {
	public T get();
	public Value duplicate();
	public boolean isUndefined();
	public int intValue();
}
