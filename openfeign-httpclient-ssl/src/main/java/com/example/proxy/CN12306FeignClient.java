package com.example.proxy;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="12306", url = "${url.12306.url:}")
public interface CN12306FeignClient {
    @GetMapping(path = "")
    Response index();
}
