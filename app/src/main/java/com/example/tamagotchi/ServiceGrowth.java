package com.example.tamagotchi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ServiceGrowth extends Service {
    private String TAG = "ServiceGrowth";
    public static String MESSEAGE_KEY = "메세지";
    /*IBinder binder = new BinderGrowth();*/

    /*class BinderGrowth extends Binder{
        ServiceGrowth getService(){
            return ServiceGrowth.this;
        }
    }*/

    public ServiceGrowth() {
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
        /*setLog("onBind");
        try {
            while (true){
                Thread.sleep(1000);
                setLog("진행중.....");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        /*return binder;*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setLog("onStartCommand");
        boolean message = intent.getExtras().getBoolean(MESSEAGE_KEY);

        try {
            while (message){
                Thread.sleep(1000);
                setLog("진행중.....");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setLog("onDestroy");
    }

    //로그생성용 메소드
    public void setLog(String content){
        Log.i(TAG,content);
    }
}
