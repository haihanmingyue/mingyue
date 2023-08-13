package com.mingyue.userrole;

import com.mingyue.mingyue.interfacePack.Code;
import com.mingyue.mingyue.interfacePack.CodeDomain;

public class SysMenuConstants {


    /** 是否是公有menu */
    @CodeDomain
    public final static String IS_ALL_MENU = "isAllMenu";
    @Code(domain = IS_ALL_MENU, caption = "公有")
    public static final Integer PUBLIC_MENU = 1;
    @Code(domain = IS_ALL_MENU, caption = "私有")
    public static final Integer PRIVATE_MENU = 0;



}
