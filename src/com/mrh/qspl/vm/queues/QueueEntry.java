package com.mrh.qspl.vm.queues;

import java.util.ArrayList;

import com.mrh.qspl.val.Value;
import com.mrh.qspl.vm.VM;

public interface QueueEntry {
	public boolean isReady();
	public Value execute(VM vm);
}
