package components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

/**
 * 进度条组件
 */
public class MyProgressBar {

    private static final Logger LOG = LoggerFactory.getLogger(MyProgressBar.class);

    private HBox hBox;

    /**
     * 进度条标题
     */
    private Label title;

    /**
     * 进度条
     */
    private ProgressBar progressBar;

    /**
     * 进度条提示
     */
    private Label message;

    /**
     * 每个步骤所占的比例
     */
    private Double step;

    /**
     * 进度条总量
     */
    private Double total = 0.0;

    /**
     * 更新进度提示
     * @param message
     */
    public void setLabel(String message) {
        this.message.setText(message);
    }

    /**
     * 更新进度条
     * @param value
     */
    public void setValue(double value) {
        if(value == 0) {
            total = 0.0;
        }
        progressBar.setProgress(value);
    }

    /**
     * 控制进度条组件显示
     * @param flag
     */
    public void setVisible(boolean flag) {
        progressBar.setVisible(flag);
        title.setVisible(flag);
        message.setVisible(flag);
        hBox.setDisable(!flag);
    }

    /**
     * 获取进度条组件
     * @return
     */
    public HBox getProgressBar() {
        hBox = new HBox();
        hBox.setPadding(new Insets(5));
        progressBar = new ProgressBar();
        message = new Label();
        if(title != null) {
            hBox.getChildren().addAll(title);
        }
        hBox.getChildren().addAll(progressBar, message);
        //默认不显示
        setVisible(false);
        return hBox;
    }

    public MyProgressBar(String title) {
        this.title = new Label(title);
    }

    public MyProgressBar() {}

    /**
     * 计算进度条步长
     * @param stepTotal
     * @return
     */
    public void calculationStep(int stepTotal) {
        BigDecimal dividend = new BigDecimal("1");
        BigDecimal divisor = new BigDecimal(stepTotal);
        LOG.info("进度组件: 计算进度条步长 除数: {}", divisor);
        LOG.info("被除数: {}, 除数: {}, 结果: {}", dividend, divisor, dividend.divide(divisor, 2, BigDecimal.ROUND_UP).doubleValue());
        step = dividend.divide(divisor, 2, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 计算进度条步长(多任务使用)
     * @param stepTotal
     * @param taskTotal
     */
    public void calculationStep(int stepTotal, int taskTotal) {
        BigDecimal dividend = new BigDecimal("1");
        BigDecimal divisor = new BigDecimal(stepTotal * taskTotal);
        LOG.info("进度组件: 计算进度条步长 除数: {}", divisor);
        LOG.info("被除数: {}, 除数: {}, 结果: {}", dividend, divisor, dividend.divide(divisor, 2, BigDecimal.ROUND_UP).doubleValue());
        step = dividend.divide(divisor, 2, BigDecimal.ROUND_UP).doubleValue();
    }


    /**
     * 自动累加
     */
    public void autoAdd() {
        if(null == step) {
            new RuntimeException("未计算进度条step!");
        }
        total += step;
        LOG.info("进度条进度: {}", total);
        setValue(total);
    }

}
