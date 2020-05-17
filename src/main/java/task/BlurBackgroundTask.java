package task;

import executor.VideoExecutor;
import java.util.concurrent.Callable;

/**
 * 虚化背景任务
 */
public class BlurBackgroundTask implements Callable<Integer> {

    private VideoExecutor videoExecutor;
    private String sourcePath;
    private String targetPath;

    @Override
    public Integer call() throws Exception {
        videoExecutor.blurBackground(sourcePath, targetPath);
        return 1;
    }

    public BlurBackgroundTask(VideoExecutor videoExecutor, String sourcePath, String targetPath) {
        this.videoExecutor = videoExecutor;
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
    }
}
