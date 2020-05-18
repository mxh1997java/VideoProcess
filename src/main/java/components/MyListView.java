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

//    /**
//     * 所有视频地址列表（不带省略号）
//     */
//    private static List<String> fileList = new ArrayList<>();
//
//    /**
//     * 当前播放的视频地址
//     */
//    private static String currentVideo = null;
//
//    /**
//     * 获取当前播放的视频地址
//     * @return
//     */
//    public static String getCurrentVideo() {
//        return currentVideo;
//    }
//
//    /**
//     * 传入文件列表获取文件列表视图
//     * @param snackList
//     * @return
//     */
//    public static VBox getVideoList(List<String> snackList) {
//        VBox vbox = new VBox(); // 创建一个垂直箱子
//        if(null == snackList || 0 == snackList.size()) {
//            snackList = Arrays.asList("暂无数据");
//            fileList.addAll(snackList);
//        } else {
//            addAll(fileList, snackList);
//            //使文件列表展示更加简洁
//            snackList = Handler.getSimplePathList(snackList);
//        }
//        // 把清单对象转换为JavaFX控件能够识别的数据对象
//        ObservableList<String> obList = FXCollections.observableArrayList(snackList);
//        ListView<String> listView = new ListView<String>(obList); // 依据指定数据创建列表视图
//        // listView.setItems(obList); // 设置列表视图的数据来源
//        listView.setPrefSize(120, 550); // 设置列表视图的推荐宽高
//        listView.setMaxWidth(150);
//        Label label = new Label("这里查看视频结果"); // 创建一个标签
//        label.setMaxWidth(150);
//        label.setWrapText(true); // 设置标签文本是否支持自动换行
//        vbox.getChildren().addAll(listView, label); // 把列表和标签一起加到垂直箱子上
//        // 设置列表视图的选择监听器
//        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> arg0, String old_str, String new_str) {
//                // getSelectedIndex方法可获得选中项的序号，getSelectedItem方法可获得选中项的对象
//                String desc = String.format("您点了第%d项，视频名称是%s", listView.getSelectionModel().getSelectedIndex(),
//                        listView.getSelectionModel().getSelectedItem());
//                label.setText(desc); // 在标签上显示当前选中的文本项
//
//                //选择视频地址并让播放组件播放视频
//                String path = fileList.get(listView.getSelectionModel().getSelectedIndex());
//                LOG.info("播放: " + path);
//                MyMediaPlayer.chooseFile(new File(path));
//
//                //将当前正在播放的视频地址赋值给currentVideo
//                currentVideo = fileList.get(listView.getSelectionModel().getSelectedIndex());
//            }
//        });
//        return vbox;
//    }
//
//    /**
//     * 获取播放列表所有视频地址
//     * @return
//     */
//    public static List<String> getFilePathList() {
//        if(fileList.contains("暂无数据")) {
//            fileList.remove("暂无数据");
//        }
//        return fileList;
//    }
//
//    public static List<String> addAll(List<String> list, List<String> itemList) {
//        list.addAll(itemList);
//        if(list.contains("暂无数据")) {
//            list.remove("暂无数据");
//        }
//        return list;
//    }

}
