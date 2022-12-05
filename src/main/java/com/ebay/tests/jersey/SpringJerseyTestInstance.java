package com.ebay.tests.jersey;

import java.util.Collection;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

public class SpringJerseyTestInstance extends JerseyTest {

    SpringJerseyTestInstance(ResourceConfig resourceConfig) {
        super(resourceConfig);
        this.resourceConfig = resourceConfig;
    }

    private boolean prepared = false;
    private final ResourceConfig resourceConfig;

    protected void prepare(Collection<Class<?>> components) {
        synchronized (this) {
            if (prepared) {
                return;
            }
            components.forEach(resourceConfig::register);
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
