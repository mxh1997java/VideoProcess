package util;

import components.MyAlertBox;
import components.MyListView;
import components.MyProgressBar;
import executor.VideoExecutor;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processor.FFMPEGExecutor;
import task.CopyFileTask;
import task.MyExecutorService;
import ws.schild.jave.MultimediaInfo;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * 主要存放了很多缓存数据
 * @author xinhai.ma
 * @description
 * @date 2020/5/9 9:11
 */
public class Handler {

    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);

    /**
     * 是否继续执行后续操作
     */
    private static boolean isStop = true;

    /**
     * 获取是否继续执行标记
     * @return
     */
    public static boolean getIsStop() {
        return isStop;
    }

    /**
     * 设置是否继续执行标记
     * @param flag
     */
    public static void setIsStop(boolean flag) {
        isStop = flag;
    }

    /**
     * 进度条对象map
     */
    private static Map<String, MyProgressBar> progressBarMap = new HashMap<>();

    /**
     * 根据key存放进度条对象
     * @param key
     * @param progressBar
     */
    public static void putProgressBar(String key, MyProgressBar progressBar) {
        progressBarMap.put(key, progressBar);
    }

    /**
     * 根据key拿到进度条对象
     * @param key
     * @return
     */
    public static MyProgressBar getProgressBar(String key) {
        return progressBarMap.get(key);
    }


    /**
     * 增加视频播放速度选项集合
     * @return
     */
    public static List<String> getAddFrameRateItemList() {
        List<String> itemList = new ArrayList<>();
        itemList.add("2.0");
        itemList.add("3.0");
        itemList.add("4.0");
        return itemList;
    }

    /**
     * 降低视频播放速度选项集合
     * @return
     */
    public static List<String> getReduceFrameRateItemList() {
        List<String> itemList = new ArrayList<>();
        itemList.add("0.5");
        return itemList;
    }

    /**
     * 程序可处理视频类型
     */
    private static Set<String> fileTypeSet = new HashSet<>();

    /**
     * 存储页面数据（已处理视频栏和未处理视频栏数据）
     */
    private static Map<String, List<String>> dataListMap = new HashMap<>();

    /**
     * 存储MyListView对象
     */
    private static Map<String, MyListView> listViewMap = new HashMap<>();

    /**
     * 存储视频播放画面比例
     */
    private static volatile BigDecimal scale = null;

    /**
     * 配置属性文件路径
     */
    private static String path = "C:\\VideoProcess\\config\\config.properties";

    /**
     * 配置文件对象
     */
    private static Properties prop = new Properties();

    /**
     * 水印图片地址集合
     */
    private static Map<String, String> watermarkImagePathMap = new HashMap<>();

    static {
        fileTypeSet.add("mp4");
        fileTypeSet.add("flv");
        fileTypeSet.add("avi");
        fileTypeSet.add("rm");
        fileTypeSet.add("rmvb");
        fileTypeSet.add("wmv");
    }

    /**
     * 屏幕录制进程
     */
    private static FFMPEGExecutor ffmpeg = null;

    private static Thread screenRecordThread = null;

    private static List<String> coverPathList = new ArrayList<>();

    /**
     * 消除水印x、y、width、height文本框对象集合
     */
    private static Map<String, TextField> textFieldMap = new HashMap<>(4);

    /**
     * 功能区域复选框选择结果
     */
    private static List<Boolean> checkBoxList = new ArrayList<>();

    /**
     * 功能区域用户操作缓存
     */
    private static Map<String, List<String>> userOperatingCache = new HashMap<>();

    /**
     * 每个视频的处理方案
     */
    private static Map<String, Map<String, List<String>>> dealWithProgram = new ConcurrentHashMap<String, Map<String, List<String>>>();

    /**
     * 返回程序方案名称集合
     * @return
     */
    public static List<String> getProgramKeys() {
        List<String> itemList = new ArrayList<>(dealWithProgram.keySet());
        return itemList;
    }

    /**
     * 根据视频名字存放对应的处理方案
     * @param videoName
     * @param program
     */
    public static void putProgram(String videoName, Map<String, List<String>> program) {
        if(EmptyUtils.isNotEmpty(program)) {
            LOG.info("存放配置方案:  视频名称:{} 方案:{}", videoName, program);
            dealWithProgram.put(videoName, program);
        }
    }


    /**
     * 根据视频名字获取对应的处理方案
     * @param videoName
     * @return
     */
    public static Map<String, List<String>> getProgram(String videoName) {
        LOG.info("获取配置方案:  视频名称:{} 方案:{}", videoName, dealWithProgram.get(videoName));
        return dealWithProgram.get(videoName);
    }

    /**
     * 获得全部视频处理方案
     * @return
     */
    public static Map<String, Map<String, List<String>>> getAllProgram() {
        return dealWithProgram;
    }


    /**
     * 清理所有的视频处理方案
     */
    public static void clearAllProgram() {
        dealWithProgram.clear();
    }


    /**
     * 当前播放的视频
     */
    private static String currentVideoPath = null;

    /**
     * 获取当前播放的视频路径
     * @return
     */
    public static String getCurrentVideoPath() {
        return currentVideoPath;
    }

    /**
     * 设置当前播放的视频路径
     * @param currentVideoPath
     */
    public static void setCurrentVideoPath(String currentVideoPath) {
        Handler.currentVideoPath = currentVideoPath;
    }

    /**
     * 根据functionName存放用户数据 如:删除水印 x、y、width、height
     * @param functionName
     * @param cache
     */
    public static void putUserOperating(String functionName, List<String> cache) {
        userOperatingCache.put(functionName, cache);
    }

    /**
     * 根据functionName获取用户数据 如:删除水印
     * @param functionName
     * @return
     */
    public static List<String> getUserOperatingCache(String functionName) {
        return userOperatingCache.get(functionName);
    }

    /**
     * 清空用户操作缓存
     */
    public static void clearUserOperatingCache() {
        userOperatingCache.clear();
    }

    /**
     * 获取所有用户操作缓存
     * @return
     */
    public static Map<String, List<String>> getAllUserOperatingCache() {
        return userOperatingCache;
    }

    /**
     * 返回一个拷贝后的新对象
     * @return
     */
    public static Map<String, List<String>> getNewAllUserOperatingCache() {
        Map<String, List<String>> cache = new HashMap<>(userOperatingCache.size());
        if(userOperatingCache.size() > 0) {
            userOperatingCache.forEach((k,v) -> {
                if(null != v && v.size() == 1) {
                    if(EmptyUtils.isNotEmpty(v.get(0))) {
                        cache.put(k, v);
                    }
                }
                if(null != v && v.size() == 2) {
                    if(EmptyUtils.isNotEmpty(v.get(0)) && EmptyUtils.isNotEmpty(v.get(1))) {
                        cache.put(k, v);
                    }
                }
                if(null != v && v.size() == 3) {
                    if(EmptyUtils.isNotEmpty(v.get(0)) && EmptyUtils.isNotEmpty(v.get(1)) && EmptyUtils.isNotEmpty(v.get(2))) {
                        cache.put(k, v);
                    }
                }
                if(null != v && v.size() == 4) {
                    if(EmptyUtils.isNotEmpty(v.get(0)) && EmptyUtils.isNotEmpty(v.get(1)) && EmptyUtils.isNotEmpty(v.get(2)) && EmptyUtils.isNotEmpty(v.get(3))) {
                        cache.put(k, v);
                    }
                }
            });
        }
        return cache;
    }

    /**
     * 添加复选框选择结果
     * @return
     */
    public static void removeSelect() {
        checkBoxList.remove(0);
    }

    /**
     * 添加复选框选择结果
     * @return
     */
    public static void addSelect(boolean flag) {
        checkBoxList.add(flag);
    }

    /**
     * 获得复选框结果
     * @return
     */
    public static List<Boolean> getCheckBoxList() {
        return checkBoxList;
    }

    /**
     * 根据key获取TextField对象
     * @param key
     * @return
     */
    public static TextField getTextField(String key) {
        return textFieldMap.get(key);
    }

    /**
     * 根据key存放TextField对象
     * @param key
     * @param textField
     */
    public static void put(String key, TextField textField) {
        textFieldMap.put(key, textField);
    }

    public static List<String> getCoverPathList() {
        return coverPathList;
    }

    public static void setCoverPathList(List<String> coverPathList) {
        Handler.coverPathList = coverPathList;
    }

    /**
     * 添加封面路径到缓存
     * @param coverPath
     */
    public static void addCoverPath(String coverPath) {
        coverPathList.add(coverPath);
    }

    /**
     * 清空封面路径缓存
     */
    public static void clearCoverPathList() {
        coverPathList.clear();
    }

    public static Thread getScreenRecordThread() {
        return screenRecordThread;
    }

    public static void setScreenRecordThread(Thread screenRecordThread) {
        Handler.screenRecordThread = screenRecordThread;
    }

    /**
     * 获取进程
     * @return
     */
    public static FFMPEGExecutor getFfmpeg() {
        return ffmpeg;
    }

    /**
     * 设置进程
     * @param ffmpeg
     */
    public static void setFfmpeg(FFMPEGExecutor ffmpeg) {
        Handler.ffmpeg = ffmpeg;
    }

    /**
     * 根据文本存放图片地址
     *
     * @param drawStr
     * @param imagePath
     */
    public static void put(String drawStr, String imagePath) {
        watermarkImagePathMap.put(drawStr, imagePath);
    }

    /**
     * 根据文本获取图片地址
     *
     * @param drawStr
     * @return
     */
    public static String getPath(String drawStr) {
        return watermarkImagePathMap.get(drawStr);
    }

    /**
     * 根据key存放集合
     *
     * @param key
     * @param dataList
     */
    public static void put(String key, List<String> dataList) {
        dataListMap.put(key, dataList);
    }

    /**
     * 根据key获取集合
     *
     * @param key
     * @return
     */
    public static List<String> getList(String key) {
        return dataListMap.get(key);
    }

    /**
     * 根据key获取ListView
     *
     * @param key
     * @return
     */
    public static MyListView getListView(String key) {
        LOG.info("根据key获取ListView: {} => {}", key, listViewMap.get(key));
        return listViewMap.get(key);
    }

    /**
     * 根据key存放ListView
     *
     * @param key
     * @param listView
     */
    public static void put(String key, MyListView listView) {
        listViewMap.put(key, listView);
    }

    /**
     * 设置当前比例
     *
     * @param value
     */
    public static void setScale(BigDecimal value) {
        LOG.info("比例: {}", value.doubleValue());
        scale = value;
    }

    /**
     * 获取当前比例
     *
     * @return
     */
    public static BigDecimal getScale() {
        return scale;
    }


    /**
     * 截取str1最后一次出现位置以后的字符串
     *
     * @param str
     * @param str1
     * @return
     */
    public static String lastStr(String str, String str1) {
        String suffix = str.substring(str.lastIndexOf(str1) + 1);
        return suffix;
    }

    /**
     * 读取某个文件夹下的所有视频文件
     *
     * @param filePath 要读取的路径
     * @return
     */
    public static List<String> readDic(String filePath) {
        List<String> fileList = new ArrayList<>();
        File file = new File(filePath);        //获取其file对象
        readDic(file, fileList);
        return fileList;
    }


    /**
     * 读取目录
     *
     * @param file
     * @param fileList
     */
    private static void readDic(File file, List<String> fileList) {
        File[] fs = file.listFiles();
        for (File f : fs) {
            //若是目录，则递归打印该目录下的文件
            if (f.isDirectory()) {
                readDic(f, fileList);
            }
            //若是文件，直接打印
            if (f.isFile() && isVideo(f.getAbsolutePath().toString())) {
                fileList.add(f.getAbsolutePath().toString());
            }
        }
    }


    /**
     * 判断文件不否是视频
     *
     * @param fileName
     * @return
     */
    public static boolean isVideo(String fileName) {
        if (fileName.indexOf(".") == -1) {
            return false;
        }
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return fileTypeSet.contains(fileType);
    }

    /**
     * 保存配置文件
     *
     * @param ffmpegPath
     * @param sourcePath
     * @param targetPath
     */
    public static void saveProp(String ffmpegPath, String sourcePath, String targetPath) {
        InputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(path);
            prop.load(input);
            delProp("ffmpegPath");
            delProp("sourcePath");
            delProp("targetPath");
            ///保存属性到b.properties文件
            LOG.info("属性文件路径: {}", path);
            output = new FileOutputStream(path, true);//true表示追加打开
            prop.setProperty("ffmpegPath", ffmpegPath);
            prop.setProperty("sourcePath", sourcePath);
            prop.setProperty("targetPath", targetPath);
            prop.store(output, "");
        } catch (IOException e) {
            LOG.error("保存配置失败: {}", e.getMessage());
            MyAlertBox.display("保存配置提示", "保存配置信息失败!");
            return;
        } finally {
            try {
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除属性文件属性
     *
     * @param key
     */
    public static void delProp(String key) {
        LOG.info("删除属性: {}", key);
        prop.remove(key);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(path);
            prop.store(output, "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取属性文件properties
     */
    public static Map<String, String> readProp() {
        Map<String, String> config = new HashMap<>();
        InputStream in;
        try {
            in = new FileInputStream(path);
            prop.load(in);    ///加载属性列表
            Enumeration enum1 = prop.propertyNames();//得到配置文件的名字
            while (enum1.hasMoreElements()) {
                String key = (String) enum1.nextElement();
                String value = prop.getProperty(key);
                LOG.info(key + "=" + value);
                config.put(key, prop.getProperty(key));
            }
            in.close();
        } catch (Exception e) {
            MyAlertBox.display("读取文件提示", "读取properties文件失败!");
        }
        return config;
    }


    /**
     * 获得格式化后的当前时间
     *
     * @return
     */
    public static String getTimeStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        LocalDateTime createTime = LocalDateTime.now();
        return createTime.format(formatter);
    }


    /**
     * 获得视频文件后缀
     *
     * @param videoPath
     * @return
     */
    public static String getVideoSuffix(String videoPath) {
        LOG.info("获取后缀的文件地址: " + videoPath);
        String suffix = videoPath.substring(videoPath.lastIndexOf("."));
        return suffix;
    }


    /**
     * 功能描述 生成新的文件路径
     *
     * @param currentVideo : 原来文件路径
     * @return targetPath : 新的文件路径
     * @author xinhai.ma
     * @date 2020/5/9 14:33
     */
    public static String getNewFilePath(String currentVideo) {
        Map<String, String> config = Handler.readProp();
        String targetPath = null;
        if (null == currentVideo) {
            targetPath = config.get("targetPath") + "\\" + Handler.getTimeStr() + ".mp4";
        } else {
            targetPath = config.get("targetPath") + "\\" + Handler.getTimeStr() + Handler.getVideoSuffix(currentVideo);
        }
        return targetPath;
    }

    /**
     * 功能描述 根据resource文件夹下的文件名称返回路径
     *
     * @param fileName :
     * @return java.lang.String
     * @author xinhai.ma
     * @date 2020/5/10 20:58
     */
    public static String getResourcePath(String fileName) {
        URL url = Handler.class.getClassLoader().getResource(fileName);
        if (url == null) {
            new RuntimeException(fileName + "文件不存在");
        }
        File file = new File(url.getFile());
        return file.getAbsolutePath();
    }


    /**
     * 返回滤镜特效选项
     *
     * @return
     */
    public static List<String> getFilterList() {
        List<String> itemList = new ArrayList<>(9);
        itemList.add("镜像");
        itemList.add("复古风");
        itemList.add("多路拼接");
        itemList.add("Ps滤镜");
        itemList.add("锐化");
        itemList.add("黑白");
        itemList.add("浮雕效果");
        itemList.add("模糊处理");
        itemList.add("色彩变幻");
        return itemList;
    }


    /**
     * 根据文件路径获取文件名
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        int startIndex = filePath.lastIndexOf("\\");
        //int lastIndex = filePath.lastIndexOf(".");
        return filePath.substring(startIndex+1, filePath.length());
    }

    /**
     * 获取简洁路径
     *
     * @param path 文件路径
     * @return 简洁路径
     */
    public static String getSimplePath(String path) {
        int firstIndex = path.indexOf("\\");
        int lastIndex = path.lastIndexOf("\\");
        String begin = path.substring(0, firstIndex + 1);
        String end = path.substring(lastIndex, path.length());
        return begin + "..." + end;
    }


    /**
     * 获取简洁路径集合
     *
     * @param pathList 文件路径集合
     * @return 简洁路径集合
     */
    public static List<String> getSimplePathList(List<String> pathList) {
        List<String> simplePathList = new ArrayList<>(pathList.size());
        pathList.forEach(path -> {
            if (!path.equals("暂无数据")) {
                simplePathList.add(getSimplePath(path));
            }
        });
        return simplePathList;
    }


    /**
     * 将秒数转化为00:00:01格式的字符串
     *
     * @param second 秒数
     * @return
     */
    public static String formatTime(long second) {
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        StringBuffer buffer = new StringBuffer();
        if (hours < 10) {
            buffer.append("0" + hours);
        } else {
            buffer.append(hours);
        }
        buffer.append(":");
        if (minutes < 10) {
            buffer.append("0" + minutes);
        } else {
            buffer.append(minutes);
        }
        buffer.append(":");
        if (second < 10) {
            buffer.append("0" + second);
        } else {
            buffer.append(second);
        }
        return buffer.toString();
    }


    /**
     * 创建多重文件夹及文件
     *
     * @param folderPath 文件夹路径
     * @param filePath   文件名称及后缀
     */
    public static void createFile(String folderPath, String filePath) {
        File folder = new File(folderPath);
        File file = new File(folderPath + File.separator + filePath);
        if (!folder.isFile() && !folder.exists()) {
            folder.mkdirs();
        }
        if (!file.exists()) {
            try {
                boolean result = file.createNewFile();
                if (!result) {
                    LOG.info("创建文件失败! {}", folderPath + File.separator + filePath);
                } else {
                    LOG.info("创建文件成功! {}", folderPath + File.separator + filePath);
                }
            } catch (IOException e) {
                LOG.error("创建文件错误: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 删除结果
     */
    public static boolean deleteFile(String filePath) {
        boolean result = false;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            result = true;
        }
        return result;
    }


    /**
     * 复制文件
     *
     * @param sourcePath 要复制的文件路径
     * @param targetPath 复制到的路径
     */
    public static void copyFile(String sourcePath, String targetPath) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(sourcePath);
            fo = new FileOutputStream(targetPath);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 异步复制文件
     * @param sourceFolderPath 要复制的文件夹
     * @param targetFolderPath 目标文件夹
     */
    public static void asynBatchCopyFile(String sourceFolderPath, String targetFolderPath) throws RuntimeException{
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        File sourceFolder = new File(sourceFolderPath);
        File targetFolder = new File(targetFolderPath);
        if (!sourceFolder.exists()) {
            new RuntimeException(sourceFolderPath + "路径不存在!");
        }
        if (!targetFolder.exists()) {
            new RuntimeException(targetFolderPath + "路径不存在!");
        }
        if (sourceFolder.isFile()) {
            new RuntimeException(sourceFolderPath + "不是文件夹!");
        }
        if (targetFolder.isFile()) {
            new RuntimeException(targetFolderPath + "不是文件夹!");
        }

        ExecutorService service = MyExecutorService.getTaskExecutor();
        Integer flag = 0;
        File[] files = sourceFolder.listFiles();
        try {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    String source = files[i].getAbsolutePath();
                    String target = targetFolderPath + File.separator + files[i].getName();
                    CopyFileTask task = new CopyFileTask(source, target, flag);
                    service.submit(task);
                    if (task.call() == files.length) {
                        service.shutdown();
                        endTime = System.currentTimeMillis();
                        LOG.info("耗时: {}", endTime-startTime);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
            endTime = System.currentTimeMillis();
            LOG.info("耗时1: {}", endTime-startTime);
        }

    }


    /**
     * 同步复制文件
     * @param sourceFolderPath 要复制的文件夹
     * @param targetFolderPath 目标文件夹
     */
    public static void batchCopyFile(String sourceFolderPath, String targetFolderPath) throws RuntimeException{
        long startTime = System.currentTimeMillis();
        File sourceFolder = new File(sourceFolderPath);
        File targetFolder = new File(targetFolderPath);
        if (!sourceFolder.exists()) {
            new RuntimeException(sourceFolderPath + "路径不存在!");
        }
        if (!targetFolder.exists()) {
            new RuntimeException(targetFolderPath + "路径不存在!");
        }
        if (sourceFolder.isFile()) {
            new RuntimeException(sourceFolderPath + "不是文件夹!");
        }
        if (targetFolder.isFile()) {
            new RuntimeException(targetFolderPath + "不是文件夹!");
        }
        File[] files = sourceFolder.listFiles();
        for(int i=0; i<files.length; i++) {
            String source = files[i].getAbsolutePath();
            String target = targetFolderPath + File.separator + files[i].getName();
            copyFile(source, target);
        }
        long endTime = System.currentTimeMillis();
        LOG.info("耗时: {}", endTime-startTime);
    }

    /**
     * 同步复制文件
     * @param filePathList     文件地址集合
     * @param targetFolderPath 目标文件夹
     */
    public static List<String> batchCopyFile(List<String> filePathList, String targetFolderPath) {
        long startTime = System.currentTimeMillis();
        List<String> targetPathList = new ArrayList<>(filePathList.size());
        File targetFolder = new File(targetFolderPath);
        if (!targetFolder.exists()) {
            new RuntimeException(targetFolderPath + "路径不存在!");
        }
        if (targetFolder.isFile()) {
            new RuntimeException(targetFolderPath + "不是文件夹!");
        }
        filePathList.forEach(path -> {
            String target = targetFolderPath + File.separator + getFileName(path);
            copyFile(path, target);
            targetPathList.add(target);
        });
        long endTime = System.currentTimeMillis();
        LOG.info("耗时: {}", endTime-startTime);
        return targetPathList;
    }


    /**
     * 根据httpUrl下载网络文件
     * @param httpUrl
     * @param saveFile
     * @return
     */
    public static boolean httpDownload(String httpUrl, String saveFile) {
        // 1.下载网络文件
        int byteRead;
        URL url;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        }

        try {
            //2.获取链接
            URLConnection conn = url.openConnection();
            //3.输入流
            InputStream inStream = conn.getInputStream();
            //3.写入文件
            FileOutputStream fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1024];
            while ((byteRead = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteRead);
            }
            inStream.close();
            fs.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 生成坐标图(未使用)
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static Map<String, Map<Integer, Integer>> GenerateCoordinateFig(int x, int y, int width, int height) {
        Map<String, Map<Integer, Integer>> result = new LinkedHashMap<>(2);
        Map<Integer, Integer> head = new LinkedHashMap<>(1);
        Map<Integer, Integer> tail = new LinkedHashMap<>(1);
        head.put(x, y);
        tail.put(width, height);
        result.put("head", head);
        result.put("tail", tail);
        return result;
    }


    /**
     * 从坐标图中获取相对坐标(未使用)
     * @param map
     * @param x
     * @param y
     * @return
     */
    public static int[] getRelativeCoordinates(Map<String, Map<Integer, Integer>> map, int x, int y) {
        Map<Integer, Integer> headMap = map.get("head");
        Map<Integer, Integer> tailMap = map.get("tail");
        Map.Entry<Integer, Integer> head = headMap.entrySet().iterator().next();
        Map.Entry<Integer, Integer> tail = tailMap.entrySet().iterator().next();
        int row = head.getKey() + tail.getKey(); //9
        int column = head.getValue() + tail.getValue(); //10
        int[] result = new int[2];
        result[0] = row - x;
        result[1] = column - y;
        return result;
    }


    /**
     * 判断String是否为数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 校验剪切视频参数是否小于视频长度
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param filePath  视频地址
     * @throws RuntimeException
     */
    public static boolean checkCutTime(String startTime, String endTime, String filePath) throws RuntimeException {
        VideoExecutor videoExecutor = new VideoExecutor();
        MultimediaInfo videoInfo = videoExecutor.getVideoInfo(filePath);
        long videoTime = videoInfo.getDuration();
        LOG.info("视频: {}, 视频时间长度: {}", filePath, videoTime);
        Long startValue = Long.valueOf(startTime) * 1000;
        if(startValue > videoTime) {
            MyAlertBox.display("提示", "开始时间超过视频长度!");
            return true;
        }
        Long endValue = Long.valueOf(endTime) * 1000;
        if(endValue > videoTime) {
            MyAlertBox.display("提示", "结束时间超过视频长度!");
            return true;
        }
        LOG.info("开始时间: {}, 结束时间: {}, 结果: {}", startValue, endValue, (startValue + endValue) > videoTime);
        if((startValue + endValue) >= videoTime) {
            MyAlertBox.display("提示", "开始时间和结束时间大于视频长度!");
            return true;
        }
        return false;
    }


    /**
     * 获取屏幕宽高
     */
    public static Map<String, Integer> getScreenSize() {
        // TODO Auto-generated constructor stub
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        LOG.info("屏幕宽度：" + screenWidth + "，屏幕高度：" + screenHeight);
        Map<String, Integer> result= new HashMap<>();
        result.put("width", screenWidth);
        result.put("height", screenHeight);
        return result;
    }


    /**
     * 调用第三方播放器播放视频
     * @param targetPath
     * @throws IOException
     */
    public static void transferOtherPlayer(String targetPath) {
        Map<String, String> config = readProp();
        if(EmptyUtils.isEmpty(config.get("ffmpegPath"))) {
            MyAlertBox.display("提示", "未配置第三方视频播放器!");
            return;
        }
        String ffmpegPath = config.get("ffmpegPath");;
        try {
            //通过cmd命令点用第三方播放器播放视频
            Runtime.getRuntime().exec(ffmpegPath + " " + targetPath);
        } catch (IOException e) {
            MyAlertBox.display("提示", "调用第三方播放器失败!");
            e.printStackTrace();
        }
    }


    /**
     * 描述：复制文件夹到指定目录
     * @param sourcePath
     * @return
     */
    public static String copyFile(String sourcePath) {
        Map<String, String> config = Handler.readProp();
        String configPath = config.get("targetPath");
        String targetPath = configPath + File.separator + getFileName(sourcePath);
        copyFile(sourcePath, targetPath);
        return targetPath;
    }

}
