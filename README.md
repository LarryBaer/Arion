# Arion

Interpreted programming language developed and designed completely in Kotlin!

## Example programs

### Conditionals

```
var theNumber = 3
if(false){
	log(“This does not execute.”)
}elif(theNumber < 5){
	log(“The number is less than 5!”)
}else{
	log(“The number is greater than 5!”)
}
```

### Loops

```
var a = 0
var temp = 0
for (var b = 1; a < 40; b = temp + b) {
    log(a)
    temp = a
    a = b
}
```

### Functions & mathematic expressions

```
function doMath(){
    log(5 + 5 - 2)
    log(((5 + 5) * 10) / 2)
    log(26 % 6)
}

doMath()

```

## Contributing

Contributions are always welcome!

See [contributing.md](/contributing.md) for ways to get started.

## License

[Apache 2.0](/LICENSE)
