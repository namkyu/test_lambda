package com.example.demo.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;


public class PredicateTest {

    @Test
    public void test1() {
        List<String> words = Arrays.asList("a", "b", "c");
        Predicate<String> filter = s -> !"b".equals(s);

        words.stream()
                .filter(filter)
                .collect(toList())
                .forEach(System.out::println);
    }
}
