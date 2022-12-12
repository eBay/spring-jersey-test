package com.ebay.tests.jersey.request_scope_injections;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Path("/compute")
@Controller
public class ComputeResource {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Context
    HttpServletRequest ctxRequest;

    @Autowired
    ApplicationContext ctx;

    @Path("/autowired")
    @GET
    public String httpAutowired() {
        return "autowired " + httpServletRequest.getMethod();
    }

    @Path("/context")
    @GET
    public String httpContext() {
        return "context " + ctxRequest.getMethod();
    }


}
