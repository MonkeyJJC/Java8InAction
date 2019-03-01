package lambdasinaction.chap5;
import lambdasinaction.chap4.*;

import java.util.stream.*;
import java.util.*;

import static lambdasinaction.chap4.Dish.menu;

/**
 * reduce:聚合
 */
public class Reducing{

    public static void main(String...args){

        List<Integer> numbers = Arrays.asList(3,4,5,1,2);
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum);

        /**
         * Integer::sum的函数描述符与reduce函数一致： (T,T) —> T
         */
        int sum2 = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum2);

        int max = numbers.stream().reduce(0, (a, b) -> Integer.max(a, b));
        System.out.println(max);

        /**
         * 无初始值的reduce函数，考虑流中可能没有元素，因此reduce的结果可能为空，故放在Optional<T>中
         */
        Optional<Integer> min = numbers.stream().reduce(Integer::min);
        min.ifPresent(System.out::println);

        /**
         * T reduce(T identity, BinaryOperator<T> accumulator)
         * public interface BinaryOperator<T> extends BiFunction<T,T,T>
         */
        int calories = menu.stream()
                           .map(Dish::getCalories)
                           .reduce(0, Integer::sum);
        System.out.println("Number of calories:" + calories);

        /**
         * Integer::max的函数描述符(T,T) —> T与lambda表达式（x,y）-> x < y ? y : x是一致的
         */
        Optional<Integer> maxNum = numbers.stream().reduce(Integer::max);

        /**
         * map-reduce模式，如可以将对象映射为1，进行计数
         */
        int count = menu.stream().map(dish -> 1).reduce(0, Integer::sum);
        /**
         * 归约
         */
        long count2 = menu.stream().count();
        long count3 = menu.stream().collect(Collectors.counting());
    }

}
