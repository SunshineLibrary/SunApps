package com.sunshine.support.application;

import android.app.Application;
import android.os.Handler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class MessageApplication extends Application{

    private Map<String, Set<UIMessageListener>> uiListenersMap;
    private Handler uiHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        uiListenersMap = new HashMap<String, Set<UIMessageListener>>();
        uiHandler = new Handler();
    }

    public void postMessage(UIMessage msg) {
        if (uiListenersMap.containsKey(msg.getMessage())) {
            Set<UIMessageListener> listeners = uiListenersMap.get(msg.getMessage());
            uiHandler.post(new MessageDispatcher(listeners, msg));
        }
    }

    public void registerUIMessageListener(String msg, UIMessageListener listener) {
        if (uiListenersMap.containsKey(msg)) {
            uiListenersMap.get(msg).add(listener);
        } else {
            Set<UIMessageListener> listeners = new HashSet<UIMessageListener>();
            listeners.add(listener);
            uiListenersMap.put(msg, listeners);
        }
    }

    public UIMessageListener removeUIMessageListener(UIMessageListener listener) {
        for (Set<UIMessageListener> listeners: uiListenersMap.values()) {
            listeners.remove(listener);
        }
        return listener;
    }

    private class MessageDispatcher implements Runnable {

        private UIMessage message;
        private Set<UIMessageListener> listeners;

        public MessageDispatcher(Set<UIMessageListener> listeners, UIMessage msg) {
            this.listeners = listeners;
            this.message= msg;
        }

        @Override
        public void run() {
            for (UIMessageListener listener: listeners) {
                listener.onUIMessageReceived(message);
            }
        }
    }
}
