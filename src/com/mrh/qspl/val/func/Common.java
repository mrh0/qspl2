package com.mrh.qspl.val.func;

import java.util.ArrayList;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.types.TArray;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.val.types.TNumber;
import com.mrh.qspl.val.types.TString;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.var.Var;
import com.mrh.qspl.vm.Scope;
import com.mrh.qspl.vm.VM;

public class Common {
	public static void defineCommons(Scope s) {
		s.setVariable("true", new Var(new TNumber(1), true));
		s.setVariable("false", new Var(new TNumber(0), true));
		s.setVariable("UNDEFINED", new Var(TUndefined.getInstance(), true));
		s.setVariable("UNDEF", new Var(TUndefined.getInstance(), true));
		s.setVariable("null", new Var(TUndefined.getInstance(), true));
		
		s.setVariable("NUMBER", new Var(new TNumber(0), true));
		s.setVariable("FUNCTION", new Var(new TFunc(), true));
		s.setVariable("STRING", new Var(new TString(""), true));
		s.setVariable("ARRAY", new Var(new TArray(), true));
		
		IFunc f = (ArrayList<ValueType> args, VM vm) -> {
			if(args.size() == 0)
				return new TNumber(0);
			ValueType min = args.get(0);
			for(ValueType v : args) {
				if(v.compare(min) < 0)
					min = v;
			}
			return min;
		};
		s.setVariable("min", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm) -> {
			if(args.size() == 0)
				return new TNumber(0);
			ValueType max = args.get(0);
			for(ValueType v : args) {
				if(v.compare(max) > 0)
					max = v;
			}
			return max;
		};
		s.setVariable("max", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm) -> {
			String r = "";
			for(int i = 0; i < args.size(); i++) {
				r+=args.get(i).toString();
				if(i+1 < args.size())
					r+=",";
			}
			System.out.print(r);
			return new TString(r);
		};
		s.setVariable("print", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm) -> {
			String r = "";
			for(int i = 0; i < args.size(); i++) {
				r+=args.get(i).toString();
				if(i+1 < args.size())
					r+=",";
			}
			System.out.println(r);
			return new TString(r);
		};
		s.setVariable("println", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm) -> {
			double from = 0;
			double to = 1;
			if(args.size() == 1)
				to = (double)args.get(0).get();
			if(args.size() == 2) {
				from = (double)args.get(0).get();
				to = (double)args.get(1).get();
			}
			if(from > to)
				return new TNumber(0);
			if(from == to)
				return new TNumber(from);
			return new TNumber(Math.random()*(to-from)+from);
		};
		s.setVariable("random", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm) -> {
			if(args.size() == 0)
				return TUndefined.getInstance();
			return new TNumber((double)Math.round((double)args.get(0).get()));
		};
		s.setVariable("round", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm) -> {
			if(args.size() == 0)
				return TUndefined.getInstance();
			return new TNumber(Math.sqrt((double)args.get(0).get()));
		};
		s.setVariable("sqrt", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm) -> {
			if(args.size() == 0)
				return TUndefined.getInstance();
			return vm.getValue((String)args.get(0).get());
		};
		s.setVariable("valueOf", new Var(new TFunc().define(f), true));
	}
}