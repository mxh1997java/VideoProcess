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
    private String selected;

    /**
     * 下拉框对象
     */
    private ChoiceBox<String> choiceBox;

    private String[] array = new String[0];

    public void setArray(String[] array) {
        this.array = array;
    }

    /**
     * 设置默认选项
     * @param itemIndex
     */
    public void selectItem(int itemIndex) {
        choiceBox.getSelectionModel().select(itemIndex);
    }

    /**
     * 设置默认选项
     * @param item
     */
    public void selectItem(String item) {
        choiceBox.getSelectionModel().select(item);
    }

    /**
     * 传入下拉框选项集合返回下拉框组件
     * @param list
     * @return
     */
    public ChoiceBox<String> getChoiceBox(List<String> list) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(list);
        // 集合转数组
        array = new String[list.size()];
        list.toArray(array);
        choiceBox = new ChoiceBox<String>(observableList);
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
                if(new_value.intValue() != -1) {
                    LOG.info("数组长度: {}", array.length);
                    LOG.info("选择: {}", new_value.intValue());
                    selected = array[new_value.intValue()];
                }
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
    public ChoiceBox<String> getChoiceBox(List<String> list, Button button) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(list);
        // 集合转数组
        array = new String[list.size()];
        list.toArray(array);
        choiceBox = new ChoiceBox<String>(observableList);
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
                LOG.info("选择: {}", array[new_value.intValue()]);
                selected = array[new_value.intValue()];
                button.setVisible(false);
                if("Ps滤镜".equals(selected)) {
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
    public String getSelected() {
        return selected;
    }

}
