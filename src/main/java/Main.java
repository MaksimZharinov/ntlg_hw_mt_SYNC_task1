import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        int threadsCount = 1_000;
        char findChar = 'R';
        List<Thread> counters = new LinkedList<>();

        for (int i = 0; i < threadsCount; i++) {
            Thread counter = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countChar = (int) route.chars()
                        .filter(ch -> ch == findChar)
                        .count();
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(countChar)) {
                        sizeToFreq.put(countChar, (sizeToFreq.get(countChar) + 1));
                    } else sizeToFreq.put(countChar, 1);
                }
            });
            counters.add(counter);
            counter.start();
        }

        for (Thread thread : counters) {
            thread.join();
        }

        int max = sizeToFreq.keySet().stream()
                .max(Integer::compare)
                .get();
        System.out.println("Самое частое количество повторений " +
                max + " (встретилось " + sizeToFreq.get(max) + " раз)");
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((k, v) ->

        {
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
