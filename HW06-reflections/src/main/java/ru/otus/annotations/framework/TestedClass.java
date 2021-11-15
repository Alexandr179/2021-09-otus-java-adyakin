package ru.otus.annotations.framework;

public class TestedClass {

    public Integer account = 5;

    @Before
    public int startInc(){
       return account ++;
    }

    @Test
    public int tested(){
        return account;
    }

    @After
    public int goBack(){
        return account --;
    }



    @Override
    public String toString() {
        return "TestedClass {" +
                "account = " + account +
                '}';
    }
}
