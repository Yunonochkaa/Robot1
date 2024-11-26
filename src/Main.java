import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final Object lock = new Object(); // Объект для синхронизации ^^

    public static void main(String[] args) throws InterruptedException {
        final int numberOfThreads = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countOccurrences(route, 'R');
                updateFrequency(countR);
                System.out.println("Generated route: " + route + " -> Count of 'R': " + countR);
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }

        printResults();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countOccurrences(String route, char character) {
        int count = 0;
        for (char c : route.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }

    public static void updateFrequency(int countR) {
        synchronized (lock) {
            sizeToFreq.put(countR, sizeToFreq.getOrDefault(countR, 0) + 1);
        }
    }

    public static void printResults() {
        int maxFrequency = Collections.max(sizeToFreq.values());
        int mostFrequentCount = -1;

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() == maxFrequency) {
                mostFrequentCount = entry.getKey();
                break;
            }
        }

        System.out.println("Самое частое количество повторений " + mostFrequentCount + " (встретилось " + maxFrequency + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getKey() != mostFrequentCount) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }
}
