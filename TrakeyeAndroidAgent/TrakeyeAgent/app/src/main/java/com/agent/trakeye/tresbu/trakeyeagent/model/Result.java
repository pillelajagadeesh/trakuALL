package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by sharmaan on 13-04-2016.
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 1346864799470440670L;
    int statusCode;
    String message;
    FieldPersonResponse detail;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FieldPersonResponse getDetail() {
        return detail;
    }

    public void setDetail(FieldPersonResponse detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Result{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", detail=" + detail +
                '}';
    }
}
