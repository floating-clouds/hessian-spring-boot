package com.teclick.framework.hessian.spring.boot.autoconfigure;

import java.lang.annotation.*;

/**
 * Value is the hessian service endpoint
 * When value does not set
 *   the entry will be the interface resource path
 * else
 *   the endpoint is the value you set
 * Created by Nelson Li on 2018-04-11 18:13.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HessianAPI {

    String value() default "";

}
