package com.mrh.qspl.io.console;

import java.io.PrintStream;

public class Console {
	public static final PrintStream defaultLogStream = System.out;
	public static final PrintStream defaultErrStream = System.err;
	private PrintStream logStream;
	private PrintStream errStream;
	
	private String logPrefix = "[OUT]: ";
	private String logSurfix = "";
	private String logSeperator = ",";
	
	private String errPrefix = "[ERR]: ";
	private String errSurfix = "";
	private String errSeperator = ",";
	
	public Console(PrintStream stream) {
		this.logStream = stream;
		this.errStream = stream;
	}
	
	public Console(PrintStream log, PrintStream err) {
		this.logStream = log;
		this.errStream = err;
	}
	
	public Console() {
		this.logStream = defaultLogStream;
		this.errStream = defaultErrStream;
	}
	
	public void log(String...s) {
		this.logStream.print(logPrefix);
		for(int i = 0; i < s.length; i++) {
			this.logStream.print(s[i]);
			if(i+1 < s.length)
				this.logStream.print(logSeperator);
		}
		this.logStream.println(logSurfix);
	}
	
	public void err(String...s) {
		this.errStream.print(errPrefix);
		for(int i = 0; i < s.length; i++) {
			this.errStream.print(s[i]);
			if(i+1 < s.length)
				this.errStream.print(errSeperator);
		}
		this.errStream.println(errSurfix);
	}
}
