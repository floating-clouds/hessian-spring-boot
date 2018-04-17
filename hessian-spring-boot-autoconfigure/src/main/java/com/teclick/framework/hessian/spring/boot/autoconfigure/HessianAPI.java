package com.teclick.framework.hessian.spring.boot.autoconfigure;

import java.lang.annotation.*;

/**
 * Created by Nelson Li on 2018-04-11 18:13.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HessianAPI {

    String value() default "";

}
