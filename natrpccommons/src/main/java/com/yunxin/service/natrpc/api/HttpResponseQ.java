package com.yunxin.service.natrpc.api;

import com.mashape.unirest.http.Headers;


/**
 * Created by hedingwei on 17/10/2017.
 */
public class HttpResponseQ {

    private int statusCode;
    private String statusText;
    private Headers headers = new Headers();
    private String body;

    private com.mashape.unirest.http.HttpResponse<String> httpResponse;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public com.mashape.unirest.http.HttpResponse<String> getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(com.mashape.unirest.http.HttpResponse<String> httpResponse) {
        this.httpResponse = httpResponse;
    }
}
