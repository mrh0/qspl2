package com.mrh.qspl;

import java.util.ArrayList;

import com.mrh.qspl.debug.Debug;
import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.val.Value;
import com.mrh.qspl.val.types.TFunc;
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
		if(Debug.enabled())
			System.out.println(getTokensString());
		return this;
	}
	
	public String getTokensString() {
		return tokens.toString();
	}
	
	public Value executeFunction(TFunc func, ArrayList<Value> args, Value _this) {
		return vm.executeFunction(func, args, _this);
	}
	
	public QSPL clearCode() {
		tokens = new Tokenizer();
		return this;
	}
	
	public void setGlobalVariable(String name, Value value) {
		vm.setValue(name, value);
	}
	
	public Value getGlobalVariable(String name) {
		return vm.getValue(name);
	}
	
	public boolean execute() {
		vm.eval();
		return true;
	}
}
