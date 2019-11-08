package com.mrh.qspl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.mrh.qspl.syntax.tokenizer.Tokenizer;
import com.mrh.qspl.val.types.TFunc;
import com.mrh.qspl.vm.ExpressionEvaluator;
import com.mrh.qspl.vm.VM;

/**
 * Test area.
 * @author MRH0
 *
 */

public class Main {

	public static void main(String[] args) {
		String code = readFromFile("C:\\MRHLang\\arrays.txt");
		System.out.println("[PROGRAM OUTPUT]:");
		new QSPL().insertCode(code).execute();
	}
	
	public static String readFromFile(String path) {
		String r = "";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			while(br.ready())
				r += br.readLine()+"\n";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}

	//Old
	public static void test() {
		VM vm = new VM();
		Tokenizer t = new Tokenizer();
		//t.toTokens("hello == 1221*true, hello(hola) 55d aa3 'hello' ] \"pleb\"l \"\'pl\'eb\" 12");
		String code = readFromFile("C:\\MRHLang\\arrays.txt");
		t.toTokens(code);
		vm.setValue("x", new TFunc());
		ExpressionEvaluator ev = new ExpressionEvaluator(vm, t);
		//System.out.println(t.toString());
		System.out.println("[PROGRAM OUTPUT]:");
		ev.eval();
	}
}
/*
 * : = if{
 * :: = while{
 * ; = end
 * lower indent = }
 * 
 * true:: ... = while(true){...}
 * x < 5: ... = if(x < 5){...}
 */