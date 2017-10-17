package com.yunxin.service.natrpc.server.controller;

import com.yunxin.service.natrpc.server.entity.EndPoint;
import com.yunxin.service.natrpc.server.service.EndPointService;
import com.yunxin.service.natrpc.server.utils.DataUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hedingwei on 16/10/2017.
 */
@Controller
@RequestMapping("/natrpc/cmd")
public class CommandController {

    @Autowired
    EndPointService endPointService;

    @RequestMapping(value = "/syn/{type}/{name}",method = RequestMethod.POST)
    @ResponseBody
    public String cmd(@PathVariable("type")String type,@PathVariable("name")String name,@RequestBody String body) throws Exception {
        EndPoint endPoint = endPointService.randomOneByTypeAndName(type,name);
        if(endPoint!=null){
            return endPointService.invokeCmdSync(endPoint, body).bodyAsString();
        }else{
            return "{\"ret\":-1}";
        }
    }
}
