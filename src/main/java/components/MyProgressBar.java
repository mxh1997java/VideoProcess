package components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 进度条组件
 */
public class MyProgressBar {

    final Label status = new Label();
    final Label folderCount = new Label();
    final Label fileCount = new Label();
    final Label mp3Count = new Label();


    public void start(Stage stage) {
        final GridPane finderResults = new GridPane();
        finderResults.setPrefWidth(400);
        finderResults.setVgap(10);
        finderResults.setHgap(10);
        finderResults.addRow(0, new Label("Status: "), status);
        finderResults.addRow(1, new Label("# Folders: "), folderCount);
        finderResults.addRow(2, new Label("# Files: "), fileCount);
        finderResults.addRow(3, new Label("# mp3s: "), mp3Count);

        final Button finderStarter = new Button("Find mp3s");
        finderStarter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //startMp3Finder(finderStarter);
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10; -fx-font-size: 16;");
        layout.getChildren().setAll(finderStarter, finderResults);
        stage.setScene(new Scene(layout));
        stage.show();
    }

}
