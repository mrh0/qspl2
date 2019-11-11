package com.mrh.qspl.val.types;

import java.util.ArrayList;

import com.mrh.qspl.syntax.parser.Block;
import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.var.Var;
import com.mrh.qspl.vm.Scope;
import com.mrh.qspl.vm.VM;

public class TUserFunc extends TFunc{
	private Block ref;
	
	public TUserFunc(Block st, String[] pars) {
		ref = st;
		paramaterList = pars;
	}
	
	@Override
	public String toString() {
		String r = "";
		for(int i = 0; i < paramaterList.length; i++) {
			r += paramaterList[i];
			if(i+1 < paramaterList.length)
				r += ",";
		}
		return "user:func("+r+")";
	}
	
	@Override
	public ValueType execute(ArrayList<ValueType> args, VM vm, ValueType pThis) {
		Scope scope = vm.getCurrentScope();
		scope.setVariable("this", new Var("this", pThis, true));
		for(int i = 0; i < paramaterList.length; i++) {
			scope.setVariable(paramaterList[i], new Var(paramaterList[i], (i < args.size())?args.get(i):TUndefined.getInstance(), false));
		}
		return null;
	}
	
	public Block getRefBlock() {
		return ref;
	}
}
