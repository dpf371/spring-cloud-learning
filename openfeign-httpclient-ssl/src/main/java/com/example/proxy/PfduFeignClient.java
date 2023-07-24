package com.example.proxy;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${url.pfdu.name:pfdu}", url = "${url.pfdu.url}")
public interface PfduFeignClient {

    @GetMapping(path = "")
    Response index();
}
