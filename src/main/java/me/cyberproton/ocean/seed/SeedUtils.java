package me.cyberproton.ocean.seed;

import java.util.*;

public class SeedUtils {
    private static final Random random = new Random();

    public static int randomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static int randomInt(int max) {
        return random.nextInt(max);
    }

    public static <T> T randomElement(Collection<T> input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        List<T> list = input instanceof List ? (List<T>) input : new ArrayList<>(input);
        return list.get(random.nextInt(input.size()));
    }

    public static <T> T randomElement(T[] list) {
        if (list.length == 0) {
            throw new IllegalArgumentException("List is empty");
        }
        return list[random.nextInt(list.length)];
    }

    public static <T> List<T> randomElements(List<T> list, int numberOfElements) {
        List<T> newList = new ArrayList<>(list);
        Collections.shuffle(newList);
        return newList.subList(0, Math.min(numberOfElements, newList.size()));
    }

    public static <T> List<T> randomElements(T[] list, int numberOfElements) {
        List<T> newList = new ArrayList<>(List.of(list));
        Collections.shuffle(newList);
        return newList.subList(0, Math.min(numberOfElements, newList.size()));
    }
}
