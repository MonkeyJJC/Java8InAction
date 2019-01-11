package lambdasinaction.chap3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description: java.util.function包中引入的几个新的函数式接口(@FunctionalInterface的介绍)
 * 函数式接口的lambda表达式的编写方法：
 * （1）确定函数描述符
 * （2）根据实际情况，按照函数描述符进行具体lambda的编写
 * @author: JJC
 * @createTime: 2019/1/11
 */
public class FunctionInterface {

    /**
     * 谓词
     * java.util.function.Predicate<T>接口定义了test抽象方法，接受一个泛型T对象，并返回一个boolean
     * 函数描述符 T-> boolean
     * @param list
     * @param p
     * @param <T>
     * @return
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (p.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * java.util.function.Consumer<T>定义了一个叫accept的抽象方法，接受泛型T对象，没有返回(void)
     * 场景：如果想访问类型T的对象，并对其执行某些操作，就可以使用
     * 函数描述符 T-> void
     * @param list
     * @param c
     * @param <T>
     */
    public static <T> void forEach(List<T> list, Consumer<T> c) {
        for (T t : list) {
            c.accept(t);
        }
    }

    /**
     * java.util.function.Function<T, R>接口定义了apply抽象方法，接受一个泛型T的对象，并返回一个泛型R的对象
     * 使用场景：如果需要定义一个Lambda，将输入对象的信息映射到输出，可以使用这个接口
     * 函数描述符 T-> R
     * @param list
     * @param f
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(f.apply(t));
        }
        return result;
    }

    public static void main(String[] args) {

        /**
         * 过滤
         */
        List<String> noEmpty = filter(Arrays.asList("lambda", "in", "", "action"), (String s) -> !s.isEmpty());
        List<String> noEmptyByStream = Arrays.asList("lambda", "in", "", "action").stream().filter(String::isEmpty).collect(Collectors.toList());

        /**
         * 将输入对象的信息映射到输出
         */
        List<Integer> list = map(Arrays.asList("lambda", "in", "action"),
                (String s) -> s.length()
                );
        /**
         * 函数复合
         */
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        List<Integer> integerList = map(Arrays.asList(1, 2, 3),
                f.andThen(g)
        );
        Function<Integer, Integer> c = f.andThen(g);
        Integer rs = c.apply(1);
        List<Integer> listByStream = Arrays.asList("lambda", "in", "action").stream().map(s -> s.length()).collect(Collectors.toList());


    }
}
