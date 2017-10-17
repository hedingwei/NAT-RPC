package com.yunxin.service.natrpc.api;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by hedingwei on 17/10/2017.
 */
public class RestAPI {

    static String host = "http://localhost:8080/natrpc";

    public static void init(String host){
        RestAPI.host = host;
    }



    public static HttpResponse<String> get(String type, String name, GetRequest getRequest) throws UnirestException {
        //http://localhost:8080/natrpc /cmd/syn/TestType/TestName
        Kryo kryo = new Kryo();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output,getRequest);
        output.close();
        String value = Base64.encodeBase64String(byteArrayOutputStream.toByteArray());

        JSONObject object = new JSONObject();
        object.put("type","GET");
        object.put("value",value);

        com.mashape.unirest.http.HttpResponse<String> response = Unirest.post(host+"/cmd/syn/"+type+"/"+name).body(object.toString()).asString();

        if(response.getStatus()==200){
            String body = response.getBody();
            byte[] data = Base64.decodeBase64(body);
            Input input = new Input(new ByteArrayInputStream(data));
            HttpResponse<String> res = (HttpResponse) kryo.readClassAndObject(input);
            input.close();
            return res;
        }else{
            return response;
        }

    }

    public static HttpResponse<String> post(String type, String name, HttpRequestWithBody post) throws UnirestException {
        //http://localhost:8080/natrpc /cmd/syn/TestType/TestName
        Kryo kryo = new Kryo();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output,post);
        output.close();
        String value = Base64.encodeBase64String(byteArrayOutputStream.toByteArray());

        JSONObject object = new JSONObject();
        object.put("type","POST");
        object.put("value",value);

        com.mashape.unirest.http.HttpResponse<String> response = Unirest.post(host+"/cmd/syn/"+type+"/"+name).body(object.toString()).asString();

        if(response.getStatus()==200){
            String body = response.getBody();
            byte[] data = Base64.decodeBase64(body);
            Input input = new Input(new ByteArrayInputStream(data));
            HttpResponse<String> res = (HttpResponse) kryo.readClassAndObject(input);

            input.close();
            return res;
        }else{
            return response;
        }

    }


    public static void main(String[] args) throws UnirestException {

        GetRequest getRequest = new GetRequest(HttpMethod.GET,"http://localhost:8080/helloworld/type1/name1");
        HttpRequestWithBody post = new HttpRequestWithBody(HttpMethod.POST,"http://localhost:8080/helloworld");
        post.body("this is body");


        RestAPI.init("http://120.76.121.210:9988/natrpc");

        System.out.println("------");
        HttpResponse<String> response = RestAPI.get("TestType","TestName",getRequest);
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());

        System.out.println("------");
        response = RestAPI.post("TestType","TestName",post);
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }

}
