package com.teclick.spring.boot.demo.server;

import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianService;
import com.teclick.spring.boot.demo.client.HelloWorld_1;
import com.teclick.spring.boot.demo.client.HelloWorld_2;
import com.teclick.spring.boot.demo.client.HelloWorld_3;

@HessianService
public class HelloWorldServiceImpl implements HelloWorld_1, HelloWorld_2, HelloWorld_3 {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @Override
    public String sayWorld(String name) {
        return "World " + name;
    }

    @Override
    public String sayHelloWorld(String name) {
        return "HelloWorld " + name;
    }
}
