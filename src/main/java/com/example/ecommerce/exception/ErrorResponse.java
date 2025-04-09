package com.example.ecommerce.exception;

public class ErrorResponse {

    private String errorCode;
    private String message;
    private long timestamp;

    // Constructor, Getters and Setters
    public ErrorResponse(String errorCode, String message, long ts) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = ts;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
