package com.example;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DummyTest {
    @Test
    public void dummyTest() {
        assertThat(true, is(true));
    }
}
