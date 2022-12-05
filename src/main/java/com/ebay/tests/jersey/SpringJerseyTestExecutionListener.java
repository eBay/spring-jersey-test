package com.ebay.tests.jersey;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class SpringJerseyTestExecutionListener extends AbstractTestExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(
        SpringJerseyTestExecutionListener.class);

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        SpringJerseyTestInstance instance = getJerseyTest(testContext);
        if (instance != null) {
            log.debug("Starting up JerseyTest instance");
            instance.setUp();
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        SpringJerseyTestInstance instance = getJerseyTest(testContext);
        if (instance != null) {
            log.debug("Shutting down JerseyTest instance");
            instance.tearDown();
        }
    }

    @Override
    public void prepareTestInstance(TestContext testContext) {
        // before instance container created
        SpringJerseyTestInstance instance = getJerseyTest(testContext);
        if (instance == null) {
            return;
        }
        SpringJerseyTest mockJerseyTest = AnnotatedElementUtils.findMergedAnnotation(
            testContext.getTestClass(), SpringJerseyTest.class);
        if (mockJerseyTest == null) {
            return;
        }

        ApplicationContext ctx = testContext.getApplicationContext();
        Predicate<Class<?>> beanNotExists = c -> ctx.getBeansOfType(c).isEmpty();

        // Find additional components that are not @Component beans. @Component beans
        // that available in the current context are automatically added to the Jersey
        // context - additional ones needed to be registered explicitly.
        // We don't want to register all components - to avoid double registration of
        // providers that were already registered.
        Set<Class<?>> components = Arrays.stream(mockJerseyTest.components())
            .filter(beanNotExists)
            .collect(Collectors.toSet());

        log.debug("Detected the following non Spring beans components to add: {}", components);
        instance.prepare(components);
    }

    private SpringJerseyTestInstance getJerseyTest(TestContext testContext) {
        if (!testContext.hasApplicationContext()) {
            return null;
        }
        try {
            ApplicationContext ctx = testContext.getApplicationContext();
            return ctx.getBean(SpringJerseyTestInstance.class);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
