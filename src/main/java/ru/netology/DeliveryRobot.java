package ru.netology;

import java.util.*;

public class DeliveryRobot {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        int numberOfRoutes = 1_000;
        List<Thread> threads = new ArrayList<>();

        Runnable logic = () -> {
            int count = 0;
            String text = generateRoute("RLRFR", 100);
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == 'R') {
                    count++;
                }
            }
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(count)) {
                    sizeToFreq.computeIfPresent(count, (k, v) -> ++v);
                } else {
                    sizeToFreq.put(count, 1);
                }
            }
        };
        for (int j = 1; j < numberOfRoutes; j++) {
            Thread thread = new Thread(logic);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }

        int maxKey = 0;
        for (int key : sizeToFreq.keySet()) {
            if (maxKey == 0 || sizeToFreq.get(key) > sizeToFreq.get(maxKey)) {
                maxKey = key;
            }
        }

        System.out.println("Самое частое количество повторений " + maxKey + " (встретилось " + sizeToFreq.get(maxKey) + " раз)");
        for (int key : sizeToFreq.keySet()) {
            System.out.println("- " + key + " (" + sizeToFreq.get(key) + " раз)");
        }
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