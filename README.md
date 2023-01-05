# Spring Jersey Test

Spring Jersey Test is a project that allows you to easily
run [JerseyTests](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/test-framework.html)
in your Spring Boot project. This project brings the testing capabilities of Spring's MockMVC to JAX-RS+Spring users,
giving you the ability to create lightweight, customizable, and fully functional containers for your testing
suites. You can test the integration of your controllers with JAX-RS filters, interceptors,
validators, and other bindings without the need to run an embedded server. While `@SpringBootTest`
is useful in some cases, it can be slow to start and/or cumbersome to configure.

# Usage

Add the following Maven dependency to your project:

```xml
<dependency>
  <artifactId>spring-jersey-test</artifactId>
  <groupId>com.ebay.tests</groupId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>test</scope>
</dependency>
```

Annotate your test class with `@SpringJerseyTest` and autowire a `TestWebTarget` field:

```java
@SpringJerseyTest
public class OrderResourceTest {

    @Autowired
    TestWebTarget target;

    // Test methods go here
}
```

The `TestWebTarget` interface extends the `WebTarget` interface. It provides a JAX-RS client for 
communication with the in-memory container and allows access to the registered Application class 
or the JAX-RS parent Client instance for the WebTarget.

See the examples below for more details on configurations.

## Testing with a Single `Application` Class

If you have a single `Application` class on your classpath and want to apply all filters and components 
registered through it as part of your test, you can simply use the `@SpringJerseyTest` annotation as 
shown above. Notice that if you include a context path in the `@Application` annotation, you will have 
to omit that prefix from the client's call.

## Testing with Multiple Application Classes

If you have multiple Application classes in your app, you can specify which one you want to test by 
including it in the `@SpringJerseyTest` annotation:

```java
@SpringJerseyTest(application = PetStoreApplication.class)
public class OrderResourceTest {

    @Autowired
    TestWebTarget target;

    // Test methods go here
}
```

## Testing Specific Controllers and Components

If you want to include just specific controllers and other components, such as filters and interceptors,
you can specify these in the `@SpringJerseyTest` annotation:

```java
@SpringJerseyTest(components = {OrderResource.class, OrderRateLimiterFeature.class})
public class OrderResourceTest {

    @Autowired
    TestWebTarget target;

    // Test methods go here
}
```

This can be particularly useful when your application contains many operational components that are 
not related to your business logic but may be difficult to mock or slow to initialize. This can be 
especially helpful in cases where the application integrates with legacy or heavy-weight platforms.

# Examples

Here is an example of using the default settings of `@SpringJerseyTest` with `TestWebTarget` to test 
an `OrderResource`:

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

## Other use cases

* [Exception Mappers](src/test/java/com/ebay/tests/jersey/exception_mappers/TestExceptionMapper.java)
* [Spring Security](src/test/java/com/ebay/tests/jersey/spring_security/TestSecurity.java)
* [Spring bean integrations](src/test/java/com/ebay/tests/jersey/mock_bean/TestMockBean.java)
* [Request Filters](src/test/java/com/ebay/tests/jersey/request_scope_injections/TestRequestScopeInjection.java)

For more examples, please refer to the unit tests.

---

**Temporary, eBay only related**

Raptor walk-arounds:

* Until [RAPTOR-29863](https://jirap.corp.ebay.com/browse/RAPTOR-29863) is released,
  add `@Import(RaptorSseEventListenerRegistry.class)`
* Until [RAPTOR-29804](https://jirap.corp.ebay.com/browse/RAPTOR-29804) is accepted and release, if
  you create a `FideliusClient` bean, make sure to annotate it as `@Lazy`
