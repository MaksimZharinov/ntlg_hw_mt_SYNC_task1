import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int threadsCount = 1_000;
        char findChar = 'R';
        final ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount);

        for (int i = 0; i < threadsCount; i++) {
            Future<Integer> task = threadPool.submit(() -> {
                String route = generateRoute("RLRFR", 100);
                return (int) route.chars()
                        .filter(ch -> ch == findChar)
                        .count();
            });
            System.out.println(task.get());
        }
        threadPool.shutdown();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
