package com.ebay.tests.jersey;

import com.ebay.tests.jersey.SpringJerseyTest.NO_APP;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.filter.StandardAnnotationCustomizableTypeExcludeFilter;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

public class JerseyTypeExcludeFilter extends
    StandardAnnotationCustomizableTypeExcludeFilter<SpringJerseyTest> {

    private static final String[] OPTIONAL_INCLUDES = {"com.fasterxml.jackson.databind.Module",
        "org.springframework.security.config.annotation.web.WebSecurityConfigurer",
        "org.springframework.security.web.SecurityFilterChain", "org.thymeleaf.dialect.IDialect"};
    private static final Set<Class<?>> DEFAULT_INCLUDES;

    static {
        Set<Class<?>> includes = new LinkedHashSet<>();
//        includes.add(ControllerAdvice.class);
        includes.add(JsonComponent.class);
//        includes.add(WebMvcConfigurer.class);
//        includes.add(ExceptionMapper.class);
//        includes.add(WebMvcRegistrations.class);
        includes.add(Provider.class);
        includes.add(javax.servlet.Filter.class);

        includes.add(ContainerRequestFilter.class);
        includes.add(ContainerResponseFilter.class);

        includes.add(FilterRegistrationBean.class);
        includes.add(DelegatingFilterProxyRegistrationBean.class);
//        includes.add(HandlerMethodArgumentResolver.class);
//        includes.add(HttpMessageConverter.class);
        includes.add(ErrorAttributes.class);
        includes.add(Converter.class);
        includes.add(GenericConverter.class);
//        includes.add(HandlerInterceptor.class);
        for (String optionalInclude : OPTIONAL_INCLUDES) {
            try {
                includes.add(ClassUtils.forName(optionalInclude, null));
            } catch (Exception ex) {
                // Ignore
            }
        }
        DEFAULT_INCLUDES = Collections.unmodifiableSet(includes);
    }

    protected JerseyTypeExcludeFilter(Class<?> testClass) {
        super(testClass);
        this.application = getAnnotation().getValue("application", Class.class)
            .orElse(SpringJerseyTest.NO_APP.class);
        this.components = getAnnotation().getValue("components", Class[].class)
            .orElse(NO_CONTROLLERS);
        if (application != NO_APP.class && !ObjectUtils.isEmpty(components)) {
            throw new BeanDefinitionValidationException(
                "Both 'application' and 'components' properties were defined: application="
                    + this.application + " and components=" + Arrays.toString(this.components));
        }
    }

    private static final Set<Class<?>> DEFAULT_INCLUDES_AND_RESOURCES;
    private static final Class<?>[] NO_CONTROLLERS = {};

    static {
        Set<Class<?>> includes = new LinkedHashSet<>(DEFAULT_INCLUDES);
        includes.add(Path.class);
        includes.add(Provider.class);
        includes.add(Application.class);
        includes.add(ApplicationPath.class);
        includes.add(ResourceConfig.class);
        DEFAULT_INCLUDES_AND_RESOURCES = Collections.unmodifiableSet(includes);
    }

    private final Class<? extends Application> application;
    private final Class<?>[] components;

    @Override
    protected Set<Class<?>> getDefaultIncludes() {
        if (ObjectUtils.isEmpty(this.components) && this.application == NO_APP.class) {
            return DEFAULT_INCLUDES_AND_RESOURCES;
        }
//        if (ObjectUtils.isEmpty(this.resources)) {
//        }
        return DEFAULT_INCLUDES;
    }

    @Override
    protected Set<Class<?>> getComponentIncludes() {
        List<Class<?>> includes = new ArrayList<>();
        includes.addAll(Arrays.asList(this.components));
        if (application != NO_APP.class) {
            includes.add(application);
        }
        return new LinkedHashSet<>(includes);
    }
}
