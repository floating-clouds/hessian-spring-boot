package com.teclick.framework.hessian.spring.boot.autoconfigure;

import org.springframework.beans.factory.annotation.Required;

import java.lang.annotation.*;

/**
 * Created by Nelson Li on 2018-04-11 10:29.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HessianClient {

    @Required
    String name();

    @Required
    String endpoint();

}
