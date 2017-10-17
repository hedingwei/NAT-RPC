package com.yunxin.service.natrpc.server.controller;

import com.yunxin.service.natrpc.server.component.Server;
import com.yunxin.service.natrpc.server.entity.EndPoint;
import com.yunxin.service.natrpc.server.service.EndPointService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/natrpc/endpoint")
public class EndPointController {


    @Autowired
    Server server;

    @Autowired
    EndPointService endPointService;


    @RequestMapping("/findAll")
    @ResponseBody
    List<EndPoint> findAll() {
        List<EndPoint> endPoints = endPointService.findAll();
        System.out.println(endPoints.get(0).getIoSession());
        return endPoints;
    }

    @RequestMapping("/findAllByType/{type}")
    @ResponseBody
    List<EndPoint> findAllByType(@PathVariable("type")String type) {
        List<EndPoint> endPoints = endPointService.findAllByType(type);
        return endPoints;
    }

    @RequestMapping("/findAllByTypeAndName/{type}/{name}")
    @ResponseBody
    List<EndPoint> findAllByTypeAndName(@PathVariable("type")String type,@PathVariable("name")String name){
        List<EndPoint> endPoints = endPointService.findAllByTypeAndName(type, name);
        return endPoints;
    }

    @RequestMapping("/findById/{id}")
    @ResponseBody
    EndPoint findById(@PathVariable("id")String id){
        EndPoint endPoint = endPointService.findById(id);
        return endPoint;
    }


    @RequestMapping(value = "/http/str", method = RequestMethod.POST)
    @ResponseBody
    String get(@RequestBody String body){
        System.out.println(body);
        return "this is response: "+ body;
    }


}