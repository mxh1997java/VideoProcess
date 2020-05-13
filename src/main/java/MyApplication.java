import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.MyHome;

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
        primaryStage.show();
    }

    public static void main(String[] args) {
        LOG.info("视频处理程序启动...");
        launch(args);
        LOG.info("视频处理程序启动完毕...");
    }

}
