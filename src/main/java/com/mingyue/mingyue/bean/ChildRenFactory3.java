package com.mingyue.mingyue.bean;

import com.mingyue.mingyue.FactoryConstans;

public class ChildRenFactory3 extends Father  implements Cloneable{
    @Override
    public Integer getType() {
        return FactoryConstans.type3;
    }

    @Override
    public String getPrint() {
        return "sssss";
    }

    static {
        System.err.println("child static");
    }

    {
        System.err.println("child no static");
    }

    public ChildRenFactory3() {
        System.err.println("child public");
    }
}
