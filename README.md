# qspl
Quick Statement Programming Language

# Language

Variables:
```
  x = 5; //x = 5
  
  //parentheses important
  x = (x-1); //x = 4
  
  y = (x = 1); //x and y = 1
  
  a = ["hi", 5]; //a = array("hi", 5)
  
  c = (((x*4)/2)+2)
```
Functions:
```
  //Define:
  
  x = func(arg1, arg2):
  
    return "Hello"+arg1;
  
  //Call:
  
  x(" World!"); //returns "Hello World!"
``` 
Operators:

Math: '+ - * / % ^'

Boolean: '&& || ! == < > <= >='

Contains: '?'

Is Type: 'is'
