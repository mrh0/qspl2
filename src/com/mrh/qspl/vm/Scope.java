package com.mrh.qspl.vm;

import java.util.HashMap;
import java.util.Map;

import com.mrh.qspl.val.types.TNumber;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.var.Var;

public class Scope {
	private String name = "unknown scope";
	private Map<String, Var> variables;
	
	public Scope(String name) {
		this.name = name;
		this.variables = new HashMap<String, Var>();
		
		setVariable("true", new Var(new TNumber(1), true));
		setVariable("false", new Var(new TNumber(0), true));
		setVariable("undefined", new Var(TUndefined.getInstance(), true));
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
}
