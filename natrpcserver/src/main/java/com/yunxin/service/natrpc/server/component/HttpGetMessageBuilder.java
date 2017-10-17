package com.yunxin.service.natrpc.server.component;

import com.yunxin.service.natrpc.commons.message.CommunicationMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hedingwei on 17/10/2017.
 */
public class HttpGetMessageBuilder {

    CommunicationMessage messageBean = new CommunicationMessage();

    String url = "";

    Map<String , String> headers = new HashMap<>();


    public HttpGetMessageBuilder() {

    }


    public HttpGetMessageBuilder get(String url){
        this.url = url;
        return this;
    }

    public HttpGetMessageBuilder header(String k, String v){
        headers.put(k,v);
        return this;
    }





    public CommunicationMessage build(){
        return null;
    }


}
