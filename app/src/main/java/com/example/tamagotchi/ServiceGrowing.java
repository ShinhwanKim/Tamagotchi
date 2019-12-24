package com.example.tamagotchi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/*이 클래스는 자동 성장 작업을 하는 service클래스다. 내 펫의 성장 데이터들을 조건에 맞게 증감 시켜준다.
  현재 ActivityMain과 Broadcast 로 데이터를 공유하고 있다.
 */
public class ServiceGrowing extends Service {
    private String TAG = "ServiceGrowing";
    int hunger ;
    int stamina;
    int health;
    int emotion;
    ThreadHunger threadHunger;
    ThreadStamina threadStamina;
    ThreadSleep threadSleep;
    ThreadTime threadTime;
    public ServiceGrowing() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setLog("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setLog("onStartCommand");
        //BroadcastReceiver 등록
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.service_broadcast");
        registerReceiver(service_broadcast,filter);
        threadTime = new ThreadTime(mHandler);
        threadTime.start();
        return START_REDELIVER_INTENT;
    }
    BroadcastReceiver service_broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setLog("메세지 받음");
            //기본 배고픔 감소 쓰레드 생성 및 실행
            if(intent.getIntExtra("hunger",-1)!=-1){
                threadHunger= new ThreadHunger(mHandler,intent.getIntExtra("hunger",0));
                threadHunger.start();
            }
            //ActivityMain에서 먹이를 먹으면 이미 실행중이던 기본 배고픔 감소 쓰레드 중지 및 먹이 먹은 후 배고픔
            //데이터 받아와서 배고픔 감소 쓰레드 생성 및 재실행.
            if (intent.getIntExtra("hungerEat",-1)!=-1){
                threadHunger.interrupt();
                threadHunger= new ThreadHunger(mHandler,intent.getIntExtra("hungerEat",0));
                threadHunger.start();
            }
            //기본 체력 감소 쓰레드 생성 및 실행
            if(intent.getIntExtra("stamina",-1)!=-1){
                threadStamina = new ThreadStamina(mHandler,intent.getIntExtra("stamina",0));
                threadStamina.start();
            }
            //기본 잠자고 있을 때 체력 회복 쓰레드 생성 및 실행
            if(intent.getIntExtra("stamina_sleeping",-1)!=-1){
                threadSleep = new ThreadSleep(mHandler,intent.getIntExtra("sleep",0));
                threadSleep.start();
            }
            //깨어 있는 펫을 재울 때. 기본 체력 감소 쓰레드 중지 및 기본 잠자는 쓰레드 생성 및 실행
            if(intent.getIntExtra("sleep",-1)!=-1){
                threadStamina.interrupt();
                threadSleep = new ThreadSleep(mHandler,intent.getIntExtra("sleep",0));
                threadSleep.start();
            }
            //자고 있는 펫을 깨울 때. 기본 체력 회복 쓰레드 중지 및 기본 체력 감소 쓰레드 생성 및 실행
            if(intent.getIntExtra("wakeUp",-1)!=-1){
                threadSleep.interrupt();
                threadStamina = new ThreadStamina(mHandler,intent.getIntExtra("wakeUp",0));
                threadStamina.start();
            }

        }
    };
    class ThreadHunger extends Thread{//배고픔 자동 감소 쓰레드
        int ddongEach=0;
        Handler handler;
        Boolean hungerNotify ;


        public ThreadHunger(Handler handler,int getHunger){
            this.handler = handler;
            hunger = getHunger;
        }

        @Override
        public void run() {
            hungerNotify = false;
            while(true){
                /*setLog("1작동중 "+hunger );*/
                Message msg = handler.obtainMessage();
                msg.what = 0;
                msg.obj = hunger;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    setLog("멈춰랏 ");
                    break;
                }

                if (hunger==70){
                    ddongEach+=1;
                }else if(hunger==40){
                    ddongEach+=1;
                }else if (hunger==10){
                    ddongEach+=1;
                    if(ddongEach>4){
                        ddongEach=3;
                    }
                }
                SharedPreferences SPDdong= getSharedPreferences("Ddong",0);
                SharedPreferences.Editor editor2 = SPDdong.edit();
                editor2.putInt("Ddong",ddongEach);
                editor2.commit();

                hunger -=10;
                if (hunger<0){
                    hunger=0;
                }

                if (hunger == 0 && hungerNotify==false) {


                PendingIntent intent;

                intent = PendingIntent.getActivity(ServiceGrowing.this,0,
                        new Intent(getApplicationContext(),ActivityMain.class),PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel = new NotificationChannel("channel_id","channel_name",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(notificationChannel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceGrowing.this,
                        notificationChannel.getId());
                builder.setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("파이리가 배고프데요!!")
                        .setContentText("밥주세요!!")
                        .setTicker("아아아아")
                        .setContentIntent(intent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setFullScreenIntent(intent,true);


                setLog("ShowerClick");


                notificationManager.notify(1,builder.build());
                    hungerNotify = true;
                }
            }
        }
    }
    class ThreadStamina extends Thread{//체력 감소 쓰레드
        Handler handler;
        Boolean staminaNotify ;


        public ThreadStamina(Handler handler,int getStamina){
            this.handler = handler;
            stamina = getStamina;
        }

        @Override
        public void run() {
            super.run();
            staminaNotify = false;
            while(true){
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = stamina;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    setLog("멈춰랏 ");
                    break;
                }

                stamina -=5;
                if (stamina<0){
                    stamina=0;
                }
                if (stamina == 0 && staminaNotify==false) {


                    PendingIntent intent;

                    intent = PendingIntent.getActivity(ServiceGrowing.this,0,new Intent(getApplicationContext(),ActivityMain.class),PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel notificationChannel = new NotificationChannel("channel_id","channel_name",NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(notificationChannel);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceGrowing.this,notificationChannel.getId());
                    builder.setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("파이리가 피곤하데요!!")
                            .setContentText("재워주세요!!")
                            .setTicker("아아아아")
                            .setContentIntent(intent)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setFullScreenIntent(intent,true);

                    notificationManager.notify(1,builder.build());
                    staminaNotify = true;
                }
            }

        }
    }
    class ThreadSleep extends Thread{//체력 회복, 잠자기 쓰레드
        Handler handler;

        public ThreadSleep(Handler handler,int getStamina){
            this.handler = handler;
            stamina = getStamina;
        }

        @Override
        public void run() {
            super.run();
            while (true){
                Message msg = handler.obtainMessage();
                msg.what = 2;
                msg.obj = stamina;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    setLog("멈춰랏 ");
                    break;
                }
                stamina +=5;
                if (stamina>100){
                    stamina=100;
                }
            }
        }
    }
    class ThreadTime extends Thread{
        Handler handler;
        int time=1;

        public ThreadTime(Handler handler){
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            while (true){
                Message msg = handler.obtainMessage();
                msg.what=3;
                msg.obj = time;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(100);
                    time++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            intent.setAction("com.example.broadcast");
            switch (msg.what){
                case 0:
                    intent.putExtra("hunger",(int)msg.obj);
                    break;
                case 1:
                    intent.putExtra("stamina",(int)msg.obj);
                    break;
                case 2:
                    intent.putExtra("sleep",(int)msg.obj);
                    break;
                case 3:
                    intent.putExtra("time",(int)msg.obj);
                    break;
            }
            sendBroadcast(intent);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        setLog("onDestroy");
        threadHunger.interrupt();
        threadStamina.interrupt();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //로그생성용 메소드
    public void setLog(String content){
        Log.i(TAG,content);
    }
}
