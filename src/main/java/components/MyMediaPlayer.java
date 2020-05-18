package components;

import java.io.File;
import java.math.BigDecimal;
import executor.VideoExecutor;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Handler;
import ws.schild.jave.MultimediaInfo;

/**
 * @author xinhai.ma
 * @description 视频播放器组件
 * @date 2020/5/9 9:03
 */
public class MyMediaPlayer {

    private static final Logger LOG = LoggerFactory.getLogger(MyMediaPlayer.class);

    private static final VideoExecutor videoExecutor = new VideoExecutor();

    private static Double endTime = new Double(0);
    private static Double currentTime = new Double(0);
    private static String source = "https://gss3.baidu.com/6LZ0ej3k1Qd3ote6lo7D0j9wehsv/tieba-smallvideo-transcode-cae/7582624_bd76685e95e44141dc814fb8e96c4366_0_cae.mp4";
    private static Media media = new Media(source);
    private static MediaPlayer mplayer = new MediaPlayer(media);
    private static MediaView mView;
    private static Button btnPlay;
    private static Slider slTime, slVolume;
    private static Label lbCurrentTime;

    public static BorderPane getMediaPlayer() {
        //mplayer = new MediaPlayer(media);
        /* MediaView */ mView = new MediaView(mplayer);
        /* Label */
        lbCurrentTime = new Label();
        /* Slider */
        slTime = new Slider(); // 时间轴
        slTime.setPrefWidth(200);
        BorderPane pane = new BorderPane();
        pane.setPrefWidth(600);
        pane.setPrefHeight(600);

//        mView.fitWidthProperty().bind(pane.widthProperty());
//        mView.fitHeightProperty().bind(pane.heightProperty());
        mView.setFitWidth(600);
        mView.setFitHeight(400);

        //计算播放画面大小
//        BorderPane pane1 = pane1 = calculationRatio(new File(source));
//        mView.fitWidthProperty().bind(pane1.widthProperty());
//        mView.fitHeightProperty().bind(pane1.heightProperty());
//        LOG.info("mVie width: {}, height: {}", mView.getFitWidth(), mView.getFitHeight());

        /* Button */
        btnPlay = new Button("播放");
        btnPlay.setOnAction(e -> {
            if (btnPlay.getText().equals("播放")) {
                btnPlay.setText("暂停");
                mplayer.play();
            } else {
                btnPlay.setText("播放");
                mplayer.pause();
            }
        });
        mplayer.setOnEndOfMedia(() -> { // 为初始存在的奇葩
            mplayer.stop();
            btnPlay.setText("播放");
        });
        Button btnReplay = new Button("停止");
        btnReplay.setOnAction(e -> {
            mplayer.stop();
            btnPlay.setText("播放");
        });

        /* Slider */
        slVolume = new Slider(); // 音量
        slVolume.setPrefWidth(150);
        slVolume.setValue(50);
        slVolume.setShowTickLabels(true);
        slVolume.setShowTickMarks(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Media...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP4 Video", "*.mp4"),
                new FileChooser.ExtensionFilter("MP3 Music", "*.mp3"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

//        Button btnOpen = new Button("打开"); // 实际初始化在这里完成
//        btnOpen.setOnAction(e -> {
//            file = fileChooser.showOpenDialog(new Stage()); // 在一个新Stage里放FileChooser
//            if (file != null) {
//                mplayer.stop();
//                btnPlay.setText("暂停");
//                media = new Media(file.toURI().toString());
//                mplayer = new MediaPlayer(media);
//                mView.setMediaPlayer(mplayer);
//                mplayer.setOnReady(() -> {
//                    endTime = mplayer.getStopTime().toSeconds();
//                });
//                // 媒体准备好时获得信息
//                mplayer.setOnEndOfMedia(() -> {
//                    mplayer.stop();
//                    mplayer.seek(Duration.ZERO);
//                    btnPlay.setText("播放");
//                });
//                mplayer.currentTimeProperty().addListener(ov -> {
//                    currentTime = mplayer.getCurrentTime().toSeconds();
//                    lbCurrentTime.setText(Seconds2Str(currentTime) + "/" + Seconds2Str(endTime));
//                    slTime.setValue(currentTime / endTime * 100);
//                });
//                slTime.valueProperty().addListener(ov -> {
//                    if (slTime.isValueChanging()) {
//                        mplayer.seek(mplayer.getTotalDuration().multiply(slTime.getValue() / 100));
//                    }
//                });
//                mplayer.volumeProperty().bind(slVolume.valueProperty().divide(100)); // 音量调节
//                mplayer.play();
//            }
//        });

        HBox paneCtl = new HBox(15);
//		paneCtl.setPadding(new Insets(30));
        paneCtl.setMaxSize(600, 300);
        paneCtl.setAlignment(Pos.CENTER);
        paneCtl.getChildren().addAll(/*btnOpen,*/ lbCurrentTime, slTime, btnReplay, btnPlay, new Label("音量"), slVolume);
        pane.setCenter(mView);
        pane.setBottom(paneCtl);
        Text title = new Text("预览区域");
        title.setFont(Font.font(java.awt.Font.SERIF, 25));
        pane.setTop(title);
        return pane;
    }

    private static String SecondsStr(Double seconds) {
        Integer count = seconds.intValue();
        Integer Hours = count / 3600;
        count = count % 3600;
        Integer Minutes = count / 60;
        count = count % 60;
        String str = Hours.toString() + ":" + Minutes.toString() + ":" + count.toString();
        return str;
    }

    /**
     * 传入视频文件播放视频
     *
     * @param file
     */
    public static void chooseFile(File file) {
        // 判断文件是否存在
        if (null == file || !file.exists()) {
            MyAlertBox.display("播放组件提示", "播放文件不存在!");
            return;
        }

        //计算播放画面大小
        BorderPane pane = calculationRatio(file);
//        mView.fitWidthProperty().bind(pane.widthProperty());
//        mView.fitHeightProperty().bind(pane.heightProperty());
        mView.setFitWidth(pane.getPrefWidth());
        mView.setFitHeight(pane.getPrefHeight());
        LOG.info("mVie width: {}, height: {}", mView.getFitWidth(), mView.getFitHeight());

        mplayer.stop();
        btnPlay.setText("播放");
        try {
            media = new Media(file.toURI().toString());
        } catch (MediaException e) {
            MyAlertBox.display("播放组件提示", "播放组件不支持该文件格式或文件已损坏!");
            return;
        }
        mplayer = new MediaPlayer(media);
        mView.setMediaPlayer(mplayer);
        mplayer.setOnReady(() -> {
            endTime = mplayer.getStopTime().toSeconds();
        });
        // 媒体准备好时获得信息
        mplayer.setOnEndOfMedia(() -> {
            mplayer.stop();
            mplayer.seek(Duration.ZERO);
            btnPlay.setText("播放");
        });
        mplayer.currentTimeProperty().addListener(ov -> {
            currentTime = mplayer.getCurrentTime().toSeconds();
            lbCurrentTime.setText(SecondsStr(currentTime) + "/" + SecondsStr(endTime));
            slTime.setValue(currentTime / endTime * 100);
        });
        slTime.valueProperty().addListener(ov -> {
            if (slTime.isValueChanging()) {
                mplayer.seek(mplayer.getTotalDuration().multiply(slTime.getValue() / 100));
            }
        });
        mplayer.volumeProperty().bind(slVolume.valueProperty().divide(100)); // 音量调节
        mplayer.play();
        LOG.info("mVie width: {}, height: {}", mView.getFitWidth(), mView.getFitHeight());
    }


    /**
     * 传入视频文件获取宽高，计算播放画面尺寸
     * @param file
     * @return
     */
    public static BorderPane calculationRatio(File file) {
        if(null == file || !file.exists()) {
            new RuntimeException("文件不存在!");
        }
        LOG.info("文件路径: {}", file.getAbsolutePath());
        //获取视频信息计算播放画面大小
        MultimediaInfo videoInfo = videoExecutor.getVideoInfo(file.getAbsolutePath());
        if(null == videoInfo) {
            new RuntimeException(file.getAbsolutePath() + "获取文件信息出错!");
        }
        double width = videoInfo.getVideo().getSize().getWidth();
        double height = videoInfo.getVideo().getSize().getHeight();
        BigDecimal standardValue = new BigDecimal("0.5");
        BigDecimal standardValue1 = new BigDecimal("0.75");
        BigDecimal standardValue2 = new BigDecimal("1");
        BigDecimal widthValue = new BigDecimal(String.valueOf(width));
        BigDecimal heightValue = new BigDecimal(String.valueOf(height));
        if(width > 1000) {
            widthValue = widthValue.multiply(standardValue);
            heightValue = heightValue.multiply(standardValue);
            Handler.setScale(standardValue);
        } else if(width > 800) {
            widthValue = widthValue.multiply(standardValue1);
            heightValue = heightValue.multiply(standardValue1);
            Handler.setScale(standardValue1);
        } else if(height > 600) {
            //针对手机做出的优化
            heightValue = heightValue.multiply(standardValue);
            Handler.setScale(standardValue);
        }
        if(width < 600 && height < 600) {
            Handler.setScale(standardValue2);
        }
        BorderPane pane = new BorderPane();
        pane.setPrefSize(widthValue.doubleValue(), heightValue.doubleValue());
        pane.setMaxSize(widthValue.doubleValue(), heightValue.doubleValue());
        return pane;
    }

}
