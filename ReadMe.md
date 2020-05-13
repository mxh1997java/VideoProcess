根据ffmpeg封装的GUI程序
======================

项目介绍
--------

        1. 参考Jave2框架，获取里面的DefaultFFMPEGLocator，实现封装ffmpeg并执行ffmpeg命令执行的功能。
        2. Jave框架只是封装了ffmpeg，并实现了视频转码、添加水印功能。
        3. 这个项目在此基础上封装了如下功能：
            视频加速、减速
            添加、去除水印
            剪切视频
            多种滤镜效果
            截取视频某一帧内容为图片
            设置视频封面
            多张图片合成视频
        4. 项目所用技术: Java Fx、 FFmpeg

项目结构
--------
        components    Java Fx组件
        executor      视频处理 图片处理
        processor     封装DefaultFFMPEGLocator
        task          异步任务
        test          测试代码
        util          工具
        view          程序页面
        MyApplication 程序启动
        
Java Fx组件
-----------
        AlertBox    弹窗
        ChoiceBox   下拉框
        Chooser     文件选择器
        MediaPlayer 视频播放器
        MenuBar     菜单栏
        VideoList   列表
        