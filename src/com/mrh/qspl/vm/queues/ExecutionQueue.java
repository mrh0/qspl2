package com.mrh.qspl.vm.queues;

import java.util.ArrayList;
import com.mrh.qspl.vm.VM;

public class ExecutionQueue {
	private ArrayList<QueueEntry> queue;
	public ExecutionQueue() {
		queue = new ArrayList<QueueEntry>();
	}
	
	public void enqueue(QueueEntry qi) {
		queue.add(qi);
	}
	
	public void queueLoop(VM vm) {
		while(!queue.isEmpty()) {
			for(QueueEntry q : queue) {
				if(q.isReady()) {
					queue.remove(q);
					q.execute(vm);
					break;
				}
			}
		}
	}
}
