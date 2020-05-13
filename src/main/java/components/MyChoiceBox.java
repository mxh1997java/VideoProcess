package components;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 下拉框组件
 * @author xinhai.ma
 * @description
 * @date 2020/5/9 16:24
 */
public class MyChoiceBox {

    private static final Logger LOG = LoggerFactory.getLogger(MyChoiceBox.class);

    /**
     * 选择的选项
     */
    private static String selected;

    /**
     * 传入下拉框选项集合返回下拉框组件
     * @param list
     * @return
     */
    public static ChoiceBox<String> getChoiceBox(List<String> list) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(list);
        // 集合转数组
        String[] strs = new String[list.size()];
        list.toArray(strs);
        final ChoiceBox<String> choiceBox = new ChoiceBox<String>(observableList);
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
                LOG.info("选择: " + strs[new_value.intValue()]);
                selected = strs[new_value.intValue()];
            }
        });
        return choiceBox;
    }


    /**
     * 传入下拉框选项集合返回下拉框组件
     * @param list   子项集合
     * @param button 按钮(根据被选子项的变化控制按钮的显示)
     * @return
     */
    public static ChoiceBox<String> getChoiceBox(List<String> list, Button button) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(list);
        // 集合转数组
        String[] strs = new String[list.size()];
        list.toArray(strs);
        final ChoiceBox<String> choiceBox = new ChoiceBox<String>(observableList);
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
                LOG.info("选择: " + strs[new_value.intValue()]);
                selected = strs[new_value.intValue()];
                button.setVisible(false);
                if(selected.equals("Ps滤镜")) {
                    button.setVisible(true);
                }
            }
        });
        return choiceBox;
    }

    /**
     * 功能描述 获取当前选中的子项
     * @author xinhai.ma
     * @date 2020/5/9 16:26
     * @return java.lang.String
     */
    public static String getSelected() {
        return selected;
    }

}
