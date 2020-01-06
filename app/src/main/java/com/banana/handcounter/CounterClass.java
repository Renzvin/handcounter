package com.banana.handcounter;

public class CounterClass {

    String id,Counter;
    public CounterClass(){}

    public CounterClass(String id,String Counter){
        this.id = id;
        this.Counter = Counter;
    }

    public String getCounter() {
        return Counter;
    }
}
