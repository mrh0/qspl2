# qspl
Quick Statement Programming Language

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
``` 
Operators:
```
Math: '+ - * / % ^'
Boolean: '&& || ! == < > <= >='
Contains: '?'
Is Type: 'is'
```

Flow control:
```
//If x print 'Hello!'
x:
  out "Hello!";
  
//While x print 'Hello!'
x::
  out "Hello!";
```

# License

Available under MIT the license more info at: https://tldrlegal.com/license/mit-license

Copyright 2019 MRH0 (aka MRH/mrhminer/hminer.lll)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
