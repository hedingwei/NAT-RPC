package com.yunxin.service.natrpc.api;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mashape.unirest.http.HttpMethod;
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



    public static HttpResponse get(String type, String name, GetRequest getRequest) throws UnirestException {
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
            HttpResponse res = (HttpResponse) kryo.readClassAndObject(input);
            res.setHttpResponse(response);
            input.close();
            return res;
        }else{
            HttpResponse myHttpResponse = new HttpResponse();
            myHttpResponse.setHttpResponse(response);
            return myHttpResponse;
        }

    }

    public static HttpResponse post(String type, String name, HttpRequestWithBody post) throws UnirestException {
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
            HttpResponse res = (HttpResponse) kryo.readClassAndObject(input);
            res.setHttpResponse(response);
            input.close();
            return res;
        }else{
            HttpResponse myHttpResponse = new HttpResponse();
            myHttpResponse.setHttpResponse(response);
            return myHttpResponse;
        }

    }


    public static void main(String[] args) throws UnirestException {

        GetRequest getRequest = new HttpGetRequest(HttpMethod.GET,"http://img.chinaz.com/max-templates/passport/styles/topbar.css1");

        HttpResponse response = RestAPI.get("TestType","TestName",getRequest);

        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }

}
