package components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Handler;
import java.util.List;

/**
 * @author xinhai.ma
 * @description  弹窗提示组件
 * @date 2020/5/9 9:05
 */
public class MyAlertBox {

    private static final Logger LOG = LoggerFactory.getLogger(MyAlertBox.class);

    public static void display(String title, String message) {
        LOG.info("弹窗提示: {}", message);
        Stage window = new Stage();
        window.setTitle(title);
        // modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);

        Button button = new Button("知道了");
        button.setOnAction(e -> window.close());

        Label label = new Label(message);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        // 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }


    /**
     * 方法区域参数填写校验弹窗
     * @param title  提示标题
     * @param data   提示信息集合
     */
    public static void display(String title, List<String> data) {
        LOG.info("弹窗提示: {}", data);
        Stage window = new Stage();
        window.setTitle(title);
        // modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);
        Button keep = new Button("继续");
        keep.setOnAction(e -> {
            window.close();
            Handler.setIsStop(true);
        });
        Button stop = new Button("停止");
        stop.setOnAction(e -> {
            window.close();
            Handler.setIsStop(false);
        });
        VBox layout = new VBox(10);
        for(String message : data) {
            Label label = new Label(message);
            layout.getChildren().addAll(label);
        }
        HBox buttonArea = new HBox();
        buttonArea.setPadding(new Insets(10, 100, 10, 100));
        buttonArea.setMinSize(100, 30);
        buttonArea.getChildren().addAll(keep, stop);
        layout.getChildren().addAll(buttonArea);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        // 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

}
