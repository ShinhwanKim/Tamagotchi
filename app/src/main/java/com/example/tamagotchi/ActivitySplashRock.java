package com.example.tamagotchi;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivitySplashRock extends AppCompatActivity {
    private String TAG = "ActivitySplashRock";
    TaskSplah taskSplah;
    ImageView imgSplash;
    TextView txtScissor;
    TextView txtRock;
    TextView txtPaper;
    String strUri;
    ConstraintLayout wholeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_rock);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imgSplash = findViewById(R.id.ac_splash_img);
        txtScissor = findViewById(R.id.ac_splash_txtScissor);
        txtRock = findViewById(R.id.ac_splash_txtRock);
        txtPaper = findViewById(R.id.ac_splash_txtPaper);
        strUri = "android.resource://com.example.tamagotchi/drawable/rspgame";
        wholeLayout = findViewById(R.id.ac_splash_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskSplah = new TaskSplah();
        if(Build.VERSION.SDK_INT>=11){
            taskSplah.executeOnExecutor(taskSplah.THREAD_POOL_EXECUTOR);
        }else {
            taskSplah.execute();
        }
        wholeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(ActivitySplashRock.this,ActivityGameRock.class);
                startActivityForResult(intent,ActivityMain.PLAY_GAME_ROCK);
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        taskSplah.isCancelled();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskSplah.isCancelled();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ActivityMain.PLAY_GAME_ROCK && resultCode == ActivityGameRock.RESULT_GAME_ROCK){
            setLog("베팅 금액 : "+data.getIntExtra("betGold",0)+"게임 결과 : "+data.getIntExtra("result",100));
            Intent intent = new Intent();
            intent.putExtra("betGold",data.getIntExtra("betGold",0));
            intent.putExtra("result",data.getIntExtra("result",4));
            setResult(ActivityGameRock.RESULT_GAME_ROCK,intent);
            finish();
        }
    }

    class TaskSplah extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            setLog("doInBackground");
            try {
                Thread.sleep(700);
                while (true){
                    for(int i=1; i<22; i++){
                        publishProgress(i);
                        Thread.sleep(300);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            setLog("onProgressUpdate");
            imgSplash.setImageURI(Uri.parse(strUri+values[0].toString()));
            switch (values[0]){
                case 2:
                    txtScissor.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    txtRock.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    txtPaper.setVisibility(View.VISIBLE);
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            setLog("onCancelled");
            super.onCancelled();
        }
    }
    //로그생성용 메소드
    public void setLog(String content) {
        Log.i(TAG, content);
        /*private String TAG = "TaskPetDance";*/
    }

}
