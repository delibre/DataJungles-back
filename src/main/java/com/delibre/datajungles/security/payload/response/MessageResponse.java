package com.delibre.datajungles.security.payload.response;

public class MessageResponse {
    private String message;

    private int status;

    public MessageResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}