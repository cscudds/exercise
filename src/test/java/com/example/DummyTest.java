package com.example;

import com.example.core.repository.UserGraphRepository;
import com.tinkerpop.blueprints.Vertex;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class DummyTest {
    @Test
    public void dummyTest() {
        assertThat(true, is(true));
    }

    @Test
    public void iCanGetCustomerGraph() {
        UserGraphRepository userGraphRepository = new UserGraphRepository();
        Vertex customer = userGraphRepository.getForUser("tom@example.com");
        assertThat(customer, is(notNullValue()));
    }
}
