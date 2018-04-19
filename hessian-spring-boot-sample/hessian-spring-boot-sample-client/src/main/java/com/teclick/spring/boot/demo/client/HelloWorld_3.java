package com.teclick.spring.boot.demo.client;

import javax.ws.rs.Path;

/**
 * Customer hessian entry with annotation value
 * "/HelloWorld_2"
 *
 * Created by Nelson Li 2018-04-17 23:19
 */
@Path("/a/b/c/HelloWorld_3")
public interface HelloWorld_3 {

    String sayHelloWorld(String name);

}
