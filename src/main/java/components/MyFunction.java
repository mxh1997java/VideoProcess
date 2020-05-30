package components;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import executor.VideoExecutor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.MyExecutorService;
import util.EmptyUtils;
import util.Handler;
import view.MyHome;
import ws.schild.jave.MultimediaInfo;

/**
 * @author xinhai.ma
 * @description 功能区域
 * @date 2020/5/9 9:04
 */
public class MyFunction {

    private static final Logger LOG = LoggerFactory.getLogger(MyFunction.class);

    private static VideoExecutor videoExecutor = new VideoExecutor();

    /**
     * 已处理视频地址集合
     */
    private static List<String> processedList = new ArrayList<>();

    /**
     * 单个任务进度条
     */
    private static MyProgressBar singleProgressBar = new MyProgressBar("单个视频进度: ");
    private static HBox singleSchedule = singleProgressBar.getProgressBar();

    /**
     * 批量任务进度条
     */
    private static MyProgressBar batchProgressBar = new MyProgressBar("批量视频进度: ");
    private static HBox batchSchedule = batchProgressBar.getProgressBar();


    //功能区域组件 start
    private static CheckBox addWatermark = new CheckBox("添加水印");
    private static TextField addWatermarkOfX = new TextField();
    private static TextField addWatermarkOfY = new TextField();
    private static TextField addWatermarkOfContent = new TextField();

    private static CheckBox delWatermark = new CheckBox("删除水印");
    private static TextField delWatermarkOfX = new TextField();
    private static TextField delWatermarkOfY = new TextField();
    private static TextField delWatermarkOfWidth = new TextField();
    private static TextField delWatermarkOfHeight = new TextField();

    private static CheckBox cutVideo = new CheckBox("去头去尾");
    private static TextField startTime = new TextField();
    private static TextField endTime = new TextField();

    private static CheckBox setCover = new CheckBox("设置封面");
    private static TextField coverPath = new TextField();

    private static CheckBox getCover = new CheckBox("截取图片");
    private static TextField cutVideoTime = new TextField();

    private static CheckBox addFilter = new CheckBox(" 添加滤镜 ");
    private static TextField acvPath = new TextField();
    private static Button psChooserButton = new Button("请选择");
    private static ChoiceBox<String> choiceBox = MyChoiceBox.getChoiceBox(Handler.getFilterList(), psChooserButton);

    private static CheckBox addFramerate = new CheckBox(" 视频加速 ");
    private static TextField addFrameRateTextField = new TextField();

    private static CheckBox reduceFramerate = new CheckBox(" 视频减速 ");
    private static TextField reduceFrameRateTextField = new TextField();

    private static CheckBox blurBackground = new CheckBox(" 背景虚化 ");

    private static CheckBox addVideo = new CheckBox("添加片头片尾");
    private static TextField startVideoText = new TextField(); //"请选择片头"
    private static TextField endVideoText = new TextField(); //"请选择片尾"
    //功能区域组件 end

