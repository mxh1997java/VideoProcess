package util;

import components.MyAlertBox;
import components.MyListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author xinhai.ma
 * @description
 * @date 2020/5/9 9:11
 */
public class Handler {

    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);

    /**
     * 程序可处理视频类型
     */
    private static Set<String> fileTypeSet = new HashSet<>();

    /**
     * 存储页面数据
     */
    private static Map<String, List<String>> dataListMap = new HashMap<>();

    /**
     * 存储MyListView对象
     */
    private static Map<String, MyListView> listViewMap = new HashMap<>();

    /**
     * 存储视频播放画面比例
     */
    private static BigDecimal scale = null;

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
     * 根据文本存放图片地址
     * @param drawStr
     * @param imagePath
     */
    public static void put(String drawStr, String imagePath) {
        watermarkImagePathMap.put(drawStr, imagePath);
    }

    /**
     * 根据文本获取图片地址
     * @param drawStr
     * @return
     */
    public static String getPath(String drawStr) {
        return watermarkImagePathMap.get(drawStr);
    }

    /**
     * 根据key存放集合
     * @param key
     * @param dataList
     */
    public static void put(String key, List<String> dataList) {
        dataListMap.put(key, dataList);
    }

    /**
     * 根据key获取集合
     * @param key
     * @return
     */
    public static List<String> getList(String key) {
        return dataListMap.get(key);
    }

    /**
     * 根据key获取ListView
     * @param key
     * @return
     */
    public static MyListView getListView(String key) {
        LOG.info("根据key获取ListView: {} => {}", key, listViewMap.get(key));
        return listViewMap.get(key);
    }

    /**
     * 根据key存放ListView
     * @param key
     * @param listView
     */
    public static void put(String key, MyListView listView) {
        listViewMap.put(key, listView);
    }

    /**
     * 设置当前比例
     * @param value
     */
    public static void setScale(BigDecimal value){
        LOG.info("比例: {}", value.doubleValue());
        scale = value;
    }

    /**
     * 获取当前比例
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
        if(null == currentVideo) {
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
        if(url == null) {
            System.out.println(url);
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
     * 获取简洁路径
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
     * @param folderPath  文件夹路径
     * @param filePath  文件名称及后缀
     */
    public static void createFile(String folderPath, String filePath) {
        File folder = new File(folderPath);
        File file = new File(folderPath + File.separator + filePath);
        if(!folder.isFile() && !folder.exists()) {
            folder.mkdirs();
        }
        if(!file.exists()) {
            try {
                boolean result = file.createNewFile();
                if(!result){
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
     * @param filePath 文件路径
     * @return 删除结果
     */
    public static boolean deleteFile(String filePath) {
        boolean result = false;
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
            result = true;
        }
        return result;
    }

}
