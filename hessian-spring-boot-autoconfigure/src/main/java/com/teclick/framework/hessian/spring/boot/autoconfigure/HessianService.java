package com.teclick.framework.hessian.spring.boot.autoconfigure;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by Nelson Li on 2018-04-10 15:51.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface HessianService {

    String value() default "";

}
