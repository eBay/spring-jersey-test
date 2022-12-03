package mockjaxrs2.testapp;

import java.lang.reflect.Proxy;
import java.util.Map;
import javax.ws.rs.Path;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

//@Configuration
public class MockJerseyAutoConfiguration implements ApplicationContextAware {

    //see org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration
//    @Autowired
    ApplicationContext ctx;

    @Bean
    public MockJerseyTestInstance jerseyTest() {
        ResourceConfig rc = getOrCreateResourceConfig();
        rc.property("contextConfig", ctx);
        return new MockJerseyTestInstance(rc);
    }

    private ResourceConfig getOrCreateResourceConfig() {
        Map<String, Application> appMap = ctx.getBeansOfType(Application.class);
        if (appMap.size() == 1) {
            return ResourceConfig.forApplication(appMap.values().iterator().next());
        }
        if (appMap.size() > 1) {
            throw new NoUniqueBeanDefinitionException(Application.class, appMap.keySet());
        }
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        Map<String, Object> providers = ctx.getBeansWithAnnotation(Provider.class);

        ResourceConfig rc = new ResourceConfig();
        resources.values().forEach(rc::register);
        providers.values().forEach(rc::register);
        return rc;
    }

    @Bean
    @Primary
    public TestWebTarget webTarget(MockJerseyTestInstance djt) {
        return (TestWebTarget) Proxy.newProxyInstance(this.getClass().getClassLoader(),
            new Class[]{TestWebTarget.class},
            (proxy, method, args) -> {
                WebTarget target = djt.target();
                return method.invoke(target, args);
            });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    public interface TestWebTarget extends WebTarget {}
}
