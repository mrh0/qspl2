package com.mrh.qspl.syntax.tokenizer;

import java.util.ArrayList;
import java.util.Stack;

import com.mrh.qspl.syntax.parser.Block;
import com.mrh.qspl.syntax.parser.Statement;
import com.mrh.qspl.syntax.parser.StatementEndType;
import com.mrh.qspl.syntax.tokenizer.Tokens.TokenType;

public class Tokenizer {
	ArrayList<Token> ts;
	TokenType cur = TokenType.none;
	TokenType last = TokenType.none;
	TokenType lasti = TokenType.none;
	Token prevToken = null;
	
	Stack<Character> bracketStack;
	Stack<Character> stringStack;
	Stack<Block> blockStack;
	
	GMCL mc;
	
	Statement lastStatement;
	
	int lineIndent;
	int prevIndent;
	
	String w;
	
	int curLine = 1;
	
	public Tokenizer() {
		w = "";
		
		bracketStack = new Stack<>();
		stringStack = new Stack<>();
		
		blockStack = new Stack<>();
		lastStatement = new Statement(0, new Token[0]);
		lastStatement.setBlock(new Block());
		blockStack.add(lastStatement.getNext());
		
		mc = new GMCL();
		mc.push();
		
		lineIndent = 0;
		prevIndent = 0;
	}
	
	public ArrayList<Token> toTokens(String s) {
		ts = new ArrayList<Token>();
		char c = ' ';
		boolean inComment = false;
		boolean inLineComment = false;
		
		for(int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			String cs = ((""+c)+(i+1<s.length()?s.charAt(i+1):' '));
			
			if(c == '\n') {
				curLine++;
			}
			
			if(stringStack.isEmpty()) {
				if(Tokens.isOpenComment(cs))
					inComment = true;
				if(Tokens.isLineComment(cs))
					inLineComment = true;
				if(c =='\n' && inLineComment)
					inLineComment = false;
				if(Tokens.isCloseComment(cs)) {
					i++;
					inComment = false;
					continue;
				}
				if(inComment || inLineComment) {
					continue;
				}
			}
			
			if(c == '\n') {
				lineIndent = 0;
			}
			
			
			if(Tokens.isString(c)) {
				if(stringStack.isEmpty()) {
					end();
					next(TokenType.string);
				}
				if(!stringStack.isEmpty() && c == stringStack.peek()) {
					stringStack.pop();
					if(stringStack.isEmpty())
						end();
					continue;
				}
				else {
					stringStack.push(c);
					continue;
				}
			}
			
			if(!stringStack.isEmpty()) {
				next(c, TokenType.string);
				continue;
			}
			if(c == '.' && (cur != TokenType.literal)) {
				end();
				next(TokenType.identifier);
				continue;
			}
			if(c == '#') {
				end();
				next(c, TokenType.keyword);
				end();
				continue;
			}
			if(Tokens.isEndOfStatement(c) && stringStack.isEmpty()) {
				StatementEndType set = StatementEndType.END;
				if(c == ':') {
					set = StatementEndType.IF;
					if(i+1 < s.length())
						if(s.charAt(i+1) == ':') {
							i++;
							set = StatementEndType.WHILE;
						}
				}
				end();
				endStatement(set);
				continue;
			}
			if(Tokens.isSeperator(c)) {
				if(Tokens.isWhitespace(c)) {
					if(Tokens.isIndent(c) && isPreStatement()) {
						lineIndent++;
						continue;
					}
					end();
					continue;
				}
				if(Tokens.isBracket(c)){
					if(Tokens.isOpenBracket(c)) 
						bracketStack.push(c);
					else{
						if(bracketStack.isEmpty() || Tokens.getOpenBracket(c) != bracketStack.pop())
							System.err.println("Unbalanced Brackets"); //replace
					}
				}
				end();
				next(c, TokenType.seperator);
				end();
				continue;
			}
			if(Tokens.isOperator(c)) {
				if(cur != TokenType.operator)
					end();
				next(c, TokenType.operator);
				continue;
			}
			if(c == '§') {
				end();
				next(c, TokenType.identifier);
				end();
				continue;
			}
			if(Tokens.canBeLiteral(c)) {
				if(cur == TokenType.identifier && w.equals(".")) {
					next(c, TokenType.literal);
					continue;
				}
				if(cur == TokenType.identifier) {
					next(c, TokenType.identifier);
					continue;
				}
				if(cur == TokenType.literal) {
					next(c, TokenType.literal);
					continue;
				}
				else {
					end();
					next(c, TokenType.literal);
					continue;
				}
			}
			if(Tokens.canBeIdentifier(c)) {
				if(cur == TokenType.literal) {
					next(c, TokenType.literal);
					continue;
				}
				if(cur != TokenType.identifier) {
					if(Tokens.canBeStartOfIdentifier(c)) {
						end();
					}
				}
				
				next(c, TokenType.identifier);
			}
			
		}
		end();
		//System.out.println("\nTokenized");
		return ts;
	}
	
