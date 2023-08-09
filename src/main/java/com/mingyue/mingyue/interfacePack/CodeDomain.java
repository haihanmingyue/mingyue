package com.mingyue.mingyue.interfacePack;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface CodeDomain {
    String DEFAULT_LAN = "zn";

    String[] lans() default {"zn"};
}
