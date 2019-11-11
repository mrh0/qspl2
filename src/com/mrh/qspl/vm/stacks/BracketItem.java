package com.mrh.qspl.vm.stacks;

import java.util.ArrayList;
import com.mrh.qspl.val.ValueType;

public class BracketItem {
	private ValueType prev;
	private char opener;
	private ArrayList<ValueType> values;
	
	public BracketItem(char opener, ValueType prev) {
		this.prev = prev;
		this.opener = opener;
		this.values = new ArrayList<ValueType>();
	}
	
	public ArrayList<ValueType> getParameters() {
		return this.values;
	}
	
	public void add(ValueType v) {
		values.add(v);
	}
	
	public ValueType getPrev() {
		return this.prev;
	}
	
	public char getOpener() {
		return opener;
	}
	
	@Override
	public String toString() {
		return "BracketItem:"+opener+":"+prev+":"+values.size();
	}
}
