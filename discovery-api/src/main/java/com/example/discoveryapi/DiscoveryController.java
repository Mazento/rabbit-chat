package com.example.discoveryapi;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class DiscoveryController {

    private final DiscoveryClient discoveryClient;

    public DiscoveryController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/chat")
    public List<String> getAllChatNodes() {

        List<ServiceInstance> instances = discoveryClient.getInstances("chat-node-service");

        List<String> results = instances
                .stream()
                .map(ServiceInstance::getUri)
                .map(URI::getAuthority)
                .collect(Collectors.toList());

        results.forEach(System.out::println);

        return results;
    }
}
