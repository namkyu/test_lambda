package com.example.demo.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@FunctionalInterface
interface FunctionWithException<T, R, E extends Exception> {
    R apply(T t) throws E;
}

interface Validator {
    void valid(String str);
}

/**
 * @Project : my_application
 * @Date : 2018-06-14
 * @Author : nklee
 * @Description :
 */
public class LambdaTest {

    List<PersonTest> dummyList;
    List<Foo> foos = new ArrayList<>();
    List<Person> persons = Arrays.asList(new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12));

    @Before
    public void init() {
        dummyList = Arrays.asList(
                new PersonTest("nklee", 37, "Lee"),
                new PersonTest("nklee2", 38, "Lee"),
                new PersonTest("dy", 36, "Kim"),
                new PersonTest("wife", 37, "Kim"),
                new PersonTest("wife2", 37, null)
        );

        IntStream.range(1, 4)
                .forEach(i -> foos.add(new Foo("Foo" + i)));
        foos.forEach(f -> IntStream.range(1, 4)
                .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));
    }

    @Test
    public void stream_필터_테스트() {
        List<String> words = Arrays.asList("a", "b", "c");
        words.stream().filter(word -> "b".equals(word) == false)
                .collect(toList())
                .forEach(System.out::println);
    }

    @Test
    public void stream_필터2_테스트() {
        List<PersonTest> persons = Arrays.asList(
                new PersonTest("nklee", 37, "Lee"),
                new PersonTest("nklee", 38, "Lee"),
                new PersonTest("dy", 36, "Kim"),
                new PersonTest("wife", 37, "Kim")
        );

        PersonTest personTest = persons.stream() // Convert to stream
                .filter(person -> "nklee".equals(person.getName())) // we want to get nklee object
                .findAny() // return found object
                .orElse(null); // If not found, return null

        assertThat(personTest.getAge(), is(37));
    }

    @Test
    public void test_filter_objectNonNull() {
        List<Bar> list = Arrays.asList(new Bar("a"), null, new Bar("b"));
        list.stream()
                .filter(Objects::nonNull)
                .map(Bar::getName)
                .forEach(System.out::println);
    }

    @Test
    public void test_filter_objectNull() {
        List<Bar> list = Arrays.asList(new Bar("a"), null, new Bar("b"));
        list.stream()
                .filter(Objects::isNull)
                .forEach(bar -> {
                    System.out.println(bar);
                });


    }

    @Test
    public void testMaptoStream() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");

        map.entrySet()
                .stream()
                .limit(2)
                .forEach(System.out::println);
    }

    @Test
    public void testStreamAPI() {
        String[] arr = new String[]{"1", "2", "3"};
        Stream.of(arr).limit(2).forEach(System.out::println);
    }

    @Test
    public void testStremTest() {
        assertThat(1L, is(Stream.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).count()));
        assertThat(10L, is(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).count()));
    }

    @Test
    public void testLambdaBasic() {
        Validator validator = new Validator() {
            @Override
            public void valid(String str) {
                System.out.println("valid : " + str);
            }
        };

        // 대상 타입을 정의했기 때문에 new Validator() 제거
        // 구현하려고 보니 인터페이스에 메서드가 한 개 밖에 없다. 메서드 명칭도 제거
        // 메서드를 추론했다면 인자도 추론 가능할 것이다. 구현해야 할 때 인자를 써야 하니 인자를 지우진 못해도 타입은 제거
        Validator validator1 = (str) -> {
            System.out.println("valid : " + str);
        };

        // 인자가 여러개이면 몰라도 한 개 이니 괄호도 제거
        // 실행구문이 한 줄이면 중괄호도 제거
        Validator validator2 = str -> System.out.println("valid : " + str);
    }



    @Test
    public void testStream() {
        List<Integer> evenList = Stream.iterate(1, n -> n + 1)
                .limit(6)
                .filter(number -> number % 2 == 0)
                .collect(toList());

        System.out.println(evenList);

        Stream.iterate(1, n -> n + 1)
                .limit(6)
                .peek(System.out::println)
                .filter(number -> number % 2 == 0);

    }

    @Test
    public void findFirst() {
        Arrays.asList("a1", "a2", "a3")
                .stream()
                .findFirst()
                .ifPresent(System.out::println);
    }

    @Test
    public void stream_of() {
        Stream.of("a1", "a2", "a3")
                .findFirst()
                .ifPresent(System.out::println);
    }

    @Test
    public void intStream() {
        IntStream.range(1, 10)
                .forEach(System.out::println);
    }

    @Test
    public void arraysStream() {
        Arrays.stream(new int[]{1, 2, 3})
                .max()
                .ifPresent(System.out::println);
    }

    @Test
    public void optional() {
        OptionalInt max = Arrays.stream(new int[]{1, 2, 3}).max();
        assertThat(max.getAsInt(), is(3));
    }

    @Test
    public void mapToInt() {
        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .ifPresent(System.out::println);
    }

    @Test
    public void mapToObj() {
        IntStream.range(1, 5)
                .mapToObj(value -> "a" + value)
                .forEach(System.out::println);
    }

    @Test
    public void use_stream_with_mapToInt_and_mapToObj() {
        Stream.of(1.0, 2.0, 3.0)
                .mapToInt(Double::intValue)
                .mapToObj(value -> "a" + value)
                .forEach(System.out::println);
    }

    @Test
    public void anyMatch() {
        // 두 번 만 실행된다.
        // 실행 순서는 d2 -> map -> anyMatch 순으로 동작한다.
        Stream.of("d2", "a2", "c1")
                .map(s -> {
                    System.out.println("map : " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch : " + s);
                    return s.startsWith("A");
                });
    }

    @Test
    public void important_order_operation() {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map : " + s);
                    return s.toUpperCase();
                })
                .filter(s -> {
                    System.out.println("filter : " + s);
                    return s.startsWith("A");
                })
                .forEach(s -> System.out.println("forEach : " + s));
    }

    @Test
    public void important_order_operation2() {
        // 연산 순서를 바꾸면 filter 를 위로 했을 때 연산의 수를 줄일 수 있다.
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter : " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map : " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach : " + s));
    }

    @Test
    public void sorted() {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .sorted((s1, s2) -> {
                    System.out.printf("sort : %s, %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    System.out.println("filter : " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map : " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach : " + s));

    }

    @Test
    public void collect() {
        // collect 는 원소들의 스트림을 List, Set, Map 같은 종류의 결과로 반환하는데 좋은 연산이다.
        Set<Person> filtered = persons.stream()
                .filter(p -> p.name.startsWith("P"))
                .collect(Collectors.toSet());

        System.out.println(filtered);
    }

    @Test
    public void collect_groupingBy() {
        persons.stream()
                .collect(Collectors.groupingBy(p -> p.age))
                .forEach((age, p) -> System.out.format("age %s : %s\n", age, p));
    }

    @Test
    public void grouping_test() {
        Map<Integer, List<Person>> map = persons.stream()
                .collect(Collectors.groupingBy(p -> p.age));
    }

    @Test
    public void grouping_test2() {
        Map<String, List<PersonTest>> collect = dummyList.stream()
                .collect(Collectors.groupingBy(p -> p.getParentName() == null ? "키가없음" : p.getParentName()));
        System.out.println(collect);
    }

    @Test
    public void collect_others_operation() {
        Double averageAge = persons.stream()
                .collect(Collectors.averagingInt(p -> p.age));
        System.out.println("averageAge : " + averageAge);

        IntSummaryStatistics ageSummary = persons.stream()
                .collect(Collectors.summarizingInt(p -> p.age));
        System.out.println("ageSummary " + ageSummary);

        String phrase = persons.stream()
                .filter(p -> p.age >= 18)
                .map(p -> p.name)
                .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));
        System.out.println("phrase : " + phrase);
    }

    @Test
    public void test_joining() {
        String collect = Stream.of("d2", "a2", "b1", "b3", "c")
                .map(String::toUpperCase)
                .collect(Collectors.joining(", "));
        System.out.println(collect);
    }

    @Test
    public void flat_map() {
        foos.stream()
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));
    }

    @Test
    public void flat_map_optimize() {
        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .peek(f -> IntStream.range(1, 4)
                        .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
                        .forEach(f.bars::add))
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));
    }

    @Test
    public void test_compare_1() {

        // as-is
        for (PersonTest person : dummyList) {
            if (person.getName().equals("nklee") && person.getAge() == 38) {
                System.out.println(person);
            }
        }

        // to-be
        dummyList.stream()
                .filter(p -> p.getName().equals("nklee"))
                .filter(p -> p.getAge() == 38)
                .forEach(System.out::println);

        // to-be
        dummyList.stream()
                .filter(p -> p.getName().equals("nklee"))
                .filter(p -> p.getAge() == 38)
                .findAny()
                .ifPresent(System.out::println);
    }

    @Test
    public void test_메소드_레퍼런스() {
        Bar bar = new Bar("a");
        Function<String, Boolean> func = (a) -> a.equalsIgnoreCase("a");
        System.out.println(func.apply(bar.getName()));

        Function<String, Boolean> func2 = Objects::nonNull;
        System.out.println(func2.apply(bar.getName()));
    }

    @Test
    public void test_익셉션_핸들링() {
        String collect = foos.stream()
                .map(wrapper(foo -> URLEncoder.encode(foo.name, "UTF-8")))
                .collect(Collectors.joining(","));
        System.out.println(collect);
    }

    private <T, R, E extends Exception> Function<T, R> wrapper(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Test
    public void test_sorted() {
        List<String> list = Arrays.asList("9", "A", "Z", "1", "B", "Y", "1", "4", "a", "c");
        list.stream().sorted().forEach(System.out::println);
    }

    @Test
    public void test_sorted_reverse() {
        List<String> list = Arrays.asList("9", "A", "Z", "1", "B", "Y", "1", "4", "a", "c");
        list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }

    @Test
    public void test_sorted_listObject() {
        List<User> users = Arrays.asList(new User("C", 30)
                , new User("D", 40)
                , new User("A", 10)
                , new User("B", 20)
                , new User("E", 50));

        users.stream()
                .sorted(Comparator.comparingInt(User::getAge))
                .forEach(System.out::println);

        System.out.println("=======================================");
        users.stream()
                .sorted(Comparator.comparingInt(User::getAge).reversed())
                .forEach(System.out::println);
    }

    @Test
    public void test_random() {
        Random random = new Random();
        // 1번째 아규먼트 : Creates your starting result
        // 2번째 아규먼트 : Adds an element (String) to your result (StringBuilder)
        // 3번째 아규먼트 : If you run the stream in parallel, multiple StringBuilders will be created. This is for combining these together.
        StringBuilder collect = random.ints(10, 0, 5)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
        System.out.println(collect.toString());
    }

}

@Data
@AllArgsConstructor
class User {
    private String name;
    private int age;
}

@Data
@AllArgsConstructor
class PersonTest {
    String name;
    Integer age;
    String parentName;
}

class AgeValidator implements Validator {
    @Override
    public void valid(String str) {
        System.out.println("valid : " + str);
    }
}

@Data
@AllArgsConstructor
class Person {
    String name;
    int age;
}

class Foo {
    String name;
    List<Bar> bars = new ArrayList<>();

    Foo(String name) {
        this.name = name;
    }
}

@Data
class Bar {
    String name;

    Bar(String name) {
        this.name = name;
    }
}