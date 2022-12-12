package com.ebay.tests.jersey;

import java.net.URI;
import javax.annotation.Priority;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import org.springframework.mock.web.MockHttpServletRequest;

@Provider
@Priority(Integer.MAX_VALUE)
public class RequestContextPopulatorFilter implements ContainerRequestFilter,
    ContainerResponseFilter {

    private final ThreadLocal<MockHttpServletRequest> mockReqHolder = new ThreadLocal<>();

    private final ThreadLocal<Boolean> processed = new ThreadLocal<>();

    @Override
    public void filter(ContainerRequestContext ctx) {
        populateMockHttpRequest(ctx);
        processed.set(true);
    }

    public HttpServletRequest getCurrentHttpRequest() {
        return mockReqHolder.get();
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
        ContainerResponseContext responseContext) {

        populateMockHttpRequest(requestContext);
        processed.set(false);
    }

    private void populateMockHttpRequest(ContainerRequestContext ctx) {
        if (Boolean.TRUE.equals(processed.get())) {
            return;
        }
        Cookie[] jshCookies = ctx.getCookies().values().stream().map(cookie -> {
            Cookie jshCookie = new Cookie(cookie.getName(), cookie.getValue());
            jshCookie.setDomain(cookie.getDomain());
            jshCookie.setPath(cookie.getPath());
            jshCookie.setVersion(cookie.getVersion());
            return jshCookie;
        }).toArray(Cookie[]::new);

        UriInfo uriInfo = ctx.getUriInfo();
        URI reqUri = uriInfo.getRequestUri();
        MockHttpServletRequest mhsr = new MockHttpServletRequest(ctx.getMethod(),
            reqUri.toString());
        mhsr.setContentType(ctx.getHeaderString("content-type"));
        mhsr.setCookies(jshCookies);
        mhsr.setPathInfo(uriInfo.getPath());
        uriInfo.getQueryParameters().forEach(
            (key, values) -> mhsr.setParameter(key, values.toArray(new String[0])));
        mhsr.setLocalPort(reqUri.getPort());
        mhsr.setRemotePort(reqUri.getPort());
        mhsr.setQueryString(reqUri.getQuery());
        ctx.getHeaders().forEach((name, values) -> mhsr.addHeader(name, values.toArray()));
        ctx.getPropertyNames().forEach(n -> mhsr.setAttribute(n, ctx.getProperty(n)));
        mockReqHolder.set(mhsr);
        processed.set(true);
    }

}
