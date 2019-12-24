package com.example.tamagotchi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ActivityGameRock extends AppCompatActivity {
    private String TAG = "ActivityGameRock";

    TextView txtBetGold;
    TextView txtWinGold;
    TextView txtVsGold;
    TextView txtLoseGold;
    TextView txtInfo;
    TextView txtVersus;
    ImageView imgScissor;
    ImageView imgRock;
    ImageView imgPaper;
    ImageView imgYouPick;
    ImageView imgVersus;
    /*TaskRock taskRock;*/
    TaskPlay taskPlay;
    final static int SCISSOR = 0;
    final static int ROCK = 1;
    final static int PAPER = 2;
    int myPick;
    int youPick;
    int betGold;
    final static int WIN = 2;
    final static int DRAW = 1;
    final static int LOSE = 0;
    public final static int RESULT_GAME_ROCK = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rock);

        txtBetGold = findViewById(R.id.ac_rock_txt_betting);
        txtWinGold = findViewById(R.id.ac_rock_txt_winGold);
        txtVsGold= findViewById(R.id.ac_rock_txt_vsGold);
        txtLoseGold = findViewById(R.id.ac_rock_txt_loseGold);
        txtInfo = findViewById(R.id.ac_rock_txt_info);
        txtVersus = findViewById(R.id.ac_rock_txt_vs);
        imgScissor = findViewById(R.id.ac_rock_img_scissor);
        imgRock = findViewById(R.id.ac_rock_img_rock);
        imgPaper = findViewById(R.id.ac_rock_img_paper);
        imgYouPick = findViewById(R.id.ac_rock_img_you);
        imgVersus = findViewById(R.id.ac_rock_img_versus);
        /*taskRock = new TaskRock();*/
        final AlertDialog.Builder betting = new AlertDialog.Builder(ActivityGameRock.this);
        betting.setTitle("베팅 금액 정하기\n현재 소지금 : "+ActivityMain.myPet.getGold());//+"\n*보상\n승리 : 베팅 금액 x2\n패배 : 0\n무승부 : 베팅 금액x1"

        final EditText etxName = new EditText(ActivityGameRock.this);
        etxName.setInputType(InputType.TYPE_CLASS_NUMBER);
        betting.setView(etxName);

        betting.setPositiveButton("베팅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                betGold = Integer.parseInt(etxName.getText().toString());
                if(betGold>ActivityMain.myPet.getGold()){
                    Toast.makeText(ActivityGameRock.this, "소지금이 부족합니다. 다시 베팅해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ActivityGameRock.this, betGold+"골드를 베팅했습니다", Toast.LENGTH_SHORT).show();
                    txtBetGold.setText(Integer.toString(betGold));
                    txtWinGold.setText(Integer.toString(betGold*2));
                    txtVsGold.setText(Integer.toString(betGold));
                    txtInfo.setVisibility(View.VISIBLE);
                    /*ActivityMain.myPet.setGold(ActivityMain.myPet.getGold()-betGold);*/
                    dialog.dismiss();
                }
            }
        });
        betting.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        betting.show();


    }

    @Override
    protected void onResume() {
        super.onResume();

        imgScissor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youPick = YouPick();
                myPick = SCISSOR;
                setLog("나 : "+myPick + " ///상대 : "+youPick);
                Play(myPick,youPick);
                /*imgPaper.setVisibility(View.GONE);
                imgRock.setVisibility(View.GONE);
                Play(SCISSOR,youPick);*/
            }
        });
        imgRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youPick = YouPick();
                myPick = ROCK;
                setLog("나 : "+myPick + " ///상대 : "+youPick);
                Play(myPick,youPick);
                /*imgPaper.setVisibility(View.GONE);
                imgScissor.setVisibility(View.GONE);
                Play(ROCK,youPick);*/
            }
        });
        imgPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youPick = YouPick();
                myPick = PAPER;
                setLog("나 : "+myPick + " ///상대 : "+youPick);
                Play(myPick,youPick);
                /*imgRock.setVisibility(View.GONE);
                imgScissor.setVisibility(View.GONE);
                Play(PAPER,youPick);*/
            }
        });
    }
    public void Play(int my, int you){
        taskPlay = new TaskPlay();
        setLog("플레이!!! 나 : "+my + " ///상대 : "+you);
        txtInfo.setVisibility(View.GONE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            taskPlay.executeOnExecutor(taskPlay.THREAD_POOL_EXECUTOR,my,you);
        }else {
            taskPlay.execute(my,you);
        }
    }
    /*public void Play(int myPick,int youPick){
        *//*txtVersus.setText("가위,바위,보!!");*//*
        try {
            txtVersus.setText("가위");
            Thread.sleep(3000);
            txtVersus.setText("바위");
            Thread.sleep(3000);
            txtVersus.setText( "보");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        txtInfo.setVisibility(View.GONE);
        taskRock.execute(1);
        setLog("상대 선택은 : "+youPick);
        imgYouPick.setVisibility(View.VISIBLE);
        if(youPick==0){
            imgYouPick.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/scissor_u"));
        }else if(youPick==1){
            imgYouPick.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/rock_u"));
        }else if(youPick==2){
            imgYouPick.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/paper_u"));
        }

        if(myPick==youPick){
            txtVersus.setText("DRAW");
            Draw();
        }else if(myPick>youPick){
            if(myPick==2&&youPick==0){
                txtVersus.setText("LOSE");
                Lose();
            }else {
                txtVersus.setText("WIN");
                Win();
            }
        }else if(myPick<youPick){
            if(myPick==0&&youPick==2){
                txtVersus.setText("WIN");
                Win();
            }else {
                txtVersus.setText("LOSE");
                Lose();
            }
        }

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskPlay.cancel(false);
        taskPlay.isCancelled();
    }

    public int YouPick(){
        Random rnd = new Random();
        int pick = rnd.nextInt(3);
        return pick;
    }
    //로그생성용 메소드
    public void setLog(String content){
        Log.i(TAG,content);
        /*private String TAG = "ActivityMain";*/
    }
    public void Win(){
        final AlertDialog.Builder betting = new AlertDialog.Builder(ActivityGameRock.this);
        betting.setTitle("이겼습니다. "+betGold+"골드를 얻었습니다");
        /*ActivityMain.myPet.setGold(ActivityMain.myPet.getGold()+betGold*2);*/
        betting.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setLog("베팅 금액 : "+betGold);
                setLog("게임 결과 이김");
                Intent intent = new Intent();
                intent.putExtra("betGold",betGold);
                intent.putExtra("result",WIN);
                setResult(RESULT_GAME_ROCK,intent);
                finish();
            }
        });

        betting.show();
    }
    public void Lose(){
        final AlertDialog.Builder betting = new AlertDialog.Builder(ActivityGameRock.this);
        betting.setTitle("졌습니다. "+betGold+"골드를 잃었습니다");
        /*ActivityMain.myPet.setGold(ActivityMain.myPet.getGold());*/

        betting.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setLog("베팅 금액 : "+betGold);
                setLog("게임 결과 짐");
                Intent intent = new Intent();
                intent.putExtra("betGold",betGold);
                intent.putExtra("result",LOSE);
                setResult(RESULT_GAME_ROCK,intent);
                finish();
            }
        });

        betting.show();
    }public void Draw(){
        final AlertDialog.Builder betting = new AlertDialog.Builder(ActivityGameRock.this);
        betting.setTitle("비겼습니다.");
        /*ActivityMain.myPet.setGold(ActivityMain.myPet.getGold()+betGold);*/

        betting.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                setLog("베팅 금액 : "+betGold);
                setLog("게임 결과 비김");
                intent.putExtra("betGold",betGold);
                intent.putExtra("result",DRAW);
                setResult(RESULT_GAME_ROCK,intent);
                finish();
            }
        });

        betting.show();
    }

    class TaskPlay extends AsyncTask<Integer,Integer,Integer>{
        String strUri;

        @Override
        protected void onPreExecute() {
            setLog("onPreExecute");
            super.onPreExecute();
            imgRock.setVisibility(View.GONE);
            setLog("onPreExecute1");
            imgPaper.setVisibility(View.GONE);
            setLog("onPreExecute2");
            imgScissor.setVisibility(View.GONE);
            setLog("onPreExecute3");
            strUri = "android.resource://com.example.tamagotchi/drawable/rspgame";
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            setLog("doInBackground");
            int myPick = integers[0];
            int youPick = integers[1];
            int result = 0;

            if(myPick==youPick){
                result = DRAW;
            }else if(myPick>youPick){
                if(myPick==2&&youPick==0){
                    result = LOSE;
                }else {
                    result = WIN;
                }
            }else if(myPick<youPick){
                if(myPick==0&&youPick==2){
                    result = WIN;
                }else {
                    result = LOSE;
                }
            }
            try {
                Thread.sleep(700);
                for(int i=1; i<6; i++){
                    publishProgress(i);
                    Thread.sleep(300);
                }
                publishProgress(6,myPick,youPick);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            setLog("onProgressUpdate");
            super.onProgressUpdate(values);
            imgVersus.setImageURI(Uri.parse(strUri+values[0].toString()));
            switch (values[0]){
                case 2:
                    txtVersus.setText("가위");
                    break;
                case 4:
                    txtVersus.setText("바위");
                    break;
                case 6:
                    setLog("과연??!!! 나 : "+values[1] + " ///상대 : "+values[2]);
                    txtVersus.setText("보!");
                    int myPick = values[1];
                    int youPick = values[2];
                    if(myPick == ROCK){
                        switch (youPick){
                            case ROCK:
                                imgVersus.setImageURI(Uri.parse(strUri+"13"));
                                break;
                            case SCISSOR:
                                imgVersus.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/rspresult1"));
                                break;
                            case PAPER:
                                imgVersus.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/rspresult2"));
                                break;
                        }
                    }else if(myPick == SCISSOR){
                        switch (youPick){
                            case ROCK:
                                imgVersus.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/rspresult3"));
                                break;
                            case SCISSOR:
                                imgVersus.setImageURI(Uri.parse(strUri+"6"));
                                break;
                            case PAPER:
                                imgVersus.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/rspresult4"));
                                break;
                        }
                    }else if (myPick == PAPER){
                        switch (youPick){
                            case ROCK:
                                imgVersus.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/rspresult5"));
                                break;
                            case SCISSOR:
                                imgVersus.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/rspresult6"));
                                break;
                            case PAPER:
                                imgVersus.setImageURI(Uri.parse(strUri+"20"));
                                break;
                        }
                    }
                    break;
            }
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer){
                case WIN:
                    Win();
                    break;
                case DRAW:
                    Draw();
                    break;
                case LOSE:
                    Lose();
                    break;
            }
        }
    }
}
