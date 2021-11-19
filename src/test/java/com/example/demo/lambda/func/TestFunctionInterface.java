package com.example.demo.lambda.func;

import java.util.function.Consumer;


@FunctionalInterface
public interface TestFunctionInterface<T> {

    void then(Consumer<T> consumer);
}
