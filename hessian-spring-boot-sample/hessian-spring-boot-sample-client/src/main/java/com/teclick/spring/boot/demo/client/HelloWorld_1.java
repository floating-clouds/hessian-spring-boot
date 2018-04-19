package com.teclick.spring.boot.demo.client;

import javax.ws.rs.Path;

/**
 * Endpoint is the interface resource path
 * "/com/teclick/spring/boot/demo/client/HelloWorld_1"
 *
 *  Created by Nelson Li 2018-04-17 23:18
 */
@Path("")
public interface HelloWorld_1 {

    String sayHello(String name);

}