	private void next(char c,  TokenType type) {
		cur = type;
		w += c;
	}
	
	private void next(TokenType type) {
		cur = type;
	}
	
	private int opValue(String s) {
		/*if(s.equals("."))
			return 12;*/
		
		if(s.equals("++"))
			return 11;
		if(s.equals("--"))
			return 11;
		if(s.equals("is"))
			return 11;
		if(s.equals("as"))
			return 11;
		if(s.equals("!"))
			return 11;
		if(s.equals("~"))
			return 11;
		
		if(s.equals("*"))
			return 10;
		if(s.equals("/"))
			return 10;
		if(s.equals("%"))
			return 10;
		
		if(s.equals("+"))
			return 9;
		if(s.equals("-"))
			return 9;
		
		if(s.equals("<"))
			return 8;
		if(s.equals("<="))
			return 8;
		if(s.equals(">"))
			return 8;
		if(s.equals(">="))
			return 8;
		if(s.equals("!="))
			return 7;
		if(s.equals("=="))
			return 7;
		
		
		if(s.equals("&"))
			return 6;
		if(s.equals("^"))
			return 5;
		if(s.equals("|"))
			return 4;
		
		if(s.equals("&&"))
			return 3;
		if(s.equals("||"))
			return 2;
		
		if(s.equals("("))
			return 1;
		
		if(s.equals("="))
			return 0;
		return 1;
	}
	
	private void gotNewToken(Token t) {
		TokenType tt = t.getType();
		String tss = t.getToken();
		
		//System.out.println("IN: " + t);// + ":" + lasti);
		if(tss.equals("[") || tss.equals("{")) {
			mc.push();
			gmc().postfixList.add(t);
		}
		else if(tss.equals("]") || tss.equals("}")) {
			finishPart();
			gmc().postfixList.add(t);
			ArrayList<Token> tl = mc.pop();
			gmc().postfixList.addAll(tl);
		}
		else if(tss.equals(",")) {
			finishPart();
			//gmc().postfixList.add(t);
			ArrayList<Token> tl = mc.pop();
			gmc().postfixList.addAll(tl);
			//toggle
			mc.push();
			gmc().postfixList.add(t);
		}
		else if(tt == TokenType.literal || tt == TokenType.identifier || tt == TokenType.string || tt == TokenType.keyword) {
			gmc().postfixList.add(t);
		}
		else if(tss.equals("(")) {
			gmc().opStack.push(t);
		}
		else if(tss.equals(")")) {
			Token top = gmc().opStack.pop();
			//System.out.println(gmc().opStack);
			while(!top.getToken().equals("(")) {// && !top.getToken().equals("[")
				gmc().postfixList.add(top);
				top = gmc().opStack.pop();
			}
		}
		else if(tt == TokenType.operator) {
			while(!gmc().opStack.isEmpty() && (opValue(gmc().opStack.peek().getToken()) >= opValue(tss))) {
				gmc().postfixList.add(gmc().opStack.pop());
			}
			gmc().opStack.push(t);
		}
		else {
			gmc().tokStack.add(t);
		}
	}
	
