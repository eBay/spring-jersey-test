package com.ebay.tests.jersey.auto_discovery_resource;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("/")
@Configuration
public class GoodbyeResourceConfig extends ResourceConfig {

    @PostConstruct
    public void init() {
        register(GoodbyeResource.class);
        register(HelloResource.class);
    }
}
