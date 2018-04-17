package com.teclick.spring.boot.demo.client;

import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianAPI;

/**
 * Custom hessian entry with annotation value
 * "/HelloWorld_2"
 * @see HessianAPI
 * Created by Nelson Li 2018-04-17 23:19
 */
@HessianAPI("HelloWorld_2")
public interface HelloWorld_2 {

    String sayWorld(String name);

}
