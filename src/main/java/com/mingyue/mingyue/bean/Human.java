package com.mingyue.mingyue.bean;

public record Human(Long id,String name) {

    public Human {

    }

    public Human(Long id) {
        this(id,null);
    }

    public void print() {
        System.out.println("hello " + name + "today your are 30 years old");
    }

}
