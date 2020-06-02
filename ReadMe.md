根据ffmpeg封装的GUI程序
======================

项目介绍
--------

        1. 参考Jave2框架，获取里面的DefaultFFMPEGLocator，实现封装ffmpeg并执行ffmpeg命令执行的功能。
        2. Jave框架只是封装了ffmpeg，并实现了视频转码、添加水印功能。
        3. 这个项目在此基础上封装了如下功能：
            视频加速、视频减速
            添加、去除水印
            剪切视频
            多种滤镜效果
            截取视频某一帧内容为图片
            设置视频封面
            多张图片合成视频
            视频倒放
            重置视频宽高
            调整视频音量大小
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
        
ffmpeg 常用命令
---------------
        视频操作:
        	视频加速
        	ffmpeg -i input.mp4 -vf "setpts=0.5*PTS" output.mp4
        	视频减速
        	ffmpeg -i input.mp4 -vf "setpts=2.0*PTS" output.mp4
        
        	音频加速
        	"atempo"滤镜对音频速度调整限制在0.5 到 2.0 之间，（即半速或倍速）
        	2倍速
        	ffmpeg -i input.mp4 -af "atempo=2.0" output.mp4
        	4倍速
        	ffmpeg -i input.mp4 -af "atempo=2.0,atempo=2.0" output.mp4
        
        	使用更复杂的滤镜图，可以同时加速视频和音频：
        	ffmpeg -i input.mp4 -filter_complex "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]" -map "[v]" -map "[a]" output.mp4
        	
        	
        	ffmpeg实现视频高斯模糊拓边效果--视频背景虚化
        	ffmpeg -i input.mp4 -vf “split[a][b];[a]scale=1080:1920,boxblur=10:5[1];[b]scale=1080:ih*1080/iw[2];[1][2]overlay=0:(H-h)/2” -c:v libx264 -crf 18 -preset veryfast -aspect 9:16 -f mp4 output.mp4 -y
        	
        	添加文字水印
        	ffmpeg -i input.mp4 -vf "drawtext=fontfile=/usr/share/fonts/truetype/freefont/FreeSerif.ttf:text='testtest':x=w-100:y=100:enable=lt(mod(t\,3)\,1):fontsize=24:fontcolor=yellow@0.5:shadowy=2" output.mp4
        	
        	消除水印
        	ffmpeg -i input.mp4 -filter_complex "delogo=x=460:y=1170:w=250:h=100:show=0" output.mp4
        	ffmpeg -i input.mp4 -vf delogo=x=170:y=190:w=560:h=90:show=0 output.mp4
        	
        	镜像
        	ffmpeg -i input.flv -vf crop=iw/2:ih:0:0,split[left][tmp];[tmp]hflip[right];[left]pad=iw*2[a];[a][right]overlay=w output.flv
        	
        	视频倒放
        	ffmpeg -i input.mp4 -vf reverse -y reverse.mp4
        	
        	//音量翻倍，写在滤镜里，例如
            ffmpeg -i 1.wav -af volume=2 -y 2.wav

            //音量翻倍，不写在滤镜中，例如
            ffmpeg -i 1.wav -vol 2000 -y 2.wav
     
技术链接
--------
        Java Fx: http://www.javafxchina.net/main/   
        FFmpeg: https://ffmpeg.org/ffmpeg.html#Complex-filtergraphs
        jave1.0: http://www.sauronsoftware.it/projects/jave/download.php
        jave2.0: https://github.com/a-schild/jave2
        