package components;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author xinhai.ma
 * @description  首页底部
 * @date 2020/5/9 9:04
 */
public class MyBottom {

    public static HBox getBottom() {
        HBox hBox = new HBox(15);
        //分别对应 上 右 下 左
        hBox.setPadding(new Insets(15, 150, 15, 150));
        //hBox.setStyle("-fx-background-color: gold");
        Text company = new Text ("苏州微智汇信息科技有限公司");
        company.setFont(Font.font(java.awt.Font.SERIF, 25));
        company.setTextAlignment(TextAlignment.CENTER);
        Text program = new Text ("视频处理程序");
        program.setFont(Font.font(java.awt.Font.SERIF, 25));
        program.setTextAlignment(TextAlignment.CENTER);
        hBox.getChildren().addAll(company, program);
        return hBox;
    }

}
