import java.math.BigInteger;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class SingleThreaded {
    public static void main(String[] args) {

        Long start = System.currentTimeMillis();
        System.out.println("Started...");

        SortedSet<BigInteger> bigIntegerPrimes = new TreeSet<>();

        while(bigIntegerPrimes.size() < 50) {
            BigInteger bigInteger = new BigInteger(2000, new Random());
            bigIntegerPrimes.add(bigInteger.nextProbablePrime());
        }

        Long end = System.currentTimeMillis();
        System.out.println(bigIntegerPrimes.size());
        System.out.println("Time taken is " + (end - start) + " ms.");
        System.out.println("Ended");
    }
}