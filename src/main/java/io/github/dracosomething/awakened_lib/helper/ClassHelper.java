package io.github.dracosomething.awakened_lib.helper;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ClassHelper {
    public static boolean hasInterface(Class<?> targetClass, Class<?> targetTnterface) {
        if (!targetTnterface.isInterface()) return false;
        return Arrays.stream(targetClass.getInterfaces()).toList().contains(targetTnterface);
    }

    public static boolean isAnotatedWith(Class<?> targetClass, Class<? extends Annotation> anotation) {
        if (!anotation.isAnnotation()) return false;
        return targetClass.isAnnotationPresent(anotation);
    }
}
