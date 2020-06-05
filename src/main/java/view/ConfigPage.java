package view;

import java.io.File;
import java.util.Map;
import components.MyChooser;
import components.MyMenuBar;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Handler;

/**
 * @author xinhai.ma
 * @description
 * @date 2020/5/9 9:16
 */
public class ConfigPage {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigPage.class);

    /**
     * 返回程序配置页面
     *
     * @return
     */
    public static Scene getConfigPage(Stage primaryStage) {
        Map<String, String> config = Handler.readProp();

        // 创建GridPane容器
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(15);
        grid.setHgap(15);

        // 文本域
        // FFmpeg路径
        final TextField ffmpegPath = new TextField();
        ffmpegPath.setDisable(true);
        ffmpegPath.setPrefSize(350, 40);
        ffmpegPath.setPromptText("请选择第三方播放器路径");
        if(null != config.get("ffmpegPath")) {
            ffmpegPath.setText(config.get("ffmpegPath"));
        }
        GridPane.setConstraints(ffmpegPath, 0, 0);
        grid.getChildren().add(ffmpegPath);

        // 要处理的文件目录
        final TextField sourcePath = new TextField();
        sourcePath.setDisable(true);
        sourcePath.setPrefSize(350, 40);
        sourcePath.setPromptText("请选择视频来源文件夹路径");
        if(null != config.get("sourcePath")) {
            sourcePath.setText(config.get("sourcePath"));
        }
        GridPane.setConstraints(sourcePath, 0, 1);
        grid.getChildren().add(sourcePath);

        // 要输出的文件目录
        final TextField targetPath = new TextField();
        targetPath.setDisable(true);
        targetPath.setPrefSize(350, 40);
        targetPath.setPromptText("请选择生成视频文件夹路径");
        if(null != config.get("targetPath")) {
            targetPath.setText(config.get("targetPath"));
        }
        GridPane.setConstraints(targetPath, 0, 2);
        grid.getChildren().add(targetPath);

        // 按钮
        Button ffmpegButton = new Button("请选择");
        ffmpegButton.setPrefSize(60, 40);
        GridPane.setConstraints(ffmpegButton, 1, 0);
        grid.getChildren().add(ffmpegButton);

        Button sourceButton = new Button("请选择");
        sourceButton.setPrefSize(60, 40);
        GridPane.setConstraints(sourceButton, 1, 1);
        grid.getChildren().add(sourceButton);

        Button targetButton = new Button("请选择");
        targetButton.setPrefSize(60, 40);
        GridPane.setConstraints(targetButton, 1, 2);
        grid.getChildren().add(targetButton);

        // 按钮事件
        ffmpegButton.setOnAction((ActionEvent even) -> {
            File file = MyChooser.getExeFileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                LOG.info("选择ffmpeg.exe文件路径: {}", file.getAbsolutePath());
                ffmpegPath.setText(file.getAbsoluteFile().toString());
            }
        });

        sourceButton.setOnAction((ActionEvent even) -> {
            File file = MyChooser.getDirectoryChooser().showDialog(primaryStage);
            String path = file.getPath();
            LOG.info("选择source文件夹: {}", path);
            sourcePath.setText(path);
        });

        targetButton.setOnAction((ActionEvent even) -> {
            File file = MyChooser.getDirectoryChooser().showDialog(primaryStage);
            String path = file.getPath();
            LOG.info("选择target文件夹: {}", path);
            targetPath.setText(path);
        });

        VBox pane = new VBox();
        HBox saveBox = new HBox();
        //上 右 下 左
        saveBox.setPadding(new Insets(40, 100, 100, 150));
        Button save = new Button("保存配置");
        save.setPrefSize(80, 40);
        save.setOnAction(even -> {
            String ffmpegInfo = ffmpegPath.getText();
            String sourceInfo = sourcePath.getText();
            String targetInfo = targetPath.getText();
            Handler.saveProp(ffmpegInfo, sourceInfo, targetInfo);
        });
        saveBox.getChildren().add(save);
        pane.getChildren().addAll(grid, saveBox);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(MyMenuBar.getMenuBar(primaryStage));
        borderPane.setCenter(pane);
        Map<String, Integer> screenSize = Handler.getScreenSize();
        Scene scene = new Scene(borderPane, screenSize.get("width"), screenSize.get("height")-55);
        return scene;
    }

}
