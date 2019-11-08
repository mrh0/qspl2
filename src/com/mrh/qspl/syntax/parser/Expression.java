package com.mrh.qspl.syntax.parser;

import java.util.Stack;

import com.mrh.qspl.syntax.tokenizer.Token;
import com.mrh.qspl.syntax.tokenizer.Tokens;
import com.mrh.qspl.val.ValueType;
import com.mrh.qspl.val.types.TNumber;

public class Expression {
	
	private Stack<ValueType> valueStack;
	private Stack<String> opStack;
	
	public Expression() {
		valueStack = new Stack<>();
		opStack = new Stack<>();
	}
	
	public void feed(Token t) {
		if(t.isOperator()) {
			opStack.add(t.getToken());
			return;
		}
		if(t.isLiteral()) {
			valueStack.add(new TNumber(Integer.parseInt(t.getToken())));
			return;
		}
		if(t.isIdentifier()) {
			valueStack.add(new TNumber(0));
			return;
		}
		if(t.isSeperator()) {
			if(t.getToken().charAt(0) == '(') {
				opStack.push(t.getToken());
			}
			if(t.getToken().charAt(0) == ')') {
				String op = opStack.pop();
				ValueType right = valueStack.pop();
				ValueType left = valueStack.pop();
				while(op.charAt(0) != '('){
					
					op = opStack.pop();
					right = valueStack.pop();
					left = valueStack.pop();
					valueStack.push(calc(op, left, right));
				}
				//1.2.5
			}
			return;
		}
	}
	
	public ValueType calc(String op, ValueType left, ValueType right) {
		if(op.equals("+")) {
			if(left instanceof TNumber && right instanceof TNumber)
				return new TNumber((double)left.get() + (double)right.get());
		}
		if(op.equals("-")) {
			if(left instanceof TNumber && right instanceof TNumber)
				return new TNumber((double)left.get() - (double)right.get());
		}
		if(op.equals("*")) {
			if(left instanceof TNumber && right instanceof TNumber)
				return new TNumber((double)left.get() * (double)right.get());
		}
		if(op.equals("/")) {
			if(left instanceof TNumber && right instanceof TNumber)
				return new TNumber((double)left.get() / (double)right.get());
		}
		return null;
	}
}
