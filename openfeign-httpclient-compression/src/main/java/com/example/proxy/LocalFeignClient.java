package com.example.proxy;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "Test", name="test", url = "${test.url:}")
public interface LocalFeignClient {
    @GetMapping(path = "/test")
    String test(@RequestParam String name);

    @GetMapping(path = "/file")
    Response file();
}
