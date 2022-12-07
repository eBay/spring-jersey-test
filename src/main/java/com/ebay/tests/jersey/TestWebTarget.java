package com.ebay.tests.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;

public interface TestWebTarget extends WebTarget {

    Client client();

    Application application();
}
