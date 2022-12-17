package com.ebay.tests.jersey;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

public class SpringJerseyTestInstance extends JerseyTest {

    private static final Logger log = LoggerFactory.getLogger(SpringJerseyTestInstance.class);

    SpringJerseyTestInstance(ResourceConfig resourceConfig) {
        super(resourceConfig);
        this.resourceConfig = resourceConfig;
    }

    private boolean prepared = false;
    private final ResourceConfig resourceConfig;

    protected void prepare(ApplicationContext context, SpringJerseyTest springJerseyTest) {
        synchronized (this) {
            if (prepared) {
                return;
            }
            Arrays.stream(springJerseyTest.components())
                .filter(c -> !resourceConfig.isRegistered(c))
                .forEach(resourceConfig::register);
            if (springJerseyTest.components().length == 0) {
                Map<String, Object> resources = context.getBeansWithAnnotation(Path.class);
                log.debug("Detected the following @Path resources: {}", resources.values());
                Map<String, Object> providers = context.getBeansWithAnnotation(Provider.class);
                log.debug("Detected the following @Provider classes: {}", providers.values());

                Predicate<Object> isNotRegistered = o -> !resourceConfig.isRegistered(
                    AopUtils.getTargetClass(o));

                resources.values()
                    .stream()
                    .filter(isNotRegistered)
                    .forEach(resourceConfig::register);
                providers.values()
                    .stream()
                    .filter(isNotRegistered)
                    .forEach(resourceConfig::register);
            }
            resourceConfig.register(context.getBean(RequestContextPopulatorFilter.class));
            prepared = true;
        }
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new InMemoryTestContainerFactory();
    }


    @Override
    protected Application configure() {
        return resourceConfig;
    }

}
