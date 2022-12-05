package com.ebay.tests.jersey;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.ws.rs.Path;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class SpringJerseyTestAutoConfiguration implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(
        SpringJerseyTestAutoConfiguration.class);
    //see org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration
    private ApplicationContext ctx;

    @Bean
    public SpringJerseyTestInstance jerseyTest() {
        ResourceConfig rc = getOrCreateResourceConfig();
        rc.property("contextConfig", ctx);
        return new SpringJerseyTestInstance(rc);
    }

    @Bean
    @ConditionalOnBean(SpringJerseyTestInstance.class)
    public TestWebTarget webTarget(SpringJerseyTestInstance djt) {
        return (TestWebTarget) Proxy.newProxyInstance(this.getClass().getClassLoader(),
            new Class[]{TestWebTarget.class},
            (proxy, method, args) -> {
                WebTarget target = djt.target();
                return method.invoke(target, args);
            });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    private <T> T firstElement(Collection<T> c) {
        Iterator<T> it = c.iterator();
        T first = null;
        if (it.hasNext()) {
            first = it.next();
        }
        return first;
    }

    private ResourceConfig getOrCreateResourceConfig() {
        Map<String, Application> appMap = ctx.getBeansOfType(Application.class);
        if (appMap.size() == 1) {
            Application app = firstElement(appMap.values());
            log.debug("Found a single Application class {}", app);
            return ResourceConfig.forApplication(app);
        }
        if (appMap.size() > 1) {
            log.warn("Found more than a single Application class: {}", appMap.keySet());
            throw new NoUniqueBeanDefinitionException(Application.class, appMap.keySet());
        }
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        log.debug("Detected the following @Path resources: {}", resources.values());
        Map<String, Object> providers = ctx.getBeansWithAnnotation(Provider.class);
        log.debug("Detected the following @Provider classes: {}", providers.values());

        ResourceConfig rc = new ResourceConfig();
        resources.values().forEach(rc::register);
        providers.values().forEach(rc::register);
        return rc;
    }
}
