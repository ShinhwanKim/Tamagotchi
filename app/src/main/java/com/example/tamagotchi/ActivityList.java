package com.example.tamagotchi;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
/*
이 액티비티는 기본적인 내 펫의 프로필을 보여주고 게임 옵션을 설정할 수 있는 액티비티이다.
내 펫의 프로필은 ActivityMain이 onPause 때 저장했던 펫의 정보들을 기반으로 출력된다.
게임 옵션 데이터들은 액티비티가 onPause 때 저장되어 종료했다 재실행 하여도 게임 옵션을
유지할 수 있다.
 */
public class ActivityList extends AppCompatActivity {
    private String TAG = "ActivityList";
    private Boolean soundOn;
    private Boolean eftSoundOn;
    private Boolean notifiOn;
    private Boolean vibratorOn;

    private Switch swSound;
    private Switch swEftSound;
    private Switch swNotifi;
    private Switch swVibrator;
    private SharedPreferences SPoptionData;
    private SharedPreferences.Editor edOptionData;

    private Button btnReset;

    private TextView txtName;
    private TextView txtAge;
    private TextView txtManuarity;

    private ClassPet myPet;
    private SharedPreferences SPmyPetData;
    private SharedPreferences.Editor edPetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        myPet = new ClassPet();
        txtName = findViewById(R.id.ac_list_txt_name);
        txtAge = findViewById(R.id.ac_list_txt_age);
        txtManuarity = findViewById(R.id.ac_list_txt_manuarity);
        swSound = findViewById(R.id.ac_list_switch_sound);
        swEftSound = findViewById(R.id.ac_list_switch_effectSound);
        swNotifi = findViewById(R.id.ac_list_switch_notification);
        swVibrator = findViewById(R.id.ac_list_switch_vibrator);

        btnReset = findViewById(R.id.ac_list_btn_reset);

        /*btnOption = findViewById(R.id.ac_list_btn_option);
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityList.this,ActivityOption.class);
                startActivity(intent);
            }
        });*/
        SPmyPetData = getSharedPreferences("MyPetData",0);
        edPetData = SPmyPetData.edit();

        String strPetData = SPmyPetData.getString("MyPetData","EMPTY");

