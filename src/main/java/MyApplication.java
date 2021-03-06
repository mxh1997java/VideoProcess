import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.MyExecutorService;
import util.Handler;
import view.MyHome;
import java.io.File;

/**
 * @author xinhai.ma
 * @description
 * @date 2020/5/9 9:22
 */
public class MyApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(MyApplication.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = MyHome.getHome(primaryStage);
        // 设置stage的scene，然后显示我们的stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("微智汇视频处理程序");
        //软件关闭监听
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.print("监听到窗口关闭");
                MyExecutorService.close();
                System.exit(0); //关闭java虚拟机
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        LOG.info("视频处理程序启动...");
        Handler.createFile("C:\\VideoProcess\\config", "config.properties");
        downloadVideo();
        launch(args);
        LOG.info("视频处理程序关闭...");
    }

    /**
     * 下载视频以便初始化视频播放器
     */
    private static void downloadVideo() {
        File file = new File("C:\\VideoProcess\\show.mp4");
        if(!file.exists()){
            String url = "https://gss3.baidu.com/6LZ0ej3k1Qd3ote6lo7D0j9wehsv/tieba-smallvideo-transcode-cae/7582624_bd76685e95e44141dc814fb8e96c4366_0_cae.mp4";
            Handler.httpDownload(url, "C:\\VideoProcess\\show.mp4");
        }
    }

}
