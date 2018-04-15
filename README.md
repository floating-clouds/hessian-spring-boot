### Hessian spring boot starter

### Server side
#### 1. Add dependency in your pom
```` xml
<dependency>
    <groupId>com.teclick.framework</groupId>
    <artifactId>hessian-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
````

#### 2. Write an interface
```` java
package com.example.demo.client;

import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianAPI;

@HessianAPI(endpoint = "/HelloWorldService")
public interface HelloWorldService {
    String sayHello(String name);
}
````

#### 3. Write interface implement
```` java
package com.example.demo.server;

import com.example.demo.client.HelloWorldService;
import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianService;

@HessianService
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String sayHello(String name) {
        return "Hello World! " + name;
    }
}
````

#### 4. Run your project

### Client side
#### 1. Test your project
```` java
package com.example.demo.test;

import com.example.demo.client.HelloWorldService;
import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TestClient {
    @HessianClient(name = "aaa", endpoint = "http://${host.ip}:8080/HelloWorldService")
    @Resource(name = "aaa")
    public HelloWorldService helloWorldService;
}
````
