package com.example.tamagotchi;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*이 액티비티는 메인 액티비티이다. 파이리 키우기 대부분의 play를 이 액티비티에서 한다.
  화면 상단에 있는 4가지 데이터 (좌측부터) 배고픔, 체력, 건강, 감정는 성장 데이터이다.
  화면 하단에 있는 6가지 이미지 뷰는 내 펫에게 할 수 있는 행동 6가지다.
  (좌측부터) 먹이주기, 재우기, 씻기기, 약먹이기, 놀아주기, 성장일지 보기 로 이루어 져 있다.
  ServiceGrowing과 계속해서 Broadcast로 통신하며 데이터를 공유한다.
*/
public class ActivityMain extends AppCompatActivity {
    private String TAG = "ActivityMain";
    public static ImageView imgPet;
    private ImageView imgFood;
    private ImageView imgSleep;
    private ImageView imgShower;
    private ImageView imgHospital;
    private ImageView imgPlay;
    private ImageView imgList;
    public static ImageView imgFire;
    public static ImageView imgDdong1;
    public static ImageView imgDdong2;
    public static ImageView imgDdong3;
    public static ImageView imgWater;
    private ImageView imgSleeping;
    private ImageView imgBackground;
    private ImageView imgEating;
    private ProgressBar prbarHunger;
    private ProgressBar prbarStamina;
    private ProgressBar prbarHealth;
    private ProgressBar prbarEmotion;

    private ConstraintLayout consLayout;
    public static TextView txtGold;
    public static TextView txtGetGold;
    public static TextView txtLoseGold;
    public static CustomTextView txtWeather;
    private AdapterFood foodAdapter;

    private Intent broadIntent;
    private Intent eatingIntent;

    public static ktClassPet myPet;
    public ArrayList<ClassFood> foodList;
    public ClassFood foodDry;
    public ClassFood foodCan;
    public ClassFood foodMeat;
    public ClassFood mediPill;
    public ClassFood mediKorean;
    public ClassFood mediRinger;

    public Boolean AutoGrow;
    public Boolean sleepIs;
    public static Boolean showerState = false;

    private int FOODDRY_PRICE = 50;
    private int FOODCAN_PRICE = 90;
    private int FOODMEAT_PRICE = 170;
    private int MEDIPILL_PRICE = 100;
    private int MEDIKOREAN_PRICE = 190;
    private int MEDIRINGER_PRICE = 350;
    public static int PLAY_GAME_ROCK = 1000;
    public static int PLAY_GAME_PUNCH = 2000;
    private int ddongEach=0;
    TaskBackground taskBackground;
    TaskFood taskFood;
    TaskGoldManage taskGoldManage;
    private SharedPreferences SPmyPetData;
    private SharedPreferences.Editor edPetData;

    static Handler weatherHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLog("onCreate");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //게임 옵션 데이터 호출하여 ActivityMain에 반영
        SharedPreferences SPoptionData = getSharedPreferences("OptionData",0);
        String strOptionData = SPoptionData.getString("OptionData","EMPTY");

        /*Intent intent2 = new Intent(ActivityMain.this, ServiceGrowth.class);
        intent2.putExtra(ServiceBgm.MESSEAGE_KEY,false);
        startService(intent2);*/

