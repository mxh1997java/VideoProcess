package task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步任务执行池
 */
public class MyExecutorService {

    private MyExecutorService() {}

    //获得计算机CPU核心数
    private static final int cpuCoreNum = Runtime.getRuntime().availableProcessors();

    private static final ExecutorService taskExecutor = Executors.newFixedThreadPool(cpuCoreNum);

    public static ExecutorService getMyExecutorService() {
        return taskExecutor;
    }

}
