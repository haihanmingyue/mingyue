package com.mingyue.mingyue.interfacePack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeHouse {

    /**
     * 需要翻译的字段
     *
     * */
    String codeKey();


    /**
     * 自定义翻译调用方法
     *
     * */
    Class<? extends CodeHouseByName> codeHouseByName() default CodeHouseByName.class;

    /**
     * 定义常量翻译
     */
    String codeDomain() default "";


    /**
     * 默认调用getValue方法翻译
     *
     * */
    Class<? extends ICodeHouse> codeHouse() default ICodeHouse.class;
}
