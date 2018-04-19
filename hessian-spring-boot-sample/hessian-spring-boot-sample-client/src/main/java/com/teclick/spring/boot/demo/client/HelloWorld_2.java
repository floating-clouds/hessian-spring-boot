package com.teclick.spring.boot.demo.client;

import javax.ws.rs.Path;

/**
 * Custom hessian entry with annotation value
 * "/HelloWorld_2"
 *
 * Created by Nelson Li 2018-04-17 23:19
 */
@Path("/HelloWorld_2")
public interface HelloWorld_2 {

    String sayWorld(String name);

}
