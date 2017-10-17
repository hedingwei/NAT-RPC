package com.yunxin.service.natrpc.api;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

/**
 * Created by hedingwei on 17/10/2017.
 */
public class HttpGetRequest extends GetRequest {
    public HttpGetRequest(HttpMethod method, String url) {
        super(method, url);
    }
    public HttpGetRequest(){
        super(HttpMethod.GET,"");

    }
}