        JSONObject JOpetOptionData = null;
        try {
            JOpetOptionData = new JSONObject(strOptionData);
            Boolean soundState = JOpetOptionData.getBoolean("SoundState");
            if(soundState){//배경음 Boolean값이 true 이므로 배경음 재생
                Intent intent = new Intent(ActivityMain.this, ServiceBgm.class);
                intent.putExtra(ServiceBgm.MESSEAGE_KEY,true);
                startService(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eatingIntent = new Intent(ActivityMain.this, ServiceEating.class);
        eatingIntent.putExtra(ServiceEating.MESSEAGE_KEY,true);

        //
       /* isService=false;
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServiceGrowth.BinderGrowth mb = (ServiceGrowth.BinderGrowth) service;
                ms = mb.getService(); // 서비스가 제공하는 메소드 호출하여
                                    // 서비스쪽 객체를 전달받을수 있슴
                isService = true;

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isService = false;
            }
        };*/

        /*Intent intentGrowth = new Intent(ActivityMain.this,ServiceGrowth.class);
        bindService(intentGrowth,conn,Context.BIND_AUTO_CREATE);*/

        myPet = new ktClassPet();
        foodList = new ArrayList<>();

        foodDry = new ClassFood();
        foodDry.setHunger(5);
        foodDry.setName("포켓몬을 위한 마른 사료");
        foodDry.setPrice(FOODDRY_PRICE);
        foodDry.setEffect("배고픔+5");
        foodDry.setFigure(Uri.parse("android.resource://com.example.tamagotchi/drawable/food1"));

        foodCan = new ClassFood();
        foodCan.setHunger(10);
        foodCan.setName("아주 달콤한 과일 통조림");
        foodCan.setPrice(FOODCAN_PRICE);
        foodCan.setEffect("배고픔+10");
        foodCan.setFigure(Uri.parse("android.resource://com.example.tamagotchi/drawable/food2"));

        foodMeat = new ClassFood();
        foodMeat.setHunger(100);
        foodMeat.setName("육즙이 가득한 고기구이");
        foodMeat.setPrice(FOODMEAT_PRICE);
        foodMeat.setEffect("배고픔+20");
        foodMeat.setFigure(Uri.parse("android.resource://com.example.tamagotchi/drawable/food3"));

        mediPill = new ClassFood();
        mediPill.setHealth(5);
        mediPill.setName("포켓몬을 위한 종합비타민");
        mediPill.setPrice(MEDIPILL_PRICE);
        mediPill.setEffect("건강+5");
        mediPill.setFigure(Uri.parse("android.resource://com.example.tamagotchi/drawable/medicine1"));

        mediKorean = new ClassFood();
        mediKorean.setHealth(10);
        mediKorean.setName("24시간 정성들여 달인 한약");
        mediKorean.setPrice(MEDIKOREAN_PRICE);
        mediKorean.setEffect("건강+10");
        mediKorean.setFigure(Uri.parse("android.resource://com.example.tamagotchi/drawable/medicine2"));

        mediRinger = new ClassFood();
        mediRinger.setHealth(20);
        mediRinger.setName("만능 포켓몬 수액");
        mediRinger.setPrice(MEDIRINGER_PRICE);
        mediRinger.setEffect("건강+20");
        mediRinger.setFigure(Uri.parse("android.resource://com.example.tamagotchi/drawable/medicine3"));

        //이미 생성된 파이리가 있는지 체크를 위해 "MyPetData" SP(= SharedPreference)파일 호출;
        SPmyPetData = getSharedPreferences("MyPetData",0);
        edPetData = SPmyPetData.edit();
        String strPetData = SPmyPetData.getString("MyPetData","EMPTY");

        JSONObject JOpetData = null;
        try {
            JOpetData = new JSONObject(strPetData);
            myPet.setName(JOpetData.getString("name"));
            myPet.setHunger(Integer.parseInt(JOpetData.getString("hunger")));
            myPet.setStamina(Integer.parseInt(JOpetData.getString("stamina")));
            myPet.setHealth(Integer.parseInt(JOpetData.getString("health")));
            myPet.setEmotion(Integer.parseInt(JOpetData.getString("emotion")));
            myPet.setGold(Integer.parseInt(JOpetData.getString("gold")));
            myPet.setGrowth(Integer.parseInt(JOpetData.getString("growth")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        prbarHunger = findViewById(R.id.ac_main_prbar_hunger);
        prbarStamina = findViewById(R.id.ac_main_prbar_stamina);
        prbarHealth = findViewById(R.id.ac_main_prbar_health);
        prbarEmotion = findViewById(R.id.ac_main_prbar_emotion);
        txtGold = findViewById(R.id.ac_main_txt_gold);
        txtGetGold = findViewById(R.id.ac_main_txt_getgold);
        txtLoseGold = findViewById(R.id.ac_main_txt_losegold);
        txtWeather=findViewById(R.id.ac_main_txt_weather);

        imgFood = findViewById(R.id.ac_main_img_food);
        imgSleep = findViewById(R.id.ac_main_img_sleep);
        imgShower = findViewById(R.id.ac_main_img_shower);
        imgHospital = findViewById(R.id.ac_main_img_hospital);
        imgPlay = findViewById(R.id.ac_main_img_play);
        imgList = findViewById(R.id.ac_main_img_list);
        imgDdong1 = findViewById(R.id.ac_main_img_ddong1);
        imgDdong2 = findViewById(R.id.ac_main_img_ddong2);
        imgDdong3 = findViewById(R.id.ac_main_img_ddong3);
        imgWater = findViewById(R.id.ac_main_img_water);
        imgPet = findViewById(R.id.ac_main_img_pet);
        imgSleeping = findViewById(R.id.ac_main_img_sleeping);
        imgBackground = findViewById(R.id.ac_main_img_background);
        imgEating = findViewById(R.id.ac_main_img_eating);
        imgFire = findViewById(R.id.ac_main_img_fire);

        consLayout = findViewById(R.id.ac_main_layout_cons);

        /*setLog("이상하네1");*/
        //액티비티 종료 후 재 실행 시 service 중복 실행을 방지하기 위해 Boolean AutoGrow 활용
        SharedPreferences SPAuto= getSharedPreferences("Auto",0);
        AutoGrow = SPAuto.getBoolean("Auto",false);

        SharedPreferences SPsleeping = getSharedPreferences("sleeping",0);
        sleepIs = SPsleeping.getBoolean("sleeping",false);

        if(!AutoGrow){//현재 실행중인 service내 성장 쓰레드가 없다면 쓰레드 실행
            broadIntent = new Intent();
            broadIntent.setAction("com.example.service_broadcast");
            broadIntent.putExtra("hunger",myPet.getHunger());
            if(sleepIs==true){//액티비티 종료전 자고 있었음.
                broadIntent.putExtra("stamina_sleeping",myPet.getStamina());
            }else if(sleepIs==false){//액티비티 종료전 깨어 있었음.
                broadIntent.putExtra("stamina",myPet.getStamina());
            }
            setLog("처음 hunger는 "+myPet.getHunger());
            sendBroadcast(broadIntent);
            setLog("이상하네2");
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.broadcast");
        registerReceiver(broadcast,filter);

        //저장된 똥 갯수 확인을 위해
        SharedPreferences SPDdong= getSharedPreferences("Ddong",0);
        ddongEach = SPDdong.getInt("Ddong",0);

        taskBackground = new TaskBackground();
        taskBackground.execute(1);


    }

    @Override
    protected void onResume() {

        super.onResume();
        if(sleepIs){
            imgSleep.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/sun_black"));
            imgSleeping.setVisibility(View.VISIBLE);
            consLayout.setBackgroundColor(Color.parseColor("#000000"));
            imgPet.setVisibility(View.INVISIBLE);
            txtGold.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(ddongEach==1){
            imgDdong1.setVisibility(View.VISIBLE);
        }else if(ddongEach==2){
            imgDdong1.setVisibility(View.VISIBLE);
            imgDdong2.setVisibility(View.VISIBLE);
        }else if(ddongEach>=3){
            imgDdong1.setVisibility(View.VISIBLE);
            imgDdong2.setVisibility(View.VISIBLE);
            imgDdong3.setVisibility(View.VISIBLE);
        }
        setLog("onResume");

        prbarHunger.setProgress(myPet.getHunger());
        prbarStamina.setProgress(myPet.getStamina());
        prbarHealth.setProgress(myPet.getHealth());
        prbarEmotion.setProgress(myPet.getEmotion());
        txtGold.setText(Integer.toString(myPet.getGold()));

        imgFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//밥먹이기, 클릭 시 원하는 먹이 선택할 수 있게끔 리사이클러뷰 다이얼로그 생성
                foodList.clear();
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                final AlertDialog dialFoodList = builder.create();

                final RecyclerView reFoodList = new RecyclerView(ActivityMain.this);
                reFoodList.setLayoutManager(new LinearLayoutManager(ActivityMain.this));
                foodAdapter=new AdapterFood(ActivityMain.this,foodList);
                reFoodList.setAdapter(foodAdapter);

                foodList.add(foodDry);
                foodList.add(foodCan);
                foodList.add(foodMeat);
                foodAdapter.notifyDataSetChanged();

                reFoodList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), reFoodList,
                        new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ClassFood selectFood = foodList.get(position);
                        taskGoldManage = new TaskGoldManage(ActivityMain.this);
                        if(myPet.getGold()<selectFood.getPrice()){
                            Toast.makeText(ActivityMain.this, "골드가 모자랍니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            myPet.EatFood(selectFood.getPrice(),selectFood.getHunger());
                            /*txtGold.setText(Integer.toString(myPet.getGold()));*/
                            prbarHunger.setProgress(myPet.getHunger());

                            taskFood = new TaskFood(selectFood.getName(),selectFood.getFigure());
                            if(Build.VERSION.SDK_INT>=11){
                                taskFood.executeOnExecutor(taskFood.THREAD_POOL_EXECUTOR);
                                taskGoldManage.executeOnExecutor(taskGoldManage.THREAD_POOL_EXECUTOR,TaskGoldManage.LOSEGOLD,selectFood.getPrice());
                            }else {
                                taskFood.execute(1);
                                taskGoldManage.execute(TaskGoldManage.LOSEGOLD,selectFood.getPrice());
                            }
                            /*if(Build.VERSION.SDK_INT>=11){
                                taskGoldManage.executeOnExecutor(taskGoldManage.THREAD_POOL_EXECUTOR,TaskGoldManage.LOSEGOLD,selectFood.getPrice());
                            }else {
                                taskGoldManage.execute(TaskGoldManage.LOSEGOLD,selectFood.getPrice());
                            }*/

                            //배고픔 상승했으므로 상승된 배고픔 데이터 서비스로 보내기
                            Intent broadIntent = new Intent();
                            broadIntent.setAction("com.example.service_broadcast");
                            broadIntent.putExtra("hungerEat",myPet.getHunger());
                            sendBroadcast(broadIntent);

                            dialFoodList.dismiss();
                        }
                    }
                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
                /*myPet.setHunger(myPet.getHunger()+10);
                prbarHunger.setProgress(myPet.getHunger());
                setLog("내펫의 배고픔 : "+myPet.getHunger());
                setLog("FoodClick");*/
                dialFoodList.setTitle("먹이 선택");
                dialFoodList.setView(reFoodList);
                dialFoodList.show();
            }
        });
        imgSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sleepIs==false){//현재 깨어 있는 상태. 버튼을 누름으로 재우는 상황 표현
                    imgSleep.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/sun_black"));
                    imgSleeping.setVisibility(View.VISIBLE);
                    imgBackground.setVisibility(View.INVISIBLE);
                    imgPet.setVisibility(View.INVISIBLE);
                    consLayout.setBackgroundColor(Color.parseColor("#000000"));
                    txtGold.setTextColor(Color.parseColor("#FFFFFF"));

                    sleepIs = true;

                    Intent broadIntent = new Intent();
                    broadIntent.setAction("com.example.service_broadcast");
                    broadIntent.putExtra("sleep",myPet.getStamina());
                    setLog("자라 "+myPet.getStamina());
                    sendBroadcast(broadIntent);
                }else if(sleepIs==true){//현재 자고있는 상태. 버튼을 누름으로 깨우는 상황 표현
                    imgSleep.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/night_black"));
                    imgSleeping.setVisibility(View.INVISIBLE);
                    imgBackground.setVisibility(View.VISIBLE);
                    imgPet.setVisibility(View.VISIBLE);
                    consLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    txtGold.setTextColor(Color.parseColor("#000000"));
                    sleepIs = false;

                    Intent broadIntent = new Intent();
                    broadIntent.setAction("com.example.service_broadcast");
                    broadIntent.putExtra("wakeUp",myPet.getStamina());
                    setLog("일어나 "+myPet.getStamina());
                    sendBroadcast(broadIntent);
                }


                /*myPet.setStamina(myPet.getStamina()+10);
                prbarStamina.setProgress(myPet.getStamina());
                setLog("내펫의 체력 : "+myPet.getStamina());
                setLog("SleepClick");*/
            }
        });
        imgShower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//똥치우기
                if(!showerState){
                    setLog("샤워하자");
                    TaskWash taskWash = new TaskWash();
                    taskWash.isCancelled();
                    if(Build.VERSION.SDK_INT>=11){
                        taskWash.executeOnExecutor(taskWash.THREAD_POOL_EXECUTOR);
                    }else {
                        taskWash.execute();
                    }
                }else {
                    setLog("이미 샤워중");
                }


