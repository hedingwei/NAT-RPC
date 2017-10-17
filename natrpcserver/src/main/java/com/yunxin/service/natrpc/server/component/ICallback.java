package com.yunxin.service.natrpc.server.component;

import com.google.common.cache.Cache;
import com.yunxin.service.natrpc.commons.message.CommunicationMessage;

/**
 * Created by hedingwei on 16/10/2017.
 */
public interface ICallback {
    public void onCallback(CommunicationMessage request, CommunicationMessage response);

    public static class InnerCallback {
        ICallback callback = null;
        CommunicationMessage request = null;
        Cache<String, InnerCallback> callbackCache;

        public InnerCallback(Cache<String, InnerCallback> callbackCache,ICallback callback, CommunicationMessage request) {
            this.callback = callback;
            this.request = request;
            this.callbackCache = callbackCache;
        }

        public void process(CommunicationMessage response){
            try {
                callback.onCallback(this.request, response);
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }finally {
                callbackCache.invalidate(this.request.getMessageId());
            }

        }
    }
}
