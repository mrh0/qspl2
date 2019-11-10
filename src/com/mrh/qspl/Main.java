package com.mrh.qspl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.mrh.qspl.debug.Debug;
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
		if(args.length == 0) {
			System.out.print("Run arguments: [path, (keys..)] but you gave: ");
			System.out.print("[");
			for(int i = 0; i < args.length; i++)
				System.out.print(args[i]+(i+1 < args.length?", ":""));
			System.out.println("]. Run 'help'.");
		}
		
		
		for(String k : args) {
			if(k.equals("-d") || k.equals("-debug"))
				Debug.enableDeepDebug = true;
			if(k.equals("-nr") || k.equals("-no-result"))
				Debug.noResult = true;
		}
		
		String code = "";
		if(args.length > 0) {
			if(args[0].equals("help")) {
				System.out.println("Run: [path, (keys..)]. Keys:(-debug, -no-result, -no-output)");
				return;
			}
			code = readFromFile(args[0]);
		}
		else
			code = readFromFile("C:\\MRHLang\\debug.qs");
		if(!Debug.noResult)
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
}