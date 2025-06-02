package io.github.dracosomething.awakened_lib.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassHelper {
    public static boolean hasInterface(Class<?> targetClass, Class<?> targetTnterface) {
        if (!targetTnterface.isInterface()) return false;
        return Arrays.stream(targetClass.getInterfaces()).toList().contains(targetTnterface) ||
                Arrays.stream(targetClass.getInterfaces()).anyMatch((clazz) -> clazz.isInstance(targetTnterface));
    }

    public static boolean isAnotatedWith(Class<?> targetClass, Class<? extends Annotation> anotation) {
        if (!anotation.isAnnotation()) return false;
        return targetClass.isAnnotationPresent(anotation);
    }

    public static boolean isAnotatedWith(Field targetField, Class<? extends Annotation> anotation) {
        if (!anotation.isAnnotation()) return false;
        return targetField.isAnnotationPresent(anotation);
    }

    public static boolean isAnotatedWith(Method targetMethod, Class<? extends Annotation> anotation) {
        if (!anotation.isAnnotation()) return false;
        return targetMethod.isAnnotationPresent(anotation);
    }

    public static <T> T getAnotation(Class<?> obj, Class<? extends Annotation> anotation) {
        if (!isAnotatedWith(obj, anotation)) return null;
        Annotation annotation = obj.getAnnotation(anotation);
        return (T) annotation;
    }
}