        JSONObject JOpetData = null;
        try {
            JOpetData = new JSONObject(strPetData);
            myPet.setName(JOpetData.getString("name"));
            setLog("저장된 이름 : "+JOpetData.getString("name"));
            myPet.setHunger(Integer.parseInt(JOpetData.getString("hunger")));
            myPet.setStamina(Integer.parseInt(JOpetData.getString("stamina")));
            myPet.setHealth(Integer.parseInt(JOpetData.getString("health")));
            myPet.setEmotion(Integer.parseInt(JOpetData.getString("emotion")));
            myPet.setGold(Integer.parseInt(JOpetData.getString("gold")));
            myPet.setGrowth(Integer.parseInt(JOpetData.getString("growth")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        txtName.setText("성장 일지");

        SPoptionData = getSharedPreferences("OptionData",0);
        edOptionData = SPoptionData.edit();

        String strOptionData = SPoptionData.getString("OptionData","EMPTY");

        JSONObject JOoptionData = null;
        try {
            JOoptionData = new JSONObject(strOptionData);
            soundOn=JOoptionData.getBoolean("SoundState");
            eftSoundOn= JOoptionData.getBoolean("EffectSoundState");
            notifiOn= JOoptionData.getBoolean("NotificationState");
            vibratorOn= JOoptionData.getBoolean("VibratorState");

            swSound.setChecked(soundOn);
            swEftSound.setChecked(eftSoundOn);
            swNotifi.setChecked(notifiOn);
            swVibrator.setChecked(vibratorOn);
            if(soundOn){
                swSound.setText("배경음 : ON ");
            }else if(!soundOn){
                swSound.setText("배경음 : OFF");
            }
            if(eftSoundOn){
                swEftSound.setText("효과음 : ON ");
            }else if(!eftSoundOn){
                swEftSound.setText("효과음 : OFF");
            }
            if(notifiOn){
                swNotifi.setText("알림 : ON ");
            }else if(!notifiOn){
                swNotifi.setText("알림 : OFF");
            }
            if(vibratorOn){
                swVibrator.setText("진동 : ON ");
            }else if(!vibratorOn){
                swVibrator.setText("진동 : OFF");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*setLog("현재상태1"+soundOn.toString());*/
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.broadcast");
        registerReceiver(broadcastReceiver,filter);
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int minute = intent.getIntExtra("time",0);
            int hour = minute/60;
            txtManuarity.setText("성장 : "+Integer.toString(hour)+"%");
            minute = minute%60;
            int day = hour/24;
            hour = hour%24;
            String content = day+"일 "+hour+"시간 "+minute+"분";
            txtAge.setText("나이 : "+content);

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        txtName.setText("이름 : "+myPet.getName());
        setLog("onResume 이름 : "+myPet.getName());
        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setLog("배경음 버튼은 "+isChecked);
                soundOn=isChecked;

                if(soundOn){
                    setLog("현재상태2"+soundOn.toString());
                    Intent intent = new Intent(ActivityList.this, ServiceBgm.class);
                    intent.putExtra(ServiceBgm.MESSEAGE_KEY,true);
                    startService(intent);
                    swSound.setText("배경음 : ON ");
                    setLog("현재상태3"+soundOn.toString());
                }else {
                    setLog("현재상태4"+soundOn.toString());
                    Intent intent = new Intent(ActivityList.this, ServiceBgm.class);
                    intent.putExtra(ServiceBgm.MESSEAGE_KEY,false);
                    startService(intent);
                    swSound.setText("배경음 : OFF");
                    setLog("현재상태5"+soundOn.toString());
                }
            }
        });
        swEftSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eftSoundOn=isChecked;
                if(eftSoundOn){
                    swEftSound.setText("효과음 : ON ");
                }else {
                    swEftSound.setText("효과음 : OFF");
                }
            }
        });
        swNotifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notifiOn=isChecked;
                if(notifiOn){
                    swNotifi.setText("알림 : ON ");
                }else {
                    swNotifi.setText("알림 : OFF");
                }
            }
        });
        swVibrator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibratorOn=isChecked;
                if(vibratorOn){
                    swVibrator.setText("진동 : ON ");
                }else {
                    swVibrator.setText("진동 : OFF");
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {//초기화 버튼
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ActivityList.this);
                alert_confirm.setMessage("데이터 초기화시  복구가 불가능합니다. 삭제하시겠습니다?").setCancelable(false)
                        .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                edPetData.clear();
                                edPetData.commit();
                                SharedPreferences SPAuto= getSharedPreferences("Auto",0);
                                SharedPreferences.Editor editor = SPAuto.edit();
                                SharedPreferences SPDdong= getSharedPreferences("Ddong",0);
                                SharedPreferences.Editor editor2 = SPDdong.edit();

                                editor.clear();
                                editor.commit();

                                editor2.clear();
                                editor2.commit();

                                Intent intentService = new Intent(ActivityList.this,ServiceGrowing.class);
                                stopService(intentService);
                                Intent intent = new Intent(ActivityList.this,ActivityCreate.class);
                                //기존에 있던 스택 모두 제거
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                finish();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();


            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //게임 옵션 데이터 저장
        JSONObject JOmyOptionData = new JSONObject();
        try {
            JOmyOptionData.put("SoundState",soundOn);
            JOmyOptionData.put("EffectSoundState",eftSoundOn);
            JOmyOptionData.put("NotificationState",notifiOn);
            JOmyOptionData.put("VibratorState",vibratorOn);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        edOptionData.putString("OptionData",JOmyOptionData.toString());
        edOptionData.commit();
    }
    //내 펫 이름 바꾸기 , 다이얼로그로 변경
    public void ChangeName(View view) {
        AlertDialog.Builder changeName = new AlertDialog.Builder(ActivityList.this);
        changeName.setTitle("파이리 이름 변경하기");

        final EditText etxName = new EditText(ActivityList.this);
        changeName.setView(etxName);
        etxName.setText(ActivityMain.myPet.getName());

        changeName.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etxName.getText().toString();
                txtName.setText("이름 : "+name);

                //ActivityMain의 myPet데이터에 바로 직접 저장
                ActivityMain.myPet.setName(name);
            }
        });
        changeName.show();
    }

    //로그생성용 메소드
    public void setLog(String content){
        Log.i(TAG,content);
    }


}
