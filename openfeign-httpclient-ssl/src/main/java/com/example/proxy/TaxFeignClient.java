package com.example.proxy;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${url.tax.name:bing}", url = "${url.tax.url}")
public interface TaxFeignClient {

    @GetMapping(path = "")
    Response index();
}
