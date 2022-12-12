# Spring Jersey Test

Running [JerseyTests](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/test-framework.html)
in Spring-Boot project, easily.

This project suppose to bring similar testing capabilities of Spring's MockMVC to JAX-RS+Spring users - 
the ability to create a very lightweight, very customizable, yet fully functional container for your
testing suites. You can test the controller integration with the JAX-RS filters, interceptors, validators
and other bindings without having to run an embedded server. `@SpringBootTest` is great, in some use-cases 
it can be slow to start and/or cumbersome to config. 

# Usage

All test classes must annotate the test class with `@SpringJerseyTest`, and have a 
`@Autowire TestWebTarget target;` field. Scroll down a few more lines for examples.

## Maven dependency

```xml

<dependenciy>
  <artifactId>spring-jersey-test</artifactId>
  <groupId>com.ebay.tests</groupId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>test</scope>
</dependenciy>
```

## SpringJerseyTest annotation

The `@SpringJerseyTest` will mark the test as a Junit5 test that should run against an in-memory
Jersey container. Please check the examples part for more details about the configurations.

## TestWebTarget interface

The `TestWebTarget` is an interface that extends the `WebTarget` interface, and should be injected
into the test using `@Autowired`. The `TestWebTarget` was created for two reasons:

1. Disambiguate your injection in case you already have a `WebTarget` bean
2. Providing more container visibility via the web target.

It will provide a JAX-RS client to communicate with the in-memory container. The extended interface
will allow you to access the registered Application class, or the JAX-RS parent `Client` instance
for the
`WebTarget`.

## Single Application class

The simplest way is to just add `@SpringJerseyTest` annotation

```java
import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;


@SpringJerseyTest
public class OrderResourceTest {

    @Autowired
    TestWebTarget target;

    @MockBean
    OrderService orderService;

    @Test
    void testGetOrderInfo() {
        Mockito.when(orderService.getOrder(1234))
            .thenReturn(new Order("new order", OrderStatus.NEW));
        try (Response response = target.path("/order/1234").request().get()) {
            assertThat(response.getStatus()).isEqualTo(200);
            String json = response.readEntity(String.class);
            assertThat(json).isEqualTo("{\"name\":\"new order\", \"status\":\"NEW\"}");
        }
    }
}
```

This would assume you have a single `Application` class available on your classpath, and that you
want
to apply all filters and components registered through it as part of your test.

## Multiple `Application` classes

If you have more than one `Application` class in the app, you can explicit tell it which one you
want to
test:

```java
import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;


@SpringJerseyTest(application = PetStoreApplication.class)
public class OrderResourceTest {
    //...
}
```

## Custom controller settings

If you need to have a tighter control on the components you want to test - just one controller, a
specific set of
features/filters/etc, you can use the `component` field.

```java
import com.ebay.tests.jersey.SpringJerseyTest;
import com.ebay.tests.jersey.TestWebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJerseyTest(components = {OrderResource.class, OrderRateLimiterFeature.class})
public class OrderResourceTest {
    //...
}
```

This can be ideal if your application contains many operational components that are not related to
your business logic that you want to test - components that are complex to mock or slow to initiate 
(corporates with legacy heavy-weight platforms, I'm looking at you!)

## Other use cases

Check out the unit tests for more examples:

* [Exception Mappers](src/test/java/com/ebay/tests/jersey/exception_mappers/TestExceptionMapper.java)
* [Spring Security](src/test/java/com/ebay/tests/jersey/spring_security/TestSecurity.java)
* [Spring bean integrations](src/test/java/com/ebay/tests/jersey/mock_bean/TestMockBean.java)
* [Request Filters](src/test/java/com/ebay/tests/jersey/request_scope_injections/TestRequestScopeInjection.java)

---

**Temporary**
Raptor walk-arounds:

* Until [RAPTOR-29863](https://jirap.corp.ebay.com/browse/RAPTOR-29863) is released,
  add `@Import(RaptorSseEventListenerRegistry.class)`
* Until [RAPTOR-29804](https://jirap.corp.ebay.com/browse/RAPTOR-29804) is accepted and release, if
  you create a `FideliusClient` bean, make sure to annotate it as `@Lazy`
