package components;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Handler;
import view.ConfigPage;
import view.MyHome;

/**
 * @author xinhai.ma
 * @description  菜单栏组件
 * @date 2020/5/9 9:02
 */
public class MyMenuBar {

    private static final Logger LOG = LoggerFactory.getLogger(MyMenuBar.class);

    public static MenuBar getMenuBar(Stage primaryStage) {
        // 菜单栏
        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("文件");
        MenuItem selectFolder = new MenuItem("选择文件夹");
        selectFolder.setOnAction((ActionEvent even) -> {
            // 选择的文件夹
            File file = MyChooser.getDirectoryChooser().showDialog(primaryStage);
            String path = file.getPath();
            LOG.info("选择文件夹: " + path);
            List<String> fileList = Handler.readDic(path);
            if(fileList.size() == 0) {
                MyAlertBox.display("菜单栏提示: ", "未在" + path + "文件夹下读取到视频文件，请重新选择!");
                return;
            }
            VBox videoList = MyVideoList.getVideoList(fileList);
            MyHome.setVideoList(videoList);
        });
        menuFile.getItems().addAll(selectFolder);

        MenuItem selectMultipleFile = new MenuItem("选择多个文件");
        selectMultipleFile.setOnAction((ActionEvent even) -> {
            List<String> pathList = new ArrayList<>();
            List<File> list = MyChooser.getFileChooser().showOpenMultipleDialog(primaryStage);
            if (list != null) {
                list.stream().forEach((file) -> {
                    LOG.info("选择多个文件: " + file.getAbsolutePath());
                    pathList.add(file.getAbsolutePath());
                });
            }
            VBox videoList = MyVideoList.getVideoList(pathList);
            MyHome.setVideoList(videoList);
        });
        menuFile.getItems().addAll(selectMultipleFile);

        MenuItem selectFile = new MenuItem("选择文件");
        selectFile.setOnAction((ActionEvent even) -> {
            File file = MyChooser.getFileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                LOG.info("选择单个文件: " + file.getAbsolutePath());
                List<String> fileList = new ArrayList<>();
                fileList.add(file.getAbsoluteFile().toString());
                VBox videoList = MyVideoList.getVideoList(fileList);
                MyHome.setVideoList(videoList);
            }
        });
        menuFile.getItems().addAll(selectFile);

        // --- Menu Edit
        Menu menuEdit = new Menu("编辑");
        MenuItem none = new MenuItem("敬请期待");
        none.setOnAction((ActionEvent t) -> {
        });
        menuEdit.getItems().addAll(none);

        // --- Menu View
        Menu menuView = new Menu("视图");
        MenuItem home = new MenuItem("首页");
        home.setOnAction((ActionEvent even) -> {
            Scene homeScene = MyHome.getHome(primaryStage);
            primaryStage.setScene(homeScene);
        });
        MenuItem config = new MenuItem("配置");
        config.setOnAction((ActionEvent even) -> {
            Scene configScene = ConfigPage.getConfigPage(primaryStage);
            primaryStage.setScene(configScene);
        });
        menuView.getItems().addAll(home, config);

        // --- Menu View
        Menu menuMore = new Menu("更多");
        MenuItem none2 = new MenuItem("敬请期待");
        none2.setOnAction((ActionEvent even) -> {
        });
        menuMore.getItems().addAll(none2);

        // --- Menu Config
        Menu menuConfig = new Menu("配置");
        MenuItem configPage = new MenuItem("配置页面");
        configPage.setOnAction((ActionEvent even) -> {
            Scene configScene = ConfigPage.getConfigPage(primaryStage);
            primaryStage.setScene(configScene);
        });
        menuConfig.getItems().addAll(configPage);

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuMore, menuConfig);
        return menuBar;
    }

}
