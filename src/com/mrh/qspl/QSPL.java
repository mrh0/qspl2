package com.mrh.qspl;

import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.vm.ExpressionEvaluator;
import com.mrh.qspl.vm.VM;

/**
 * QSPL - API
 * @author MRH0
 */

public class QSPL {
	private Tokenizer tokens;
	private VM vm;
	
	public QSPL() {
		tokens = new Tokenizer();
		vm = new VM(tokens);
	}
	
	public QSPL insertCode(String c) {
		tokens.toTokens(c);
		return this;
	}
	
	public QSPL clearCode() {
		tokens = new Tokenizer();
		return this;
	}
	
	public void setGlobalVariable(String name, ValueType value) {
		vm.setValue(name, value);
	}
	
	public ValueType getGlobalVariable(String name) {
		return vm.getValue(name);
	}
	
	public boolean execute() {
		vm.eval();
		return true;
	}
}
