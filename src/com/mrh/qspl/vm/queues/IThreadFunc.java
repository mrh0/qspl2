package com.mrh.qspl.vm.queues;

import java.util.ArrayList;

import com.mrh.qspl.val.Value;

public interface IThreadFunc {
	public Value execute(ArrayList<Value> input);
}
