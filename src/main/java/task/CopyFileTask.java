package task;

import util.Handler;

import java.util.concurrent.Callable;

/**
 * 复制文件任务
 */
public class CopyFileTask implements Callable<Integer> {

    private String sourcePath;

    private String targetPath;

    private Integer flag;

    @Override
    public Integer call() throws Exception {
        Handler.copyFile(sourcePath, targetPath);
        flag++;
        return flag;
    }

    public CopyFileTask(String sourcePath, String targetPath, Integer flag) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.flag = flag;
    }

}
