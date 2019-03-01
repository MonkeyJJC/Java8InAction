package lambdasinaction.chap7;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

import static lambdasinaction.chap7.ParallelStreamsHarness.FORK_JOIN_POOL;

/**
 * 使用Fork/Join框架执行并行求和
 */
public class ForkJoinSumCalculator extends RecursiveTask<Long> {

    public static final long THRESHOLD = 10_000;

    private final long[] numbers;
    private final int start;
    private final int end;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    /**
     * Fork/Join框架的目的是以递归方式将可以并行的任务拆分成更小的任务，然后计算每个子任务，然后将每个子任务的结果合并起来生成整体结果。
     * RecursiveTask中抽象方法compute伪代码：
     * if(任务足够小，不可分割) {
     *     顺序计算该任务
     * } else {
     *     将任务分成两个子任务
     *     递归调用本方法，拆分每个子任务，等待所有子任务完成
     *     合并每个子任务结果
     * }
     * @return
     */
    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);
        /*
         *（真正开启多线程的位置）
         * fork()方法利用另一个ForkJoinPool线程异步执行新创建的子任务
         */
        leftTask.fork();
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length/2, end);
        // 同步执行第二个子任务，比再进行一次fork()效率更高，可以复用当前线程，避免线程池中的开销，有可能允许进一步递归划分
        Long rightResult = rightTask.compute();
        // 读取第一个子任务的结果，如果尚未完成就等待，因此join方法会阻塞调用方，有必要在两个子任务的计算都开始之后再调用，不然rightTask.compute()方法一直阻塞
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }

    /**
     * 使用RecursiveTask（Fork/Join框架）
     * @param n
     * @return
     */
    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        // 多态，动态绑定
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        return FORK_JOIN_POOL.invoke(task);
    }
}