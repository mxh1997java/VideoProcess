package task;

import executor.VideoExecutor;
import java.util.concurrent.Callable;

/**
 * 消除水印异步任务
 */
public class RemoveWatermarkTask implements Callable<Integer> {

    private VideoExecutor videoExecutor;
    private String sourcePath;
    private String x;
    private String y;
    private String width;
    private String height;
    private String targetPath;

    @Override
    public Integer call() throws Exception {
        videoExecutor.removeWatermark(sourcePath, x, y, width, height, targetPath);
        return 1;
    }

    public RemoveWatermarkTask(VideoExecutor videoExecutor, String sourcePath, String x, String y, String width, String height, String targetPath) {
        this.videoExecutor = videoExecutor;
        this.sourcePath = sourcePath;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.targetPath = targetPath;
    }
}
