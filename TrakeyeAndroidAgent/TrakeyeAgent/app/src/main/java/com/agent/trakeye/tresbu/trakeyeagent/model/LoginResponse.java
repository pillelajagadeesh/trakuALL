package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 20-Oct-16.
 */

public class LoginResponse implements Serializable {

    String id_token;

    String AuthenticationException;

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getAuthenticationException() {
        return AuthenticationException;
    }

    public void setAuthenticationException(String authenticationException) {
        AuthenticationException = authenticationException;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "id_token='" + id_token + '\'' +
                ", AuthenticationException='" + AuthenticationException + '\'' +
                '}';
    }
}
