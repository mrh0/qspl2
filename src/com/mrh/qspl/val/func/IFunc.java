package com.mrh.qspl.val.func;

import java.util.ArrayList;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.vm.VM;

public interface IFunc {
	public ValueType execute(ArrayList<ValueType> args, VM vm);
}
