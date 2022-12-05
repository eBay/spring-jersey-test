package com.ebay.tests.jersey.multiple_resources;

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
