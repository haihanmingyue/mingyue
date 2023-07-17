package com.mingyue.mingyue.bean;

import com.mingyue.mingyue.FactoryConstans;

public class ChildRenFactory2 extends Father  {
    @Override
    public Integer getType() {
        return FactoryConstans.type2;
    }

    @Override
    public String getPrint() {
        return "iwuieu";
    }

}
