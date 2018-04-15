package com.teclick.framework.hessian.spring.boot.autoconfigure;

import org.springframework.beans.factory.annotation.Required;

import java.lang.annotation.*;

/**
 * Created by Nelson Li on 2018-04-11 18:13.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HessianAPI {

    @Required
    String endpoint();

}
