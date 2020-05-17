package components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

/**
 * 进度条组件
 */
public class MyProgressBar {

    private static ProgressBar progressBar = null;

    public static HBox getProgressBar() {
        Label label = new Label("任务进度: ");
        progressBar = new ProgressBar();
        progressBar.setProgress(0);
        progressBar.setPrefWidth(200);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.setPrefHeight(60);
        hBox.getChildren().addAll(label, progressBar);
        hBox.setVisible(false);
        return hBox;
    }

    public static void setProgress(double value) {
        progressBar.setProgress(value);
    }

}
