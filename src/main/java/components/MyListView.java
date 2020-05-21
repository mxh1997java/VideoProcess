package components;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Handler;

/**
 * @author xinhai.ma
 * @description  视频播放列表组件
 * @date 2020/5/9 9:01
 */
public class MyListView {

    private static final Logger LOG = LoggerFactory.getLogger(MyListView.class);

    /**
     * 视图名称
     */
    private String viewName;

    /**
     * 列表视图
     */
    private ListView<String> listView = new ListView<>();

    /**
     * 显示选中项label
     */
    private final Label label = new Label();

    /**
     * 显示列表名称
     */
    private Label nameLabel = new Label();

    /**
     * 所有视频地址（不带省略号）
     */
    private List<String> filePathList = new ArrayList<>();

    /**
     * 所有简洁路径地址
     */
    private List<String> simplePathList = new ArrayList<>();

    /**
     * 当前播放的视频地址
     */
    private String currentVideoPath;

    public List<String> getFilePathList() {
        return filePathList;
    }

    /**
     * 获得当前正在播放的视频文件地址
     * @return
     */
    public String getCurrentVideoPath() {
        return currentVideoPath;
    }

    /**
     * 设置列表视图大小
     * @param width
     * @param height
     */
    public void setListVideoSize(double width, double height) {
        listView.setPrefSize(width, height);
    }

    /**
     * 设置试图名称
     * @param viewName
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
        nameLabel.setText(this.viewName);
        nameLabel.setFont(new Font("仿宋",20));
    }

    /**
     * 传入数据集合获得列表视图
     */
    public VBox getListView() {
        VBox box = new VBox();
        VBox.setMargin(box, new Insets(15,0,0,0));
        box.getChildren().addAll(nameLabel, listView, label);
        VBox.setVgrow(listView, Priority.ALWAYS);
        label.setLayoutX(10);
        label.setLayoutY(115);
        label.setMaxWidth(200);
        label.setWrapText(true);
        label.setFont(Font.font("Verdana", 10));
        listView.setPrefWidth(200);
        listView.setMaxWidth(200);
        listView.setPrefHeight(300);
        ObservableList<String> data = FXCollections.observableArrayList(simplePathList);
        listView.setItems(data);
        listView.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    LOG.info("您点了第{}项，视频名称是{}", listView.getSelectionModel().getSelectedIndex(), listView.getSelectionModel().getSelectedItem());
                    currentVideoPath = filePathList.get(listView.getSelectionModel().getSelectedIndex());
                    label.setText(currentVideoPath);

                    //选择视频地址并让播放组件播放视频
                    LOG.info("播放: " + currentVideoPath);
                    MyMediaPlayer.chooseFile(new File(currentVideoPath));
                });
        return box;
    }

    public MyListView(List<String> dataList) {
        if(null == dataList || 0 == dataList.size()) {
            LOG.info("初始化filePathList,dataList为空，填入暂无数据");
            dataList = Arrays.asList("暂无数据");
            simplePathList.add("暂无数据");
            filePathList.addAll(dataList);
        } else {
            LOG.info("初始化filePathList,dataList不为空，填入dataList");
            filePathList.addAll(dataList);
            //使文件列表展示更加简洁
            LOG.info("初始化simplePathList");
            simplePathList = Handler.getSimplePathList(dataList);
        }
    }

}
