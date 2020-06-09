package task;

import java.util.concurrent.*;

/**
 * 异步任务执行池
 */
public class MyExecutorService {

    private MyExecutorService() {}

    //获得计算机CPU核心数
    //private static final int cpuCoreNum = Runtime.getRuntime().availableProcessors();

    private static ExecutorService taskExecutor;

    public static final int TASKTOTAL = 50;

    /**
     * 返回有界队列线程池，超出队列会抛异常
     * @return
     */
    public static ExecutorService getTaskExecutor() {

        if(null == taskExecutor) {
            taskExecutor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(TASKTOTAL) {
            });
        }
        return taskExecutor;
    }


    /**
     * 关闭线程池
     */
    public static void close() {
        if(null != taskExecutor) {
            taskExecutor.shutdown();
        }
    }

}
