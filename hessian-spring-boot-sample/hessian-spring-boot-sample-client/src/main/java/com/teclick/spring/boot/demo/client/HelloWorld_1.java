package com.teclick.spring.boot.demo.client;

import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianAPI;

/**
 * Endpoint is the interface resource path
 * "/com/teclick/spring/boot/demo/client/HelloWorld_1"
 * @see HessianAPI
 *
 * Created by Nelson Li 2018-04-17 23:18
 */
@HessianAPI
public interface HelloWorld_1 {

    String sayHello(String name);

}
