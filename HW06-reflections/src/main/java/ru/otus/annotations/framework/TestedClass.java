package ru.otus.annotations.framework;

public class TestedClass {

    @Before
    public void beforeTest(){
    }

    @Test
    public void test(){
    }

    @Test
    public void test2(){
        throw new IllegalArgumentException();
    }

    @After
    public void afterTest(){
    }


    @Override
    public String toString() {
        return "TestedClass{}";
    }
}
