package com.ebay.tests.jersey.auto_discovery_resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Controller;

@Path("/hello")
@Controller
public class HelloResource {

    @Path("/greet")
    @GET
    public String greet() {
        return "Hello, world!";
    }
}
