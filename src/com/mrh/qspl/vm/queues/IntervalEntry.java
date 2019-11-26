package com.mrh.qspl.vm.queues;

import java.util.ArrayList;

import com.mrh.qspl.val.Value;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.vm.VM;

public class IntervalEntry extends TimeoutEntry{
	long interval = 0;
	public IntervalEntry(TFunc f, long when, ArrayList<Value> args) {
		super(f, when + System.nanoTime(), args);
		interval = when;
	}
	
	@Override
	public Value execute(VM vm) {
		Value r = this.f.execute(getArgs(), vm, this.getThis());
		this.when = interval + System.nanoTime();
		return r;
	}
	
	@Override
	public boolean cancelAfterReady() {
		return false; //TODO: Cancel after (arg) number of runs.
	}
}
