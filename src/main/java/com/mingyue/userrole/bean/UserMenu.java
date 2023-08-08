package com.mingyue.userrole.bean;

import com.mingyue.mingyue.bean.BaseBean;
import com.mingyue.mingyue.interfacePack.DeleteStatus;

public class UserMenu extends BaseBean implements DeleteStatus {

    private String userUuid;

    private String menuUuid;

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getMenuUuid() {
        return menuUuid;
    }

    public void setMenuUuid(String menuUuid) {
        this.menuUuid = menuUuid;
    }

}
