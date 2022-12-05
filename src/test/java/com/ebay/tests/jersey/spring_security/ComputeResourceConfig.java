package com.ebay.tests.jersey.spring_security;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("/")
@Configuration
public class ComputeResourceConfig extends ResourceConfig {

    public ComputeResourceConfig() {
        register(ComputeResource.class);
    }

}
