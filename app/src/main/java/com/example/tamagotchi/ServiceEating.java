package com.example.tamagotchi;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class ServiceEating extends Service {
    private String TAG = "ServiceEating";
    MediaPlayer eatingPlayer;
    public static String MESSEAGE_KEY = "메세지";
    public ServiceEating() {
    }
    @Override
    public void onCreate() {
        setLog("onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setLog("onStartCommand");
        boolean message = intent.getExtras().getBoolean(MESSEAGE_KEY);
        if(message){
            eatingPlayer = MediaPlayer.create(this,R.raw.eating);
            eatingPlayer.setLooping(false);
            eatingPlayer.start();
        }else{
            eatingPlayer.stop();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        setLog("onDestroy");
        eatingPlayer.stop();
        eatingPlayer.release();
        super.onDestroy();
    }
    //로그생성용 메소드
    public void setLog(String content){
        Log.i(TAG,content);
    }
}
