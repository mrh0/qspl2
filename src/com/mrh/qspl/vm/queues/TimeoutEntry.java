package com.mrh.qspl.vm.queues;

import java.util.ArrayList;

import com.mrh.qspl.val.Value;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.vm.VM;

public class TimeoutEntry extends CallbackEntry{
	private TFunc f;
	private long when;
	private ArrayList<Value> args;
	
	public TimeoutEntry(TFunc f, long when, ArrayList<Value> args) {
		this.f = f;
		this.when = when;
		this.args = args;
	}
	
	@Override
	public boolean isReady() {
		return System.nanoTime() > when;
	}

	@Override
	public Value execute(VM vm) {
		return this.f.execute(getArgs(), vm, this.getThis());
	}

	@Override
	public Value getThis() {
		return TUndefined.getInstance();
	}

	@Override
	public ArrayList<Value> getArgs() {
		return this.args;
	}

	@Override
	public TFunc getCallback() {
		return this.f;
	}
}
