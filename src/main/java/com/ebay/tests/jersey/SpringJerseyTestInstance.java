package com.ebay.tests.jersey;

import java.util.Arrays;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.springframework.context.ApplicationContext;

public class SpringJerseyTestInstance extends JerseyTest {

    SpringJerseyTestInstance(ResourceConfig resourceConfig) {
        super(resourceConfig);
        this.resourceConfig = resourceConfig;
    }

    private boolean prepared = false;
    private final ResourceConfig resourceConfig;

    protected void prepare(ApplicationContext context, Class<?>[] components) {
        synchronized (this) {
            if (prepared) {
                return;
            }
            Arrays.stream(components)
                .filter(c -> !resourceConfig.isRegistered(c))
                .forEach(resourceConfig::register);
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
