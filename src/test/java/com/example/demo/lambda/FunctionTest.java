package com.example.demo.lambda;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;


public class FunctionTest {

    @Test
    public void test1() {
        Function<String, Integer> f2 = Integer::parseInt;
        Integer apply = f2.apply("3");
        MatcherAssert.assertThat(3, is(apply));
    }
}
