package com.example.tamagotchi;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class ServiceBgm extends Service {
    private String TAG = "ServiceBgm";
    public static String MESSEAGE_KEY = "메세지";
    MediaPlayer bgmPlayer;
    public ServiceBgm() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setLog("onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setLog("onStartCommand");
        boolean message = intent.getExtras().getBoolean(MESSEAGE_KEY);
        if (message){

            bgmPlayer = MediaPlayer.create(this,R.raw.mainbgm);
            bgmPlayer.setLooping(true);
            bgmPlayer.start();
        }else {
            bgmPlayer.stop();
            /*bgmPlayer.release();*/
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        bgmPlayer.stop();
        bgmPlayer.release();
        setLog("onDestroy");
        super.onDestroy();
    }
    //로그생성용 메소드
    public void setLog(String content){
        Log.i(TAG,content);
    }

}
