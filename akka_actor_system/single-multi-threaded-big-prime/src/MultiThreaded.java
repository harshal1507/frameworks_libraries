import java.util.ArrayList;
import java.util.List;

public class MultiThreaded {

    // interrupted exception is need to handle
    // disadvantage as we need to handle too many exception and quite difficult
    // Results object is not thread safe
    public static void main(String[] args) throws InterruptedException {

        Long start = System.currentTimeMillis();
        System.out.println("Started...");

        Results results = new Results();

        Runnable status = new CurrentStatus(results);
        Thread statusTask = new Thread(status);
        statusTask.start();

        Runnable task = new PrimeGenerator(results);

        List<Thread> threadList = new ArrayList<>();

        // for 200 we get concurrentModificationException where two are more threads trying to access
        // same data at a same time making the result object thread unsafe
        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(task);
            threadList.add(thread);
            thread.start();
        }

        // Even after making result object thread safe we can still have the issue of thread blocking
        // which can take longer time to process

        // Also we can use Futures and thread safe collections object
        // But all these implementation includes the complexity of handling this code
        // akka overcome all this issues and provides simple with new and better approach for this implementation
        // which is also fast and works very well in distributed systems

        for(Thread t: threadList){
            t.join();
        }

        Long end = System.currentTimeMillis();
        System.out.println(results.getSize());
        System.out.println("Time taken is " + (end - start) + " ms.");
        System.out.println("Ended");
    }
}
