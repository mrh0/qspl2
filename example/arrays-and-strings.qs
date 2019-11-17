f = func o i a:
	exit o+1;
a = new [1,5,3,5];
out "IN: " + a;
out a#map[f];

f = func o i a:
	exit o<5;
out a#filter[f];

s = "Hello";
out "IN: " + s;
f = func o i a:
	o == "H":
		exit "h";
	else:
		exit o;
out s#map[f];

f = func o i a:
	exit o != "l";
out s#filter[f];

s = "Test String";
out "IN: " + s;
out s = s - new ["T", "t", "g"];

out s[];
