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
import java.io.IOException;

/**
 * @author xinhai.ma
 * @description
 * @date 2020/5/8 15:10
 */
public class TestDefaultFFMPEGLocator {

    @Test
    public void test() {
        //DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
        //System.out.println(locator);

        VideoExecutor videoExecutor = new VideoExecutor();

        //String sourcePath = "D:\\Users\\xinhai.ma\\Videos\\Captures\\3-7.mp4";
        //String targetPath = "D:\\Users\\xinhai.ma\\Videos\\Captures\\程序测试结果\\3-7-1.mp4";

        //获取封面 测试通过
        //videoExecutor.cutVideoImage(null, null, null, null);
        //删除水印 测试通过
        //videoExecutor.removeWatermark(sourcePath, "100", "740", "300", "200", targetPath);

        //添加图片水印
        //String imgPath = "D:\\Users\\xinhai.ma\\Pictures\\本人照片\\微信图片_20200420133157.jpg";
        //targetPath = "D:\\Users\\xinhai.ma\\Videos\\Captures\\程序测试结果\\3-7-3.mp4";
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

//        videoExecutor.addWatermarkByFont("百度贴吧", 18, "微软雅黑",
//                "D:\\Users\\15735400536\\Videos\\台湾综艺\\12.mp4", "D:\\2.mp4");


//        String sourcePath = "D:\\test1";
//        String targetPath = "D:\\test";
//        Handler.batchCopyFile(sourcePath, targetPath);

//        String fileName = Handler.getFileName("D:\\123.mp4");
//        System.out.println(fileName);


//        String startVideo = "D:\\Users\\15735400536\\Videos\\台湾综艺\\10.mp4";
//        String video = "D:\\Users\\15735400536\\Videos\\台湾综艺\\12.mp4";
//        String endVideo = "D:\\Users\\15735400536\\Videos\\台湾综艺\\11.mp4";
//        String targetPath = Handler.getNewFilePath(null);
//        videoExecutor.mergeVideo(startVideo, video, endVideo, targetPath);

//        String filePath = Handler.getNewFilePath(null);
//        String url = "https://s184.convertio.me/p/ZuM4utNbttyfzOCg0BsLtA/0fcd2080dbbfff74459f449cd8112fc2/%E4%B8%80%E5%89%AA%E6%A2%85.avi";
//        Handler.httpDownload(url, filePath);

        String sourcePath = "D:\\Users\\15735400536\\Videos\\国漫\\连理枝.mp4";
        String targetPath = "D:\\start.mp4";
        //String size = "1920x1080";
        //videoExecutor.cropVidoe(sourcePath, targetPath, size);


        //测试视频音频减速
        //videoExecutor.reduceVideoAudioFrameRate(sourcePath, targetPath, "0.5");


//        targetPath = "D:\\end3.mp4";
//        //测试视频音频提速
//        videoExecutor.addVideoAudioFrameRate(sourcePath, targetPath, "4");

        //Handler.getScreenSize();

//        try {
//            Runtime.getRuntime().exec("D:\\Software\\VLC\\vlc.exe" + " " + "D:\\Users\\15735400536\\Videos\\国漫\\连理枝.mp4");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        videoExecutor.cutVideo(sourcePath, targetPath, "00:00:01", "00:00:02");

    }

}
