package com.yunxin.service.natrpc.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.yunxin.service.natrpc.commons.message.CommunicationMessage;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by hedingwei on 11/10/2017.
 */
public class TestClient implements IoHandler {

    NioSocketConnector connector = new NioSocketConnector();
    private long CONNECT_TIMEOUT = 10000;

    private String hostname;

    private int port;


    IoSession session = null;

    public TestClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void init(){
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        connector.getFilterChain().addLast( "logger", new LoggingFilter() );
        connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ObjectSerializationCodecFactory()));
        connector.setHandler(this);
        connector.getSessionConfig().setKeepAlive(true);
    }

    public void connect(){
        init();
        while(true){
            if(session==null){
                System.out.println("trying reconnect at "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                for (;;) {
                    try {
                        ConnectFuture future = connector.connect(new InetSocketAddress(hostname, port));
                        future.awaitUninterruptibly();
                        session = future.getSession();
                        break;
                    } catch (RuntimeIoException e) {
                        System.err.println("Failed to connect at "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        session = null;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }else{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("connected");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("session opened");
        CommunicationMessage messageBean = new CommunicationMessage();
        try {
            messageBean.header("client_login");
            JSONObject object = new JSONObject();
            object.put("id",UUID.randomUUID().toString());
            object.put("type","TestType");
            object.put("name","TestName");
            messageBean.body(object.toString());
            session.write(messageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("session closed");
        this.session = null;
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("session idle: "+status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("messageReceived["+message+"]: "+session);


        if(message instanceof CommunicationMessage){
            CommunicationMessage msgBean = (CommunicationMessage) message;
            if(msgBean.headerAsString().equals("request_cmd")){
                // process cmd;
                CommunicationMessage response = new CommunicationMessage();
                response.setMessageId(msgBean.getMessageId());
                response.setSessionId(msgBean.getSessionId());
                response.header("response_cmd");

                JSONObject object = JSONObject.fromObject(msgBean.bodyAsString());
                String httpMethod = object.getString("type");
                if("GET".equals(httpMethod)){
                    String value = object.getString("value");
                    Kryo kryo = new Kryo();
                    Input input = new Input(new ByteArrayInputStream(Base64.decodeBase64(value)));
                    GetRequest getRequest = (GetRequest) kryo.readClassAndObject(input);
                    input.close();

                    com.mashape.unirest.http.HttpResponse<String> ret = getRequest.asString();


                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    Output output = new Output(os);
                    kryo.writeClassAndObject(output,ret);
                    output.close();
                    response.body(Base64.encodeBase64String(os.toByteArray()));
                }else if("POST".equals(httpMethod)){
                    String value = object.getString("value");
                    Kryo kryo = new Kryo();
                    Input input = new Input(new ByteArrayInputStream(Base64.decodeBase64(value)));
                    HttpRequestWithBody postRequest = (HttpRequestWithBody) kryo.readClassAndObject(input);
                    input.close();

                    com.mashape.unirest.http.HttpResponse<String> ret = postRequest.asString();

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    Output output = new Output(os);
                    kryo.writeClassAndObject(output,ret);
                    output.close();
                    response.body(Base64.encodeBase64String(os.toByteArray()));
                }else{

                }
                session.write(response);
            }
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        System.out.println("input closed.");
        session.closeNow();
    }



    public static void main(String[] args){
//        TestClient testClient = new TestClient("localhost",9123);

        TestClient testClient = new TestClient("120.76.121.210",9123);

        testClient.connect();
        System.out.println("ddddd");
    }

}
