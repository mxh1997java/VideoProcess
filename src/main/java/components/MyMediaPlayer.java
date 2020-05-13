package components;

import java.io.File;

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

/**
 * @author xinhai.ma
 * @description 视频播放器组件
 * @date 2020/5/9 9:03
 */
public class MyMediaPlayer {

    private static Double endTime = new Double(0);
    private static Double currentTime = new Double(0);
    //private static File file = new File("");
    private static Media media = new Media("https://gss3.baidu.com/6LZ0ej3k1Qd3ote6lo7D0j9wehsv/tieba-smallvideo-transcode-cae/7582624_bd76685e95e44141dc814fb8e96c4366_0_cae.mp4");
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
        mView.fitWidthProperty().bind(pane.widthProperty());
        mView.fitHeightProperty().bind(pane.heightProperty().subtract(30));

        //mView.fitWidthProperty().bind(Bindings.selectDouble(mView.sceneProperty(), "width"));;
        //mView.fitHeightProperty().bind(Bindings.selectDouble(mView.sceneProperty(), "height"));;

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
        paneCtl.setMaxSize(600, 600);
        paneCtl.setAlignment(Pos.CENTER);
        paneCtl.getChildren().addAll(/*btnOpen,*/ lbCurrentTime, slTime, btnReplay, btnPlay, new Label("音量"), slVolume);
        pane.setCenter(mView);
        pane.setBottom(paneCtl);
        Text title = new Text("预览区域");
        title.setFont(Font.font(java.awt.Font.SERIF, 25));
        pane.setTop(title);
        return pane;
    }

    private static String Seconds2Str(Double seconds) {
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
            lbCurrentTime.setText(Seconds2Str(currentTime) + "/" + Seconds2Str(endTime));
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

}
