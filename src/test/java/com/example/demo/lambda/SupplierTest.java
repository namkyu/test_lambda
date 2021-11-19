package com.example.demo.lambda;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;


public class SupplierTest {

    @Test
    public void test1() {
        Supplier<String> supplier = () -> "10";
        String result = supplier.get();
        MatcherAssert.assertThat("10", is(result));
    }
}
