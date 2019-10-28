function Person(first, last, age, eyecolor) {
  this.firstName = first;
  this.lastName = last;
  this.age = age;
  this.eyeColor = eyecolor;
  this.getName = function() {
  return this.firstName + " " + this.lastName;
  }
}

console.log(Person.prototype)

var r = new Person("Riccardo", "Degni", 32, "hazel");
console.log(r)

Person.prototype.getName2Times = function() {
  return this.getName() + this.getName();
}

console.log(r.getName2Times())

var a = new Person("Alan", "Toro", 36, "green");
console.log(a);
console.log(a.getName2Times())

console.log(Person.prototype)
