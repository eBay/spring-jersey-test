package com.ebay.tests.jersey.request_scope_injections;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

@Provider
@Component
public class ReqFilter implements ContainerResponseFilter {

    @Context
    HttpServletRequest httpReq;

    @Override
    public void filter(ContainerRequestContext requestContext,
        ContainerResponseContext responseContext) {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add("echo", httpReq.getHeader("custom"));
        headers.add("local-port", httpReq.getLocalPort());
        headers.add("remote-port", httpReq.getRemotePort());
        headers.add("local-address", httpReq.getLocalAddr());
        headers.add("remote-address", httpReq.getRemoteAddr());
        headers.add("remote-host", httpReq.getRemoteHost());
        headers.add("local-host", httpReq.getLocalName());
        headers.add("path-info", httpReq.getPathInfo());
        headers.add("query-string", httpReq.getQueryString());
    }

}
