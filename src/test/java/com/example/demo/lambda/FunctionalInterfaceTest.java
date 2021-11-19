package com.example.demo.lambda;

import com.example.demo.lambda.func.TestFunctionInterface;
import com.example.demo.lambda.func.TestFunctionInterface2;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;


public class FunctionalInterfaceTest {

    // 익명 클래스를 사용
    @Test
    public void test2() {
        TestFunctionInterface2 t = new TestFunctionInterface2() {
            @Override
            public void then(String str) {
                MatcherAssert.assertThat("1111", is(str));
            }
        };

        t.then("1111");
    }

    // lambda 사용
    @Test
    public void test3() {
        TestFunctionInterface2 t = str -> {
            MatcherAssert.assertThat("1111", is(str));
        };

        t.then("1111");
    }

    @Test
    public void test1() {
        exec().then((str) -> {
            MatcherAssert.assertThat("1111", is(str));
        });
    }

    private TestFunctionInterface exec() {
        return consumer -> {
            consumer.accept("1111");
        };
    }
}
