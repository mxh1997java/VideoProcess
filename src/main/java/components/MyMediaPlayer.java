package components;

import java.io.File;
import java.math.BigDecimal;
import executor.VideoExecutor;
import javafx.geometry.Insets;
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
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.EmptyUtils;
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

    private static Double endTime = new Double(36);
    private static Double currentTime = new Double(0);
    private static String source = "C:\\VideoProcess\\show.mp4";
    private static Media media = null;
    private static MediaPlayer mplayer = null;
    private static MediaView mView;
    private static Button btnPlay;
    private static Slider slTime, slVolume;
    private static Label lbCurrentTime = new Label();

    public static BorderPane getMediaPlayer() {
        if(EmptyUtils.isNotEmpty(Handler.getCurrentVideoPath())) {
            source = Handler.getCurrentVideoPath();
        }
        media = new Media(new File(source).toURI().toString());
        mplayer = new MediaPlayer(media);
        mView = new MediaView(mplayer);
        slTime = new Slider(); // 时间轴
        slTime.setPrefWidth(200);
        BorderPane pane = new BorderPane();
        //pane.setStyle("-fx-background-color: gold");
        pane.setPrefWidth(600);
        pane.setPrefHeight(500);
        mView.setFitWidth(600);
        mView.setFitHeight(360);

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
            mplayer.seek(Duration.ZERO);
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
        lbCurrentTime.setMinWidth(60);
        lbCurrentTime.setText("0:0:0/0:0:36");

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

        HBox paneCtl = new HBox(15);
        //paneCtl.setStyle("-fx-background-color: gold");
        paneCtl.setPadding(new Insets(0,0,0, 60));
        paneCtl.setMaxSize(1000, 300);
        paneCtl.setAlignment(Pos.CENTER);
        paneCtl.getChildren().addAll(lbCurrentTime, slTime, btnReplay, btnPlay, new Label("音量"), slVolume);
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
        } else if(width > 600){
            Handler.setScale(standardValue2);
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

    public static MediaView getmView() {
        if(mView == null) {
            new RuntimeException("播放试图未初始化!");
        }
        return mView;
    }

}
