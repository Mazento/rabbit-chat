package com.example.discoveryapi;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class DiscoveryController {

    private final DiscoveryClient discoveryClient;

    public DiscoveryController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     *
     * @return list of ports for all available instances of chat-signal-service
     */
    @GetMapping("/chat")
    public List<String> getAllChatSignals() {

        List<ServiceInstance> instances = discoveryClient.getInstances("chat-signal-service");

        return instances
                .stream()
                .map(ServiceInstance::getPort)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }
}
