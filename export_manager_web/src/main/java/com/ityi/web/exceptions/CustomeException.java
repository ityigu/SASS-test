package com.ityi.web.exceptions;

public class CustomeException extends Exception {
    private String message;
    public CustomeException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
