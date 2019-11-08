package com.mrh.qspl.val;

public interface Arithmetic {
	public ValueType add(ValueType v);
	
	public ValueType sub(ValueType v);
	
	public ValueType multi(ValueType v);
	
	public ValueType div(ValueType v);
	
	public ValueType mod(ValueType v);
	
	public ValueType pow(ValueType v);
	
	public ValueType root();
	
	public boolean bool();
	
	public boolean equals(ValueType v);
	
	public int compare(ValueType v);
	
	public boolean contains(ValueType v);
	
	public ValueType childObject(ValueType v);
	
	public ValueType[] childObjects(ValueType v);
	
	public ValueType accessor(ValueType[] v);
	
	public int getType();
	
	public int getSize();
}
