package com.teclick.spring.boot.demo.client;

import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianAPI;

/**
 * Customer hessian entry with annotation value
 * "/HelloWorld_2"
 * @see HessianAPI
 *
 * Created by Nelson Li 2018-04-17 23:19
 */
@HessianAPI("/a/b/c/HelloWorld_3")
public interface HelloWorld_3 {

    String sayHelloWorld(String name);

}
