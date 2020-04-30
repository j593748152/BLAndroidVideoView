# 基础配置

###添加远程仓库
<pre>
allprojects {
    repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
</pre>
###依赖
<pre>
dependencies {
implementation'com.github.BailunMobileDev:BLAndroidVideoView:v0.1.1'
}
</pre>

### 视频播放so库配置
<pre>
    sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
    }
</pre>
<pre>
    implementation fileTree(dir: 'libs', include: ['*.so'])
</pre>

自行复制so文件到对应位置，并进行对应配置

### 引用so

    static {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

# 简单介绍
分为两部分：视频播放，视频控制
###视频播放器[BLVideoView](./video/src/main/java/com/bailun/video/BLVideoView.java)
实现 IVideoView；
会根据当前视频自动计算宽高，尽量保证视频宽高比
###视频控制器[BLMediaController](./video/src/main/java/com/bailun/video/BLMediaController.java)
实现 IMediaController；
可完全继承，进行自定义修改图标、显示隐藏等
###控制器点击
IMediaControllerAction
开发者Activity实现，响应控制器点击等事件

##接口相关见doc
[doc](./播放接口介绍.doc)

