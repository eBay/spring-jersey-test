package com.ebay.tests.jersey;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.core.Application;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(JerseyTypeExcludeFilter.class)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@Import({SpringJerseyTestAutoConfiguration.class,JerseyAutoConfiguration.class
  })
//,JerseyConfigs.class})
public @interface SpringJerseyTest {

    @AliasFor("application")
    Class<? extends Application> value() default NO_APP.class;

    @AliasFor("value")
    Class<? extends Application> application() default NO_APP.class;

    Class<?>[] components() default {};

    String[] enable() default {};
    String[] disable() default {};


    final class NO_APP extends Application {}


    /**
     * Determines if default filtering should be used with
     * {@link SpringBootApplication @SpringBootApplication}. By default only
     * {@code @Controller} (when no explicit {@link #resources() controllers} are
     * defined), {@code @ControllerAdvice} and {@code WebMvcConfigurer} beans are
     * included.
     * @see #includeFilters()
     * @see #excludeFilters()
     * @return if default filters should be used
     */
    boolean useDefaultFilters() default true;

    /**
     * A set of include filters which can be used to add otherwise filtered beans to the
     * application context.
     * @return include filters to apply
     */
    Filter[] includeFilters() default {};

    /**
     * A set of exclude filters which can be used to filter beans that would otherwise be
     * added to the application context.
     * @return exclude filters to apply
     */
    Filter[] excludeFilters() default {};

}
