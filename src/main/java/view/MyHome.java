package view;

import components.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author xinhai.ma
 * @description
 * @date 2020/5/9 9:18
 */
public class MyHome {

    private static BorderPane pane = null;


    /**
     * 主界面
     *
     * @param primaryStage
     * @return
     */
    public static Scene getHome(Stage primaryStage) {
        // 布局: 上、下、左、右、中五部分
        /*BorderPane*/ pane = new BorderPane();
        pane.setTop(MyMenuBar.getMenuBar(primaryStage));
        pane.setLeft(MyVideoList.getVideoList(null));
        pane.setCenter(MyMediaPlayer.getMediaPlayer());
        pane.setRight(MyFunction.getFunction(primaryStage));
        pane.setBottom(MyBottom.getBottom());
        // 将pane加入到scene中
        Scene homeScene = new Scene(pane, 1366, 708);
        return homeScene;
    }

    public static void setVideoList(VBox videoList) {
        pane.setLeft(videoList);
    }

}
