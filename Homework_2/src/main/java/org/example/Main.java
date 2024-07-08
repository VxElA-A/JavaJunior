package org.example;

public class Main {
    public static void main(String[] args) {
        TestRunner.run(Main.class);
    }
    @AfterAll
    void test1(){
        System.out.println("AfterAll");
    }
    @Test
    private void test2(){ // не будет работать, потому что private
        System.out.println("test2");
    }
    void test3(){// не будет работать, потому что нет аннотаций
        System.out.println("test3");
    }
    @Test
    void test4(){
        System.out.println("test2");
    }
    @Test
    void test5(){
        System.out.println("test1");
    }
    @BeforeAll
    void test6(){
        System.out.println("BeforeAll");
    }
    @AfterEach
    void test7(){
        System.out.println("AfterEach");
    }
    @BeforeEach
    void test8(){
        System.out.println("BeforeEach");
    }

}