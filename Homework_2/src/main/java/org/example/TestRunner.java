package org.example;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunner {

    public static void run(Class<?> testCLass){
        final Object testObj = initTestObj(testCLass);
        for (Method testMethod : testCLass.getDeclaredMethods()){
            if(testMethod.accessFlags().contains(AccessFlag.PRIVATE)){
                continue;
            }
            if(testMethod.getAnnotation(BeforeAll.class)!= null){
                try {
                    testMethod.invoke(testObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (Method testMethod : testCLass.getDeclaredMethods()){
            if(testMethod.accessFlags().contains(AccessFlag.PRIVATE)){
                continue;
            }
            if(testMethod.getAnnotation(Test.class)!= null){
                try {
                    for (Method beforeEachMethod : testCLass.getDeclaredMethods()) {
                        if (beforeEachMethod.getAnnotation(BeforeEach.class)!=null) {
                            beforeEachMethod.invoke(testObj);
                        }
                    }
                    testMethod.invoke(testObj);
                    for (Method afterEachMethod : testCLass.getDeclaredMethods()) {
                        if (afterEachMethod.getAnnotation(AfterEach.class)!= null) {
                            afterEachMethod.invoke(testObj);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (Method testMethod : testCLass.getDeclaredMethods()){
            if(testMethod.accessFlags().contains(AccessFlag.PRIVATE)){
                continue;
            }
            if(testMethod.getAnnotation(AfterAll.class)!= null){
                try {
                    testMethod.invoke(testObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private static Object initTestObj(Class<?> testCLass){
        try{
            Constructor<?> noArgsConstructor = testCLass.getConstructor();
            return noArgsConstructor.newInstance();
        }catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException();
        }
    }
}
