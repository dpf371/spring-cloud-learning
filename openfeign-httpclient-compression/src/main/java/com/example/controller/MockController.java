package com.example.controller;

import com.example.proxy.LocalFeignClient;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@RestController
@RequestMapping(path = "/mock")
public class MockController {

    @Autowired
    public LocalFeignClient serverProxy;

    /**
     * 服务端
     * 响应字符串
     */
    @GetMapping(path = "/test")
    @ResponseBody
    public String test(ServletRequest request, @RequestParam String name) {
        System.out.println("服务端，收到请求参数的长度是：" + name.length());
        return name;
    }


    /**
     * 客户端
     * 调用服务端，读取中文
     */
    @GetMapping(path = "/cn")
    @ResponseBody
    public String test2() {
        String responseBody = serverProxy.test(Chinese.value);
        System.out.println("响应体长度" + responseBody.length());
        System.out.println(responseBody);
        return "成功";
    }

    /**
     * 客户端
     * 调用服务端，读取English
     */
    @GetMapping(path = "/en")
    @ResponseBody
    public String test3(ServletResponse response) {
        String responseBody = serverProxy.test(English.value);
        System.out.println("响应体长度" + responseBody.length());
        System.out.println(responseBody);
        return "成功";
    }


    /**
     * 服务端
     * 提供文件下载
     */
    @GetMapping(path = "/file")
    public void file(ServletRequest request, HttpServletResponse response) throws IOException {
        Path realFilePath = Paths.get("C:\\Users\\Administrator\\Pictures\\stsci-01evvfya7x23asyxfwh5ejgmt911211.jpg");

        //设置Content-type
        response.addHeader(HttpHeaders.CONTENT_TYPE, "image/jpg");

        OutputStream out = response.getOutputStream();
        Files.copy(realFilePath, out);
    }

    /**
     * 客户端
     * 调用服务端，下载文件
     */
    @GetMapping(path = "/read-file")
    public void readFile() {
        long start = System.currentTimeMillis();
        try {
            Response responseEntity = serverProxy.file();
            int status = responseEntity.status();
            System.out.println(status);
            Response.Body body = responseEntity.body();
            InputStream inputStream = body.asInputStream();
//            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
            Path path = Paths.get("D:\\work\\知识\\" +
                    new Random().nextInt(100000) + ".jpg");
//            Files.copy(gzipInputStream, path);
            Files.copy(inputStream, path);
        } catch (Exception e) {

        }
        System.out.println("##############################" + (System.currentTimeMillis() - start));
    }
}
