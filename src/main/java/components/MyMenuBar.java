package components;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.EmptyUtils;
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
            if(EmptyUtils.isNotEmpty(file)) {
                String path = file.getPath();
                LOG.info("选择文件夹: {}", path);
                List<String> fileList = Handler.readDic(path);
                if(fileList.size() == 0) {
                    MyAlertBox.display("菜单栏提示: ", "未在" + path + "文件夹下读取到视频文件，请重新选择!");
                    return;
                }
                MyHome.setLeft(fileList, null);
            }
        });
        menuFile.getItems().addAll(selectFolder);

        MenuItem selectMultipleFile = new MenuItem("选择多个文件");
        selectMultipleFile.setOnAction((ActionEvent even) -> {
            List<String> pathList = new ArrayList<>();
            List<File> list = MyChooser.getFileChooser().showOpenMultipleDialog(primaryStage);
            if (list != null) {
                list.stream().forEach((file) -> {
                    LOG.info("选择多个文件: {}", file.getAbsolutePath());
                    pathList.add(file.getAbsolutePath());
                });
            }
            MyHome.setLeft(pathList, null);
        });
        menuFile.getItems().addAll(selectMultipleFile);

        MenuItem selectFile = new MenuItem("选择文件");
        selectFile.setOnAction((ActionEvent even) -> {
            File file = MyChooser.getFileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                LOG.info("选择单个文件: {}", file.getAbsolutePath());
                List<String> fileList = new ArrayList<>();
                fileList.add(file.getAbsoluteFile().toString());
                MyHome.setLeft(fileList, null);
            }
        });
        menuFile.getItems().addAll(selectFile);

        // --- Menu Edit
        Menu menuEdit = new Menu("列表");
        MenuItem clearUnPrecessed = new MenuItem("清空视频列表");
        clearUnPrecessed.setOnAction((ActionEvent t) -> {
            Handler.put("unProcessed", new ArrayList<String>());
            MyHome.setLeft(null, null);
        });
        MenuItem clearPrecessed = new MenuItem("清空已处理视频列表");
        clearPrecessed.setOnAction((ActionEvent t) -> {
            Handler.put("processed", new ArrayList<String>());
            MyHome.setLeft(null, null);
        });
        MenuItem clearAll = new MenuItem("清空所有视频列表");
        clearAll.setOnAction((ActionEvent t) -> {
            Handler.put("unProcessed", new ArrayList<String>());
            Handler.put("processed", new ArrayList<String>());
            MyHome.setLeft(null, null);
        });
        menuEdit.getItems().addAll(clearUnPrecessed, clearPrecessed, clearAll);

        // --- Menu View
        Menu menuView = new Menu("视图");
        MenuItem home = new MenuItem("首页");
        home.setOnAction((ActionEvent even) -> {
            Scene homeScene = MyHome.getHome(primaryStage);
            //保存首页功能区域已勾选选项和填入的数据
            MyFunction.saveUserOperatingCache();
            primaryStage.setScene(homeScene);
        });
        MenuItem config = new MenuItem("配置");
        config.setOnAction((ActionEvent even) -> {
            Scene configScene = ConfigPage.getConfigPage(primaryStage);
            //保存首页功能区域已勾选选项和填入的数据
            MyFunction.saveUserOperatingCache();
            primaryStage.setScene(configScene);
        });
        menuView.getItems().addAll(home, config);

        // --- Menu Config
        Menu menuConfig = new Menu("配置");
        MenuItem configPage = new MenuItem("配置页面");
        configPage.setOnAction((ActionEvent even) -> {
            Scene configScene = ConfigPage.getConfigPage(primaryStage);
            primaryStage.setScene(configScene);
        });
        menuConfig.getItems().addAll(configPage);

        // --- Menu View
        Menu menuMore = new Menu("更多");
        MenuItem menuItem = new MenuItem("敬请期待");
        menuMore.getItems().addAll(menuItem);
//        Menu screenRecord = new Menu("屏幕录制");
//        screenRecord.setOnAction((ActionEvent even) -> {
//        });
//        MenuItem screenRecordStart = new MenuItem("开始");
//        screenRecordStart.setOnAction(event -> {
//            LOG.info("开始录制屏幕");
//            String filePath = Handler.getNewFilePath("D:\\file\\1.mp4");
//            VideoExecutor videoExecutor = new VideoExecutor();
//            ExecutorService myExecutorService = MyExecutorService.getTaskExecutor();
//            Thread thread = new Thread(() -> {
//                videoExecutor.screenRecord(filePath);
//            });
//            myExecutorService.submit(thread);
//        });
//        MenuItem screenRecordStop = new MenuItem("停止");
//        screenRecordStop.setOnAction(event -> {
//            LOG.info("关闭屏幕录制");
//            MyExecutorService.getTaskExecutor().shutdown();
//            VideoExecutor videoExecutor = new VideoExecutor();
//            videoExecutor.closeScreenRecord();
//        });
//        screenRecord.getItems().addAll(screenRecordStart, screenRecordStop);
//
//        Menu takeVideo = new Menu("拍摄视频");
//        takeVideo.setOnAction((ActionEvent even) -> {
//        });
//        MenuItem takeVideoStart = new MenuItem("开始拍摄");
//        takeVideoStart.setOnAction(event -> {
//            LOG.info("开始拍摄视频");
//            new Thread(()->{
//                String filePath = Handler.getNewFilePath("D:\\file\\1.mp4");
//                VideoExecutor videoExecutor = new VideoExecutor();
//                videoExecutor.takeVideo(filePath);
//            }).start();
//        });
//        MenuItem takeVideoEnd = new MenuItem("结束拍摄");
//        takeVideo.getItems().addAll(takeVideoStart, takeVideoEnd);
//        menuMore.getItems().addAll(screenRecord, takeVideo);

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuConfig, menuMore);
        return menuBar;
    }

}
