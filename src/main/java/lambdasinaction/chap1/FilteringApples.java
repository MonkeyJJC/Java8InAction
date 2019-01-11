package lambdasinaction.chap1;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilteringApples{

    public static void main(String ... args){

        List<Apple> inventory = Arrays.asList(new Apple(80,"green"),
                                              new Apple(155, "green"),
                                              new Apple(120, "red"));

        /**
         * 传递方法
         */
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples = filterApples(inventory, FilteringApples::isGreenApple);
        System.out.println(greenApples);
        
        // [Apple{color='green', weight=155}]
        List<Apple> heavyApples = filterApples(inventory, FilteringApples::isHeavyApple);
        System.out.println(heavyApples);

        /**
         * 传递lambda表达式，避免写类似isHeavyApple的函数
         */
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples2 = filterApples(inventory, (Apple a) -> "green".equals(a.getColor()));
        System.out.println(greenApples2);
        
        // [Apple{color='green', weight=155}]
        List<Apple> heavyApples2 = filterApples(inventory, (Apple a) -> a.getWeight() > 150);
        /**
         * 等价于：
         *         List<Apple> heavyApples3 = new ArrayList<>();
         *         inventory.stream().forEach(apple -> {
         *             if (apple.getWeight() > 150) {
         *                 heavyApples3.add(apple);
         *             }
         *         });
         *         但是对于复用性等角度，封装为filterApples函数，并通过谓词Predicate传入函数，代码冗余更少
         *         对于仅一次情况，感觉两种都可以
         */
        System.out.println(heavyApples2);
        
        // []
        List<Apple> weirdApples = filterApples(inventory, (Apple a) -> a.getWeight() < 80 || 
                                                                       "brown".equals(a.getColor()));
        System.out.println(weirdApples);

        /**
         * Stream流（同时可以利用多核CPU进行天然的并行计算，传统上是利用synchronized关键字，但是要是用􏱅了地方，就可能出现很多难以􏵇觉的􏱅􏱆。
         * Java 8基于Stream 的并行提􏵈很少使用synchronized的函数式编程风格，它关注数据分块而不是􏵀调访问。）
         * 流提供的这个􏵨􏵫􏳞􏴥的并行，只有在传递给filter之类的􏵥方 法的方法不会􏴴动(比方说有可变的共享对象)时才能􏵬作。（即各块的计算结果互不影响）
         * Collection主要是为了存储和访问数据，而Stream则主要用于描述对数据的计算
         * 使用filter
         */
        List<Apple> getByFilter = inventory.stream().filter(apple -> apple.getWeight() < 80).collect(Collectors.toList());

        getByFilter.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
        getByFilter.sort(Comparator.comparing(Apple::getWeight));
    }

    public static List<Apple> filterGreenApples(List<Apple> inventory){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterHeavyApples(List<Apple> inventory){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if (apple.getWeight() > 150) {
                result.add(apple);
            }
        }
        return result;
    }

    public static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor()); 
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }

    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p){
        List<Apple> result = new ArrayList<>();
        for(Apple apple : inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }       

    public static class Apple {
        private int weight = 0;
        private String color = "";

        public Apple(int weight, String color){
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String toString() {
            return "Apple{" +
                   "color='" + color + '\'' +
                   ", weight=" + weight +
                   '}';
        }
    }

}
