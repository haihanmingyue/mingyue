package com.mingyue.mingyue.bean;

public abstract class Father {

    public abstract Integer getType();

    public abstract String getPrint();

    private Integer number;

    public Integer getNumber() {
        return number;
    }

    static {
        System.err.println("father static");
    }

    {
        System.err.println("father no static");
    }

    public Father() {
        System.err.println("futher public");
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
