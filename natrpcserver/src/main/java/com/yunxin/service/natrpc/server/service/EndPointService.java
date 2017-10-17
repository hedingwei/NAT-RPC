package com.yunxin.service.natrpc.server.service;

import com.yunxin.service.natrpc.commons.message.CommunicationMessage;
import com.yunxin.service.natrpc.server.component.ICallback;
import com.yunxin.service.natrpc.server.component.Server;
import com.yunxin.service.natrpc.server.dao.EndPointRepository;
import com.yunxin.service.natrpc.server.entity.EndPoint;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by hedingwei on 16/10/2017.
 */
@Service
public class EndPointService {
    @Autowired
    private EndPointRepository endPointRepository;

    @Autowired
    private Server server;

    public List<EndPoint> findAll(){
        return endPointRepository.findAll();
    }

    public List<EndPoint> findAllByTypeAndName(String type, String name) {
        return endPointRepository.findAllByTypeAndName(type, name);
    }

    public List<EndPoint> findAllByType(String type) {
        return endPointRepository.findAllByType(type);
    }

    public EndPoint findById(String id) {
        return endPointRepository.findById(id);
    }

    public IoSession getEndPointIoSession(String id){
        EndPoint endPoint = findById(id);
        return server.getEndPointsLookup().get(endPoint.getId());
    }

    public EndPoint randomOneByTypeAndName(String type,String name){
        List<EndPoint> endPoints = endPointRepository.findAllByTypeAndName(type, name);
        if(endPoints.isEmpty()){
            return null;
        }else{
            Random randomizer = new Random();
            EndPoint random = endPoints.get(randomizer.nextInt(endPoints.size()));

            random.setIoSession(getEndPointIoSession(random.getId()));
            return random;
        }
    }

    public CommunicationMessage invokeCmdSync(EndPoint endPoint, String cmd){
        CommunicationMessage request = new CommunicationMessage();
        try{
            request.header("request_cmd");
            request.body(cmd);
        }catch (Exception e){
            e.printStackTrace();
        }

        CountDownLatch latch=new CountDownLatch(1);
        final Map<String,CommunicationMessage> data = new HashMap<>();
        server.sendMessage(endPoint.getIoSession(), request, new ICallback() {
            @Override
            public void onCallback(CommunicationMessage request, CommunicationMessage response) {
                data.put("ret",response);
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data.get("ret");
    }
}
