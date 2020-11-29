package com.izzyacademy.sales.models;

public class ErrorMessage {

    private String message;

    public ErrorMessage(String msg) {

        this.message = msg;
    }
    public ErrorMessage setMessage(String msg) {

        this.message = msg;

        return this;
    }

    public String getMessage() {
        return this.message;
    }

}
