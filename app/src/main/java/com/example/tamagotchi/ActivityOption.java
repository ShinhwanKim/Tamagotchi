package com.example.tamagotchi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityOption extends AppCompatActivity {
    private String TAG = "ActivityOption";
    private Button btnSound;
    private Boolean soundOn;
    SharedPreferences SPoptionData;
    SharedPreferences.Editor edOptionData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        SPoptionData = getSharedPreferences("OptionData",0);
        edOptionData = SPoptionData.edit();

        String strOptionData = SPoptionData.getString("OptionData","EMPTY");

        btnSound = findViewById(R.id.ac_option_btn_sound);

        JSONObject JOpetData = null;
        try {
            JOpetData = new JSONObject(strOptionData);
            soundOn=Boolean.valueOf(JOpetData.getString("SoundState"));
            if(!soundOn){
                btnSound.setText("소리 : ON");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setLog("현재상태1"+soundOn.toString());

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundOn){
                    setLog("현재상태2"+soundOn.toString());
                    Intent intent = new Intent(ActivityOption.this, ServiceBgm.class);
                    intent.putExtra(ServiceBgm.MESSEAGE_KEY,false);
                    startService(intent);
                    btnSound.setText("소리 : ON");
                    setLog("현재상태3"+soundOn.toString());
                    soundOn = false;
                }else {
                    setLog("현재상태4"+soundOn.toString());
                    Intent intent = new Intent(ActivityOption.this, ServiceBgm.class);
                    intent.putExtra(ServiceBgm.MESSEAGE_KEY,true);
                    startService(intent);
                    btnSound.setText("소리 : OFF");
                    setLog("현재상태5"+soundOn.toString());
                    soundOn = true;
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        JSONObject JOmyOptionData = new JSONObject();
        try {
            JOmyOptionData.put("SoundState",soundOn);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        edOptionData.putString("OptionData",JOmyOptionData.toString());
        edOptionData.commit();
    }

    //로그생성용 메소드
    public void setLog(String content){
        Log.i(TAG,content);
    }
}
