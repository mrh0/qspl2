package com.mrh.qspl;

import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.vm.ExpressionEvaluator;
import com.mrh.qspl.vm.VM;

public class QSPL {
	private Tokenizer tokens;
	private VM vm;
	
	public QSPL() {
		tokens = new Tokenizer();
		vm = new VM();
	}
	
	public QSPL insertCode(String c) {
		tokens.toTokens(c);
		return this;
	}
	
	public QSPL clearCode() {
		tokens = new Tokenizer();
		return this;
	}
	
	public boolean execute() {
		ExpressionEvaluator ev = new ExpressionEvaluator(vm, tokens);
		ev.eval();
		return true;
	}
}
