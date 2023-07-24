package com.example.controller;

import com.example.proxy.CN12306FeignClient;
import com.example.proxy.PfduFeignClient;
import com.example.proxy.TaxFeignClient;
import feign.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping(path = "/mock")
@AllArgsConstructor
public class MockController {

    public PfduFeignClient pfdu;
    public TaxFeignClient tax;
    public CN12306FeignClient cn12306;


    @GetMapping(path = "/pfdu")
    public void pfduIndex(HttpServletResponse servletResponse) {
        Response response = pfdu.index();
        request(servletResponse, response);
    }

    @GetMapping(path = "/tax")
    public void taxIndex(HttpServletResponse servletResponse) {
        Response response = tax.index();
        request(servletResponse, response);
    }

    @GetMapping(path = "/12306")
    public void cn12306Index(HttpServletResponse servletResponse) {
        Response response = cn12306.index();
        request(servletResponse, response);
    }

    private static void request(HttpServletResponse servletResponse, Response response) {
        Response.Body body = response.body();
        InputStream fileInputStream = null;
        OutputStream outStream;
        try {
            fileInputStream = body.asInputStream();
            outStream = servletResponse.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outStream.write(bytes, 0, len);
            }
            fileInputStream.close();
            outStream.close();
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
