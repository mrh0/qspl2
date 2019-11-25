package com.mrh.qspl.vm.queues;

import java.util.ArrayList;

import com.mrh.qspl.val.Value;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.vm.VM;

public abstract class CallbackEntry implements QueueEntry{
	public abstract Value getThis();
	public abstract ArrayList<Value> getArgs();
	public abstract TFunc getCallback();
}
