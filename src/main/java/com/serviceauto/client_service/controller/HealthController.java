package com.serviceauto.client_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/client/ping")
    public String ping() {
        return "client-service is running";
    }
}
