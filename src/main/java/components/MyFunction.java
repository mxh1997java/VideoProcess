package components;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import executor.VideoExecutor;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.BlurBackgroundTask;
import task.MyExecutorService;
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

    private static List<String> processedList = new ArrayList<>();

    //private static HBox progressBarBox = MyProgressBar.getProgressBar();

    public static VBox getFunction(Stage primaryStage) {
        // 创建一个垂直箱子
        VBox vbox = new VBox();
        // vbox.setStyle("-fx-background-color: red");
        vbox.setStyle("-fx-border-style: solid inside");
        vbox.setMaxSize(400, 670);
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
        CheckBox addWatermark = new CheckBox("添加水印");
        HBox addWatermarkBox1 = new HBox();
        addWatermarkBox1.setPadding(new Insets(5, 5, 5, 5));
        Label watermarkXAxisLabel = new Label(" 水印x轴: ");
        TextField addWatermarkOfX = new TextField();
        addWatermarkOfX.setPrefWidth(100);
        Label watermarkYAxisLabel = new Label(" 水印y轴: ");
        TextField addWatermarkOfY = new TextField();
        addWatermarkOfY.setPrefWidth(100);
        HBox addWatermarkBox2 = new HBox();
        addWatermarkBox2.setPadding(new Insets(5, 5, 5, 5));
        Label watermarkWidthLabel = new Label(" 水印宽度: ");
        TextField addWatermarkOfWidth = new TextField();
        addWatermarkOfWidth.setPrefWidth(100);
        Label watermarkHeightLabel = new Label(" 水印高度: ");
        TextField addWatermarkOfHeight = new TextField();
        addWatermarkOfHeight.setPrefWidth(100);
        HBox addWatermarkBox3 = new HBox();
        addWatermarkBox3.setPadding(new Insets(5, 5, 5, 5));
        Label watermarkContentLabel = new Label(" 水印内容: ");
        TextField addWatermarkOfContent = new TextField();
        addWatermarkOfContent.setPrefWidth(150);
        addWatermarkBox.getChildren().addAll(addWatermark);
        addWatermarkBox1.getChildren().addAll(watermarkXAxisLabel, addWatermarkOfX);
        addWatermarkBox1.getChildren().addAll(watermarkYAxisLabel, addWatermarkOfY);
        addWatermarkBox2.getChildren().addAll(watermarkWidthLabel, addWatermarkOfWidth);
        addWatermarkBox2.getChildren().addAll(watermarkHeightLabel, addWatermarkOfHeight);
        addWatermarkBox3.getChildren().addAll(watermarkContentLabel, addWatermarkOfContent);

        //删除水印
        HBox delWatermarkBox = new HBox();
        delWatermarkBox.setPadding(new Insets(5, 5, 5, 5));
        CheckBox delWatermark = new CheckBox("删除水印");
        HBox delWatermarkBox1 = new HBox();
        delWatermarkBox1.setPadding(new Insets(5, 5, 5, 5));
        Label deleteWatermarkXAxisLabel = new Label(" 水印x轴: ");
        TextField delWatermarkOfX = new TextField();
        delWatermarkOfX.setPrefWidth(100);
        Label deleteWatermarkYAxisLabel = new Label(" 水印y轴: ");
        TextField delWatermarkOfY = new TextField();
        delWatermarkOfY.setPrefWidth(100);
        HBox delWatermarkBox2 = new HBox();
        delWatermarkBox2.setPadding(new Insets(5, 5, 5, 5));
        Label deleteWatermarkWidthLabel = new Label(" 水印宽度: ");
        TextField delWatermarkOfWidth = new TextField();
        delWatermarkOfWidth.setPrefWidth(100);
        Label deleteWatermarkHeightLabel = new Label(" 水印高度: ");
        TextField delWatermarkOfHeight = new TextField();
        delWatermarkOfHeight.setPrefWidth(100);
        delWatermarkBox.getChildren().addAll(delWatermark);
        delWatermarkBox1.getChildren().addAll(deleteWatermarkXAxisLabel, delWatermarkOfX);
        delWatermarkBox1.getChildren().addAll(deleteWatermarkYAxisLabel, delWatermarkOfY);
        delWatermarkBox2.getChildren().addAll(deleteWatermarkWidthLabel, delWatermarkOfWidth);
        delWatermarkBox2.getChildren().addAll(deleteWatermarkHeightLabel, delWatermarkOfHeight);


        //截取视频
        HBox cutVideoBox = new HBox();
        cutVideoBox.setPadding(new Insets(5, 5, 5, 5));
        CheckBox cutVideo = new CheckBox("去头去尾");
        HBox cutVideoBox1 = new HBox();
        cutVideoBox1.setPadding(new Insets(5, 5, 5, 5));
        Label startTimeLabel = new Label(" 开头删除");
        TextField startTime = new TextField();
        startTime.setPrefWidth(50);
        Label startTimeLabel1 = new Label("秒");
        Label endTimeLabel = new Label("  结尾删除");
        TextField endTime = new TextField();
        endTime.setPrefWidth(50);
        Label endTimeLabel1 = new Label("秒");
        cutVideoBox.getChildren().addAll(cutVideo);
        cutVideoBox1.getChildren().addAll(startTimeLabel, startTime, startTimeLabel1);
        cutVideoBox1.getChildren().addAll(endTimeLabel, endTime, endTimeLabel1);


        //设置视频封面
        HBox setCoverBox = new HBox();
        setCoverBox.setPadding(new Insets(5, 5, 5, 5));
        CheckBox setCover = new CheckBox("设置封面");
        HBox setCoverBox1 = new HBox();
        setCoverBox1.setPadding(new Insets(5, 5, 5, 5));
        Label coverPathLabel = new Label(" 封面路径: ");
        TextField coverPath = new TextField();
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
        CheckBox getCover = new CheckBox("截取图片");
        //HBox getCoverBox1 = new HBox();
        Label cutVideoTimeLabel = new Label(" 视频时间: ");
        TextField cutVideoTime = new TextField();
        cutVideoTime.setPrefWidth(100);
        getCoverBox.getChildren().addAll(getCover, cutVideoTimeLabel, cutVideoTime);
        //getCoverBox1.getChildren().addAll(cutVideoTimeLabel, cutVideoTime);


        //添加滤镜
        HBox addFilterBox = new HBox();
        addFilterBox.setPadding(new Insets(5, 5, 5, 5));
        CheckBox addFilter = new CheckBox(" 添加滤镜 ");
        List<String> itemList = Handler.getFilterList();
        TextField acvPath = new TextField();
        acvPath.setVisible(false);
        Button psChooserButton = new Button("请选择");
        psChooserButton.setOnAction(even -> {
            File file = MyChooser.getImageChooser().showOpenDialog(primaryStage);
            if (file != null) {
                LOG.info("选择图片: " + file.getAbsolutePath());
                acvPath.setText(file.getAbsolutePath());
            }
        });
        psChooserButton.setVisible(false);
        ChoiceBox<String> choiceBox = MyChoiceBox.getChoiceBox(itemList, psChooserButton);
        addFilterBox.getChildren().addAll(addFilter, choiceBox, psChooserButton);


        //视频加速
        HBox addFramerateBox = new HBox();
        addFramerateBox.setPadding(new Insets(5));
        CheckBox addFramerate = new CheckBox(" 视频加速 ");
        Label addFramerateLabel = new Label("加速倍数: ");
        TextField addFramerateTextField = new TextField();
        addFramerateTextField.setPrefWidth(100);
        addFramerateBox.getChildren().addAll(addFramerate, addFramerateLabel, addFramerateTextField);


        //视频减速
        HBox reduceFramerateBox = new HBox();
        reduceFramerateBox.setPadding(new Insets(5));
        CheckBox reduceFramerate = new CheckBox(" 视频减速 ");
        Label reduceFramerateLabel = new Label(" 减速倍数: ");
        TextField reduceFramerateTextField = new TextField();
        reduceFramerateTextField.setPrefWidth(100);
        reduceFramerateBox.getChildren().addAll(reduceFramerate, reduceFramerateLabel, reduceFramerateTextField);

        //合并视频
        HBox mergeVideoBox = new HBox();
        mergeVideoBox.setPadding(new Insets(5));
        CheckBox mergeVideo = new CheckBox(" 视频合并 ");
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
        mergeVideoBox.getChildren().addAll(mergeVideo);


        //视频背景虚化
        HBox blurBackgroundBox = new HBox();
        blurBackgroundBox.setPadding(new Insets(5));
        CheckBox blurBackground = new CheckBox(" 背景虚化 ");
        blurBackgroundBox.getChildren().addAll(blurBackground);

        //处理视频按钮
        HBox dealWithBox = new HBox();
        //dealWithBox.setPadding(new Insets(5, 5, 5, 5));
        Button dealWithSingle = new Button("处理当前视频");
        Button dealWithBath = new Button("处理列表视频");
        HBox.setMargin(dealWithSingle, new Insets(10));
        HBox.setMargin(dealWithBath, new Insets(10));

        //单个处理
        dealWithSingle.setOnAction(even -> {
            LOG.info("处理单个");
            //dealWithSingle.setDisable(true);
            //progressBarBox.setVisible(true);
            if(null == Handler.getListView("unProcessed")) {
                MyAlertBox.display("程序提示", "请导入视频!");
                return;
            }
            //当前播放器播放的视频地址
            String currentVideo = Handler.getListView("unProcessed").getCurrentVideoPath();
            String targetPath = "";
            if (null == currentVideo || "".equals(currentVideo) || "暂无数据".equals(currentVideo)) {
                MyAlertBox.display("程序提示", "未选择视频!");
                return;
            }

            boolean addWatermarkSelected = addWatermark.isSelected();
            boolean delWatermarkSelected = delWatermark.isSelected();
            boolean cutVideoSelected = cutVideo.isSelected();
            boolean setCoverSelected = setCover.isSelected();
            boolean addFilterSelected = addFilter.isSelected();
            boolean addFramerateSelected = addFramerate.isSelected();
            boolean reduceFramerateSelected = reduceFramerate.isSelected();
            boolean mergeVideoSelected = mergeVideo.isSelected();
            boolean blurBackgroundSelected = blurBackground.isSelected();
            boolean getCoverSelected = getCover.isSelected();

            //加水印
            if (addWatermarkSelected) {
                String x = addWatermarkOfX.getText();
                String y = addWatermarkOfY.getText();
                String text = addWatermarkOfContent.getText();
                LOG.info("操作步骤:加水印 操作对象: " + currentVideo);
                targetPath = Handler.getNewFilePath(currentVideo);
                videoExecutor.addWatermarkByFont(text, 30, "微软雅黑", x, y, currentVideo, targetPath);
                //File targetFile = new File(targetPath);
                currentVideo = targetPath;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //消除水印
            if (delWatermarkSelected) {
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
                targetPath = Handler.getNewFilePath(currentVideo);
                videoExecutor.removeWatermark(currentVideo, x, y, width, height, targetPath);
                //删除上一步产生的视频
                Handler.deleteFile(currentVideo);
                currentVideo = targetPath;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //剪切视频
            if (cutVideoSelected) {
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
                targetPath = Handler.getNewFilePath(currentVideo);
                videoExecutor.cutVideo(currentVideo, targetPath, start, end);
                //删除上一步产生的视频
                Handler.deleteFile(currentVideo);
                currentVideo = targetPath;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (setCoverSelected) {
                String imgPath = coverPath.getText();
                //TODO 宽和高暂时没有
                //String height = coverHeight.getText();
                //String width = coverWidth.getText();
                LOG.info("操作步骤:设置封面 操作对象: {}", currentVideo);
                targetPath = Handler.getNewFilePath(currentVideo);
                videoExecutor.setCover(currentVideo, imgPath, targetPath);
                //删除上一步产生的视频
                Handler.deleteFile(currentVideo);
                currentVideo = targetPath;
            }

            //截取图片
            if(getCoverSelected) {
                String time = cutVideoTime.getText();
                time = Handler.formatTime(Long.valueOf(time));
                LOG.info("操作步骤: 截取图片 操作对象: {}", currentVideo);
                targetPath = Handler.getNewFilePath("D:\\MaXinHai\\file\\1.png");
                videoExecutor.cutVideoImage(currentVideo, targetPath, time);
                try {
                    //通过cmd命令打开图片
                    Runtime.getRuntime().exec("cmd /c " + targetPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //添加滤镜效果
            if (addFilterSelected) {
                String selected = MyChoiceBox.getSelected();
                if (selected.equals("复古风")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.ancientStyleFilter(currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (selected.equals("镜像")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.mirror(currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (selected.equals("多路拼接")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.spliceVideo(currentVideo, currentVideo, currentVideo, currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (selected.equals("Ps滤镜")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    videoExecutor.revisionCurveByPs(currentVideo, acvPath.getText(), targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (selected.equals("锐化")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    System.out.println("操作步骤:设置锐化效果 操作对象: " + targetPath);
                    videoExecutor.sharpen(currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (selected.equals("黑白")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    System.out.println("操作步骤:设置黑白效果 操作对象: " + targetPath);
                    videoExecutor.blackWhite(currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (selected.equals("浮雕效果")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    System.out.println("操作步骤:设置浮雕效果 操作对象: " + targetPath);
                    videoExecutor.reliefEffect(currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (selected.equals("模糊处理")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    System.out.println("操作步骤:设置模糊处理 操作对象: " + targetPath);
                    videoExecutor.blur(currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (selected.equals("色彩变幻")) {
                    targetPath = Handler.getNewFilePath(currentVideo);
                    LOG.info("操作步骤:设置色彩变幻 操作对象: {}", targetPath);
                    videoExecutor.colorChange(currentVideo, targetPath);
                    //删除上一步产生的视频
                    Handler.deleteFile(currentVideo);
                    currentVideo = targetPath;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //提升视频播放速度
            if (addFramerateSelected) {
                targetPath = Handler.getNewFilePath(currentVideo);
                String videoFrameRate = addFramerateTextField.getText();
                String audioFrameRate = addFramerateTextField.getText();
                videoExecutor.addVideoAudioFramerate(currentVideo, targetPath, videoFrameRate, audioFrameRate);
                //删除上一步产生的视频
                Handler.deleteFile(currentVideo);
                currentVideo = targetPath;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //降低视频播放速度
            if (reduceFramerateSelected) {
                String videoFrameRate = reduceFramerateTextField.getText();
                videoExecutor.reduceFramerate(currentVideo, targetPath, videoFrameRate);
                //删除上一步产生的视频
                Handler.deleteFile(currentVideo);
                currentVideo = targetPath;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            //合并多个视频
//            if (mergeVideoSelected) {
//                targetPath = Handler.getNewFilePath(currentVideo);
//                List<String> filePathList = Handler.getListView("unProcessed").getFilePathList();
//                if(null == filePathList || filePathList.size() == 0) {
//                    MyAlertBox.display("合并视频提示", "请先导入要合并的视频!");
//                    return;
//                }
//                videoExecutor.mergeVideo(filePathList, targetPath);
//                //删除上一步产生的视频
//                Handler.deleteFile(currentVideo);
//                currentVideo = targetPath;
//            }
            //背景虚化
            if (blurBackgroundSelected) {
                targetPath = Handler.getNewFilePath(currentVideo);
                videoExecutor.blurBackground(currentVideo, targetPath);
                //删除上一步产生的视频
                Handler.deleteFile(currentVideo);
                currentVideo = targetPath;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //更新进度
            //MyProgressBar.setProgress(100);
            //关掉进度显示
            //progressBarBox.setVisible(false);
            //刷新左侧视频列表
            processedList.add(currentVideo);
            MyHome.setLeft(null, processedList);
            //选择视频地址并让播放组件播放视频
            //MyMediaPlayer.chooseFile(new File(targetPath));
        });

        //批量处理
        dealWithBath.setOnAction(even -> {
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

            boolean addWatermarkSelected = addWatermark.isSelected();
            boolean delWatermarkSelected = delWatermark.isSelected();
            boolean cutVideoSelected = cutVideo.isSelected();
            boolean setCoverSelected = setCover.isSelected();
            boolean addFilterSelected = addFilter.isSelected();
            boolean addFramerateSelected = addFramerate.isSelected();
            boolean reduceFramerateSelected = reduceFramerate.isSelected();
            boolean mergeVideoSelected = mergeVideo.isSelected();
            boolean blurBackgroundSelected = blurBackground.isSelected();

            Map<String, String> config = Handler.readProp();
            String targetPath = config.get("targetPath");
            if(null == targetPath || "".equals(targetPath)) {
                MyAlertBox.display("程序提示", "请设置生成文件路径!");
                return;
            }
            //copy文件到生成文件夹下
            List<String> targetPathList = Handler.batchCopyFile(filePathList, targetPath);
            //要删除的多余文件
            Set<String> deletePathSet = new HashSet<>(targetPathList);

            if (addWatermarkSelected) {
                ListIterator<String> iterator = targetPathList.listIterator();
                String x = addWatermarkOfX.getText();
                String y = addWatermarkOfY.getText();
                String text = addWatermarkOfContent.getText();
                try {
                    while (iterator.hasNext()) {
                        String path = iterator.next();
                        String target = Handler.getNewFilePath(path);
                        LOG.info("操作步骤:加水印 操作对象: {}", path);
                        videoExecutor.addWatermarkByFont(text, 30, "微软雅黑", x, y, path, target);
                        //videoExecutor.addWatermarkByFont(path, x, y, null, null, null, text, null, target);
                        iterator.set(target);
                        deletePathSet.add(path);
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    LOG.info("批量添加水印失败! {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if (delWatermarkSelected) {
                ListIterator<String> iterator = targetPathList.listIterator();
                String x = delWatermarkOfX.getText();
                String y = delWatermarkOfY.getText();
                String width = delWatermarkOfWidth.getText();
                String height = delWatermarkOfHeight.getText();
                BigDecimal xValue = new BigDecimal(x);
                BigDecimal yValue = new BigDecimal(y);
                BigDecimal widthValue = new BigDecimal(width);
                BigDecimal heightValue = new BigDecimal(height);
                try {
                    while (iterator.hasNext()) {
                        String path = iterator.next();

                        //先计算比例，才能去拿值
                        MyMediaPlayer.calculationRatio(new File(path));
                        x = xValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                        y = yValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                        width = widthValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();
                        height = heightValue.divide(Handler.getScale(), 0, BigDecimal.ROUND_HALF_UP).toString();

                        String target = Handler.getNewFilePath(path);
                        LOG.info("操作步骤:消除水印 操作对象: {}", path);
                        videoExecutor.removeWatermark(path, x, y, width, height, target);
                        Thread.sleep(500);
                        deletePathSet.add(path);
                        iterator.set(target);
                    }
                } catch (InterruptedException e) {
                    LOG.info("批量消除水印出错! {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if (cutVideoSelected) {
                ListIterator<String> iterator = targetPathList.listIterator();
                String start = startTime.getText();
                String end = endTime.getText();
                String startPoint = null;
                String endPoint = null;
                try {
                    while (iterator.hasNext()) {
                        String path = iterator.next();
                        MultimediaInfo videoInfo = videoExecutor.getVideoInfo(path);
                        long duration = videoInfo.getDuration(); //获取视频长度
                        BigDecimal durationValue = new BigDecimal(String.valueOf(duration));
                        BigDecimal endValue = new BigDecimal(end + "000");
                        startPoint = Handler.formatTime(Long.valueOf(start));
                        BigDecimal standardValue= new BigDecimal("1000");
                        //视频总长度减去要删除的秒数再除以1000在向上取整得到的就是结束秒数
                        endPoint = Handler.formatTime(durationValue.subtract(endValue).divide(standardValue).setScale(0, BigDecimal.ROUND_UP).longValue());
                        String target = Handler.getNewFilePath(path);
                        LOG.info("操作步骤:剪切视频 操作对象: {}", path);
                        videoExecutor.cutVideo(path, target, startPoint, endPoint);
                        Thread.sleep(500);
                        deletePathSet.add(path);
                        iterator.set(target);
                    }
                } catch (InterruptedException e) {
                    LOG.info("批量剪切视频出错! {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if (setCoverSelected) {
                ListIterator<String> iterator = targetPathList.listIterator();
                String imgPath = coverPath.getText();
                //TODO 宽和高暂时没有
                //String height = coverHeight.getText();
                //String width = coverWidth.getText();
                while (iterator.hasNext()) {
                    String path = iterator.next();
                    String target = Handler.getNewFilePath(path);
                    LOG.info("操作步骤:设置封面 操作对象: {}", path);
                    videoExecutor.setCover(path, imgPath, target);
                    iterator.set(target);
                }
            }
            if (addFilterSelected) {
                ListIterator<String> iterator = targetPathList.listIterator();
                String selected = MyChoiceBox.getSelected();
                if (selected.equals("镜像")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤:设置镜像效果 操作对象: {}", path);
                            videoExecutor.mirror(path, target);
                            Thread.sleep(500);
                            deletePathSet.add(path);
                            iterator.set(target);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量设置镜像效果出错! {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (selected.equals("复古风")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤:设置复古风效果 操作对象: {}", path);
                            videoExecutor.ancientStyleFilter(path, target);
                            Thread.sleep(500);
                            deletePathSet.add(path);
                            iterator.set(target);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量设置复古风效果出错! {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (selected.equals("多路拼接")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤:设置多路拼接效果 操作对象: {}", path);
                            videoExecutor.spliceVideo(path, path, path, path, target);
                            Thread.sleep(500);
                            deletePathSet.add(path);
                            iterator.set(target);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量设置多路拼接效果出错 {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (selected.equals("Ps滤镜")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤: 根据Ps预设文件设置效果 操作对象: {}", path);
                            videoExecutor.revisionCurveByPs(path, acvPath.getText(), target);
                            deletePathSet.add(path);
                            iterator.set(target);
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量根据Ps预设文件设置效果出错! {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (selected.equals("锐化")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤:设置锐化效果 操作对象: {}", path);
                            videoExecutor.sharpen(path, target);
                            iterator.set(target);
                            deletePathSet.add(path);
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量设置锐化效果出错! {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (selected.equals("黑白")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤:设置黑白效果 操作对象: {}", path);
                            videoExecutor.blackWhite(path, target);
                            iterator.set(target);
                            deletePathSet.add(path);
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量设置黑白效果出错 {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (selected.equals("浮雕效果")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤:设置浮雕效果 操作对象: {}", path);
                            videoExecutor.reliefEffect(path, target);
                            iterator.set(target);
                            deletePathSet.add(path);
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量设置浮雕效果出错! {}", e.getMessage());
                        e.printStackTrace();
                    }

                }
                if (selected.equals("模糊处理")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤:设置模糊处理 操作对象: {}", path);
                            videoExecutor.blur(path, target);
                            iterator.set(target);
                            deletePathSet.add(path);
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量设置模糊处理出错!  {}", e.getMessage());
                        e.printStackTrace();
                    }

                }
                if (selected.equals("色彩变幻")) {
                    try {
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            String target = Handler.getNewFilePath(path);
                            LOG.info("操作步骤:设置色彩变幻 操作对象: {}", path);
                            videoExecutor.colorChange(path, target);
                            iterator.set(target);
                            deletePathSet.add(path);
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        LOG.info("批量设置色彩变幻出错! {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            //增加视频帧率
            if (addFramerateSelected) {
                LOG.info("增加视频速率");
                String frameRate = addFramerateTextField.getText();
                ListIterator<String> iterator = targetPathList.listIterator();
                try {
                    while (iterator.hasNext()) {
                        String path = iterator.next();
                        String target = Handler.getNewFilePath(path);
                        LOG.info("操作步骤:增加视频速率 操作对象: {}", path);
                        videoExecutor.addFramerate(path, target, frameRate);
                        Thread.sleep(500);
                        deletePathSet.add(path);
                        iterator.set(target);
                    }
                } catch (InterruptedException e) {
                    LOG.info("批量设置镜像效果增加视频速率 {}", e.getMessage());
                    e.printStackTrace();
                }

            }

            //降低视频帧率
            if (reduceFramerateSelected) {
                String frameRate = reduceFramerateTextField.getText();
                ListIterator<String> iterator = targetPathList.listIterator();
                try {
                    while (iterator.hasNext()) {
                        String path = iterator.next();
                        String target = Handler.getNewFilePath(path);
                        LOG.info("操作步骤:降低视频帧率 操作对象: {}", path);
                        videoExecutor.reduceFramerate(path, target, frameRate);
                        iterator.set(target);
                        deletePathSet.add(path);
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    LOG.info("批量降低视频帧率! {}", e.getMessage());
                    e.printStackTrace();
                }

            }

            //合并多个视频
            if (mergeVideoSelected) {
                String newTargetPath = Handler.getNewFilePath("D:\\MaXinHai\\file\\1.mp4");
                videoExecutor.mergeVideo(filePathList, newTargetPath);
                try {
                    Runtime.getRuntime().exec("cmd /c " + newTargetPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //模糊视频背景
            if (blurBackgroundSelected) {
                //ExecutorService myExecutorService = MyExecutorService.getMyExecutorService();
                ListIterator<String> iterator = targetPathList.listIterator();
                try {
                    while (iterator.hasNext()) {
                        String path = iterator.next();
                        String target = Handler.getNewFilePath(path);
                        LOG.info("操作步骤:设置模糊视频背景 操作对象: {}", path);
                        videoExecutor.blurBackground(path, target);
                        Thread.sleep(500);
                        deletePathSet.add(path);
                        iterator.set(target);
                    }
                } catch (Exception e) {
                    LOG.info("批量设置模糊视频背景出错! {}", e.getMessage());
                    e.printStackTrace();
                }
            }

            MyHome.setLeft(null, targetPathList);
            File videoFile = new File(targetPathList.get(0));
            //播放第一个视频并让播放组件播放视频
            MyMediaPlayer.chooseFile(videoFile);
            LOG.info("开始删除多余文件...");
            deletePathSet.forEach(delPath -> {
                Handler.deleteFile(delPath);
            });
            LOG.info("删除多余文件完毕...");
        });
        dealWithBox.getChildren().addAll(dealWithSingle, dealWithBath);

        //progressBarBox.setVisible(false);

        vbox.getChildren().addAll(titleBox);
        vbox.getChildren().addAll(delWatermarkBox, delWatermarkBox1, delWatermarkBox2); //删除水印
        vbox.getChildren().addAll(cutVideoBox, cutVideoBox1); //剪切视频
        vbox.getChildren().addAll(setCoverBox, setCoverBox1); //设置视频封面
        vbox.getChildren().addAll(getCoverBox); //截图图片
        vbox.getChildren().addAll(addFilterBox); //添加滤镜效果
        vbox.getChildren().addAll(addFramerateBox, reduceFramerateBox); //视频加速 减速
        vbox.getChildren().addAll(mergeVideoBox); //合并多个视频
        vbox.getChildren().addAll(blurBackgroundBox); //视频背景虚化
        vbox.getChildren().addAll(addWatermarkBox, addWatermarkBox1, addWatermarkBox2, addWatermarkBox3); //添加水印
        vbox.getChildren().addAll(dealWithBox); //处理按钮
        //vbox.getChildren().addAll(progressBarBox); //任务进度条
        return vbox;
    }

}
