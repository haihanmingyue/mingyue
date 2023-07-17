package com.mingyue.mingyue.bean;

import com.mingyue.mingyue.FactoryConstans;

public class ChildRenFactory extends Father  {

    @Override
    public Integer getType() {
        return FactoryConstans.type1;
    }

    @Override
    public String getPrint() {
        return ",xmncmnd" + getNumber();
    }

}
