package executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理
 */
public class ImageExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ImageExecutor.class);

    /**
     * 根据文字生成png图片，并返回图片路径
     * @param drawStr
     * @param font
     * @param targetPath
     * @return
     */
    public static void createImage(String drawStr, Font font, File targetPath) {
        //获取font的样式应用在str上的整个矩形
        Rectangle2D r= font.getStringBounds(drawStr, new FontRenderContext(AffineTransform.getScaleInstance(1, 1),false,false));
        int unitHeight=(int)Math.floor(r.getHeight());//获取单个字符的高度
        //获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width=(int)Math.round(r.getWidth())+1;
        int height=unitHeight+3;//把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度

        // 创建图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();

        //设置透明  start
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g = image.createGraphics();
        //设置透明  end
        g.setFont(font); //设置字体
        g.setColor(Color.ORANGE); //设置颜色
        //g.drawRect(0, 0, width - 1, height - 1); //画边框
        g.drawString(drawStr, 0, font.getSize());
        g.dispose();
        try {
            // 输出png图片
            ImageIO.write(image, "png", targetPath);
        } catch (IOException e) {
            LOG.info("生成图片出错: {}", e.getMessage());
            e.printStackTrace();
        }
    }

}
