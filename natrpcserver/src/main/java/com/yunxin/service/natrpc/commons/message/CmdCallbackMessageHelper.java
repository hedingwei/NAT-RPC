package com.yunxin.service.natrpc.commons.message;

import com.yunxin.service.natrpc.server.entity.EndPoint;
import net.sf.json.JSONObject;

/**
 * Created by hedingwei on 17/10/2017.
 */
public class CmdCallbackMessageHelper {
    public static boolean isCmdCallbackMessageHelper(CommunicationMessage messageBean){
        try {
            String headerStr = messageBean.headerAsString();
            if("response_cmd".equals(headerStr)){
                return true;
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
}
