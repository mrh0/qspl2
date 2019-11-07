package com.mrh.qspl.val.types;

import java.util.ArrayList;

import com.mrh.qspl.syntax.parser.Block;
import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.var.Var;
import com.mrh.qspl.vm.Scope;
import com.mrh.qspl.vm.VM;

public class TUserFunc extends TFunc{
	private Block ref;
	private String[] paramaterList;
	
	public TUserFunc(Block st, String[] pars) {
		ref = st;
		paramaterList = pars;
	}
	
	@Override
	public ValueType execute(ArrayList<ValueType> args, VM vm) {
		Scope scope = vm.getCurrentScope();
		for(int i = 0; i < paramaterList.length; i++) {
			scope.setVariable(paramaterList[i], new Var((i < args.size())?args.get(i):TUndefined.getInstance()));
		}
		return null;
	}
	
	public Block getRefBlock() {
		return ref;
	}
}
