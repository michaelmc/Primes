package primes;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimeCounterTest {

    @Test
    public final void testCountPrimes() {
        assertEquals(168, PrimeCounter.countPrimes(0, 1000));
        assertEquals(1229, PrimeCounter.countPrimes(0, 10000));
    }

    @Test
    public final void testIsPrime() {
        assertFalse(PrimeCounter.isPrime(1));
        assertTrue(PrimeCounter.isPrime(2));
        assertTrue(PrimeCounter.isPrime(3));
        assertFalse(PrimeCounter.isPrime(4));
        assertTrue(PrimeCounter.isPrime(5));
        assertFalse(PrimeCounter.isPrime(6));
        assertTrue(PrimeCounter.isPrime(7));
        assertFalse(PrimeCounter.isPrime(8));
        assertFalse(PrimeCounter.isPrime(9));
        assertFalse(PrimeCounter.isPrime(10));
    }

}
