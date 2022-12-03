package org.example.mockjaxrs2.non_jerseytest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ComputeResource.class)
public class OnlySpringBootTest {

    @Autowired
    ComputeResource computeResource;

    @Test
    void testSpringBootTest(){
        assertThat(computeResource.computePayload()).isEqualTo("something");
    }
}
