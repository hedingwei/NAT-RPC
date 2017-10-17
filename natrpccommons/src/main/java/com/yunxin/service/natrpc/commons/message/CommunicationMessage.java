package com.yunxin.service.natrpc.commons.message;


import com.yunxin.service.natrpc.utils.DataUtils;
import com.yunxin.service.natrpc.utils.Encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by hedingwei on 16/10/2017.
 */
public class CommunicationMessage implements Serializable{

    String messageId = UUID.randomUUID().toString();
    String sessionId = UUID.randomUUID().toString();
    byte[] header;
    byte[] body;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void header(String headerStr) throws Exception {
        header = headerStr.getBytes("utf-8");
        header = DataUtils.compress(header);
        header = Encrypt.encrypt(header);
    }

    public void body(String bodyStr) throws Exception {
        body = bodyStr.getBytes("utf-8");
        body = DataUtils.compress(body);
        body = Encrypt.encrypt(body);
    }

    public String headerAsString() throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
            byte[] data = Encrypt.decrypt(header);
            data = DataUtils.decompressData(data);
            return new String(data,"utf-8");
    }



    public String bodyAsString() throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] data = Encrypt.decrypt(body);
        data = DataUtils.decompressData(data);
        return new String(data,"utf-8");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommunicationMessage)) return false;
        CommunicationMessage that = (CommunicationMessage) o;
        return Objects.equals(messageId, that.messageId) &&
                Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, sessionId);
    }

    public static void main(String[] args) throws Exception {
        CommunicationMessage messageBean = new CommunicationMessage();
        messageBean.header("dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的dddd我饿哭的");
        messageBean.body("skadfja;lsdkfjas;ldkfjas;ldkfjasldflk3jh4ip2u34p234uiposfjms,x.cvxzmf;lakfa;lskfj#$%#@$%@#$5");
        System.out.println(messageBean.header.length);
        System.out.println(messageBean.body.length);
        System.out.println(messageBean.headerAsString());
        System.out.println(messageBean.bodyAsString());

    }
}
