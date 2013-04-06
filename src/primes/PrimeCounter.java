package primes;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class PrimeCounter extends RecursiveTask<Integer> {    
    int high;
    int low;
    
    PrimeCounter(int low, int high) {
        this.high = high;
        this.low = low;
    }

    /** Sequential program to count primes below some limits. */
    public static void main(String[] args) {
        
        for (int limit = 1000; limit < 1000000000; limit *= 10) {
            long start = System.nanoTime();
            int n = countPrimes(1, limit);
            long finish = System.nanoTime();
            System.out.println(n + " primes less than " + limit);
            System.out.println("Time: " + ((finish - start) / 1e9) + " seconds.\n");
        }
        System.out.println("Done.");
    }
    
    /** Count the number of primes below n. */
    private static int countPrimes(int low, int high) {
        return fjPool.invoke(new PrimeCounter(low, high));
    }

    @Override
    protected Integer compute() {
        int count = 0;
        if (high - low < 500) {
            for (int i = low; i < high; i++) {
                if (isPrime(i)) count += 1;
            }
        } else {
            PrimeCounter small = new PrimeCounter(low, ((high + low) / 2));
            PrimeCounter large = new PrimeCounter(((high + low) / 2), high);
            large.fork();
            count += small.compute();
            count += large.join();
        }
        return count;
    }

    static final ForkJoinPool fjPool = new ForkJoinPool();
    
    /** Return true iff n is a prime number. */
    private static boolean isPrime(int n) {
        if (n == 1) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;
        int limit = (int)(Math.sqrt(n)+ 0.5);
        for (int i = 3; i <= limit; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
    

}