                /*imgWater.setVisibility(View.VISIBLE);
                try {
                    *//*setLog("imgShower버튼 1");*//*
                    Thread.sleep(1000);
                    imgWater.setVisibility(View.INVISIBLE);
                    *//*setLog("imgShower버튼 2");*//*
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    *//*setLog("imgShower버튼 3");*//*
                    imgWater.setVisibility(View.INVISIBLE);
                }*/
                /*setLog("imgShower버튼 4");*/
                /*imgDdong1.setVisibility(View.INVISIBLE);
                imgDdong2.setVisibility(View.INVISIBLE);
                imgDdong3.setVisibility(View.INVISIBLE);*/
                /*setLog("imgShower버튼 5");*/
                ddongEach=0;

                /*setLog("ShowerClick");*/
            }
        });
        imgHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//약먹이기, 클릭 시 원하는 약 선택할 수 있게끔 리사이클러뷰 다이얼로그 생성
                foodList.clear();
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                final AlertDialog dialMedicList = builder.create();

                final RecyclerView reMedicList = new RecyclerView(ActivityMain.this);
                reMedicList.setLayoutManager(new LinearLayoutManager(ActivityMain.this));
                foodAdapter=new AdapterFood(ActivityMain.this,foodList);
                reMedicList.setAdapter(foodAdapter);

                foodList.add(mediPill);
                foodList.add(mediKorean);
                foodList.add(mediRinger);
                foodAdapter.notifyDataSetChanged();

                /*myPet.setHealth(myPet.getHealth()+10);
                prbarHealth.setProgress(myPet.getHealth());
                setLog("내펫의 건강 : "+myPet.getHealth());
                setLog("HospitalClick");*/

                reMedicList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), reMedicList,
                        new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ClassFood selectFood = foodList.get(position);
                        if(myPet.getGold()<selectFood.getPrice()){
                            Toast.makeText(ActivityMain.this, "골드가 모자랍니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            taskFood = new TaskFood(selectFood.getName(),selectFood.getFigure());
                            if(Build.VERSION.SDK_INT>=11){
                                taskFood.executeOnExecutor(taskFood.THREAD_POOL_EXECUTOR);
                            }else {
                                taskFood.execute(1);
                            }
                            myPet.EatMedicine(selectFood.getPrice(),selectFood.getHealth());
                            txtGold.setText(Integer.toString(myPet.getGold()));
                            prbarHealth.setProgress(myPet.getHealth());
                            dialMedicList.dismiss();
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

                dialMedicList.setTitle("약 선택");
                dialMedicList.setView(reMedicList);
                dialMedicList.show();
            }
        });
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//놀아주기
                final CharSequence[] items = {"골드 획득","베팅 가위 바위 보!!","두더지 게임"};
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityMain.this);
                alertDialogBuilder.setTitle("놀아줄 방법 선택");
                alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                myPet.setEmotion(myPet.getEmotion()+10);
                                /*myPet.setGold(myPet.getGold()+1000);*/
                                prbarEmotion.setProgress(myPet.getEmotion());
                                /*txtGold.setText(Integer.toString(myPet.getGold()));*/

                                taskGoldManage = new TaskGoldManage(ActivityMain.this);

                                if(Build.VERSION.SDK_INT>=11){
                                    taskGoldManage.executeOnExecutor(taskGoldManage.THREAD_POOL_EXECUTOR,TaskGoldManage.GETGOLD,500);
                                }else {
                                    taskGoldManage.execute(TaskGoldManage.GETGOLD,500);
                                }
                                if(myPet.getEmotion()>=100){
                                    TaskPetDance taskPetDance = new TaskPetDance();
                                    if(Build.VERSION.SDK_INT>=11){
                                        taskPetDance.executeOnExecutor(taskPetDance.THREAD_POOL_EXECUTOR);
                                    }else {
                                        taskPetDance.execute();
                                    }
                                }
                                break;
                            case 1:
                                Intent intent_rock =new Intent(ActivityMain.this,ActivitySplashRock.class);
                                startActivityForResult(intent_rock,PLAY_GAME_ROCK);
                                break;
                            case 2:
                                Intent intent_punch =new Intent(ActivityMain.this,ActivityGamePunch.class);
                                startActivityForResult(intent_punch,PLAY_GAME_PUNCH);
                                break;
                        }
                        /*setLog(items[which].toString()+"를 선택하셨습니다");*/
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


                setLog("내펫의 감정 : "+myPet.getEmotion());
                setLog("PlayClick");
            }
        });
        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//성장 일지 보기
                Intent intent = new Intent(ActivityMain.this, ActivityList.class);
                startActivity(intent);

                /*Intent intent = new Intent(ActivityMain.this, ServiceBgm.class);
                intent.putExtra(ServiceBgm.MESSEAGE_KEY,false);
                stopService(intent);*/
                setLog("ListClick");
            }
        });
        imgPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskPetMove taskPetMove = new TaskPetMove();
                ThreadWeather threadWeather = new ThreadWeather();
                threadWeather.start();
                /*TaskWeather taskWeather = new TaskWeather();*/
                if(Build.VERSION.SDK_INT>=11){
                    taskPetMove.executeOnExecutor(taskPetMove.THREAD_POOL_EXECUTOR);
                    /*taskWeather.executeOnExecutor(taskPetMove.THREAD_POOL_EXECUTOR);*/
                }else {
                    taskPetMove.execute(1);
                    /*taskWeather.execute();*/
                }

            }
        });
        weatherHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    setLog("나온나");
                    Double[] weatherData = (Double[]) msg.obj;
                    txtWeather.setVisibility(View.VISIBLE);

                    long now = System.currentTimeMillis();
                    Date dDate = new Date(now);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
                    String getTime = sdf.format(dDate);
                    Log.e("로그","측정한 현재 날짜는 1 "+getTime);

                    String[] nowDate = new String[3];
                    nowDate[0]=getTime.split(",")[0];
                    int MM = Integer.parseInt(getTime.split(",")[1]);
                    nowDate[1]=Integer.toString(MM);
                    int dd = Integer.parseInt(getTime.split(",")[2]);
                    nowDate[2]=Integer.toString(dd);
                    String todayDate = nowDate[0]+"년 "+nowDate[1]+"월 "+nowDate[2]+"일";
                    String cloudState = null;
                    String windState = null;
                    Log.e("로그","메인에서 정리된 현재 날짜는 1 "+todayDate);
                    if(weatherData[2]==1){
                        cloudState = "맑음";
                    }else if(weatherData[2]==2){
                        cloudState = "구름조금";
                    }else if(weatherData[2]==3){
                        cloudState = "구름많음";
                    }else if(weatherData[2]==4){
                        cloudState = "흐림";
                    }
                    if(weatherData[4]==null){
                        windState = "없음";
                    }else if(weatherData[4]<4.0){
                        windState = "약한 바람";
                    }else if(weatherData[4]>=4.0 && weatherData[4]<9.0){
                        windState = "약간 강한 바람";
                    }else if(weatherData[4]>=9.0 && weatherData[4]<14.0) {
                        windState = "강한 바람";
                    }else if(weatherData[4]>=14.0) {
                        windState = "매우 강한 바람";
                    }
                    txtWeather.setText("현재 "+todayDate+" 서울시 동작구 날씨입니다!! 비가올 확률은 "+weatherData[0].toString()
                            +"%, 습도는 "+weatherData[1].toString()+"%, 하늘상태는 "+cloudState+", 현재 기온은 "+weatherData[3]
                            +"℃, 바람은 "+windState+" 입니다");
                }
                else if(msg.what==2){
                    txtWeather.setVisibility(View.INVISIBLE);
                }
            }
        };

    }

    @Override
    protected void onPause() {
        super.onPause();
        setLog("onPause");

        //펫 정보 저장
        JSONObject JOmyPetData = new JSONObject();
        try {
            JOmyPetData.put("name",myPet.getName());
            JOmyPetData.put("hunger",Integer.toString(myPet.getHunger()));
            JOmyPetData.put("stamina",Integer.toString(myPet.getStamina()));
            JOmyPetData.put("health",Integer.toString(myPet.getHealth()));
            JOmyPetData.put("emotion",Integer.toString(myPet.getEmotion()));
            JOmyPetData.put("gold",Integer.toString(myPet.getGold()));
            JOmyPetData.put("growth",Integer.toString(myPet.getGrowth()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        edPetData.putString("MyPetData",JOmyPetData.toString());
        edPetData.commit();
        //종료 전 자고 있는지 깨어 있는지 수면상태 저장
        SharedPreferences SPsleeping = getSharedPreferences("sleeping",0);
        SharedPreferences.Editor editor = SPsleeping.edit();
        editor.putBoolean("sleeping",sleepIs);
        editor.commit();

    }

    @Override
    protected void onDestroy() {
        //배경음악 종료
        Intent intent = new Intent(ActivityMain.this, ServiceBgm.class);
        intent.putExtra(ServiceBgm.MESSEAGE_KEY,false);
        stopService(intent);

        /*Intent intentGrowth = new Intent(ActivityMain.this,ServiceGrowth.class);
        bindService(intentGrowth,conn,Context.BIND_AUTO_CREATE);*/

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*setLog("베팅 금액 : "+data.getIntExtra("betGold",0)+"게임 결과 : "+data.getIntExtra("result",100));*/
        if(requestCode == ActivityMain.PLAY_GAME_ROCK && resultCode == ActivityGameRock.RESULT_GAME_ROCK){
            if(data.getIntExtra("result",4)==ActivityGameRock.WIN){
                taskGoldManage = new TaskGoldManage(ActivityMain.this);
                if(Build.VERSION.SDK_INT>=11){
                    taskGoldManage.executeOnExecutor(taskGoldManage.THREAD_POOL_EXECUTOR,TaskGoldManage.GETGOLD,data.getIntExtra("betGold",0));
                }else {
                    taskGoldManage.execute(TaskGoldManage.GETGOLD,data.getIntExtra("betGold",0));
                }
            }else if(data.getIntExtra("result",4)==ActivityGameRock.LOSE){
                taskGoldManage = new TaskGoldManage(ActivityMain.this);
                if(Build.VERSION.SDK_INT>=11){
                    taskGoldManage.executeOnExecutor(taskGoldManage.THREAD_POOL_EXECUTOR,TaskGoldManage.LOSEGOLD,data.getIntExtra("betGold",0));
                }else {
                    taskGoldManage.execute(TaskGoldManage.LOSEGOLD,data.getIntExtra("betGold",0));
                }
            }
        }
        else if(requestCode == ActivityMain.PLAY_GAME_PUNCH && resultCode == ActivityGamePunch.RESULT_GAME_PUNCH){
            taskGoldManage = new TaskGoldManage(ActivityMain.this);
            if(Build.VERSION.SDK_INT>=11){
                taskGoldManage.executeOnExecutor(taskGoldManage.THREAD_POOL_EXECUTOR,TaskGoldManage.GETGOLD,data.getIntExtra("gold",0));
            }else {
                taskGoldManage.execute(TaskGoldManage.GETGOLD,data.getIntExtra("gold",0));
            }
        }
    }

    //recyclerview 터치시 이벤트 발생하는 기능 사용하려면 이 class가 있어야됨, 정확한 이유는 모름, 나중에
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    //recyclerview 터치시 이벤트 발생하는 기능 사용하려면 이 class가 있어야됨, 정확한 이유는 모름, 나중에
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ActivityMain.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ActivityMain
                .ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
    BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getIntExtra("hunger",-1)!=-1){
                int data = intent.getIntExtra("hunger",0);
                /*setLog("hunger받아온다~~ : "+data);*/
                myPet.setHunger(data);
                prbarHunger.setProgress(myPet.getHunger());
                AutoGrow = true;


                if(myPet.getHunger()==70){
                    imgDdong1.setVisibility(View.VISIBLE);
                    ddongEach+=1;
                }if(myPet.getHunger()==40){
                    imgDdong2.setVisibility(View.VISIBLE);
                    ddongEach+=1;
                }if(myPet.getHunger()==10){
                    imgDdong3.setVisibility(View.VISIBLE);
                    ddongEach+=1;
                }if (myPet.getHunger()<=0){
                    /*setLog("얼랄라라라라라");*/
                    TaskPetFire taskPetFire = new TaskPetFire();
                    if(Build.VERSION.SDK_INT>=11){
                        taskPetFire.executeOnExecutor(taskPetFire.THREAD_POOL_EXECUTOR);
                        /*setLog("얼랄라라라라라1");*/
                    }else {
                        taskPetFire.execute();
                        /*setLog("얼랄라라라라라2");*/
                    }
                }

                SharedPreferences SPAuto= getSharedPreferences("Auto",0);
                SharedPreferences.Editor editor = SPAuto.edit();
                editor.putBoolean("Auto",AutoGrow);
                editor.commit();

                SharedPreferences SPDdong= getSharedPreferences("Ddong",0);
                SharedPreferences.Editor editor2 = SPDdong.edit();
                editor2.putInt("Ddong",ddongEach);
                editor2.commit();
            }
            if (intent.getIntExtra("stamina",-1)!=-1){
                int data = intent.getIntExtra("stamina",0);
                /*setLog("stamina받아온다~~ : "+data);*/
                myPet.setStamina(data);
                prbarStamina.setProgress(myPet.getStamina());
            }
            if(intent.getIntExtra("sleep",-1)!=-1){
                int data = intent.getIntExtra("sleep",0);
                /*setLog("sleep~~ : "+data);*/
                myPet.setStamina(data);
                prbarStamina.setProgress(myPet.getStamina());
            }






        }
    };
    class TaskBackground extends AsyncTask<Integer, Integer, Integer> {
        int index = 0;

        @Override
        protected Integer doInBackground(Integer... integers) {
            setLog("TaskBackground doInBackground!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            while (isCancelled() == false) {
                /*setLog("TaskBackground doInBackground"+index);*/
                index++;
                publishProgress(index);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            /*setLog("TaskBackground onProgressUpdate"+index);*/
            if (values[0].intValue() % 3 == 0) {
                imgBackground.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/sunset"));
            } else if (values[0].intValue() % 3 == 1) {
                imgBackground.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/night"));
            } else if (values[0].intValue() % 3 == 2) {
                imgBackground.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/sunny"));

            }
        }
    }
    class TaskFood extends AsyncTask<Integer,Integer,Integer>{
        int index = 0;
        String food;
        Uri figure;

        public TaskFood(String food,Uri figure){
            this.food = food;
            this.figure = figure;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setLog("TaskFood onPreExecute");
            imgEating.setVisibility(View.VISIBLE);
            imgEating.setImageURI(Uri.parse(figure.toString()));
            /*imgEating.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/food1"));*/
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            setLog("TaskFood doInBackground");
            try {
                Thread.sleep(1000);
                for(int i =0; i<2; i++){
                    setLog("돌아간다"+i);
                    publishProgress(i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            setLog("TaskFood onProgressUpdate");
            if(values[0].intValue()==0){
                imgEating.setImageURI(Uri.parse(figure.toString()+"1"));
                /*imgEating.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/food12"));*/
                startService(eatingIntent);
            }else if(values[0].intValue()==1){
                imgEating.setImageURI(Uri.parse(figure.toString()+"2"));
                /*imgEating.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/food13"));*/
                startService(eatingIntent);
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            setLog("TaskFood onPostExecute");
            imgEating.setVisibility(View.INVISIBLE);
            startService(eatingIntent);
        }
    }
    //로그생성용 메소드
    public void setLog(String content) {
        Log.i(TAG, content);
        /*private String TAG = "ActivityMain";*/
    }
}
