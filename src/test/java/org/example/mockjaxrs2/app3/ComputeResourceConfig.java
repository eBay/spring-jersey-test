package org.example.mockjaxrs2.app3;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("/")
@Configuration
public class ComputeResourceConfig extends ResourceConfig {

    @PostConstruct
    public void init() {
        register(ComputeResource.class);
//        register(HelloResource.class);
    }
}
