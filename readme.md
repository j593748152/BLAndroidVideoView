本播放器依赖于 ijk视频播放器 : https://github.com/Bilibili/ijkplayer  开发实现
 BLVideoView ：视频播放界面，是一个 SurfaceView 合并 IJKMediaPlayer；
 BLMediaController：视频播放控制器，是一个 ConstraintLayout；由于有大概率自定义样式，建议自写；或者根据源码，自行继承修改布局
 
 
 
 基本使用
 1) BLVideoView 
 设置控制器，主要为播放状态改变对控制器的回调
 mBLVideoView.setMediaController(IMediaController);   
 
 2) BLMediaController
 设置回调，主要为该视图的点击事件
  mMediaController.setMediaAction(IMediaAction);
   
         
 

