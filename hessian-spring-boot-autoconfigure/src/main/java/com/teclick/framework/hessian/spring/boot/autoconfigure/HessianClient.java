package com.teclick.framework.hessian.spring.boot.autoconfigure;

import java.lang.annotation.*;

/**
 * Created by Nelson Li on 2018-04-11 10:29.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HessianClient {

    String value() default "";

    String protocol() default "http";

    String host() default "127.0.0.1";

    String port() default "8080";

    String endpoint() default "";

}
