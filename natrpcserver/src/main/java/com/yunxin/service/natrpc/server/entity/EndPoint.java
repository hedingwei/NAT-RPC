package com.yunxin.service.natrpc.server.entity;

import org.apache.mina.core.session.IoSession;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by hedingwei on 16/10/2017.
 */
@Entity
public class EndPoint {

    @Id
    private String id ;

    private String type;
    private String name;

    private String ipAddress;

    private Date lastLoginTime;

    @Transient
    private IoSession ioSession;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public IoSession getIoSession() {
        return ioSession;
    }

    public void setIoSession(IoSession ioSession) {
        this.ioSession = ioSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndPoint)) return false;
        EndPoint endPoint = (EndPoint) o;
        return Objects.equals(id, endPoint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
