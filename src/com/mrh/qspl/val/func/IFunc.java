package com.mrh.qspl.val.func;

import java.util.ArrayList;

import com.mrh.qspl.val.Value;
import com.mrh.qspl.vm.VM;

public interface IFunc {
	public Value execute(ArrayList<Value> args, VM vm, Value pThis);
}
