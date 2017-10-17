package com.yunxin.service.natrpc.api;

import com.mashape.unirest.http.HttpMethod;

/**
 * Created by hedingwei on 17/10/2017.
 */
public class HttpRequestWithBody extends com.mashape.unirest.request.HttpRequestWithBody {
    public HttpRequestWithBody(HttpMethod method, String url) {
        super(method, url);
    }

    public HttpRequestWithBody(){
        super(HttpMethod.POST,"");
    }
}
