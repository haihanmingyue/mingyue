package com.mingyue.mingyue.bean;

import com.mingyue.mingyue.FactoryConstans;

import java.io.*;

public class ChildRenFactory3  implements Serializable , Cloneable{


    private Integer I ;

    public Integer getI() {
        return I;
    }

    public void setI(Integer i) {
        I = i;
    }

    static {
        System.err.println("child static");
    }

    {
        System.err.println("child no static");
    }

    public ChildRenFactory3(Integer i) {
        System.err.println("child public");
    }

    @Override
    protected ChildRenFactory3 clone() throws CloneNotSupportedException {
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this);
            oos.flush();

            ObjectInputStream inputStream = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray())
            );


            return (ChildRenFactory3) inputStream.readObject();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
