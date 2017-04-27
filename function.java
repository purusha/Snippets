Function<Integer, Integer> times2 = e -> e * 2;

Function<Integer, Integer> squared = e -> e * e;  

times2.compose(squared).apply(4);  
// Returns 32

times2.andThen(squared).apply(4);  
// Returns 64

As you can see, the difference between compose and andThen is the order they execute the functions. While the compose function executes the caller last and the parameter first, the andThen executes the caller first and the parameter last.
