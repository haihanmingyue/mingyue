package com.mingyue.mingyue.interfacePack;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogInter {

    String type() default "";

    Class<?> setClass() default Object.class;
}
