package com.mingyue.mingyue.controller;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class NettyClient {


    @PostConstruct
    public void init () {
        nettyClient();
    }

    public void nettyClient() {

    }

}
