import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class Program1
{
    // NUMTHREADS can be modified for use of more or less threads to compute primes
    public static final int NUMTHREADS = 8;

    // simple way to gather a sum without using an atomic variable or counter
    public static long sum()
    {
        long sum = 0;
        for(long prime: IsPrime.q) 
            sum+=prime;
        return sum;
    }

    // simple way to count total primes without using an atomic variable
    public static long count()
    {
        return IsPrime.q.size();
    }

    // grabs top ten primes from priority q and reverses so they are increasing
    public static List<Long> topTen() throws InterruptedException
    {
        PriorityBlockingQueue<Long> q = IsPrime.q;
        List<Long> ten = new ArrayList<Long>();
        for (int i = 0; i < 10; i++)
        {
            ten.add(q.take());
        }
        Collections.reverse(ten);
        return ten;

    }
    // this prints the results in the required format from rubric
    public static void results(long time) throws Exception
    {
        FileWriter fw = new FileWriter("primes.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.println("< "+time/1e9+" s> < "+sum()+" > < "+count()+" >");
        pw.print("< ");
        topTen().forEach(prime -> pw.print(prime + " "));
        pw.print(" >");
        pw.println();
        pw.close();
    }

    public static void main (String args[]) throws Exception
    {
        Thread tArr[] = new Thread[NUMTHREADS];
        long start = System.nanoTime();

        for (int i = 0; i < NUMTHREADS; i++)
        {
            Thread t = new Thread(new IsPrime());
            tArr[i] = t;
            t.start();
        }
        for (Thread t : tArr) 
        {
            t.join();
        }

        long time = System.nanoTime() - start;
        results(time);
    }
}
class IsPrime implements Runnable
{
    // TOPNUM is used for the top limit for the desired primes
    private static final long TOPNUM = (long) 1e8;

    static Counter counter = new Counter();
    // creating a priority maximum queue will result in easy collection of primes as well as 
    // ordering, Blocking is used to properly add and remove concurrent elements
    public static PriorityBlockingQueue<Long> q = new PriorityBlockingQueue<Long>(11,
        new Comparator<Long>(){
            public int compare(Long lhs, Long rhs) {
                if (lhs < rhs) return 1;
                if (lhs.equals(rhs)) return 0;
                return -1;
            }
        }
    );

    public static void isPrime(long n)
    {
        if (n == 2) 
        {
            q.add(2L);
            return;
        }
        if (n < 2 || n % 2 == 0) 
        {
            return;
        }

        // method learned in PL with Leinecker to find primes more efficiently
        // even numbers are never prime after 2 and any number larger than the square root will
        // not divide evenly into n if the other multiple wasn't already found
        long sqrt = (long)(Math.sqrt(n));
        for (long i = 3; i < sqrt + 1; i+=2)
        {
            if(n % i == 0)
            {
                return;
            }
        }
        q.add(n);
        return;
    }

    public void run()
    {
        // cheack that the current number in counter is still less than the max number
        while (counter.get() < TOPNUM)
        {
            long j = counter.getAndIncrement();
            isPrime(j);

        }
    }
}

// counter implementation to handle concurrent selection of numbers
class Counter 
{
    private volatile long value = 1;

    public synchronized long getAndIncrement()
    {
        long temp;
        temp = value;
        value = temp + 1;
        return temp;
    }
    public synchronized long get()
    {
        return value;
    }
}