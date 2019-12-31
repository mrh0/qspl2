package com.mrh.qspl.io.files;

import com.mrh.qspl.internal.common.Common;
import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.val.types.TObject;
import com.mrh.qspl.val.types.TUndefined;
import com.mrh.qspl.var.Var;
import com.mrh.qspl.vm.VM;

public class Include {
	public static TObject fromFile(String path) {
		String code = FileIO.readFromFile(path);
		if(code.length() == 0) 
			return new TObject();
		Tokenizer t = new Tokenizer();
		t.toTokens(code);
		VM vm = new VM(t, "import@"+path);
		vm.getCurrentScope().setVariable("this", new Var("this", TUndefined.getInstance()));
		Common.defineCommons(vm.getCurrentScope());
		vm.eval();
		TObject r = new TObject(vm.getGlobalScopeExports());
		return r;
	}
}
