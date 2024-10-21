import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

public class Results {
    SortedSet<BigInteger> primes;

    public Results(){
        primes = new TreeSet<>();
    }

    /*
       commenting below method to make the result object thread safe
       so can't be access directly by any concurrent threads

       public SortedSet<BigInteger> getPrimes(){
        return primes;
    }*/

    // by adding below methods we are handling the concurrent modification exception
    // by making result object thread safe
    public int getSize(){
        synchronized (this){
            return primes.size();
        }
    }

    public void addPrime(BigInteger prime){
        synchronized (this){
            primes.add(prime);
        }
    }

    public void print(){
        synchronized (this){
            primes.forEach(System.out::println);
        }
    }
}