	private Token end() {
		//check if w is keyword
		cur = Tokens.tokenSwapType(w, cur);
		
		if(cur == TokenType.identifier)
			if(Tokens.isKeyword(w))
				cur = TokenType.keyword;
		
		if(w.length() > 0 || cur == TokenType.string) {
			Token t = null;
			if(w.length() > 1 && w.charAt(0) == '.' && cur == TokenType.identifier) {
				if(Tokens.canBeLiteral(w.charAt(1))) {
					t = new Token(w, cur);
					gotNewToken(t);
				}
				else {
					gotNewToken(new Token("[", TokenType.seperator));
					t = new Token(w.substring(1), TokenType.string);
					gotNewToken(t);
					gotNewToken(new Token("]", TokenType.seperator));
				}
			}
			else {
				t = new Token(w, cur);
				gotNewToken(t);
			}
			
			w = "";
			
			if(cur != TokenType.none)
				lasti = cur;
			
			last = cur;
			cur = TokenType.none;
			return t;
		}
		w = "";
		
		last = cur;
		cur = TokenType.none;
		return null;
	}
	
	public String toString() {
		return tsrec(blockStack.peek());
	}
	int depth = -1;
	private String tsrec(Block b) {
		depth++;
		String r = "";
		for(Statement s : b.getAll()) {
			for(int i = 0; i < depth; i++)
				r += "\t";
			r += s.toString();
			if(s.hasNext())
				r += tsrec(s.getNext());
		}
		depth--;
		return r;
	}
	
	public Block getRootBlock() {
		return blockStack.peek();
	}
	
	private boolean inString() {
		return !stringStack.isEmpty();
	}
	
	private class GMCL{
		Stack<MapContext> stack;
		public GMCL() {
			stack = new Stack<MapContext>();
		}
		
		public MapContext get() {
			return stack.peek();
		}
		
		public ArrayList<Token> pop() {
			return stack.pop().postfixList;
		}
		
		public void push() {
			stack.add(new MapContext());
		}
	}
	
	private MapContext gmc() {
		return mc.get();
	}
	
	//Postfix last step
	private void finishPart() {
		while(!gmc().opStack.isEmpty())
			gmc().postfixList.add(gmc().opStack.pop());
		//System.out.println("FINISHED PART: " + gmc().postfixList);
		ts = gmc().postfixList;
	}
	
	private Statement endStatement(StatementEndType set) {
		finishPart();
		
		mc = new GMCL();
		mc.push();
		
		Statement s = new Statement(curLine, ts.toArray(new Token[0]), set);
		String ind = "";
		ts.clear();
		
		if(prevIndent > lineIndent) {
			for(int i = 0; i < prevIndent - lineIndent; i++) {
				blockStack.pop();
				//System.out.println("Popped:"+(i+1));
			}
		}
		
		if(prevIndent < lineIndent) {
			for(int i = 0; i < lineIndent - prevIndent; i++) {
				blockStack.push(new Block());
				//System.out.println("Pushed:"+(i+1));
			}
			if(lastStatement!=null)
				lastStatement.setBlock(blockStack.peek());
		}
		
		if(blockStack.isEmpty()) 
			System.err.println("Block stack empty!");
		blockStack.peek().add(s);
		for(int i = 0; i < lineIndent; i++)
			ind+="\t";
		//System.out.println(ind+s+"("+set+"):"+blockStack.size());
		
		prevIndent = lineIndent;
		
		lineIndent = 0;
		lastStatement = s;
		return s;
	}
	
	private boolean isPreStatement() {
		return ts.size() == 0;
	}
	
	
}
