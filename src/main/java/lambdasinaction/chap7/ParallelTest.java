package lambdasinaction.chap7;

import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @description: 并行流就是一个把内容分成多个数据块，并用不同的线程分别处理每个数据块的流。流在幕后应用Java7引入的分支/合并（Fork/Join）框架。
 * 分支/合并（Fork/Join）框架：
 * （1）把包含的数据结构分成若干个子部分
 * （2）每个子部分分配一个独立的线程
 * （3）恰当的时候进行同步，避免竞争条件，等待所有线程完成，最后把这些部分结果合并起来
 * parallel并行流内部使用了默认的ForkJoinPool，默认线程数量就是处理器数量 Runtime.getRuntime().availableProcessors()
 * availableProcessors()方法虽然看起来是处理器，但实际上返回的是可用内核的数量，包括超线程生成的虚拟内核
 * 可以对系统默认线程数进行修改
 * System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");
 * 测试用例 ParallelStreamsHarness
 * @author: JJC
 * @createTime: 2019/3/1
 */
public class ParallelTest {

    /**
     * 比传统for循环更慢，因为for更底层，且不需要对原始类型进行任何装箱和拆箱操作
     * @param n
     * @return
     */
    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(0L, Long::sum);
    }

    /**
     * 选用数据结构LongStream.rangeClosed,直接生成long对象
     * @param args
     */
    public static long parallelSumFast(long n) {
        return LongStream.rangeClosed(1, n).parallel().reduce(0L, Long::sum);
    }


    public static void main(String[] args) {
        System.out.println(ForkJoinSumCalculator.forkJoinSum(10000));
    }
}
