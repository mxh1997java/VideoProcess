package components;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 进度条组件
 */
public class MyProgress {

    //进度条
    private static ProgressIndicator pin =  null;

    private static VBox vBox = null;

    private static Stage window = new Stage();

    public static VBox getMyProgress() {
        //初始化
        pin = new ProgressIndicator(0);
        pin.setMinSize(100, 100);
        vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(pin);
        return vBox;
    }


    /**
     * 设置进度
     * @param value
     */
    public static void setProgress(int value) {
        pin.setProgress(value);
    }


    public static void display() {
        window.setTitle("任务进度显示");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(300);
        window.setOnCloseRequest(event -> {
            MyAlertBox.display("进度组件提示", "进度弹窗无法关闭!");
        });
        Scene scene = new Scene(getMyProgress());
        window.setScene(scene);
        window.showAndWait();
    }


    /**
     * 关闭进度条弹窗
     */
    public static void close() {
        setProgress(100);
        window.close();
    }


}
