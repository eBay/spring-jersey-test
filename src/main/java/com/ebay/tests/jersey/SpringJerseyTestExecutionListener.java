package com.ebay.tests.jersey;

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

        // Find additional components that might not be @Component or @Provider beans.
        // The prepare() method will remove any beans that were already registered.
        instance.prepare(mockJerseyTest.components());
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
