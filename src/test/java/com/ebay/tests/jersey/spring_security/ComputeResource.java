package com.ebay.tests.jersey.spring_security;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Path("/compute")
@Controller
public class ComputeResource {

    @Path("/secured")
    @GET
    @PreAuthorize("hasAuthority('https://api.ebay.com/oauth/scope/@public')")
    public String computeSecurily() {
        return "secret";
    }
}
