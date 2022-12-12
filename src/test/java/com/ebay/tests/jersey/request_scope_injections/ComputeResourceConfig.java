package com.ebay.tests.jersey.request_scope_injections;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("/")
@Configuration
public class ComputeResourceConfig extends ResourceConfig {

    public ComputeResourceConfig() {
        register(ComputeResource.class);
        register(ReqFilter.class);
    }

}
