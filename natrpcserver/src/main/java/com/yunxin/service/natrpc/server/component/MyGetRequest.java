package com.yunxin.service.natrpc.server.component;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.request.GetRequest;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by hedingwei on 17/10/2017.
 */
public class MyGetRequest extends GetRequest implements Serializable {
    public MyGetRequest(HttpMethod method, String url) {
        super(method, url);
    }

    public static void main(String[] args){
        GetRequest getRequest = new GetRequest(HttpMethod.GET,"http://localhost:8080/natrpc");
        getRequest.header("accept","application/json");

        Kryo kryo = new Kryo();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Output output = new Output(os);
        kryo.writeClassAndObject(output,getRequest);
        output.close();

        System.out.println(os.size());

    }
}
