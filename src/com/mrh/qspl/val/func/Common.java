package com.mrh.qspl.val.func;

import java.util.ArrayList;
import java.util.Scanner;

import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.types.TArray;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.val.types.TNumber;
import com.mrh.qspl.val.types.TString;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.val.types.Types;
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
		
		IFunc f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
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
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
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
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			String r = "";
			for(int i = 0; i < args.size(); i++) {
				r+=args.get(i).toString();
				if(i+1 < args.size())
					r+=", ";
			}
			System.out.print(r);
			return new TString(r);
		};
		s.setVariable("print", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			String r = "";
			for(int i = 0; i < args.size(); i++) {
				r+=args.get(i).toString();
				if(i+1 < args.size())
					r+=", ";
			}
			System.out.println(r);
			return new TString(r);
		};
		s.setVariable("println", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			Scanner scan = new Scanner(System.in);
			ValueType r = TUndefined.getInstance();
			if(args.size() >= 1) {
				if(args.get(0).getType() == Types.NUMBER) {
					return new TNumber(scan.nextDouble());
				}
			} 
			return new TString(scan.nextLine());
		};
		s.setVariable("read", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			System.exit(0);
			return TUndefined.getInstance();
		};
		s.setVariable("stop", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(args.size() == 0)
				return TUndefined.getInstance();
			try {
				Thread.sleep(args.get(0).intValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return TUndefined.getInstance();
		};
		s.setVariable("sleep", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
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
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(args.size() == 0)
				return TUndefined.getInstance();
			return new TNumber((double)Math.round((double)args.get(0).get()));
		};
		s.setVariable("round", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(args.size() == 0)
				return TUndefined.getInstance();
			return new TNumber((double)Math.abs((double)args.get(0).get()));
		};
		s.setVariable("abs", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(args.size() == 0)
				return TUndefined.getInstance();
			return new TNumber(Math.sqrt((double)args.get(0).get()));
		};
		s.setVariable("sqrt", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(args.size() == 0)
				return TUndefined.getInstance();
			return vm.getValue((String)args.get(0).get());
		};
		s.setVariable("valueOf", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(args.size() == 0)
				return _this;
			if(args.get(0).getType() != Types.FUNC)
				return TUndefined.getInstance();
			if(_this.getType() == Types.ARRAY) {
				TArray a = (TArray) _this;
				ArrayList<ValueType> parms = new ArrayList<>();
				parms.add(TUndefined.getInstance());
				parms.add(TUndefined.getInstance());
				parms.add(a);
				for(int i = 0; i < a.getSize(); i++) {
					parms.set(0, a.getIndex(i));
					parms.set(1, new TNumber(i));
					a.setIndex(i, vm.executeFunction((TFunc)args.get(0), parms, _this));
				}
				return _this;
			}
			if(_this.getType() == Types.STRING) {
				TString a = (TString) _this;
				ArrayList<ValueType> parms = new ArrayList<>();
				parms.add(TUndefined.getInstance());
				parms.add(TUndefined.getInstance());
				parms.add(a);
				ArrayList<ValueType> ns = new ArrayList<>();
				for(int i = 0; i < a.getSize(); i++) {
					parms.set(0, new TString(a.get().charAt(i)+""));
					parms.set(1, new TNumber(i));
					ns.add((vm.executeFunction((TFunc)args.get(0), parms, _this)));
				}
				return new TArray(ns);
			}
			return _this;
		};
		s.setVariable("map", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(_this.getType() == Types.ARRAY) {
				TArray a = (TArray) _this;
				String rs = "";
				for(ValueType vs : a.getAll())
					rs += vs.toString();
				return new TString(rs);
			}
			return _this;
		};
		s.setVariable("collapse", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(_this.getType() == Types.ARRAY && args.size() == 2) {
				TArray a = (TArray) _this;
				if(args.get(0).getType() == Types.NUMBER)
					a.setIndex(args.get(0).intValue(), args.get(1));
				if(args.get(0).getType() == Types.ARRAY) {
					TArray ar = (TArray) args.get(0);
					for(int i = 0; i < ar.getSize(); i++) {
						a.setIndex(ar.getIndex(i).intValue(), args.get(1));
					}
				}
			}
			return _this;
		};
		s.setVariable("set", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(_this.getType() == Types.ARRAY && args.size() == 2) {
				TArray a = (TArray) _this;
				a.getAll().addAll(args);
			}
			return _this;
		};
		s.setVariable("add", new Var(new TFunc().define(f), true));
		s.setVariable("push", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(_this.getType() == Types.ARRAY) {
				TArray a = (TArray) _this;
				return a.getAll().remove(a.getSize()-1);
			}
			return _this;
		};
		s.setVariable("pop", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(_this.getType() == Types.ARRAY) {
				TArray a = (TArray) _this;
				return a.getAll().remove(0);
			}
			return TUndefined.getInstance();
		};
		s.setVariable("dequeue", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(_this.getType() == Types.ARRAY && args.size() == 1) {
				TArray a = (TArray) _this;
				return a.getAll().remove(args.get(0).intValue());
			}
			return TUndefined.getInstance();
		};
		s.setVariable("removeAt", new Var(new TFunc().define(f), true));
		
		f = (ArrayList<ValueType> args, VM vm, ValueType _this) -> {
			if(_this.getType() == Types.ARRAY && args.size() == 1) {
				TArray a = (TArray) _this;
				ValueType vt = a.find(args.get(0));
				if(!vt.isUndefined())
					a.getAll().remove(vt);
			}
			return _this;
		};
		s.setVariable("remove", new Var(new TFunc().define(f), true));
	}
}
