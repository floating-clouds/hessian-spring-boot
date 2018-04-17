package com.teclick.spring.boot.demo.server;

import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianClient;
import com.teclick.spring.boot.demo.client.HelloWorld_1;
import com.teclick.spring.boot.demo.client.HelloWorld_2;
import com.teclick.spring.boot.demo.client.HelloWorld_3;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @HessianClient
    private HelloWorld_1 helloWorld_1;

    @HessianClient
    private HelloWorld_2 helloWorld_2;

    @HessianClient
    private HelloWorld_3 helloWorld_3;

    @RequestMapping("/")
    @ResponseBody
    public String welcome() {
        return "Welcome to my demo web site";
    }

    @RequestMapping("/h1")
    @ResponseBody
    public String h1(@RequestParam("name") String name) {
        return helloWorld_1.sayHello(name);
    }

    @RequestMapping("/h2")
    @ResponseBody
    public String h2(@RequestParam("name") String name) {
        return helloWorld_2.sayWorld(name);
    }

    @RequestMapping("/h3")
    @ResponseBody
    public String h3(@RequestParam("name") String name) {
        return helloWorld_3.sayHelloWorld(name);
    }
}
