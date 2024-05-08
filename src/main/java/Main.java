import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

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
            int key = task.get();
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(key)) {
                    sizeToFreq.put(key, (sizeToFreq.get(key) + 1));
                } else sizeToFreq.put(key, 1);
            }
        }
        threadPool.shutdown();
        int max = sizeToFreq.keySet().stream()
                .max(Integer::compare)
                .get();
        System.out.println("Самое частое количество повторений " +
                max + " (встретилось " + sizeToFreq.get(max) + " раз)");
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((k, v) -> {
            if (k != max) System.out.println("- " + k +
                    " (" + v + " раз)");
        });
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
