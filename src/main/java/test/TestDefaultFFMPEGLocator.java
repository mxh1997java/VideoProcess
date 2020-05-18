package test;

import processor.DefaultFFMPEGLocator;
import executor.VideoExecutor;
import org.junit.Test;
import sun.nio.cs.HistoricallyNamedCharset;
import util.Handler;
//import util.Handler;
//import ws.schild.jave.EncoderException;
//import ws.schild.jave.MultimediaInfo;
//import ws.schild.jave.MultimediaObject;

import java.io.File;

/**
 * @author xinhai.ma
 * @description
 * @date 2020/5/8 15:10
 */
public class TestDefaultFFMPEGLocator {

    @Test
    public void test() {
        DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
        System.out.println(locator);

        VideoExecutor videoExecutor = new VideoExecutor();

        String sourcePath = "D:\\Users\\xinhai.ma\\Videos\\Captures\\3-7.mp4";
        String targetPath = "D:\\Users\\xinhai.ma\\Videos\\Captures\\程序测试结果\\3-7-1.mp4";

        //获取封面 测试通过
        //videoExecutor.cutVideoImage(null, null, null, null);
        //删除水印 测试通过
        //videoExecutor.removeWatermark(sourcePath, "100", "740", "300", "200", targetPath);

        //添加图片水印
        String imgPath = "D:\\Users\\xinhai.ma\\Pictures\\本人照片\\微信图片_20200420133157.jpg";
        targetPath = "D:\\Users\\xinhai.ma\\Videos\\Captures\\程序测试结果\\3-7-3.mp4";
        //videoExecutor.addWatermarkByImage(sourcePath, imgPath, "0", "0", targetPath);

        //String newPath = Handler.getSimplePath(targetPath);
        //LOG.info(newPath);

//        File file = new File("D:\\Users\\15735400536\\Videos\\台湾综艺\\2.mp4");
//        MultimediaObject instance = new MultimediaObject(file);
//        try {
//            MultimediaInfo result = instance.getInfo();
//            String format = result.getFormat();
//            long videoTime = result.getDuration();
//            int height = result.getVideo().getSize().getHeight();
//            int width = result.getVideo().getSize().getWidth();
//            System.out.println("视频格式: " + format);
//            System.out.println("视频长度: " + videoTime);
//            System.out.println("视频宽: " + width);
//            System.out.println("视频高: " + height);
//
//        } catch (EncoderException e) {
//            e.printStackTrace();
//        }

        //Handler.saveProp("2018", "2020", "2022");

        //Handler.createFile("D:\\MaXinHai\\file", "config.properties");

        videoExecutor.addWatermarkByFont("百度贴吧", 18, "微软雅黑",
                "D:\\Users\\15735400536\\Videos\\台湾综艺\\12.mp4", "D:\\2.mp4");

    }

}
