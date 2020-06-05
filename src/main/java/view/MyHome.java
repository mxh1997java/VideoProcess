package view;

import components.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Handler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xinhai.ma
 * @description
 * @date 2020/5/9 9:18
 */
public class MyHome {

    private static final Logger LOG = LoggerFactory.getLogger(MyHome.class);

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
        List<String> unProcessedList = Handler.getList("unProcessed");
        List<String> processedList = Handler.getList("processed");
        MyListView videoView = new MyListView(unProcessedList);
        videoView.setViewName("未处理视频");
        MyListView processedVideoView = new MyListView(processedList);
        processedVideoView.setViewName("已处理视频");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(videoView.getListView(), processedVideoView.getListView());
        pane.setLeft(vBox);
        pane.setCenter(MyMediaPlayer.getMediaPlayer());
        pane.setRight(MyFunction.getFunction(primaryStage));
        pane.setBottom(MyBottom.getBottom());
        // 将pane加入到scene中
        Map<String, Integer> screenSize = Handler.getScreenSize();
        Scene homeScene = new Scene(pane, screenSize.get("width"), screenSize.get("height")-55);
        return homeScene;
    }


    /**
     * 设置左边列表视图
     * @param unProcessedList
     * @param processedList
     */
    public static void setLeft(List<String> unProcessedList, List<String> processedList) {
        VBox vBox = new VBox();
        //未处理的视频列表
        List<String> unProcessed = Handler.getList("unProcessed");
        MyListView unProcessedView = null;
        if(null != unProcessedList && unProcessedList.size() > 0) {
            if(null != unProcessed && unProcessed.size() > 0) {
                unProcessedList.addAll(unProcessed);
                //去重
                unProcessedList = unProcessedList.stream().distinct().collect(Collectors.toList());
            }
            unProcessedView = new MyListView(unProcessedList);
            LOG.info("创建unProcessedView对象");
            Handler.put("unProcessed", unProcessedList);
        } else {
            unProcessedView = new MyListView(unProcessed);
            Handler.put("unProcessed", unProcessed);
        }
        unProcessedView.setViewName("未处理视频");
        VBox unProcessedViewListView = unProcessedView.getListView();
        Handler.put("unProcessed", unProcessedView);
        vBox.getChildren().addAll(unProcessedViewListView);

        //处理后的视频列表
        List<String> processed = Handler.getList("processed");
        MyListView processedView = null;
        if(null != processedList && processedList.size() > 0) {
            if(null != processed && processed.size() > 0) {
                processedList.addAll(processed);
                //去重
                processedList = processedList.stream().distinct().collect(Collectors.toList());
            }
            processedView = new MyListView(processedList);
            LOG.info("创建processedView对象");
            Handler.put("processed", processedList);
        } else {
            processedView = new MyListView(processed);
            Handler.put("processed", processed);
        }
        processedView.setViewName("已处理视频");
        VBox processedlistView = processedView.getListView();
        Handler.put("processed", processedView);
        vBox.getChildren().addAll(processedlistView);
        pane.setLeft(vBox);
    }

}
