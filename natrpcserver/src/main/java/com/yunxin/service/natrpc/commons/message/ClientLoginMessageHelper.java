package com.yunxin.service.natrpc.commons.message;

import com.yunxin.service.natrpc.server.entity.EndPoint;
import net.sf.json.JSONObject;

/**
 * Created by hedingwei on 17/10/2017.
 */
public class ClientLoginMessageHelper {
    public static boolean isClientLoginMessage(CommunicationMessage messageBean){
        try {
            String headerStr = messageBean.headerAsString();
            if("client_login".equals(headerStr)){
                JSONObject body = JSONObject.fromObject(messageBean.bodyAsString());
                if(body.containsKey("id")&&body.containsKey("type")&&body.containsKey("name")){
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static EndPoint parseToEndPoint(CommunicationMessage messageBean) throws Exception{

            JSONObject body = JSONObject.fromObject(messageBean.bodyAsString());
            EndPoint endPoint = new EndPoint();
            endPoint.setId(body.getString("id"));
            endPoint.setType(body.getString("type"));
            endPoint.setName(body.getString("name"));

            return endPoint;
    }

    public static CommunicationMessage buildClinetLoginMessage(String id, String type, String name){
        CommunicationMessage messageBean = new CommunicationMessage();
        try {
            messageBean.header("client_login");
            JSONObject object = new JSONObject();
            object.put("id",id);
            object.put("type",type);
            object.put("name",name);
            messageBean.body(object.toString());

            return messageBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageBean;
    }
}
