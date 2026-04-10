package com.serviceauto.client_service.exception;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(Long requestId) {
        super("Request not found: " + requestId);
    }
}
