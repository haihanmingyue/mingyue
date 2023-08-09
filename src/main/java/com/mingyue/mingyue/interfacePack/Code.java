package com.mingyue.mingyue.interfacePack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Code {
    String caption() default "";

    String[] captions() default {};

    String[] domain() default {};
}
