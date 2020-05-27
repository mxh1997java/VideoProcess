package components;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Handler;

/**
 * 原来截图代码，改造为选取水印区域代码
 */
public class CapterScreen {

    Stage stage; // 切图时候的辅助舞台
    double start_x; // 切图区域的起始位置x
    double start_y; // 切图区域的起始位置y
    double w; // 切图区域宽
    double h; // 切图区域高
    HBox hBox; // 切图区域

    /**
     * 展示出截图画面
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void show(int x, int y, int width, int height) {
        // 将主舞台缩放到任务栏
        //primaryStage.setIconified(true);
        // 创建辅助舞台，并设置场景与布局
        stage = new Stage();
        // 锚点布局采用半透明
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #85858522");
        // 场景设置白色全透明
        Scene scene = new Scene(anchorPane);
        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.setScene(scene);
        // 清楚全屏中间提示文字
        stage.setFullScreenExitHint("");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setX(x);
        stage.setY(y);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.show();

        // 切图窗口绑定鼠标按下事件
        anchorPane.setOnMousePressed(event -> {
            // 清除锚点布局中所有子元素
            anchorPane.getChildren().clear();
            // 创建切图区域
            hBox = new HBox();
            // 设置背景保证能看到切图区域桌面
            hBox.setBackground(null);
            // 设置边框
            hBox.setBorder(new Border(
                    new BorderStroke(Paint.valueOf("#c03700"), BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
            anchorPane.getChildren().add(hBox);
            // 记录并设置起始位置
            start_x = event.getSceneX();
            start_y = event.getSceneY();
            AnchorPane.setLeftAnchor(hBox, start_x);
            AnchorPane.setTopAnchor(hBox, start_y);
        });
        // 绑定鼠标按下拖拽的事件
        anchorPane.setOnMouseDragged(event -> {
            // 计算宽高并且完成切图区域的动态效果
            w = Math.abs(event.getSceneX() - start_x);
            h = Math.abs(event.getSceneY() - start_y);
            hBox.setPrefWidth(w);
            hBox.setPrefHeight(h);
            System.out.println("x: " + event.getSceneX() + " y: " + event.getSceneY());
            System.out.println("宽：" + w + " 高：" + h);
        });

        // 绑定鼠标松开事件
        anchorPane.setOnMouseReleased(event -> {
            // 记录最终长宽
            w = Math.abs(event.getSceneX() - start_x);
            h = Math.abs(event.getSceneY() - start_y);
            anchorPane.setStyle("-fx-background-color: #00000000");
            // 添加剪切按钮，并显示在切图区域的底部
            Button confirm = new Button("确认");
            Button cancel = new Button("取消");
            confirm.setStyle("-fx-background-color: #00000000");
            cancel.setStyle("-fx-background-color: #00000000");
            hBox.setBorder(new Border(
                    new BorderStroke(Paint.valueOf("#85858544"), BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
            hBox.getChildren().addAll(confirm, cancel);
            hBox.setAlignment(Pos.BOTTOM_RIGHT);
            // 为切图按钮绑定切图事件
            confirm.setOnAction(event1 -> {
                // 切图辅助舞台小时
                stage.close();
                anchorPane.setDisable(true);
                TextField xTextField = Handler.getTextField("x");
                TextField yTextField = Handler.getTextField("y");
                TextField widthTextField = Handler.getTextField("width");
                TextField heightTextField = Handler.getTextField("height");
                xTextField.setText(String.valueOf(start_x));
                yTextField.setText(String.valueOf(start_y));
                widthTextField.setText(String.valueOf(w));
                heightTextField.setText(String.valueOf(h));
            });
            cancel.setOnAction(event1 -> {
                stage.close();
                anchorPane.setDisable(true);
            });
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
    }

}
