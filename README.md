# qspl
Quick Statement Programming Language v1.1

# Language

Variables & Values:
```
x = 5; //x = 5

//parentheses important [WIP]
x = (x-1); //x = 4

c = (((x*4)/2)+2) //c = 10

y = (x = 1); //x and y = 1

a = new ["hi", 5]; //a = array("hi", 5)
a[0]; //returns "hi"
a[1]; //returns 5

k = new [new[5,4,3], new[4]];

out "Hello World"[1]; //Prints 'e'
out "Hello World"[0,5]; //Prints 'Hello'

text = "Hi my name is MrH.";
out j/" "; //Prints [Hi,my,name,is,MrH.]
out j[]; //Prints size (5)
out j[2]; //Prints 'name'

CONSTANTVALUE = 5; //Constant when variable name is all capital letters
```
Functions:
```
//Define:
x = func(arg1, arg2):
exit "Hello"+arg1;

//Call:
x(" World!"); //returns "Hello World!"

//Chain (functional):
double = func():
exit (this*2);
x = 5;
out x double() double(); //Prints 20

array = [1,2,3,4];
x = func(o,i,a):
  exit (o+1);
out array map(x); //Prints [2,3,4,5]
``` 
Operators:
```
Math: '+ - * / % ^'
Boolean: '&& || ! == < > <= >='
Contains: '?'
Is type: 'is'
As type: 'as'
```
Arrays:
```
out a = new [5,3,2,1,9]; //Prints [5.0,3.0,2.0,1.0,9.0]
out a + 4; //Prints [5.0,3.0,2.0,1.0,9.0,4.0]
out a - 3; //Prints [5.0,2.0,1.0,9.0,4.0]
out a / 1; //Prints [5.0,1.0,9.0,4.0]
out a * 2; //Prints [10.0,2.0,18.0,8.0]
```
Flow control:
```
//If x print 'Hello!'
x:
  out "Hello!";
  
//While x print 'Hello!'
x::
  out "Hello!";
  
j = 5;
out prev; //prints result of previous statement (5)
j = false;
out else; //prints not result of previous statement (1)

//Prints "Bye":
-3:
  out "Hi";
else:
  out "Bye";
  
//Prints "Hi" and "Bye":
4:
  out "Hi";
prev:
  out "Bye";

```

Predefined:
```
//constants:
true, false,
NUMBER, STRING, ARRAY, OBJECT, FUNCTION,
UNDEFINED, UNDEF, null //All same.

//functions:
print(string), println(string)
min(...), max(...), clamp(n, lower, upper), 
random(lower, upper), 
round(n), sqrt(n), 
sin(n), cos(n), tan(n), asin(n), acos(n), atan(n),
valueOf(string) //get value of variable with name

//chain functions:
map(func(value, index, array))
```

# License

Available under MIT the license more info at: https://tldrlegal.com/license/mit-license

MIT License

Copyright 2019 MRH0 (aka MRH/mrhminer/hminer.lll)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
