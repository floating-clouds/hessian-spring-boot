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
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.caucho.HessianServiceExporter;

import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * Created by Nelson Li on 2018-04-15 10:29.
 */
public class HessianContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(HessianContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addBeanFactoryPostProcessor(new HessianFactoryPostProcessor());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private static class HessianFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

        private BeanDefinitionRegistry registry;

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            this.registry = registry;
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(HessianAnnotationBeanPostProcessor.class);
            registry.registerBeanDefinition("hessianAnnotationBeanPostProcessor", beanDefinition);
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            Iterator<String> iterator = beanFactory.getBeanNamesIterator();
            while (iterator.hasNext()) {
                String beanName = iterator.next();
                if (beanFactory.containsBeanDefinition(beanName)) {
                    AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) beanFactory.getBeanDefinition(beanName);
                    String beanClassName = beanDefinition.getBeanClassName();
                    Class clazz = getClassByName(beanClassName);
                    if (null != clazz) {
                        addHessianServiceImplBean(beanName, clazz);
                        addHessianClientBean(clazz);
                    }
                }
            }
        }

        private void addHessianServiceImplBean(String definitionName, Class clazz) {
            Annotation hessianService = clazz.getAnnotation(HessianService.class);
            if (null != hessianService) {
                logger.info("Found hessian service implement bean [{}]", definitionName);
                Class[] clazzInterfaces = clazz.getInterfaces();
                if (null != clazzInterfaces) {
                    for (Class clazzInterface : clazzInterfaces) {
                        Annotation[] annotations = clazzInterface.getAnnotations();
                        if (null != annotations) {
                            for (Annotation annotation : annotations) {
                                Path hessianAPI = annotation instanceof Path ? ((Path) annotation) : null;
                                if (null != hessianAPI) {
                                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(HessianServiceExporter.class);
                                    beanDefinitionBuilder.addPropertyReference("service", definitionName);
                                    beanDefinitionBuilder.addPropertyValue("serviceInterface", clazzInterface);
                                    String endpoint = hessianAPI.value().trim();
                                    if ("".equals(endpoint)) {
                                        endpoint = "/" + clazzInterface.getName().replace(".", "/");
                                    } else if (!endpoint.startsWith("/")) {
                                        endpoint = "/" + endpoint;
                                    }
                                    registry.registerBeanDefinition(endpoint, beanDefinitionBuilder.getBeanDefinition());
                                    logger.info("Publishing the endpoint [{}]", endpoint);
                                }
                            }
                        }
                    }
                }
            }
        }

        private void addHessianClientBean(Class clazz) {
            Field[] fields = clazz.getDeclaredFields();
            if (null != fields) {
                for (Field field : fields) {
                    HessianClient hessianClient = field.getAnnotation(HessianClient.class);
                    if (null != hessianClient) {
                        String beanId = hessianClient.value();
                        if ("".equals(beanId)) {
                            //Introspector.decapitalize(field.getType().getSimpleName());
                            beanId = field.getType().getName();
                        }

                        if (registry.containsBeanDefinition(beanId)) {
                            logger.info("The hessian client bean [{}] already exists, skipping.", beanId);
                        } else {
                            String endpoint = hessianClient.endpoint().trim();
                            if ("".equals(endpoint)) {
                                Path hessianAPI = field.getType().getAnnotation(Path.class);
                                String apiEndpoint = hessianAPI.value().trim();
                                if ("".equals(apiEndpoint)) {
                                    endpoint = field.getType().getName().replace(".", "/");
                                } else {
                                    if (apiEndpoint.startsWith("/")) {
                                        endpoint = apiEndpoint.substring(1);
                                    } else {
                                        endpoint = apiEndpoint;
                                    }
                                }
                            } else {
                                if (endpoint.startsWith("/")) {
                                    endpoint = endpoint.substring(1);
                                }
                            }

                            String serviceUrl = String.format("%s://%s:%s/%s", hessianClient.protocol(), hessianClient.host(), hessianClient.port(), endpoint);
                            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(HessianProxyFactoryBean.class);
                            beanDefinitionBuilder.addPropertyValue("serviceUrl", serviceUrl);
                            beanDefinitionBuilder.addPropertyValue("serviceInterface", field.getType());

                            registry.registerBeanDefinition(beanId, beanDefinitionBuilder.getBeanDefinition());
                            logger.info("Register hessian client bean[{}] success", beanId);
                        }
                    }
                }
            }
        }

        private Class getClassByName(String className) {
            Class clazz = null;
            try {
                if (null != className) {
                    clazz = Class.forName(className);
                }
            } catch (ClassNotFoundException e) {
                logger.warn("ClassNotFoundException: " + className, e);
            }
            return clazz;
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }

}
