package com.mrh.qspl.vm.queues;

import java.util.ArrayList;

import com.mrh.qspl.val.Value;
import com.mrh.qspl.vm.VM;

public interface IQueueEntry {
	public boolean isReady();
	public Value execute(VM vm);
	public int getId();
	public void setId(int id);
	public boolean cancelAfterReady();
}
