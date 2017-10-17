package com.yunxin.service.natrpc.server.component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.yunxin.service.natrpc.commons.message.ClientLoginMessageHelper;
import com.yunxin.service.natrpc.commons.message.CmdCallbackMessageHelper;
import com.yunxin.service.natrpc.commons.message.CommunicationMessage;
import com.yunxin.service.natrpc.server.dao.EndPointRepository;
import com.yunxin.service.natrpc.server.entity.EndPoint;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hedingwei on 11/10/2017.
 */
@Component
public class Server {

    private static final int PORT = 9123;

    @Autowired
    private EndPointRepository endPointRepository;

    private NioSocketAcceptor acceptor = new NioSocketAcceptor();

    private Map<String, IoSession> endPointsLookup = new HashMap<>();

    private Cache<String, ICallback.InnerCallback> callbackCache = null;


    public Server(){
        callbackCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<String, ICallback.InnerCallback>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, ICallback.InnerCallback> removalNotification) {
                        System.out.println(removalNotification.toString());
                    }
                }).build();
        init();
        start();
    }

    protected void init(){
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new ObjectSerializationCodecFactory()));
        acceptor.setHandler(  new IoHandlerAdapter(){
            @Override
            public void sessionCreated(IoSession session) throws Exception {
                System.out.println("sessionCreated: "+session);
            }

            @Override
            public void sessionOpened(IoSession session) throws Exception {


            }

            @Override
            public void sessionClosed(IoSession session) throws Exception {
                System.out.println("sessionClosed: "+session);
                EndPoint endPoint= (EndPoint) session.getAttribute("endpoint");
                endPointsLookup.remove(endPoint.getId());
                endPointRepository.delete(endPoint.getId());

            }

            @Override
            public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

            }

            @Override
            public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
                System.out.println("exceptionCaught:["+cause+"] "+session);
            }

            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                System.out.println("---"+session);
                System.out.println("messageReceived["+message+"]: "+session);

                if(message instanceof CommunicationMessage){
                    CommunicationMessage msgBean = (CommunicationMessage) message;
                    if(ClientLoginMessageHelper.isClientLoginMessage(msgBean)){
                        EndPoint endPoint = ClientLoginMessageHelper.parseToEndPoint(msgBean);
                        session.setAttribute("endpoint",endPoint);
                        endPoint.setIpAddress(((session.getRemoteAddress())).toString());
                        endPoint.setLastLoginTime(new Date());
                        endPointsLookup.put(endPoint.getId(),session);
                        endPointRepository.save(endPoint);
                        return;
                    }


                    if(CmdCallbackMessageHelper.isCmdCallbackMessageHelper(msgBean)){
                        ICallback.InnerCallback innerCallback = callbackCache.getIfPresent(msgBean.getMessageId());
                        if(innerCallback!=null){
                            innerCallback.process(msgBean);
                        }
                        return;
                    }

                }
            }
        });

        acceptor.getSessionConfig().setReadBufferSize( 2048 );
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
        acceptor.setReuseAddress(true);

    }

    public void start(){
        try {
            acceptor.bind( new InetSocketAddress(PORT) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(IoSession session, CommunicationMessage messageBean, ICallback callback){
        session.write(messageBean);
        callbackCache.put(messageBean.getMessageId(),new ICallback.InnerCallback(callbackCache,callback,messageBean));

    }

    public Map<String, IoSession> getEndPointsLookup() {
        return endPointsLookup;
    }

    public static void main(String[] args){

        Server server = new Server();


    }
}
