package components;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * @author xinhai.ma
 * @description  文件选择器
 * @date 2020/5/9 9:04
 */
public class MyChooser {

    public static DirectoryChooser getDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser;
    }

    /**
     * 功能描述 返回视频选择器
     * @author xinhai.ma
     * @date 2020/5/9 22:27
     * @return javafx.stage.FileChooser
     */
    public static FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        //在文件选择器做了格式限制
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("MP4", "*.mp4"));
                /*new ExtensionFilter("AVI", "*.avi"),*/
                /*new ExtensionFilter("RM", "*.rm"),*/
                /*new ExtensionFilter("WMV", ".wmv"),*/
                /*new ExtensionFilter("FLV", ".flv"),*/
                /*new ExtensionFilter("MPEG", ".mpeg")*/
        return fileChooser;
    }

    /**
     * 功能描述 返回exe选择器
     * @author xinhai.ma
     * @date 2020/5/9 22:27
     * @return javafx.stage.FileChooser
     */
    public static FileChooser getExeFileChooser() {
        FileChooser fileChooser = new FileChooser();
        //在文件选择器做了格式限制
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("EXE", "*.exe"));
        return fileChooser;
    }

    /**
     * 功能描述 返回图片选择器
     * @author xinhai.ma
     * @date 2020/5/9 22:27
     * @return javafx.stage.FileChooser
     */
    public static FileChooser getImageChooser() {
        FileChooser fileChooser = new FileChooser();
        //在文件选择器做了格式限制
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("JPEG", "*.jpg"),
                new ExtensionFilter("PNG", "*.png"),
                new ExtensionFilter("GIF", "*.gif"),
                new ExtensionFilter("BMP", "*.bmp"));
        return fileChooser;
    }

}
