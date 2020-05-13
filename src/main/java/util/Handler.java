package util;

import components.MyAlertBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    private static Set<String> fileTypeSet = new HashSet<>();

    static {
        fileTypeSet.add("mp4");
        fileTypeSet.add("flv");
        fileTypeSet.add("avi");
        fileTypeSet.add("rm");
        fileTypeSet.add("rmvb");
        fileTypeSet.add("wmv");
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

    //属性文件路径
    private static String path = getResourcePath("config.properties");
    private static Properties prop = new Properties();

    /**
     * 保存配置文件
     *
     * @param ffmpegPath
     * @param sourcePath
     * @param targetPath
     */
    public static void saveProp(String ffmpegPath, String sourcePath, String targetPath) {
        InputStream is = Handler.class.getClassLoader().getResourceAsStream("config.properties");
        FileOutputStream oFile = null;
        LOG.info("获取属性文件对象: {}", is);
        try {
            prop.load(is);
            delProp("ffmpegPath");
            delProp("sourcePath");
            delProp("targetPath");
            ///保存属性到b.properties文件
            LOG.info("属性文件路径: {}", path);
            oFile = new FileOutputStream(path, true);//true表示追加打开
            prop.setProperty("ffmpegPath", ffmpegPath);
            prop.setProperty("sourcePath", sourcePath);
            prop.setProperty("targetPath", targetPath);
            prop.store(oFile, "");
        } catch (IOException e) {
            LOG.error("保存配置失败: {}", e.getMessage());
            MyAlertBox.display("保存配置提示", "保存配置信息失败!");
            return;
        } finally {
            try {
                oFile.close();
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
        FileOutputStream oFile = null;
        try {
            oFile = new FileOutputStream(path);
            prop.store(oFile, "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                oFile.close();
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
            in = Handler.class.getClassLoader().getResourceAsStream("config.properties");
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
        String targetPath = config.get("targetPath") + "\\" + Handler.getTimeStr() + Handler.getVideoSuffix(currentVideo);
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
     *
     * @param path
     * @return
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
     * @param pathList
     * @return
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

}
