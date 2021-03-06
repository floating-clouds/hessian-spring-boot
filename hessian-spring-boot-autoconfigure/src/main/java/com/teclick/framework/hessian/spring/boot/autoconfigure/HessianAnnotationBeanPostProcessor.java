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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * Created by Nelson Li on 2018-04-16
 */
public class HessianAnnotationBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(HessianAnnotationBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            HessianClient hessianClient = field.getAnnotation(HessianClient.class);
            if (null != hessianClient) {
                logger.info("Found hessian client field: [{}.{}]", beanName, field.getName());
                if (field.isAccessible()) {
                    setHessianClient(bean, field, hessianClient, beanName);
                } else {
                    field.setAccessible(true);
                    try {
                        setHessianClient(bean, field, hessianClient, beanName);
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void setHessianClient(Object bean, Field field, HessianClient hessianClient, String beanName) {
        try {
            String beanId = hessianClient.value().trim();
            if ("".equals(beanId)) {
                //Introspector.decapitalize(field.getType().getSimpleName());
                beanId = field.getType().getName();
            }
            Object propertyBean = applicationContext.getBean(beanId);
            field.set(bean, propertyBean);
            logger.info("Hessian client field [{}.{}] assigned {}", beanName, field.getName(), propertyBean);
        } catch (IllegalAccessException e) {
            logger.error("Assigned hessian client", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
