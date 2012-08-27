package com.sunshine.support.application;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class UIMessage<T> {

    private String message;
    private T payload;

    public UIMessage(String msg, T payload) {
        this.message = msg;
        this.payload = payload;
    }

    public String getMessage(){
        return message;
    }

    public T getPayload() {
        return payload;
    }
}
