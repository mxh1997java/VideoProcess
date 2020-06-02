package executor;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processor.DefaultFFMPEGLocator;
import processor.FFMPEGExecutor;
import processor.FFMPEGLocator;
import processor.RBufferedReader;
import util.Handler;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xinhai.ma
 * @description
 * @date 2020/5/8 15:20
 */
public class VideoExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(VideoExecutor.class);

    private static FFMPEGLocator locator;

    /**
     * 修改视频封面命令
     */
    //private static final String COMMAND = "-i %s -i %s -map 1 -map 0 -c copy -disposition:0 attached_pic -y %s";

    /**
     * 可以处理的视频格式
     */
    public final static String[] VIDEO_TYPE = { "MP4", "WMV" };

    public VideoExecutor() {
        locator = new DefaultFFMPEGLocator();
    }

    /**
     * 功能描述 截取视频某一帧画面
     * @author xinhai.ma
     * @date 2020/5/8 16:32
     * @param sourcePath :   截取视频路径
     * @param targetPath :   生成图像路径
     * @param time :         帧数  0.003
     * @description ffmpeg -i test.asf -y -f image2 -t 0.001 -s 352x240 a.jpg(不管用)
     *               ffmpeg -y -i input.mp4 -ss 00:00:04 -vframes 1 -f image2 -vcodec png output.png
     * @return void
     */
    public void cutVideoImage(String sourcePath, String targetPath, String time) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-ss");
        ffmpeg.addArgument(time);
        ffmpeg.addArgument("-vframes");
        ffmpeg.addArgument("1");
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("image2");
        ffmpeg.addArgument("-vcodec");
        ffmpeg.addArgument("png");
        ffmpeg.addArgument(targetPath);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
            LOG.info("截取视频" + time + "画面 执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }

    /**
     * 功能描述 根据startTime和timeLength剪切出新视频(弃用：截出来的都是第一帧)
     * @author xinhai.ma
     * @date 2020/4/29 17:58
     * @param videoFile :
     * @param outputFile :
     * @param startTime :
     * @param endTime :
     * @return void
     * @desc ffmpeg -i input.mp4 -vcodec copy -acodec copy -ss 00:00:10 -to 00:00:40 output.mp4 -y
     */
    public void cutVideoByTime(String videoFile, String outputFile, String startTime, String endTime) {
        if(startTime.indexOf("-") > -1) {
            new RuntimeException("startTime " + startTime + " 错误!");
        }
        if(endTime.indexOf("-") > -1) {
            new RuntimeException("endTime " + endTime + " 错误!");
        }
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoFile);
        ffmpeg.addArgument("-vcodec");
        ffmpeg.addArgument("copy");
        ffmpeg.addArgument("-acodec");
        ffmpeg.addArgument("copy");
        ffmpeg.addArgument("-ss");
        ffmpeg.addArgument(startTime);
        ffmpeg.addArgument("-to");
        ffmpeg.addArgument(endTime);
        ffmpeg.addArgument(outputFile);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     *
     * @param sourcePath
     * @param targetPath
     * @param startTime
     * @param endTime
     * @desc ffmpeg -ss 00:10 -t 30 -i 0.mp4 -c copy 2.mp4
     */
    public void cutVideo(String sourcePath, String targetPath, String startTime, String endTime) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-ss");
        ffmpeg.addArgument(startTime);
        ffmpeg.addArgument("-t");
        ffmpeg.addArgument(endTime);
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument(targetPath);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("剪切视频: {}", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }

    /**
     * 功能描述 给视频去除水印
     * @author xinhai.ma
     * @date 2020/4/28 14:29
     * @param sourcePath : 必填：此处是你要处理的视频位置
     * @param x : 必填：此处是你要删除的水印x轴坐标
     * @param y : 必填：此处是你要删除的水印y轴坐标
     * @param width : 必填：此处是你要删除的水印宽度
     * @param height : 必填：此处是你要删除的水印高度
     * @param targetPath :  必填：此处是完成添加水印后输入视频的位置并重新命名该视频
     * @description ffmpeg -i input.mp4 -filter_complex "delogo=x=460:y=1170:w=250:h=100:show=0" output.mp4
     * @return void
     */
    public void removeWatermark(String sourcePath, String x, String y, String width, String height, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("\"delogo=x=" + x + ":y=" + y + ":w=" + width + ":h=" + height + ":show=0\"");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("消除水印完毕! " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 给视频添加图片水印
     * @author xinhai.ma
     * @date 2020/4/28 14:29
     * @param sourcePath : 必填：此处是你要处理的视频位置
     * @param imgPath : 必填：此处是你要添加的水印位置
     * @param x : 必填：此处是你要添加的水印位置
     * @param y : 必填：此处是你要添加的水印位置
     * @param targetPath :  必填：此处是完成添加水印后输入视频的位置并重新命名该视频
     * @desription ffmpeg -i input -i LOGo -filter_complex 'overlay=10:main_h-overlay_h-10' output
     * @return void
     */
    public void addWatermarkByImage(String sourcePath, String imgPath, String x, String y, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(imgPath);
        ffmpeg.addArgument("-filter_complex");
        ffmpeg.addArgument("overlay=" + x + ":" + y);
        ffmpeg.addArgument(targetPath);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
            LOG.info("添加图片水印 执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述  给视频添加字体水印
     * @author xinhai.ma
     * @date 2020/4/28 16:21
     * @param sourcePath :
     * @param x :
     * @param y :
     * @param fontFilePath :
     * @param fontSize :
     * @param fontColor :
     * @param text :
     * @param shadowy :
     * @param targetPath :
     * @description ffmpeg -i input.mp4 -vf "drawtext=fontfile=simsun.ttc:text='xinhai.ma':x=w-100:y=100:fontsize=24:fontcolor=yellow@0.5:shadowy=2" output.mp4
     * @return void
     */
    public void addWatermarkByFont(String sourcePath, String x, String y, String fontFilePath, String fontSize, String fontColor, String text, String shadowy, String targetPath) {
        LOG.info("添加水印开始");
        if(null == fontFilePath) {
            fontFilePath = Handler.getResourcePath("simsun.ttc");
            LOG.info("字体文件路径: " + fontFilePath);
        }
        if(null == fontSize) {
            fontSize = "24";
        }
        if(null == fontColor) {
            fontColor = "yellow@0.5";
        }
        if(null == shadowy) {
            shadowy = "2";
        }
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("\"drawtext=");
        ffmpeg.addArgument("fontfile=" + fontFilePath);
        ffmpeg.addArgument(":text=" + text);
        ffmpeg.addArgument(":x=" + x);
        ffmpeg.addArgument(":y=" + y);
        ffmpeg.addArgument(":fontsize=" + fontSize);
        ffmpeg.addArgument(":fontcolor=" + fontColor);
        ffmpeg.addArgument(":shadowy=" + shadowy + "\"");
        ffmpeg.addArgument(targetPath);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
            LOG.info("添加水印完毕! " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 根据Photoshop预设文件实现画风修改
     * @author xinhai.ma
     * @date 2020/4/30 14:11
     * @param sourcePath :
     * @param acvPath :
     * @param targetPath :
     * @return void
     * @desc ffmpeg -i test.flv -vf curves=psfile='test.acv':green='0.45/0.53' out.flv
     */
    public void revisionCurveByPs(String sourcePath, String acvPath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("curves=");
        ffmpeg.addArgument("psfile=" + acvPath);
        ffmpeg.addArgument(":green='0.45/0.53'");
        ffmpeg.addArgument(targetPath);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
            LOG.info("根据Ps预设文件修改视频效果 执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 多路输入拼接（多个视频放在同一画面播放）
     * @author xinhai.ma
     * @date 2020/4/30 13:50
     * @param videoPath :
     * @param videoPath1 :
     * @param videoPath2 :
     * @param videoPath3 :
     * @return void
     * @desc ffmpeg -i test1.mp4 -i test2.mp4 -i test3.mp4 -i test4.mp4 -filter_complex "[0:v]pad=iw*2:ih*2[a];[a][1:v]overlay=w[b];[b][2:v]overlay=0:h[c];[c][3:v]overlay=w:h" out.mp4
     */
    public void spliceVideo(String videoPath, String videoPath1, String videoPath2, String videoPath3, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoPath);
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoPath1);
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoPath2);
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoPath3);
        ffmpeg.addArgument("-filter_complex");
        ffmpeg.addArgument("\"[0:v]pad=iw*2:ih*2[a];");
        ffmpeg.addArgument("[a][1:v]overlay=w[b];");
        ffmpeg.addArgument("[b][2:v]overlay=0:h[c];");
        ffmpeg.addArgument("[c][3:v]overlay=w:h\"");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("多路输入拼接 执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 调整视频曲线(古风画质)
     * @author xinhai.ma
     * @date 2020/4/30 13:38
     * @param videoPath :
     * @param targetPath :
     * @return void
     * @desc ffmpeg -i test.flv -vf curves=vintage out.flv
     */
    public void ancientStyleFilter(String videoPath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoPath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("curves=vintage");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("复古风完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 镜像效果
     * @author xinhai.ma
     * @date 2020/4/29 19:26
     * @param videoPath :
     * @param targetPath :
     * @return void
     * @desc ffmpeg -i test.flv -vf crop=iw/2:ih:0:0,split[left][tmp];[tmp]hflip[right];[left]pad=iw*2[a];[a][right]overlay=w out.flv
     */
    public void mirror(String videoPath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoPath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("crop=iw/2:ih:0:0,split[left][tmp];[tmp]hflip[right];[left]pad=iw*2[a];[a][right]overlay=w");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("镜像效果完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("镜像: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 多张图片合成视频
     * @author xinhai.ma
     * @date 2020/4/29 16:53
     * @param imgPath :     图片路径
     * @param audioPath:    音频路径
     * @param targetPath :  生成文件路径
     * @param frameRate :   每秒多少帧
     * @param videoTime :   生成视频时长(秒)
     * @return void
     */
    public void imageConvertVideo(String imgPath, String audioPath, String targetPath, String frameRate, String videoTime) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-loop");
        ffmpeg.addArgument("1");
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("image2");
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(imgPath);
        //添加音频
        if(null != audioPath && !"".equals(audioPath)) {
            ffmpeg.addArgument("-i");
            ffmpeg.addArgument(audioPath);
        }
        ffmpeg.addArgument("-vcodec");
        ffmpeg.addArgument("libx264");
        ffmpeg.addArgument("-r");
        ffmpeg.addArgument(frameRate);
        ffmpeg.addArgument("-t");
        ffmpeg.addArgument(videoTime);
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("多张图片合成视频 执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }

    /**
     * 功能描述 给视频设置封面(设置视频封面)
     * @author xinhai.ma
     * @date 2020/4/29 15:34
     * @param sourcePath :
     * @param imgPath :
     * @param targetPath :
     * @return void
     */
    public void setCover(String sourcePath, String imgPath, String targetPath) {
        File file = new File(imgPath);
        //多个方法都是异步的
        while (!file.exists()) {
            LOG.info("封面不存在 等待...");
        }
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(imgPath);
        ffmpeg.addArgument("-map");
        ffmpeg.addArgument("1");
        ffmpeg.addArgument("-map");
        ffmpeg.addArgument("0");
        ffmpeg.addArgument("-c");
        ffmpeg.addArgument("copy");
        ffmpeg.addArgument("-disposition:0");
        ffmpeg.addArgument("attached_pic");
        ffmpeg.addArgument(targetPath);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
            LOG.info("给视频设置封面 执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 视频转换
     *
     * 注意指定视频分辨率时，宽度和高度必须同时有值；
     *
     * @param fileInput 源视频路径
     * @param fileOutPut 转换后的视频输出路径
     * @param withAudio 是否保留音频；true-保留，false-不保留
     * @param crf 指定视频的质量系数（值越小，视频质量越高，体积越大；该系数取值为0-51，直接影响视频码率大小）,取值参考：CrfValueEnum.code
     * @param preset 指定视频的编码速率（速率越快压缩率越低），取值参考：PresetVauleEnum.presetValue
     * @param width 视频宽度；为空则保持源视频宽度
     * @param height 视频高度；为空则保持源视频高度
     */
    public void convertVideo(File fileInput, File fileOutPut, boolean withAudio, Integer crf, String preset, Integer width, Integer height) {
        if (null == fileInput || !fileInput.exists()) {
            throw new RuntimeException("源视频文件不存在，请检查源视频路径");
        }
        if (null == fileOutPut) {
            throw new RuntimeException("转换后的视频路径为空，请检查转换后的视频存放路径是否正确");
        }

        if (!fileOutPut.exists()) {
            try {
                fileOutPut.createNewFile();
            } catch (IOException e) {
                LOG.error("视频转换时新建输出文件失败");
            }
        }

        String format = getFormat(fileInput);
        if (!isLegalFormat(format, VIDEO_TYPE)) {
            throw new RuntimeException("无法解析的视频格式：" + format);
        }

        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(fileInput.getAbsolutePath());
        if (!withAudio) { // 设置是否保留音频
            ffmpeg.addArgument("-an"); // 去掉音频
        }
        if (null != width && width > 0 && null != height && height > 0) { // 设置分辨率
            ffmpeg.addArgument("-s");
            String resolution = width.toString() + "x" + height.toString();
            ffmpeg.addArgument(resolution);
        }

        ffmpeg.addArgument("-vcodec"); // 指定输出视频文件时使用的编码器
        ffmpeg.addArgument("libx264"); // 指定使用x264编码器
        ffmpeg.addArgument("-preset"); // 当使用x264时需要带上该参数
        ffmpeg.addArgument(preset); // 指定preset参数
        ffmpeg.addArgument("-crf"); // 指定输出视频质量
        ffmpeg.addArgument(crf.toString()); // 视频质量参数，值越小视频质量越高
        ffmpeg.addArgument("-y"); // 当已存在输出文件时，不提示是否覆盖
        ffmpeg.addArgument(fileOutPut.getAbsolutePath());
        try {
            ffmpeg.execute();
            LOG.info("加速视频播放速度 执行完毕: " + fileOutPut.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 加速视频播放速度（并不加速音频）
     * @author xinhai.ma
     * @date 2020/5/8 21:38
     * @param sourcePath :  来源路径
     * @param targetPath :  生成路径
     * @param framerate :   帧率
     * @description ffmpeg -i input.mp4 -vf "setpts=0.5*PTS" output.mp4
     * @return void
     */
    public void addFrameRate(String sourcePath, String targetPath, String framerate) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("setpts=PTS/" + framerate);
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("加速视频播放速度 执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述  加速视频播放速度并生成gif图片
     * @author xinhai.ma
     * @date 2020/5/8 21:44
     * @param sourcePath :
     * @param targetPath :
     * @param framerate :
     * @description    ffmpeg -i 0.mp4 -vf setpts=PTS/8 -af atempo=8 -f gif 0.gif
     * @return void
     */
    public void createGifImage(String sourcePath, String targetPath, String framerate) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("setpts=PTS/" + framerate);
        ffmpeg.addArgument("-af");
        ffmpeg.addArgument("atempo=" + framerate);
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("加速视频播放速度并生成gif图片 执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 降低视频播放速度（并不降低音频播放速率）
     * @author xinhai.ma
     * @date 2020/5/8 21:38
     * @param sourcePath :  来源路径
     * @param targetPath :  生成路径
     * @param framerate :   帧率
     * @description ffmpeg -i input.mp4 -vf "setpts=2.0*PTS" output.mp4
     * @return void
     */
    public void reduceFrameRate(String sourcePath, String targetPath, String framerate) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("setpts=PTS*" + framerate);
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("降低视频播放速度执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 降低视频音频播放速度
     * @author xinhai.ma
     * @date 2020/5/8 21:58
     * @param sourcePath :
     * @param targetPath :
     * @param frameRate :  视频帧率音频帧率（倍数） 2、0.5
     * @desccription ffmpeg -i 1.mp4 -filter_complex "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]" -map "[v]" -map "[a]" 11quanbu.mp4
     * @return void
     */
    public void reduceVideoAudioFrameRate(String sourcePath, String targetPath, String frameRate) {
        BigDecimal frameRate1 = new BigDecimal(frameRate);
        BigDecimal frameRate2 = new BigDecimal("1");
        String videoFrameRate = frameRate2.divide(frameRate1, 1, BigDecimal.ROUND_UP).toString();

        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-filter_complex");
        ffmpeg.addArgument("\"[0:v]setpts=" + videoFrameRate + "*PTS[v];[0:a]atempo=" + frameRate + "[a]\" -map \"[v]\" -map \"[a]\"");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("加速视频音频播放速度执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 加速视频音频播放速度
     * @author xinhai.ma
     * @date 2020/5/8 21:58
     * @param sourcePath :
     * @param targetPath :
     * @param frameRate :  视频帧率音频帧率（倍数） 2、0.5
     * @desccription ffmpeg -i 1.mp4 -filter_complex "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]" -map "[v]" -map "[a]" 11quanbu.mp4
     * @return void
     */
    public void addVideoAudioFrameRate(String sourcePath, String targetPath, String frameRate) {
        BigDecimal frameRate1 = new BigDecimal(frameRate);
        BigDecimal frameRate2 = new BigDecimal("1");
        String videoFrameRate = frameRate2.divide(frameRate1, 1, BigDecimal.ROUND_UP).toString();

        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-filter_complex");
        ffmpeg.addArgument("\"[0:v]setpts=" + videoFrameRate + "*PTS[v];[0:a]atempo=" + frameRate + "[a]\" -map \"[v]\" -map \"[a]\"");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("加速视频音频播放速度执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 功能描述 合并多个视频
     * @author xinhai.ma
     * @date 2020/5/8 22:10
     * @param folderPath :  文件夹(文件夹下必须全部是要合并的视频)
     * @param targetPath :  生成文件路径
     * @description ffmpeg -i "concat:f00282urkwd.321002.1.ts|f00282urkwd.321002.2.ts|f00282urkwd.321002.3.ts|f00282urkwd.321002.4.ts|f00282urkwd.321002.5.ts|f00282urkwd.321002.6.ts|f00282urkwd.321002.7.ts|f00282urkwd.321002.8.ts|f00282urkwd.321002.9.ts|f00282urkwd.321002.10.ts|f00282urkwd.321002.11.ts|f00282urkwd.321002.12.ts|f00282urkwd.321002.13.ts|f00282urkwd.321002.14.ts|f00282urkwd.321002.15.ts|f00282urkwd.321002.16.ts|f00282urkwd.321002.17.ts|" -c copy output.mp4
     * @return void
     */
    public void mergeVideo(String folderPath, String targetPath) {
        File folder = new File(folderPath);
        if(!folder.exists()) {
            new RuntimeException("文件夹不存在!");
        }
        File[] files = folder.listFiles();
        if(files.length <= 0) {
            new RuntimeException("文件夹为空");
        }
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        StringBuffer buffer = new StringBuffer("\"concat:");
        for(int i=0; i<files.length; i++) {
            if(files[i].isFile()) {
                buffer.append(files[i].getAbsolutePath());
                buffer.append("|");
            }
        }
        buffer.append("\"");
        ffmpeg.addArgument(buffer.toString());
        ffmpeg.addArgument("-c");
        ffmpeg.addArgument("copy");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("合并多个视频执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }

    /**
     * 功能描述 合并多个视频
     * @author xinhai.ma
     * @date 2020/5/8 22:10
     * @param fileList :    视频地址集合
     * @param targetPath :  生成文件路径
     * @description  ffmpeg -i input1.flv -c copy -bsf:v h264_mp4toannexb -f mpegts input1.ts
     *                ffmpeg -i "concat:input1.mpg|input2.mpg|input3.mpg" -c copy output.mpg
     * @return void
     */
    public void mergeVideo(List<String> fileList, String targetPath) {
        List<String> newFileList = new ArrayList<>();
        fileList.forEach(path -> {
            //先执行: ffmpeg -i input1.flv -c copy -bsf:v h264_mp4toannexb -f mpegts input1.ts
            FFMPEGExecutor ffmpeg = this.locator.createExecutor();
            ffmpeg.addArgument("-i");
            ffmpeg.addArgument(path);
            ffmpeg.addArgument("-c");
            ffmpeg.addArgument("copy");
            ffmpeg.addArgument("-bsf:v");
            ffmpeg.addArgument("h264_mp4toannexb");
            ffmpeg.addArgument("-f");
            ffmpeg.addArgument("mpegts");
            String target = Handler.getNewFilePath("D:\\MaXinHai\\file\\1.ts");
            ffmpeg.addArgument(target);
            try {
                ffmpeg.execute();
                Thread.sleep(1000);
                LOG.info("{} 转化视频格式: {}", path, target);
                newFileList.add(target);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                ffmpeg.destroy();
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        StringBuffer buffer = new StringBuffer("\"concat:");
        for(int i=0; i<newFileList.size(); i++) {
            buffer.append(newFileList.get(i));
            if(i != newFileList.size()-1) {
                buffer.append("|");
            }
        }
        buffer.append("\"");
        ffmpeg.addArgument(buffer.toString());
        ffmpeg.addArgument("-c");
        ffmpeg.addArgument("copy");
        ffmpeg.addArgument("-bsf:a");
        ffmpeg.addArgument("aac_adtstoasc");
        ffmpeg.addArgument("-movflags");
        ffmpeg.addArgument("+faststart");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("合并多个视频执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("ffmpeg执行信息: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 转换视频为ts流
     * @param sourcePath
     * @return
     */
    public void videoConvertTs(String sourcePath, String targetPath) {
        //先执行: ffmpeg -i input1.flv -c copy -bsf:v h264_mp4toannexb -f mpegts input1.ts
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-c");
        ffmpeg.addArgument("copy");
        ffmpeg.addArgument("-bsf:v");
        ffmpeg.addArgument("h264_mp4toannexb");
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("mpegts");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("{} 转化视频格式: {}", sourcePath, targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 给视频添加片头片尾
     * @param startVideoPath   片头
     * @param centerVideoPath  内容视频
     * @param endVideoPath     片尾
     * @param targetPath       生成地址
     * @desc ffmpeg -i "concat:input1.mpg|input2.mpg|input3.mpg" -c copy output.mpg
     */
    public void mergeVideo(String startVideoPath, String centerVideoPath, String endVideoPath, String targetPath) {
        List<String> filePathList = new ArrayList<>(3);
        filePathList.add(startVideoPath);
        filePathList.add(centerVideoPath);
        filePathList.add(endVideoPath);
        mergeVideo(filePathList, targetPath);
    }


    /**
     * 功能描述 视频背景虚化效果
     * @author xinhai.ma
     * @date 2020/5/9 16:11
     * @param sourcePath :
     * @param targetPath :
     * @description ffmpeg -i input.mp4 -vf “split[a][b];[a]scale=1080:1920,boxblur=10:5[1];[b]scale=1080:ih*1080/iw[2];[1][2]overlay=0:(H-h)/2”
     *                -c:v libx264 -crf 18 -preset veryfast -aspect 9:16 -f mp4 output.mp4 -y
     * @return void
     */
    public void blurBackground(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("\"split[a][b];[a]scale=1080:1920,boxblur=10:5[1];[b]scale=1080:ih*1080/iw[2];[1][2]overlay=0:(H-h)/2\"");
        ffmpeg.addArgument("-c:v");
        ffmpeg.addArgument("libx264");
        ffmpeg.addArgument("-crf");
        ffmpeg.addArgument("18");
        ffmpeg.addArgument("-preset");
        ffmpeg.addArgument("veryfast");
        ffmpeg.addArgument("-aspect");
        ffmpeg.addArgument("9:16");
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("mp4");
        ffmpeg.addArgument(targetPath);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
            LOG.info("背景虚化执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("背景虚化: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 色彩变幻
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf hue="H=2*PI*t:s=sin(2*PI*t)+1" out.mp4
     */
    public void colorChange(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("hue=\"H=2*PI*t:s=sin(2*PI*t)+1\"");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("色彩变幻执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("色彩变幻: ", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 模糊处理
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf boxblur=5:1:cr=0:ar=0 out.mp4
     */
    public void blur(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("boxblur=5:1:cr=0:ar=0");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("模糊处理执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("模糊处理: {}", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 水平翻转
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf geq=p(W-X\\,Y) out.mp4
     */
    public void horizontalFlip(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("geq=p(W-X\\\\,Y)");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("水平翻转执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("水平翻转: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }

    /**
     * 垂直翻转
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf vflip out.mp4
     */
    public void verticallyFlip(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("vflip");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("垂直翻转执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("垂直翻转: " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }

    /**
     * 浮雕效果
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf format=gray,geq=lum_expr=‘(p(X,Y)+(256-p(X-4,Y-4)))/2‘ out.mp4
     */
    public void reliefEffect(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("format=gray,geq=lum_expr='(p(X,Y)+(256-p(X-4,Y-4)))/2'");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("浮雕效果执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("浮雕效果: {}", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 均匀噪声
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf noise=alls=20:allf=t+u out.mp4
     */
    public void uniformNoise(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("noise=alls=20:allf=t+u");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("均匀噪声执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 视频颤抖
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf crop="in_w/2:in_h/2:(in_w-out_w)/2+((in_w-out_w)/2)*sin(n/10):(in_h-out_h)/2+((in_h-out_h)/2)*sin(n/7)" out.mp4
     */
    public void videoTrembling(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("crop=\"in_w/2:in_h/2:(in_w-out_w)/2+((in_w-out_w)/2)*sin(n/10):(in_h-out_h)/2+((in_h-out_h)/2)*sin(n/7)\"");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("视频颤抖执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 闪烁渐晕
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf vignette=‘PI/4+random(1)*PI/50‘:eval=frame out.mp4
     */
    public void flashingVignetting(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("vignette='PI/4+random(1)*PI/50':eval=frame");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("闪烁渐晕执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 渐晕
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf vignette=PI/4 out.mp4
     */
    public void vignetting(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("vignette=PI/4");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("渐晕执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 反锐化
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf unsharp=7:7:-2:7:7:-2 out.mp4
     */
    public void unsharp(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("unsharp=7:7:-2:7:7:-2");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("反锐化执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 锐化
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf unsharp=luma_msize_x=7:luma_msize_y=7:luma_amount=2.5 out.mp4
     */
    public void sharpen(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("unsharp=luma_msize_x=7:luma_msize_y=7:luma_amount=2.5");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("锐化执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 黑白
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf lutyuv="u=128:v=128" out.mp4
     */
    public void blackWhite(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("lutyuv=\"u=128:v=128\"");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("黑白执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 渐入（可以实现淡入淡出效果）
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg i in.mp4 -vf fade=in:0:90 out.mp4
     */
    public void gradually(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("fade=in:0:90");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("渐入执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 返回视频文件基本信息
     * @param filePath
     * @return
     */
    public MultimediaInfo getVideoInfo(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            new RuntimeException("文件不存在!");
        }
        MultimediaObject instance = new MultimediaObject(file);
        MultimediaInfo result = null;
        try {
            result = instance.getInfo();
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * ffmpeg添加文字不生效，自己实现的添加文字水印
     * @param drawStr    文字
     * @param fontSize   文字大小
     * @param fontName   字体名称
     * @param sourcePath 来源视频地址
     * @param targetPath 生成视频地址
     */
    public void addWatermarkByFont(String drawStr, int fontSize, String fontName, String x, String y, String sourcePath, String targetPath) {
        //减少生成不必要的图片
        String imagePath = Handler.getPath(drawStr);
        if(null == imagePath) {
            //生成图片路径
            imagePath = Handler.getNewFilePath("D:\\MaXinHai\\file\\1.png");
            //生成图片
            ImageExecutor.createImage(drawStr, new Font(fontName, Font.PLAIN, fontSize), new File(imagePath));
        }
        //添加图片到视频
        addWatermarkByImage(sourcePath, imagePath, x, y, targetPath);
    }


    /**
     * 视频倒放
     * @param sourcePath
     * @param targetPath
     * ffmpeg -i input.mp4 -vf reverse -y reverse.mp4
     */
    public void videoUpsideDown(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("reverse");
        ffmpeg.addArgument("-y");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("视频倒放执行完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 提取视频
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg -i input_file -vcodec copy -an output_file_video
     */
    public void extractVideo(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vcodec");
        ffmpeg.addArgument("copy");
        ffmpeg.addArgument("-an");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("提取视频完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 提取音频
     * @param sourcePath
     * @param targetPath
     * @desc ffmpeg -i input_file -acodec copy -vn output_file_audio
     */
    public void extractAudio(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-acodec");
        ffmpeg.addArgument("copy");
        ffmpeg.addArgument("-vn");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("提取音频完毕: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ffmpeg.destroy();
        }
    }


    /**
     * 屏幕录制
     * @param targetPath
     * @desc ffmpeg -f gdigrab -i desktop -f mp4 output.mp4
     */
    public String screenRecord(String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("gdigrab");
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument("desktop");
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("mp4");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            //ffmpeg.getProcessExitCode();
            LOG.info("屏幕录制完毕: " + targetPath);
            Handler.setFfmpeg(ffmpeg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("屏幕录制: {}", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "true";
    }

    /**
     * 关闭屏幕录制进程
     */
    public void closeScreenRecord() {
        FFMPEGExecutor ffmpeg = Handler.getFfmpeg();
        if(null == ffmpeg) {
            new RuntimeException("没有发现屏幕录制进程!");
        }
        ffmpeg.destroy();
    }


    /**
     * 打开摄像头拍摄视频
     * @param targetPath
     * @desc ffmpeg -t 20 -f vfwcap -i 0 -r 8 -f mp4 cap1111.mp4
     */
    public void takeVideo(String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-t");
        ffmpeg.addArgument("20");
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("vfwcap");
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument("0");
        ffmpeg.addArgument("-r");
        ffmpeg.addArgument("8");
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("mp4");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("打开摄像头拍摄视频: " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("打开摄像头拍摄视频: {}", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 裁剪视频尺寸（相当于重新设置视频宽高）
     * @param sourcePath
     * @param targetPath
     * @param size 1280x720
     * @desc ffmpeg  -i  input_file  -s  wxh  output_file (wxh 指 1280x720)
     */
    public void cropVidoe(String sourcePath, String targetPath, String size) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-s");
        ffmpeg.addArgument(size);
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("裁剪视频尺寸: {}", targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("裁剪视频尺寸: {}", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 从目标视频根据坐标宽高裁剪出新的视频
     * @param sourcePath
     * @param targetPath
     * @param width
     * @param height
     * @param x
     * @param y
     *  ffmpeg -i input.mp4 -vf crop=400:400 output.mp4 -y
     *  ffmpeg -i input.mp4 -vf crop=400:400:0:0 output.mp4 -y
     *  crop的参数格式为w:h:x:y，
     *  w、h为输出视频的宽和高，
     *  x、y标记输入视频中的某点，将该点作为基准点，向右下进行裁剪得到输出视频。
     * 	如果x y不写的话，默认居中剪切
     */
    public void cropVideo(String sourcePath, String targetPath, String width, String height, String x, String y) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument("crop=" + width + ":" + height + ":" + x + ":" + y);
        ffmpeg.addArgument(targetPath);
        ffmpeg.addArgument("-y");
        try {
            ffmpeg.execute();
            LOG.info("裁剪视频尺寸: {}", targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("裁剪视频尺寸: {}", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 增大视频音量(两倍)
     * @param sourcePath
     * @param targetPath
     * ffmpeg -i 1.wav -af volume=2 -y 2.wav
     */
    public void increaseVolume(String sourcePath, String targetPath) {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(sourcePath);
        ffmpeg.addArgument("-af");
        ffmpeg.addArgument("volume=2");
        ffmpeg.addArgument("-y");
        ffmpeg.addArgument(targetPath);
        try {
            ffmpeg.execute();
            LOG.info("裁剪视频尺寸: {}", targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                LOG.info("裁剪视频尺寸: {}", line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取指定文件的后缀名
     * @param file
     * @return
     */
    private String getFormat(File file) {
        String fileName = file.getName();
        String format = fileName.substring(fileName.indexOf(".") + 1);
        return format;
    }

    /**
     * 检测视频格式是否合法
     * @param format
     * @param formats
     * @return
     */
    private boolean isLegalFormat(String format, String formats[]) {
        for (String item : formats) {
            if (item.equals(StringUtils.upperCase(format))) {
                return true;
            }
        }
        return false;
    }

}
