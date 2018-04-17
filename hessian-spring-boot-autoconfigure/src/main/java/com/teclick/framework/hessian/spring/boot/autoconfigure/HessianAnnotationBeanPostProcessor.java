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

    private static final Logger logger = LoggerFactory.getLogger(HessianAnnotationBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            HessianClient hessianClient = field.getAnnotation(HessianClient.class);
            if (null != hessianClient) {
                logger.info("Handle bean [" + beanName + "] field: " + field.getName());
                if (field.isAccessible()) {
                    setHessianClient(bean, field, hessianClient);
                } else {
                    field.setAccessible(true);
                    try {
                        setHessianClient(bean, field, hessianClient);
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

    private void setHessianClient(Object bean, Field field, HessianClient hessianClient) {
        try {
            String beanId = hessianClient.value().trim();
            if ("".equals(beanId)) {
                //Introspector.decapitalize(field.getType().getSimpleName());
                beanId = field.getType().getName();
            }
            Object propertyBean = applicationContext.getBean(beanId);
            field.set(bean, propertyBean);
        } catch (IllegalAccessException e) {
            logger.error("Set hessian client issue", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
