### Hessian spring boot starter

### Server side
#### 1. Add dependency in your pom
```` xml
<dependency>
    <groupId>com.teclick.framework</groupId>
    <artifactId>hessian-spring-boot-starter</artifactId>
    <version>0.0.1-master</version>
</dependency>
````

#### 2. Write an interface
##### 2.1 Endpoint use interface resource path
```` java
package com.teclick.spring.boot.demo.client;
import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianAPI;

@HessianAPI  // <-- Define service endpoint 1
public interface HelloWorld_1 {
    String sayHello(String name);
}
````
##### 2.2 Customer annotation value as endpoint
```` java
package com.teclick.spring.boot.demo.client;
import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianAPI;

@HessianAPI("HelloWorld_2") // <-- Define service endpoint 2
public interface HelloWorld_2 {
    String sayWorld(String name);
}
````
#### 3. Write interface implement
```` java
package com.teclick.spring.boot.demo.server;

import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianService;
import com.teclick.spring.boot.demo.client.HelloWorld_1;
import com.teclick.spring.boot.demo.client.HelloWorld_2;
import com.teclick.spring.boot.demo.client.HelloWorld_3;

@HessianService // <-- Publishing the service
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
````

#### 4. Run your service project
```` js
java -jar your_package.jar  ## you will see the follow log
Mapped URL path [/com/teclick/spring/boot/demo/client/HelloWorld_1] onto handler '/com/teclick/spring/boot/demo/client/HelloWorld_1'
Mapped URL path [/HelloWorld_2] onto handler '/HelloWorld_2'
Mapped URL path [/a/b/c/HelloWorld_3] onto handler '/a/b/c/HelloWorld_3'````

### Client side
#### 1. Test your project
```` java
package com.example.demo.test;

import com.teclick.spring.boot.demo.client.HelloWorld_1;
import com.teclick.spring.boot.demo.client.HelloWorld_2;
import com.teclick.spring.boot.demo.client.HelloWorld_3;
import com.teclick.framework.hessian.spring.boot.autoconfigure.HessianClient;
import org.springframework.stereotype.Component;

@Component
public class TestClient {

    @HessianClient(value = "first", host = "localhost")
    private HelloWorld_1 helloWorld_1;

    @HessianClient(value = "second", host = "${host.name}")
    private HelloWorld_2 helloWorld_2;

    @HessianClient(value = "third", host = "{host.name}, port = "8080")
    private HelloWorld_3 helloWorld_3;
    
    // the follow code is your method
    public String yourMethod_1(String name) {
        return helloWorld_1.sayHello(name); 
    }
    ......
}
````

### Enjoy
