package com.mrh.qspl.vm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mrh.qspl.internal.common.Common;
import com.mrh.qspl.val.Value;
import com.mrh.qspl.var.Var;

public class Scope {
	private String name = "unknown scope";
	private Map<String, Var> variables;
	private boolean depthLock = false;
	
	public Scope(String name) {
		this.name = name;
		this.variables = new HashMap<String, Var>();
	}
	
	public Scope(String name, boolean lock) {
		this.depthLock = true;
		this.name = name;
		this.variables = new HashMap<String, Var>();
	}
	
	public boolean isLocked() {
		return depthLock;
	}
	
	public Var getVariable(String name) {
		return variables.get(name);
	}
	
	public Var setVariable(String name, Var v) {
		return variables.put(name, v);
	}
	
	@Override
	public String toString() {
		return "SCOPE:"+name+":"+variables.size();
	}
	
	protected Map<String, Var> getAllVariables(){
		return variables;
	}
	
	protected Map<String, Value> getAllValues(){
		Set<String> c = variables.keySet();
		Map<String, Value> map = new HashMap<>();
		for(String s : c) {
			map.put(s, this.getVariable(s).get());
		}
		return map;
	}
}
