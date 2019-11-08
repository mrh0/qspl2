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
	
	Stack<Character> bracketStack;
	Stack<Character> stringStack;
	Stack<Block> blockStack;
	
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
		
		lineIndent = 0;
		prevIndent = 0;
	}
	
	public ArrayList<Token> toTokens(String s) {
		ts = new ArrayList<Token>();
		char c = ' ';
		boolean inComment = false;
		
		for(int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			String cs = ((""+c)+(i+1<s.length()?s.charAt(i+1):' '));
			if(Tokens.isOpenComment(cs))
				inComment = true;
			if(Tokens.isCloseComment(cs)) {
				i++;
				inComment = false;
				continue;
			}
			if(inComment) {
				continue;
			}
			
			if(c == '\n')
				curLine++;
			if(Tokens.isEndOfStatement(c)) {
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
			if(Tokens.canBeLiteral(c)) {
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
	
	private Token end() {
		//check if w is keyword
		cur = Tokens.tokenSwapType(w, cur);
		
		if(cur == TokenType.identifier)
			if(Tokens.isKeyword(w))
				cur = TokenType.keyword;
		last = cur;
		cur = TokenType.none;
		
		if(w.length() > 0 || last == TokenType.string) {
			Token t = new Token(w, last);
			ts.add(t);
			w = "";
			return t;
		}
		w = "";
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
	
	private Statement endStatement(StatementEndType set) {
		ts.add(0, new Token("(", TokenType.seperator));
		ts.add(new Token(")", TokenType.seperator));
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
