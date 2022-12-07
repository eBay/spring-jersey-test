package com.ebay.tests.jersey;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;

public class TestWebTargetFactory {

    public TestWebTarget create(SpringJerseyTestInstance jerseyTestInstance) {
        return (TestWebTarget) Proxy.newProxyInstance(this.getClass().getClassLoader(),
            new Class[]{TestWebTarget.class},
            (proxy, method, args) -> {
                if (methodMatch(method, "client", 0, Client.class)) {
                    return jerseyTestInstance.client();
                }
                if (methodMatch(method, "application", 0, Application.class)) {
                    return jerseyTestInstance.configure();
                }
                WebTarget target = jerseyTestInstance.target();
                return method.invoke(target, args);
            });
    }

    private boolean methodMatch(Method method, String name, int paramCount, Class<?> returnType) {
        return name.equals(method.getName()) &&
            method.getParameterCount() == paramCount &&
            returnType.equals(method.getReturnType());
    }
}
