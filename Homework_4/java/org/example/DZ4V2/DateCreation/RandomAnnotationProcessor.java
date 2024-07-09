package org.example.DZ4V2.DateCreation;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Random;

public class RandomAnnotationProcessor {
    public static void processAnnotationForData(Object obj) {
        Random random = new Random();
        Class<?> objClass = obj.getClass();
        for (Field field : objClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(RandomDate.class)) {
                RandomDate annotation = field.getAnnotation(RandomDate.class);
                long min = annotation.min();
                long max = annotation.max();

                field.setAccessible(true);
                if (min < max) {
                    try {
                        long rnd = min + (long) (random.nextDouble() * (max - min));
                        if (field.getType() == Timestamp.class) {
                            Timestamp timestamp = new Timestamp(rnd);
                            field.set(obj, timestamp);
                            System.out.println("Field " + field.getName() + " set to " + timestamp + " for object " + obj);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new IllegalArgumentException("min должно быть меньше max. min = " + min + ", max = " + max);
                }
            }
        }
    }
}
