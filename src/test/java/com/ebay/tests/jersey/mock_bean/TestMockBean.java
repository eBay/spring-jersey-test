package com.ebay.tests.jersey.mock_bean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.ebay.tests.jersey.SpringJerseyTest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

public class TestMockBean {

    /*
    One way to add more beans to the context
     */
    @SpringJerseyTest
    @Nested
    @Import(ExpensiveService.class)
    public class TestWithRealBeanInContextType2 {

        @Autowired
        WebTarget target;

        @Test
        void shouldFailOnRealCall() {
            assertThatThrownBy(() -> target.path("compute/expensive").request().get())
                .isInstanceOf(ProcessingException.class)
                .hasRootCauseMessage("Too expensive to compute in a unitest");
        }
    }

    /*
    Another way to get more beans to the context
     */
    @SpringJerseyTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExpensiveService.class))
    @Nested
    public class TestWithRealBeanInContext {

        @Autowired
        WebTarget target;

        @Test
        void shouldFailOnRealCall() {
            assertThatThrownBy(() -> target.path("compute/expensive").request().get())
                .isInstanceOf(ProcessingException.class)
                .hasRootCauseMessage("Too expensive to compute in a unitest");
        }
    }


    /*
    Using @MockBean to mock required beans in the context
     */
    @SpringJerseyTest
    @Nested
    public class TestWithMock {

        @Autowired
        WebTarget target;

        @MockBean
        ExpensiveService expensiveService;

        @Test
        void shouldCallMock() {
            when(expensiveService.computeSomethingExpensive()).thenReturn("$$$$");

            String response = target.path("compute/expensive").request().get(String.class);
            assertThat(response).isEqualTo("$$$$");
        }

        @Test
        void shouldFailOnRealCall() {
            when(expensiveService.computeSomethingExpensive()).thenCallRealMethod();

            assertThatThrownBy(() -> target.path("compute/expensive").request().get())
                .isInstanceOf(ProcessingException.class)
                .hasRootCauseMessage("Too expensive to compute in a unitest");
        }
    }
}