    public static VBox getFunction(Stage primaryStage) {
        // 创建一个垂直箱子
        VBox vbox = new VBox();
        vbox.setStyle("-fx-border-style: solid inside");
        vbox.setMaxSize(400, 700);
        vbox.setMinSize(300, 670);

        // 创建一个水平箱子
        HBox titleBox = new HBox();
        titleBox.setPadding(new Insets(5, 5, 5, 5));
        Text title = new Text("功能区域");
        title.setFont(Font.font(java.awt.Font.SERIF, 25));
        titleBox.getChildren().addAll(title);

        //添加水印
        HBox addWatermarkBox = new HBox();
        addWatermarkBox.setPadding(new Insets(5, 5, 5, 5));
        HBox addWatermarkBox1 = new HBox();
        addWatermarkBox1.setPadding(new Insets(5, 5, 5, 5));
        Label watermarkXAxisLabel = new Label(" 水印x轴: ");
        addWatermarkOfX.setPrefWidth(100);
        Label watermarkYAxisLabel = new Label(" 水印y轴: ");
        addWatermarkOfY.setPrefWidth(100);
//        HBox addWatermarkBox2 = new HBox();
//        addWatermarkBox2.setPadding(new Insets(5, 5, 5, 5));
//        Label watermarkWidthLabel = new Label(" 水印宽度: ");
//        TextField addWatermarkOfWidth = new TextField();
//        addWatermarkOfWidth.setPrefWidth(100);
//        Label watermarkHeightLabel = new Label(" 水印高度: ");
//        TextField addWatermarkOfHeight = new TextField();
//        addWatermarkOfHeight.setPrefWidth(100);
        HBox addWatermarkBox3 = new HBox();
        addWatermarkBox3.setPadding(new Insets(5, 5, 5, 5));
        Label watermarkContentLabel = new Label(" 水印内容: ");
        addWatermarkOfContent.setPrefWidth(150);
        addWatermarkBox.getChildren().addAll(addWatermark);
        addWatermarkBox1.getChildren().addAll(watermarkXAxisLabel, addWatermarkOfX);
        addWatermarkBox1.getChildren().addAll(watermarkYAxisLabel, addWatermarkOfY);
        addWatermarkBox3.getChildren().addAll(watermarkContentLabel, addWatermarkOfContent);

        //删除水印
        HBox delWatermarkBox = new HBox();
        delWatermarkBox.setPadding(new Insets(5, 5, 5, 5));
        HBox delWatermarkBox1 = new HBox();
        delWatermarkBox1.setPadding(new Insets(5, 5, 5, 5));
        Label deleteWatermarkXAxisLabel = new Label(" 水印x轴: ");
        delWatermarkOfX.setPrefWidth(100);
        Label deleteWatermarkYAxisLabel = new Label(" 水印y轴: ");
        delWatermarkOfY.setPrefWidth(100);
        HBox delWatermarkBox2 = new HBox();
        delWatermarkBox2.setPadding(new Insets(5, 5, 5, 5));
        Label deleteWatermarkWidthLabel = new Label(" 水印宽度: ");
        delWatermarkOfWidth.setPrefWidth(100);
        Label deleteWatermarkHeightLabel = new Label(" 水印高度: ");
        delWatermarkOfHeight.setPrefWidth(100);

        //将删除水印文本框对象放入缓存
        Handler.put("x", delWatermarkOfX);
        Handler.put("y", delWatermarkOfY);
        Handler.put("width", delWatermarkOfWidth);
        Handler.put("height", delWatermarkOfHeight);

        HBox delWatermarkBox3 = new HBox();
        delWatermarkBox3.setPadding(new Insets(5, 5, 5, 5));
        Button selectWatermarkLocation = new Button("选择水印位置");
        selectWatermarkLocation.setOnAction(even -> {
            MediaView mediaView = MyMediaPlayer.getmView();
            int x = new Double(mediaView.getLayoutX()).intValue();
            int y = new Double(mediaView.getLayoutY()).intValue();
            int width = new Double(mediaView.getFitWidth()).intValue();
            int height = new Double(mediaView.getFitHeight()).intValue();
            x = x + 200;
            y = y + 50;
            LOG.info("x: {}, y: {}, 长: {}, 宽: {}", x, y, width, height);
            CapterScreen capterScreen = new CapterScreen();
            capterScreen.show(x, y, width, height);
        });
        delWatermarkBox.getChildren().addAll(delWatermark);
        delWatermarkBox1.getChildren().addAll(deleteWatermarkXAxisLabel, delWatermarkOfX);
        delWatermarkBox1.getChildren().addAll(deleteWatermarkYAxisLabel, delWatermarkOfY);
        delWatermarkBox2.getChildren().addAll(deleteWatermarkWidthLabel, delWatermarkOfWidth);
        delWatermarkBox2.getChildren().addAll(deleteWatermarkHeightLabel, delWatermarkOfHeight);
        delWatermarkBox3.getChildren().addAll(selectWatermarkLocation);


        //截取视频
        HBox cutVideoBox = new HBox();
        cutVideoBox.setPadding(new Insets(5, 5, 5, 5));
        HBox cutVideoBox1 = new HBox();
        cutVideoBox1.setPadding(new Insets(5, 5, 5, 5));
        Label startTimeLabel = new Label(" 开头删除");
        startTime.setPrefWidth(50);
        Label startTimeLabel1 = new Label("秒");
        Label endTimeLabel = new Label("  结尾删除");
        endTime.setPrefWidth(50);
        Label endTimeLabel1 = new Label("秒");
        cutVideoBox.getChildren().addAll(cutVideo);
        cutVideoBox1.getChildren().addAll(startTimeLabel, startTime, startTimeLabel1);
        cutVideoBox1.getChildren().addAll(endTimeLabel, endTime, endTimeLabel1);


        //设置视频封面
        HBox setCoverBox = new HBox();
        setCoverBox.setPadding(new Insets(5, 5, 5, 5));
        HBox setCoverBox1 = new HBox();
        setCoverBox1.setPadding(new Insets(5, 5, 5, 5));
        Label coverPathLabel = new Label(" 封面路径: ");
        coverPath.setPrefWidth(150);
        Button imgChooserButton = new Button("请选择");
        imgChooserButton.setOnAction(even -> {
            File file = MyChooser.getImageChooser().showOpenDialog(primaryStage);
            if (file != null) {
                LOG.info("选择图片: " + file.getAbsolutePath());
                coverPath.setText(file.getAbsolutePath());
            }
        });
        //HBox setCoverBox2 = new HBox();
        //setCoverBox2.setPadding(new Insets(5, 5, 5, 5));
//        Label coverWidthLabel = new Label(" 封面宽度: ");
//        TextField coverWidth = new TextField();
//        Label coverHeightLabel = new Label(" 封面高度: ");
//        TextField coverHeight = new TextField();
        setCoverBox.getChildren().addAll(setCover);
        setCoverBox1.getChildren().addAll(coverPathLabel, coverPath, imgChooserButton);
//        setCoverBox2.getChildren().addAll(coverWidthLabel, coverWidth);
//        setCoverBox2.getChildren().addAll(coverHeightLabel, coverHeight);


        //获取视频封面
        HBox getCoverBox = new HBox();
        getCoverBox.setPadding(new Insets(5, 5, 5, 5));
        Label cutVideoTimeLabel = new Label(" 视频时间: ");
        cutVideoTime.setPrefWidth(100);
        getCoverBox.getChildren().addAll(getCover, cutVideoTimeLabel, cutVideoTime);


        //添加滤镜
        HBox addFilterBox = new HBox();
        addFilterBox.setPadding(new Insets(5, 5, 5, 5));
        acvPath.setVisible(false);
        psChooserButton.setOnAction(even -> {
            File file = MyChooser.getAllFileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                LOG.info("选择PS预设文件: {}", file.getAbsolutePath());
                acvPath.setText(file.getAbsolutePath());
            }
        });
        psChooserButton.setVisible(false);
        addFilterBox.getChildren().addAll(addFilter, choiceBox, psChooserButton);


        //视频加速
        HBox addFrameRateBox = new HBox();
        addFrameRateBox.setPadding(new Insets(5));
        Label addFrameRateLabel = new Label("加速倍数: ");
        addFrameRateTextField.setPrefWidth(100);
        addFrameRateBox.getChildren().addAll(addFramerate, addFrameRateLabel, addFrameRateTextField);


        //视频减速
        HBox reduceFrameRateBox = new HBox();
        reduceFrameRateBox.setPadding(new Insets(5,5,5,5));
        Label reduceFrameRateLabel = new Label(" 减速倍数: ");
        reduceFrameRateTextField.setPrefWidth(100);
        reduceFrameRateBox.getChildren().addAll(reduceFramerate, reduceFrameRateLabel, reduceFrameRateTextField);

        //合并视频
//        HBox mergeVideoBox = new HBox();
//        mergeVideoBox.setPadding(new Insets(5,5,5,5));
//        CheckBox mergeVideo = new CheckBox(" 视频合并 ");
//        TextField folder = new TextField();
//        folder.setPrefWidth(150);
//        Button folderChooserButton = new Button("请选择");
//        folderChooserButton.setOnAction(even -> {
//            File file = MyChooser.getDirectoryChooser().showDialog(primaryStage);
//            if (file != null) {
//                LOG.info("选择文件夹: " + file.getAbsolutePath());
//                folder.setText(file.getAbsolutePath());
//            }
//        });
//        mergeVideoBox.getChildren().addAll(mergeVideo);


