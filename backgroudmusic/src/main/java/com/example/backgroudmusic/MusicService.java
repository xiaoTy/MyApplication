package com.example.backgroudmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by Xuhaitao on 2017/2/12.
 */

public class MusicService extends Service {
    private MediaPlayer mediaPlayer = null;
    private boolean isReady =false;
  public  void  onCreate()
  {
      //OnCreate在Service的生命周期只会调用一次
      super.onCreate();
      //初始化媒体播放器
      mediaPlayer=MediaPlayer.create(this,R.raw.music);
      if (mediaPlayer==null)
      {
          return;
      }
      mediaPlayer.stop();
      mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
          public boolean onError(MediaPlayer mediaPlayer,int what, int extra)
          {
              mediaPlayer.release();
              stopSelf();
              return  false;
          }

      });
      try {
          mediaPlayer.prepare();
          isReady =true;
      } catch (IOException e) {
          e.printStackTrace();
          isReady =false;
      }
     if (isReady)
     {
         //将背景音乐设置循环播放
         mediaPlayer.setLooping(true);
     }
  }
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        //每次调用Context的startService都会触发onStartCommand回调方法
        //所以OnStartCommand 在Servide的生命周期中可能会被调用多次
        if(isReady && !mediaPlayer.isPlaying())
        {
            //播放背景音乐
            mediaPlayer.start();
            Toast.makeText(this,"开始播放背景音乐",Toast.LENGTH_LONG).show();
        }
        return  START_STICKY;
    }
    public IBinder onBind(Intent intent)
    {//该Service中不支持bindService方法，所以此处直接返回null
        return  null;
    }
    public void onDestroy()
    {//当调用Context的stopService内部执行stopSelf方法就会触发onDestory
        super.onDestroy();
        if (mediaPlayer!=null)
        {
            if (mediaPlayer.isPlaying())
            {
                //停止播放音乐
                mediaPlayer.stop();
            }
            //释放媒体播放器资源
            mediaPlayer.release();
            Toast.makeText(this,"停止播放背景音乐",Toast.LENGTH_LONG).show();
        }

    }

}
