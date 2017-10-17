package com.yunxin.service.natrpc.server.dao;

import com.yunxin.service.natrpc.server.entity.EndPoint;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by hedingwei on 16/10/2017.
 */
public interface EndPointRepository extends CrudRepository<EndPoint, String> {

    List<EndPoint> findAllByType(String type);

    List<EndPoint> findAll();

    List<EndPoint> findAllByTypeAndName(String type,String name);

    EndPoint findById(String id);

}