        //视频背景虚化
        HBox blurBackgroundBox = new HBox();
        blurBackgroundBox.setPadding(new Insets(5,5,5,5));
        blurBackgroundBox.getChildren().addAll(blurBackground);

        //添加片头片尾
        HBox addVideoBox = new HBox();
        addVideoBox.setPadding(new Insets(5,5,5,5));
        HBox addVideoBox1 = new HBox();
        addVideoBox1.setPadding(new Insets(5));
        Label startVideoLabel = new Label(" 片头: ");
        Button startVideoButton = new Button("请选择");
        startVideoButton.setOnAction(even -> {
            File file = MyChooser.getFileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                LOG.info("选择片头: " + file.getAbsolutePath());
                startVideoText.setText(file.getAbsolutePath());
            }
        });
        HBox addVideoBox2 = new HBox();
        addVideoBox2.setPadding(new Insets(5,5,5,5));
        Label endVideoLabel = new Label(" 片尾: ");
        Button endVideoButton = new Button("请选择");
        endVideoButton.setOnAction(even -> {
            File file = MyChooser.getFileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                LOG.info("选择片尾: " + file.getAbsolutePath());
                endVideoText.setText(file.getAbsolutePath());
            }
        });
        addVideoBox.getChildren().addAll(addVideo);
        addVideoBox1.getChildren().addAll(startVideoLabel, startVideoText, startVideoButton);
        addVideoBox2.getChildren().addAll(endVideoLabel, endVideoText, endVideoButton);

        //处理视频按钮
        HBox dealWithBox = new HBox();
        //dealWithBox.setPadding(new Insets(5, 5, 5, 5));
        Button dealWithSingle = new Button("处理当前视频");
        Button dealWithBath = new Button("处理列表视频");
        HBox.setMargin(dealWithSingle, new Insets(10));
        HBox.setMargin(dealWithBath, new Insets(10));

        //处理单个
        dealWithSingle.setOnAction(even -> {
            LOG.info("处理单个");
            if(null == Handler.getListView("unProcessed")) {
                MyAlertBox.display("程序提示", "请导入视频!");
                return;
            }

            //校验用户是否配置targetPath
            Map<String, String> config = Handler.readProp();
            String configPath = config.get("targetPath");
            if(null == configPath || "".equals(configPath)) {
                MyAlertBox.display("程序提示", "请设置生成文件路径!");
                return;
            }

            if(cutVideo.isSelected()) {
                //校验剪切视频参数是否合法
                boolean flag = Handler.checkCutTime(startTime.getText(), endTime.getText(), Handler.getListView("unProcessed").getCurrentVideoPath());
                if(flag) {
                    return;
                }
            }

            //清空封面路径缓存
            Handler.clearCoverPathList();

            //不可再点击
            dealWithSingle.setDisable(true);

            //显示进度条并给初始值
            singleProgressBar.setVisible(true);
            singleProgressBar.setValue(0.0);
            singleProgressBar.setLabel("开始执行");

            Thread task = new Thread(()->{
                boolean addWatermarkSelected = addWatermark.isSelected();
                boolean delWatermarkSelected = delWatermark.isSelected();
                boolean cutVideoSelected = cutVideo.isSelected();
                boolean setCoverSelected = setCover.isSelected();
                boolean addFilterSelected = addFilter.isSelected();
                boolean addFramerateSelected = addFramerate.isSelected();
                boolean reduceFramerateSelected = reduceFramerate.isSelected();
                //boolean mergeVideoSelected = mergeVideo.isSelected();
                boolean blurBackgroundSelected = blurBackground.isSelected();
                boolean getCoverSelected = getCover.isSelected();
                boolean addVideoSelected = addVideo.isSelected();

                //功能区域每个复选框绑定了监听事件
                singleProgressBar.calculationStep(Handler.getCheckBoxList());

                //当前播放器播放的视频地址
                String currentVideo = Handler.getListView("unProcessed").getCurrentVideoPath();
                if (null == currentVideo || "".equals(currentVideo) || "暂无数据".equals(currentVideo)) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            MyAlertBox.display("程序提示", "未选择视频!");
                            dealWithSingle.setDisable(false);
                        }
                    });
                    return;
                }

                //产生的视频地址都放入这里
                List<String> pathList = new ArrayList<>();

                //截取图片
                if(getCoverSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在截取视频封面");
                        }
                    });
                    String time = cutVideoTime.getText();
                    time = Handler.formatTime(Long.valueOf(time));
                    LOG.info("操作步骤: 截取图片 操作对象: {}", currentVideo);
                    String targetPath = Handler.getNewFilePath("D:\\MaXinHai\\file\\1.png");
                    videoExecutor.cutVideoImage(currentVideo, targetPath, time);
                    try {
                        //通过cmd命令打开图片
                        Runtime.getRuntime().exec("cmd /c " + targetPath);
                        //把封面路径写入缓存
                        Handler.addCoverPath(targetPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //添加片头片尾
                if(addVideoSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在添加片头片尾");
                        }
                    });
                    String startVideoPath = startVideoText.getText();
                    String endVideoPath = endVideoText.getText();
                    String targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.mergeVideo(startVideoPath, currentVideo, endVideoPath, targetPath);
                    Handler.deleteFile(currentVideo);
                    pathList.add(targetPath);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //消除水印
                if (delWatermarkSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在消除水印");
                        }
                    });
                    String x = delWatermarkOfX.getText();
                    String y = delWatermarkOfY.getText();
                    String width = delWatermarkOfWidth.getText();
                    String height = delWatermarkOfHeight.getText();

                    BigDecimal xValue = new BigDecimal(x);
                    BigDecimal yValue = new BigDecimal(y);
                    BigDecimal widthValue = new BigDecimal(width);
                    BigDecimal heightValue = new BigDecimal(height);
                    //四舍五入不保留小数
                    x = xValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                    y = yValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                    width = widthValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                    height = heightValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                    LOG.info("操作步骤:消除水印 操作对象: " + currentVideo);
                    LOG.info("消除水印 水印坐标: x {}, y {}, height {}, width: {}", x, y, height, width);
                    String targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.removeWatermark(currentVideo, x, y, width, height, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    pathList.add(targetPath);
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        MyAlertBox.display("消除水印提示", "消除水印失败");
                        e.printStackTrace();
                    } finally {
                        dealWithSingle.setDisable(true);
                    }
                }

                //加水印
                if (addWatermarkSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在添加水印");
                        }
                    });
                    String x = addWatermarkOfX.getText();
                    String y = addWatermarkOfY.getText();
                    String text = addWatermarkOfContent.getText();
                    LOG.info("操作步骤:加水印 操作对象: " + currentVideo);
                    String targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.addWatermarkByFont(text, 30, "微软雅黑", x, y, currentVideo, targetPath);
                    pathList.add(targetPath);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //剪切视频
                if (cutVideoSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在剪切视频");
                        }
                    });
                    String start = startTime.getText();
                    String end = endTime.getText();
                    MultimediaInfo videoInfo = videoExecutor.getVideoInfo(currentVideo);
                    long duration = videoInfo.getDuration(); //获取视频长度
                    BigDecimal durationValue = new BigDecimal(String.valueOf(duration));
                    BigDecimal endValue = new BigDecimal(end + "000");
                    start = Handler.formatTime(Long.valueOf(start));
                    BigDecimal standardValue= new BigDecimal("1000");
                    //视频总长度减去要删除的秒数再除以1000在向上取整得到的就是结束秒数
                    end = Handler.formatTime(durationValue.subtract(endValue).divide(standardValue).setScale(0, BigDecimal.ROUND_UP).longValue());
                    String targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.cutVideo(currentVideo, targetPath, start, end);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    pathList.add(targetPath);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //添加滤镜效果
                if (addFilterSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在添加滤镜");
                        }
                    });
                    String selected = MyChoiceBox.getSelected();
                    filterDealWith(selected, currentVideo, acvPath, pathList);
                }
                //提升视频播放速度
                if (addFramerateSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在提升视频播放速度");
                        }
                    });
                    String targetPath = Handler.getNewFilePath(currentVideo);
                    String videoFrameRate = addFrameRateTextField.getText();
                    String audioFrameRate = addFrameRateTextField.getText();
                    videoExecutor.addVideoAudioFramerate(currentVideo, targetPath, videoFrameRate, audioFrameRate);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    pathList.add(targetPath);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //降低视频播放速度
                if (reduceFramerateSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在降低视频播放速度");
                        }
                    });
                    String targetPath = Handler.getNewFilePath(currentVideo);
                    String videoFrameRate = reduceFrameRateTextField.getText();
                    videoExecutor.reduceFramerate(currentVideo, targetPath, videoFrameRate);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    pathList.add(targetPath);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //背景虚化
                if (blurBackgroundSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在背景虚化");
                        }
                    });
                    String targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.blurBackground(currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    pathList.add(targetPath);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (setCoverSelected) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            singleProgressBar.autoAdd();
                            singleProgressBar.setLabel("正在设置视频封面");
                        }
                    });
                    String imgPath = coverPath.getText();
                    //封面路径为空
                    if(null == imgPath || "".equals(imgPath)) {
                        //是否勾选获取图片及获取封面功能
                        if(getCoverSelected) {
                            imgPath = Handler.getCoverPathList().get(0);
                        } else {
                            new RuntimeException("未勾选获取图片或未选择封面文件");
                        }
                    }
                    //TODO 宽和高暂时没有
                    //String height = coverHeight.getText();
                    //String width = coverWidth.getText();
                    LOG.info("操作步骤:设置封面 操作对象: {}", currentVideo);
                    String targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.setCover(currentVideo, imgPath, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    pathList.add(targetPath);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //更新UI
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //不是视频截图 就刷新已处理视频栏
                        if(pathList.size() > 0) {
                            String videoPath = pathList.get(pathList.size() - 1);
                            //刷新左侧视频列表
                            processedList.add(videoPath);
                            MyHome.setLeft(null, processedList);
                            //选择视频地址并让播放组件播放视频
                            MyMediaPlayer.chooseFile(new File(videoPath));
                        }
                        dealWithSingle.setDisable(false);
                        singleProgressBar.setVisible(true);
                        singleProgressBar.setValue(1.0);
                        singleProgressBar.setLabel("处理完毕");
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        myProgressBar.setVisible(false);
                    }
                });
            });
            MyExecutorService.getTaskExecutor().execute(task);
        });

        //批量处理
        dealWithBath.setOnAction(event -> {
            LOG.info("批量处理");
            if(null == Handler.getListView("unProcessed")) {
                MyAlertBox.display("程序提示", "请导入视频!");
                return;
            }

            List<String> filePathList = Handler.getListView("unProcessed").getFilePathList();
            if (null == filePathList || filePathList.size() == 0) {
                MyAlertBox.display("程序提示", "未选择视频!");
                return;
            }

            //清除封面路径缓存
            Handler.clearCoverPathList();

            Map<String, String> config = Handler.readProp();
            String targetPath = config.get("targetPath");
            if(null == targetPath || "".equals(targetPath)) {
                MyAlertBox.display("程序提示", "请设置生成文件路径!");
                return;
            }

            //copy文件到生成文件夹下
            List<String> targetPathList = Handler.batchCopyFile(filePathList, targetPath);

            //转换为线程安全的list,所有线程每个步骤产生的视频路径都存放在这个集合
            List<String> allPathList = Collections.synchronizedList(new ArrayList<>());

            //要删除的多余文件(会出现多线程添加元素)
            Set<String> deletePathSet = new CopyOnWriteArraySet(targetPathList);

            final int count = filePathList.size();
            final CountDownLatch endGate = new CountDownLatch(count);

            boolean addWatermarkSelected = addWatermark.isSelected();
            boolean delWatermarkSelected = delWatermark.isSelected();
            boolean cutVideoSelected = cutVideo.isSelected();
            boolean getCoverSelected = getCover.isSelected();
            boolean setCoverSelected = setCover.isSelected();
            boolean addFilterSelected = addFilter.isSelected();
            boolean addFramerateSelected = addFramerate.isSelected();
            boolean reduceFramerateSelected = reduceFramerate.isSelected();
            // boolean mergeVideoSelected = mergeVideo.isSelected();
            boolean blurBackgroundSelected = blurBackground.isSelected();
            boolean addVideoSelected = addVideo.isSelected();

            //不可点击批量处理按钮
            dealWithBath.setDisable(true);

            //计算进度条step
            batchProgressBar.calculationStep(Handler.getCheckBoxList().size(), filePathList.size());

            //显示进度条并给初始值
            batchProgressBar.setVisible(true);
            batchProgressBar.setValue(0.0);
            batchProgressBar.setLabel("开始执行");

            ExecutorService executorService = MyExecutorService.getTaskExecutor();
            filePathList.forEach(path -> {
                Thread task = new Thread(()->{
                    final  VideoExecutor executor = new VideoExecutor();
                    String currentPath = path;

                    //截取图片
                    if(getCoverSelected) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在截取视频图片 " + Handler.getFileName(path));
                            }
                        });
                        String time = cutVideoTime.getText();
                        time = Handler.formatTime(Long.valueOf(time));
                        try {
                            LOG.info("操作步骤: 批量截取图片 操作对象: {}", currentPath);
                            String target = Handler.getNewFilePath("D:\\MaXinHai\\file\\1.png");
                            executor.cutVideoImage(currentPath, target, time);
                            Thread.sleep(500);
                            Handler.addCoverPath(target);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }
                    //删除水印
                    if (delWatermarkSelected) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在消除水印 " + Handler.getFileName(path));
                            }
                        });
                        String x = delWatermarkOfX.getText();
                        String y = delWatermarkOfY.getText();
                        String width = delWatermarkOfWidth.getText();
                        String height = delWatermarkOfHeight.getText();
                        BigDecimal xValue = new BigDecimal(x);
                        BigDecimal yValue = new BigDecimal(y);
                        BigDecimal widthValue = new BigDecimal(width);
                        BigDecimal heightValue = new BigDecimal(height);
                        try {
                            //先计算比例，才能去拿值
                            MyMediaPlayer.calculationRatio(new File(currentPath));
                            x = xValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                            y = yValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                            width = widthValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                            height = heightValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();

                            String target = Handler.getNewFilePath(currentPath);
                            LOG.info("操作步骤:消除水印 操作对象: {}", currentPath);
                            executor.removeWatermark(currentPath, x, y, width, height, target);
                            Thread.sleep(500);
                            deletePathSet.add(currentPath);
                            allPathList.add(target);
                            currentPath = target;
                        } catch (InterruptedException e) {
                            LOG.info("批量消除水印出错! {}", e.getMessage());
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }
                    //添加水印
                    if (addWatermarkSelected) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在添加水印 " + Handler.getFileName(path));
                            }
                        });
                        String x = addWatermarkOfX.getText();
                        String y = addWatermarkOfY.getText();
                        String text = addWatermarkOfContent.getText();
                        try {
                            String target = Handler.getNewFilePath(currentPath);
                            LOG.info("操作步骤:加水印 操作对象: {}", currentPath);
                            executor.addWatermarkByFont(text, 30, "微软雅黑", x, y, currentPath, target);
                            deletePathSet.add(currentPath);
                            allPathList.add(target);
                            currentPath = target;
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            LOG.info("批量添加水印失败! {}", e.getMessage());
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }
                    //添加片头片尾
                    if(addVideoSelected) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在添加片头片尾 " + Handler.getFileName(path));
                            }
                        });
                        String startVideoPath = startVideoText.getText();
                        String endVideoPath = endVideoText.getText();
                        try {
                            String target = Handler.getNewFilePath(currentPath);
                            LOG.info("操作步骤:批量添加片头片尾 操作对象: {}", currentPath);
                            executor.mergeVideo(startVideoPath, currentPath, endVideoPath, target);
                            Thread.sleep(500);
                            deletePathSet.add(currentPath);
                            allPathList.add(target);
                            currentPath = target;
                        } catch (InterruptedException e) {
                            LOG.info("批量添加片头片尾出错! {}", e.getMessage());
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }
                    if (cutVideoSelected) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在剪切视频 " + Handler.getFileName(path));
                            }
                        });
                        String start = startTime.getText();
                        String end = endTime.getText();
                        String startPoint = null;
                        String endPoint = null;
                        try {
                            MultimediaInfo videoInfo = videoExecutor.getVideoInfo(currentPath);
                            long duration = videoInfo.getDuration(); //获取视频长度
                            BigDecimal durationValue = new BigDecimal(String.valueOf(duration));
                            BigDecimal endValue = new BigDecimal(end + "000");
                            startPoint = Handler.formatTime(Long.valueOf(start));
                            BigDecimal standardValue= new BigDecimal("1000");
                            //视频总长度减去要删除的秒数再除以1000在向上取整得到的就是结束秒数
                            endPoint = Handler.formatTime(durationValue.subtract(endValue).divide(standardValue).setScale(0, BigDecimal.ROUND_UP).longValue());
                            String target = Handler.getNewFilePath(currentPath);
                            LOG.info("操作步骤:剪切视频 操作对象: {}", currentPath);
                            executor.cutVideo(currentPath, target, startPoint, endPoint);
                            Thread.sleep(500);
                            deletePathSet.add(currentPath);
                            allPathList.add(target);
                            currentPath = target;
                        } catch (InterruptedException e) {
                            LOG.info("批量剪切视频出错! {}", e.getMessage());
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }
                    if (addFilterSelected) {
                        String selected = MyChoiceBox.getSelected();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在添加滤镜,滤镜效果:" + selected);
                            }
                        });
                        if (selected.equals("镜像")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤:设置镜像效果 操作对象: {}", currentPath);
                                executor.mirror(currentPath, target);
                                Thread.sleep(500);
                                deletePathSet.add(currentPath);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量设置镜像效果出错! {}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        if (selected.equals("复古风")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤:设置复古风效果 操作对象: {}", currentPath);
                                executor.ancientStyleFilter(currentPath, target);
                                Thread.sleep(500);
                                deletePathSet.add(currentPath);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量设置复古风效果出错! {}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        if (selected.equals("多路拼接")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤:设置多路拼接效果 操作对象: {}", currentPath);
                                executor.spliceVideo(currentPath, currentPath, currentPath, currentPath, target);
                                Thread.sleep(500);
                                deletePathSet.add(currentPath);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量设置多路拼接效果出错 {}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        if (selected.equals("Ps滤镜")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤: 根据Ps预设文件设置效果 操作对象: {}", currentPath);
                                executor.revisionCurveByPs(currentPath, acvPath.getText(), target);
                                deletePathSet.add(currentPath);
                                Thread.sleep(500);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量根据Ps预设文件设置效果出错! {}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        if (selected.equals("锐化")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤:设置锐化效果 操作对象: {}", currentPath);
                                executor.sharpen(currentPath, target);
                                deletePathSet.add(currentPath);
                                Thread.sleep(500);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量设置锐化效果出错! {}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        if (selected.equals("黑白")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤:设置黑白效果 操作对象: {}", currentPath);
                                executor.blackWhite(currentPath, target);
                                deletePathSet.add(currentPath);
                                Thread.sleep(500);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量设置黑白效果出错 {}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        if (selected.equals("浮雕效果")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤:设置浮雕效果 操作对象: {}", currentPath);
                                executor.reliefEffect(currentPath, target);
                                deletePathSet.add(currentPath);
                                Thread.sleep(500);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量设置浮雕效果出错! {}", e.getMessage());
                                e.printStackTrace();
                            }

                        }
                        if (selected.equals("模糊处理")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤:设置模糊处理 操作对象: {}", currentPath);
                                executor.blur(currentPath, target);
                                deletePathSet.add(currentPath);
                                Thread.sleep(500);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量设置模糊处理出错!  {}", e.getMessage());
                                e.printStackTrace();
                            } finally {
                                dealWithBath.setDisable(false);
                            }

                        }
                        if (selected.equals("色彩变幻")) {
                            try {
                                String target = Handler.getNewFilePath(currentPath);
                                LOG.info("操作步骤:设置色彩变幻 操作对象: {}", currentPath);
                                executor.colorChange(currentPath, target);
                                deletePathSet.add(currentPath);
                                Thread.sleep(500);
                                allPathList.add(target);
                                currentPath = target;
                            } catch (InterruptedException e) {
                                LOG.info("批量设置色彩变幻出错! {}", e.getMessage());
                                e.printStackTrace();
                            } finally {
                                dealWithBath.setDisable(false);
                            }
                        }
                    }

                    //增加视频帧率
                    if (addFramerateSelected) {
                        LOG.info("增加视频速率");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在增加视频速率 " + Handler.getFileName(path));
                            }
                        });
                        String frameRate = addFrameRateTextField.getText();
                        try {
                            String target = Handler.getNewFilePath(currentPath);
                            LOG.info("操作步骤:增加视频速率 操作对象: {}", currentPath);
                            executor.addFramerate(currentPath, target, frameRate);
                            Thread.sleep(500);
                            deletePathSet.add(currentPath);
                            allPathList.add(target);
                            currentPath = target;
                        } catch (InterruptedException e) {
                            LOG.info("批量设置镜像效果增加视频速率 {}", e.getMessage());
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }

                    //降低视频帧率
                    if (reduceFramerateSelected) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在降低视频帧率 " + Handler.getFileName(path));
                            }
                        });
                        String frameRate = reduceFrameRateTextField.getText();
                        try {
                            String target = Handler.getNewFilePath(currentPath);
                            LOG.info("操作步骤:降低视频帧率 操作对象: {}", currentPath);
                            executor.reduceFramerate(currentPath, target, frameRate);
                            deletePathSet.add(currentPath);
                            Thread.sleep(500);
                            allPathList.add(target);
                            currentPath = target;
                        } catch (InterruptedException e) {
                            LOG.info("批量降低视频帧率! {}", e.getMessage());
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }

                    //模糊视频背景
                    if (blurBackgroundSelected) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在模糊视频背景 " + Handler.getFileName(path));
                            }
                        });
                        try {
                            String target = Handler.getNewFilePath(currentPath);
                            LOG.info("操作步骤:设置模糊视频背景 操作对象: {}", currentPath);
                            executor.blurBackground(currentPath, target);
                            Thread.sleep(500);
                            deletePathSet.add(currentPath);
                            allPathList.add(target);
                            currentPath = target;
                        } catch (Exception e) {
                            LOG.info("批量设置模糊视频背景出错! {}", e.getMessage());
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }

                    //设置封面
                    if (setCoverSelected) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                batchProgressBar.autoAdd();
                                batchProgressBar.setLabel("正在设置视频封面 " + Handler.getFileName(path));
                            }
                        });
                        String imgPath = coverPath.getText();
                        if(null == imgPath || "".equals(imgPath)) {
                            new RuntimeException("未设置封面文件!");
                        }
                        //TODO 宽和高暂时没有
                        //String height = coverHeight.getText();
                        //String width = coverWidth.getText();
                        try {
                            String target = Handler.getNewFilePath(currentPath);
                            LOG.info("操作步骤:批量设置封面 操作对象: {}", currentPath);
                            executor.setCover(currentPath, imgPath, target);
                            Thread.sleep(500);
                            deletePathSet.add(currentPath);
                            allPathList.add(target);
                        } catch (InterruptedException e) {
                            LOG.info("批量设置封面出错! {}", e.getMessage());
                            e.printStackTrace();
                        } finally {
                            dealWithBath.setDisable(false);
                        }
                    }

                    endGate.countDown();
                    LOG.info("任务数量: {}", endGate.getCount());
                });
                executorService.execute(task);
            });

            Thread uploadUI = new Thread(()->{
                //判断线程池里的线程是否全部执行完毕
                try {
                    endGate.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                LOG.info("所有视频: {}", allPathList);
                LOG.info("要删除的视频: {}", deletePathSet);
                //所有线程产生的视频路径减去所有线程要删除的视频路径就是最终的视频
                allPathList.removeAll(deletePathSet);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MyHome.setLeft(null, allPathList);
                        File videoFile = new File(targetPathList.get(0));
                        //播放第一个视频并让播放组件播放视频
                        MyMediaPlayer.chooseFile(videoFile);
                        batchProgressBar.setValue(1);
                        batchProgressBar.setLabel("执行完毕!");
                        dealWithBath.setDisable(false);
                    }
                });

                LOG.info("开始删除多余文件...");
                deletePathSet.forEach(delPath -> {
                    Handler.deleteFile(delPath);
                });
                LOG.info("删除多余文件完毕...");
            });
            executorService.execute(uploadUI);
        });

        dealWithBox.getChildren().addAll(dealWithSingle, dealWithBath);

        //跳转场景后，恢复用户数据
        setUserOperating();

        vbox.getChildren().addAll(titleBox);
        vbox.getChildren().addAll(delWatermarkBox, delWatermarkBox1, delWatermarkBox2, delWatermarkBox3); //删除水印
        vbox.getChildren().addAll(cutVideoBox, cutVideoBox1); //剪切视频
        vbox.getChildren().addAll(setCoverBox, setCoverBox1); //设置视频封面
        vbox.getChildren().addAll(getCoverBox); //截图图片
        vbox.getChildren().addAll(addFilterBox); //添加滤镜效果
        vbox.getChildren().addAll(addFrameRateBox, reduceFrameRateBox); //视频加速 减速
        //vbox.getChildren().addAll(mergeVideoBox); //合并多个视频
        vbox.getChildren().addAll(blurBackgroundBox); //视频背景虚化
        vbox.getChildren().addAll(addWatermarkBox, addWatermarkBox1, /*addWatermarkBox2,*/ addWatermarkBox3); //添加水印
        vbox.getChildren().addAll(addVideoBox, addVideoBox1, addVideoBox2); //添加片头片尾
        vbox.getChildren().addAll(dealWithBox); //处理按钮
        vbox.getChildren().addAll(singleSchedule); //任务进度条
        vbox.getChildren().addAll(batchSchedule);
        return vbox;
    }


    static {
        //给复选框添加监听事件
        addWatermark.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });
        delWatermark.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });
        cutVideo.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });
        setCover.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });
        getCover.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });
        addFilter.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });
        addFramerate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });
        reduceFramerate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });
        blurBackground.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Handler.addSelect(newValue);
                } else {
                    Handler.removeSelect();
                }
                LOG.info("当前选择结果: {}, 复选框选择结果: {}", newValue, Handler.getCheckBoxList().size());
            }
        });

        //给文本框添加监听事件
        startTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(null != newValue && !"".equals(newValue)) {
                    boolean isNumber = Handler.isNumber(newValue);
                    if(!isNumber) {
                        MyAlertBox.display("文本框提示", "输入内容不是整数！");
                    }
                }
            }
        });
        endTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(null != newValue && !"".equals(newValue)) {
                    boolean isNumber = Handler.isNumber(newValue);
                    if(!isNumber) {
                        MyAlertBox.display("文本框提示", "输入内容不是整数！");
                    }
                }
            }
        });
        addFrameRateTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(null != newValue && !"".equals(newValue)) {
                    boolean isNumber = Handler.isNumber(newValue);
                    if(!isNumber) {
                        MyAlertBox.display("文本框提示", "输入内容不是整数！");
                    }
                }
            }
        });
        reduceFrameRateTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(null != newValue && !"".equals(newValue)) {
                    boolean isNumber = Handler.isNumber(newValue);
                    if(!isNumber) {
                        MyAlertBox.display("文本框提示", "输入内容不是整数！");
                    }
                }
            }
        });
        cutVideoTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(null != newValue && !"".equals(newValue)) {
                    boolean isNumber = Handler.isNumber(newValue);
                    if(!isNumber) {
                        MyAlertBox.display("文本框提示", "输入内容不是整数！");
                    }
                }
            }
        });
    }

    /**
     * 是否配置targetPath路径
     * @throws RuntimeException
     */
    private static void isTargetPath() throws RuntimeException{
        Map<String, String> config = Handler.readProp();
        String targetPath = config.get("targetPath");
        if(null == targetPath || "".equals(targetPath)) {
            MyAlertBox.display("程序提示", "请设置生成文件路径!");
            new RuntimeException("未设置生成文件路径!");
        }
    }


    /**
     * 视频滤镜处理
     * @param selected
     * @param currentVideo
     * @param acvPath
     * @param pathList
     */
    private static void filterDealWith(String selected, String currentVideo, TextField acvPath, List<String> pathList) {
        if (selected.equals("复古风")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            videoExecutor.ancientStyleFilter(currentVideo, targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (selected.equals("镜像")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            videoExecutor.mirror(currentVideo, targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (selected.equals("多路拼接")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            videoExecutor.spliceVideo(currentVideo, currentVideo, currentVideo, currentVideo, targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (selected.equals("Ps滤镜")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            videoExecutor.revisionCurveByPs(currentVideo, acvPath.getText(), targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (selected.equals("锐化")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            System.out.println("操作步骤:设置锐化效果 操作对象: " + targetPath);
            videoExecutor.sharpen(currentVideo, targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (selected.equals("黑白")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            System.out.println("操作步骤:设置黑白效果 操作对象: " + targetPath);
            videoExecutor.blackWhite(currentVideo, targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (selected.equals("浮雕效果")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            System.out.println("操作步骤:设置浮雕效果 操作对象: " + targetPath);
            videoExecutor.reliefEffect(currentVideo, targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (selected.equals("模糊处理")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            System.out.println("操作步骤:设置模糊处理 操作对象: " + targetPath);
            videoExecutor.blur(currentVideo, targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (selected.equals("色彩变幻")) {
            String targetPath = Handler.getNewFilePath(currentVideo);
            LOG.info("操作步骤:设置色彩变幻 操作对象: {}", targetPath);
            videoExecutor.colorChange(currentVideo, targetPath);
            //删除上一步产生的视频
            Handler.deleteFile(currentVideo);
            pathList.add(targetPath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 保存用户操作数据方法
     */
    public static void saveUserOperatingCache() {
        //每次保存用户缓存前，先清空用户操作缓存
        Handler.clearUserOperatingCache();

        boolean addWatermarkSelected = addWatermark.isSelected();
        boolean delWatermarkSelected = delWatermark.isSelected();
        boolean cutVideoSelected = cutVideo.isSelected();
        boolean getCoverSelected = getCover.isSelected();
        boolean setCoverSelected = setCover.isSelected();
        boolean addFilterSelected = addFilter.isSelected();
        boolean addFramerateSelected = addFramerate.isSelected();
        boolean reduceFramerateSelected = reduceFramerate.isSelected();
        // boolean mergeVideoSelected = mergeVideo.isSelected();
        boolean blurBackgroundSelected = blurBackground.isSelected();
        boolean addVideoSelected = addVideo.isSelected();

        //添加水印
        if(addWatermarkSelected) {
            String x = addWatermarkOfX.getText();
            String y = addWatermarkOfY.getText();
            String content = addWatermarkOfContent.getText();
            List<String> data = new ArrayList<>();
            data.add(x);
            data.add(y);
            data.add(content);
            Handler.putUserOperating("添加水印", data);
        }
        //删除水印
        if(delWatermarkSelected) {
            String x = delWatermarkOfX.getText();
            String y = delWatermarkOfY.getText();
            String width = delWatermarkOfWidth.getText();
            String height = delWatermarkOfHeight.getText();
            List<String> data = new ArrayList<>(4);
            data.add(x);
            data.add(y);
            data.add(width);
            data.add(height);
            Handler.putUserOperating("删除水印", data);
        }
        //剪切视频(去头去尾)
        if(cutVideoSelected) {
            String start = startTime.getText();
            String end = endTime.getText();
            List<String> data = new ArrayList<>(2);
            data.add(start);
            data.add(end);
            Handler.putUserOperating("剪切视频", data);
        }
        //设置封面
        if(setCoverSelected) {
            String cover = coverPath.getText();
            List<String> data = new ArrayList<>(1);
            data.add(cover);
            Handler.putUserOperating("设置封面", data);
        }
        //截取图片
        if(getCoverSelected) {
            String cutTime = cutVideoTime.getText();
            List<String> data = new ArrayList<>(1);
            data.add(cutTime);
            Handler.putUserOperating("截取图片", data);
        }
        //添加滤镜
        if(addFilterSelected) {
            List<String> data = new ArrayList<>(2);
            String selected = MyChoiceBox.getSelected();
            data.add(selected);
            if(selected.equals("Ps滤镜")) {
                String path = acvPath.getText();
                data.add(path);
            }
            Handler.putUserOperating("添加滤镜", data);
        }
        //视频加速
        if(addFramerateSelected) {
            String frameRate = addFrameRateTextField.getText();
            List<String> data = new ArrayList<>(1);
            data.add(frameRate);
            Handler.putUserOperating("视频加速", data);
        }
        //视频减速
        if(reduceFramerateSelected) {
            String frameRate = reduceFrameRateTextField.getText();
            List<String> data = new ArrayList<>(1);
            data.add(frameRate);
            Handler.putUserOperating("视频减速", data);
        }
        //背景虚化
        if(blurBackgroundSelected) {
            //只要hashMap中有key为背景虚化，就认为它是被选中的
            Handler.putUserOperating("背景虚化", null);
        }
        //添加片头片尾
        if(addVideoSelected) {
            String start = startVideoText.getText();
            String end = endVideoText.getText();
            List<String> data = new ArrayList<>(2);
            data.add(start);
            data.add(end);
            Handler.putUserOperating("添加片头片尾", data);
        }
    }


    /**
     * 设置用户操作
     */
    public static void setUserOperating() {
        Map<String, List<String>> allUserOperatingCache = Handler.getAllUserOperatingCache();
        if(allUserOperatingCache.containsKey("添加水印")) {
            addWatermark.setSelected(true);
            List<String> data = allUserOperatingCache.get("添加水印");
            if(!EmptyUtils.isEmpty(data)) {
                addWatermarkOfX.setText(data.get(0));
                addWatermarkOfY.setText(data.get(1));
                addWatermarkOfContent.setText(data.get(2));
            }
        }
        if(allUserOperatingCache.containsKey("删除水印")) {
            delWatermark.setSelected(true);
            List<String> data = allUserOperatingCache.get("删除水印");
            if(!EmptyUtils.isEmpty(data)) {
                delWatermarkOfX.setText(data.get(0));
                delWatermarkOfY.setText(data.get(1));
                delWatermarkOfWidth.setText(data.get(2));
                delWatermarkOfHeight.setText(data.get(3));
            }
        }
        if(allUserOperatingCache.containsKey("剪切视频")) {
            cutVideo.setSelected(true);
            List<String> data = allUserOperatingCache.get("剪切视频");
            if (!EmptyUtils.isEmpty(data)) {
                startTime.setText(data.get(0));
                endTime.setText(data.get(1));
            }
        }
        if(allUserOperatingCache.containsKey("设置封面")) {
            setCover.setSelected(true);
            List<String> data = allUserOperatingCache.get("设置封面");
            if (!EmptyUtils.isEmpty(data)) {
                coverPath.setText(data.get(0));
            }
        }
        if(allUserOperatingCache.containsKey("截取图片")) {
            getCover.setSelected(true);
            List<String> data = allUserOperatingCache.get("截取图片");
            if (!EmptyUtils.isEmpty(data)) {
                cutVideoTime.setText(data.get(0));
            }
        }
        if(allUserOperatingCache.containsKey("添加滤镜")) {
            addFilter.setSelected(true);
            List<String> data = allUserOperatingCache.get("添加滤镜");
            if (!EmptyUtils.isEmpty(data)) {
                MyChoiceBox.selectItem(data.get(0));
                if(data.get(0).equals("Ps滤镜")) {
                    acvPath.setText(data.get(1));
                }
            }
        }
        if(allUserOperatingCache.containsKey("视频加速")) {
            addFramerate.setSelected(true);
            List<String> data = allUserOperatingCache.get("视频加速");
            if (!EmptyUtils.isEmpty(data)) {
                addFrameRateTextField.setText(data.get(0));
            }
        }
        if(allUserOperatingCache.containsKey("视频减速")) {
            reduceFramerate.setSelected(true);
            List<String> data = allUserOperatingCache.get("视频减速");
            if (!EmptyUtils.isEmpty(data)) {
                reduceFrameRateTextField.setText(data.get(0));
            }
        }
        if(allUserOperatingCache.containsKey("背景虚化")) {
            blurBackground.setSelected(true);
        }
    }
}
