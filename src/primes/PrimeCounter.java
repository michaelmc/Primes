package primes;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * A class to count the number of prime numbers under certain thresholds.
 * 
 * PrimeCounter runs in parallel using ForkJoin on available processors so
 * it runs considerably faster on multi-core machines than a sequential
 * version.
 * 
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version CIT594 Spring 2013, 4/6/2013
 */
public class PrimeCounter extends RecursiveTask<Integer> {    
    
    /**
     * Required when extending another class.
     */
    private static final long serialVersionUID = 1L;
    
    int high;
    int low;
    
    /**
     * Constructor for the PrimeCounter class.
     * 
     * @param low The lower bound of numbers to test for primeness.
     * @param high The upper bound of numbers to test for primeness.
     */
    PrimeCounter(int low, int high) {
        this.high = high;
        this.low = low;
    }

    /**
     * Main method for PrimeCounter, calculates the number of primes under:
     * 
     * 1,000
     * 10,000
     * 100,000
     * 1,000,000
     * 10,000,000
     * 100,000,000
     * 1,000,000,000
     * 
     * Also times and prints the duration required to calculate those numbers.
     *  
     * @param args
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        int result;
        // priming the program with a short loop
        for (int limit = 1000; limit < 1000000; limit *= 10) {
            int n = countPrimes(1, limit);
            result = n; 
        }
        // running the actual timings
        for (int limit = 1000; limit < 1000000000; limit *= 10) {
            long start = System.nanoTime();
            int n = countPrimes(1, limit);
            long finish = System.nanoTime();
            System.out.println(n + " primes less than " + limit);
            System.out.println("Time: " + ((finish - start) / 1e9) + " seconds.\n");
        }
        System.out.println(Runtime.getRuntime().availableProcessors() + " processors.");
        System.out.println("Done.");
    }
    
    /**
     * Begins the prime number counting process by invoking the ForkJoin pool
     * and creating a new PrimeCounter instance.
     * 
     * @param low The lower bound of numbers to test for primeness.
     * @param high The upper bound of numbers to test for primeness.
     * @return The number of prime numbers between the low and high bounds.
     */
    private static int countPrimes(int low, int high) {
        return fjPool.invoke(new PrimeCounter(low, high));
    }

    /**
     * The compute() function for the PrimeCounter class. If the difference
     * between the high and low bounds is <= 250, calculates the number of
     * primes; else the range is divided in half again recursively as needed.
     */
    @Override
    protected Integer compute() {
        int count = 0;
        if (high - low <= 250) {
            for (int i = low; i < high; i++) {
                if (isPrime(i)) count += 1;
            }
        } else {
            int divider = ((high - low) / 2) + low;
            PrimeCounter small = new PrimeCounter(low, divider);
            PrimeCounter large = new PrimeCounter(divider, high);
            large.fork();
            count += small.compute();
            count += large.join();
        }
        return count;
    }

    /**
     * Creates the ForkJoin pool to run the PrimeCounter processes.
     */
    static final ForkJoinPool fjPool = new ForkJoinPool();
    
    /**
     * Calculates whether an integer is prime.
     * 
     * Borrowed from Prof. David Matuszek, CIT594 Spring 2013.
     * 
     * @param n The integer to test for primeness.
     * @return True if the integer is prime.
     */
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
