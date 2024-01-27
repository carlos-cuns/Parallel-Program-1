# Carlos Cuns

## To compile and run this program use:

```
javac Program1.java
java Program 1

```

## To view the output use:

```
cat primes.txt
```

Alternatively one can use:

```
javac Program1.java && java Program1 && cat primes.txt
```

## Correctness and Experimentation

My efficency approach was in modifying my prime algorithm to count up to only the square root of a number as well as skipping any evens after two. I also made sure to avoid using synchronized and static variables and methods if possible. So far, this has lead to a fastest runtime of around 14 to 15 seconds.

To test the correctness of my program, I started with smaller number sets and compared to a known list of primes. For larger data sets, I ran several trials (to view consitency), and used a Haskell program to calculate my necessary values.

I started my general approach by attempting to implement a brute prime algorithm and testing it on a single thread. Then, I looked into how to create several threads that can run the IsPrime's run function (my previous algorithm) across an even work load. After that, the main issue was in having consistent counter that wasn't affected by the parallel aspect of the assignment. I solved this by writing a modified version of the Counter Class from our class notes. It uses a synchronized get and getAndIncrement approach to settle any issues. The next issue was storing all of the primes so that I could have a simple way to order and keep the top ten primes. I solved by using a Priority Queue, but then a Priority Blocking Queue so that each thread's input wouldn't affect other threads' actions.
