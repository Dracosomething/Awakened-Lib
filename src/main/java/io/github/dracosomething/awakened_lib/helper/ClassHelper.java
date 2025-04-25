package io.github.dracosomething.awakened_lib.helper;

import java.util.Arrays;

public class ClassHelper {
    public static boolean hasInterface(Class<?> targetClass, Class<?> targetTnterface) {
        return Arrays.stream(targetClass.getInterfaces()).toList().contains(targetTnterface);
    }
}
