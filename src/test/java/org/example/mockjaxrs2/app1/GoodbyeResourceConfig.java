package org.example.mockjaxrs2.app1;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@ApplicationPath("goodbye")
@Configuration
public class GoodbyeResourceConfig extends ResourceConfig {

    @PostConstruct
    public void init() {
        register(GoodbyeResource.class);
    }
}
