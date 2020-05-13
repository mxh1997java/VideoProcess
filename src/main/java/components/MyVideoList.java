package components;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Handler;

/**
 * @author xinhai.ma
 * @description  视频播放列表组件
 * @date 2020/5/9 9:01
 */
public class MyVideoList {

    private static final Logger LOG = LoggerFactory.getLogger(MyVideoList.class);

    private static List<String> fileList = new ArrayList<>();

    private static String currentVideo = null;

    /**
     * 传入文件列表获取文件列表视图
     * @param snackList
     * @return
     */
    public static VBox getVideoList(List<String> snackList) {
        VBox vbox = new VBox(); // 创建一个垂直箱子
        if(null == snackList || 0 == snackList.size()) {
            snackList = Arrays.asList("暂无数据");
            fileList.addAll(snackList);
        } else {
            addAll(fileList, snackList);
            //使文件列表展示更加简洁
            snackList = Handler.getSimplePathList(snackList);
        }
        // 把清单对象转换为JavaFX控件能够识别的数据对象
        ObservableList<String> obList = FXCollections.observableArrayList(snackList);
        ListView<String> listView = new ListView<String>(obList); // 依据指定数据创建列表视图
        // listView.setItems(obList); // 设置列表视图的数据来源
        listView.setPrefSize(120, 550); // 设置列表视图的推荐宽高
        listView.setMaxWidth(150);
        Label label = new Label("这里查看视频结果"); // 创建一个标签
        label.setMaxWidth(150);
        label.setWrapText(true); // 设置标签文本是否支持自动换行
        vbox.getChildren().addAll(listView, label); // 把列表和标签一起加到垂直箱子上
        // 设置列表视图的选择监听器
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String old_str, String new_str) {
                // getSelectedIndex方法可获得选中项的序号，getSelectedItem方法可获得选中项的对象
                String desc = String.format("您点了第%d项，视频名称是%s", listView.getSelectionModel().getSelectedIndex(),
                        listView.getSelectionModel().getSelectedItem());
                label.setText(desc); // 在标签上显示当前选中的文本项

                //选择视频地址并让播放组件播放视频
                String path = fileList.get(listView.getSelectionModel().getSelectedIndex());
                LOG.info("播放: " + path);
                MyMediaPlayer.chooseFile(new File(path));

                //将当前正在播放的视频地址赋值给currentVideo
                currentVideo = fileList.get(listView.getSelectionModel().getSelectedIndex());
            }
        });
        return vbox;
    }

    /**
     * 获取播放列表所有视频地址
     * @return
     */
    public static List<String> getFilePathList() {
        if(fileList.contains("暂无数据")) {
            fileList.remove("暂无数据");
        }
        return fileList;
    }

    public static List<String> addAll(List<String> list, List<String> itemList) {
        list.addAll(itemList);
        if(list.contains("暂无数据")) {
            list.remove("暂无数据");
        }
        return list;
    }

    /**
     * 获取当前播放的视频地址
     * @return
     */
    public static String getCurrentVideo() {
        return currentVideo;
    }

}
