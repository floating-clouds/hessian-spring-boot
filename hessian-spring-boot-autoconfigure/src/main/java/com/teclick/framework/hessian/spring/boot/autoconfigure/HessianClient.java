/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
