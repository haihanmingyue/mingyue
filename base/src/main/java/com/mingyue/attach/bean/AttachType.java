package com.mingyue.attach.bean;

import com.mingyue.base.bean.BaseBean;

public class AttachType extends BaseBean {


    private String name;
    private String uuid;
    private String pictureUuid;

    public String getPictureUuid() {
        return pictureUuid;
    }

    public void setPictureUuid(String pictureUuid) {
        this.pictureUuid = pictureUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


}
