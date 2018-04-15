package com.teclick.framework.hessian.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.PriorityOrdered;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.caucho.HessianServiceExporter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Iterator;

public class HessianContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(HessianContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addBeanFactoryPostProcessor(new HessianFactoryPostProcessor());
    }

    private static class HessianFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

        private BeanDefinitionRegistry registry;

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            this.registry = registry;
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
                Class[] clazzInterfaces = clazz.getInterfaces();
                if (null != clazzInterfaces) {
                    for (Class clazzInterface : clazzInterfaces) {
                        Annotation[] annotations = clazzInterface.getAnnotations();
                        if (null != annotations) {
                            for (Annotation annotation : annotations) {
                                HessianAPI hessianAPI = annotation instanceof HessianAPI ? ((HessianAPI) annotation) : null;
                                if (null != hessianAPI) {
                                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(HessianServiceExporter.class);
                                    beanDefinitionBuilder.addPropertyReference("service", definitionName);
                                    beanDefinitionBuilder.addPropertyValue("serviceInterface", clazzInterface);
                                    registry.registerBeanDefinition(hessianAPI.endpoint(), beanDefinitionBuilder.getBeanDefinition());
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
                        String beanId = hessianClient.name();
                        if (registry.containsBeanDefinition(beanId)) {
                            logger.info("The bean [" + beanId + "] already exists, skipping.");
                        } else {
                            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(HessianProxyFactoryBean.class);
                            beanDefinitionBuilder.addPropertyValue("serviceUrl", hessianClient.endpoint());
                            beanDefinitionBuilder.addPropertyValue("serviceInterface", field.getType());

                            registry.registerBeanDefinition(beanId, beanDefinitionBuilder.getBeanDefinition());
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
                logger.warn("Bean class not found.", e);
            }
            return clazz;
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }

}
