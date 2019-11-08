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
