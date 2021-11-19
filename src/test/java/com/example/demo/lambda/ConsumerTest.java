package com.example.demo.lambda;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;


public class ConsumerTest {

    @Test
    public void test1() {
        Consumer<String> consumer = str -> {
            int result = Integer.parseInt(str);
            MatcherAssert.assertThat(10, is(result));
        };

        consumer.accept("10");
    }

    @Test
    public void test2() {
        Consumer<String> c = (x) -> System.out.println(x.toLowerCase());
        c.andThen(c).accept("Java2s.com");
    }
}
