package com.mrh.qspl.val;

public interface ValueType<T> extends Arithmetic {
	public T get();
	public ValueType duplicate();
	public boolean isUndefined();
	public int intValue();
}
