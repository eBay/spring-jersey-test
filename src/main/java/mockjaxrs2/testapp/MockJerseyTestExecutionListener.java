package mockjaxrs2.testapp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class MockJerseyTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        System.out.println("beforeTestMethod");
        MockJerseyTestInstance instance = getJerseyTest(testContext);
        if (instance != null) {
            instance.setUp();
        }
    }

    @Override
    public void beforeTestExecution(TestContext testContext) throws Exception {
        System.out.println("beforeTestExecution");
        //just before each test is executed
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        // before instance container created
        MockJerseyTestInstance instance = getJerseyTest(testContext);
        MockJerseyTest mockJerseyTest = AnnotatedElementUtils.findMergedAnnotation(
            testContext.getTestClass(), MockJerseyTest.class);
        if (mockJerseyTest == null || instance == null) {
            return;
        }
        Set<Class<?>> components = Arrays.stream(mockJerseyTest.components())
            .filter(c -> beanNotExists(testContext.getApplicationContext(), c))
            .collect(Collectors.toSet());
        instance.prepare(components);
//        testContext.getApplicationContext().containsBean();
//        instance.prepare(mockJerseyTest.components());
    }

    private boolean beanNotExists(ApplicationContext ctx, Class<?> clazz) {
        return ctx.getBeansOfType(clazz).isEmpty();
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        //before spring context starts
        System.out.println("beforeTestClass");

    }


    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        MockJerseyTestInstance instance = getJerseyTest(testContext);
        if (instance != null) {
            instance.tearDown();
        }
    }

    private MockJerseyTestInstance getJerseyTest(TestContext testContext) {
        if (!testContext.hasApplicationContext()) {
            return null;
        }
        try {
            ApplicationContext ctx = testContext.getApplicationContext();
            return ctx.getBean(MockJerseyTestInstance.class);
        } catch (NoSuchBeanDefinitionException e){
            return null;
        }
    }
}
