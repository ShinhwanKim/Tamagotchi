package com.example.tamagotchi;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class ActivityGamePunch extends AppCompatActivity {
    public final static int RESULT_GAME_PUNCH = 2001;
    private String TAG = "ActivityGamePunch";
    ImageView btn1;
    ImageView btn2;
    ImageView btn3;
    ImageView btn4;
    ImageView btn5;
    ImageView btn6;
    ImageView btn7;
    ImageView btn8;
    ImageView btn9;
    Boolean btn1State = false;
    Boolean btn2State = false;
    Boolean btn3State = false;
    Boolean btn4State = false;
    Boolean btn5State = false;
    Boolean btn6State = false;
    Boolean btn7State = false;
    Boolean btn8State = false;
    Boolean btn9State = false;
    TextView txtScore;
    int score = 0;

    private static Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_punch);

        btn1 =findViewById(R.id.imageView1);
        btn2 =findViewById(R.id.imageView2);
        btn3 =findViewById(R.id.imageView3);
        btn4 =findViewById(R.id.imageView4);
        btn5 =findViewById(R.id.imageView5);
        btn6 =findViewById(R.id.imageView6);
        btn7 =findViewById(R.id.imageView7);
        btn8 =findViewById(R.id.imageView8);
        btn9 =findViewById(R.id.imageView9);
        txtScore = findViewById(R.id.ac_punch_txt_score);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    /*Log.e("봐봐","핸들러에서 상태는1?"+btn9State);*/
                    ImageView btnsel = (ImageView) msg.obj;
                    if(btnsel==btn9){
                        Log.e("봐봐","핸들러에서 상태는1?"+btn9State);
                    }
                    btnsel.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_click"));
                    Log.e("봐봐","바꼈다");
                }else if(msg.what==2){
                    /*Log.e("봐봐","핸들러에서 상태는2?"+btn9State);*/
                    ImageView btnsel = (ImageView) msg.obj;
                    btnsel.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));

                }
            }
        };

        Play play = new Play(btn1,1);
        play.start();
        Play play2 = new Play(btn2,2);
        play2.start();
        Play play3 = new Play(btn3,3);
        play3.start();
        Play play4 = new Play(btn4,4);
        play4.start();
        Play play5 = new Play(btn5,5);
        play5.start();
        Play play6 = new Play(btn6,6);
        play6.start();
        Play play7 = new Play(btn7,7);
        play7.start();
        Play play8 = new Play(btn8,8);
        play8.start();
        Play play9 = new Play(btn9,9);
        play9.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn1State){
                    btn1State = false;
                    btn1.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn1State){
                    btn1State = false;
                    btn1.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn2State){
                    btn2State = false;
                    btn2.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn2State){
                    btn2State = false;
                    btn2.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn3State){
                    btn3State = false;
                    btn3.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn3State){
                    btn3State = false;
                    btn3.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn4State){
                    btn4State = false;
                    btn4.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn4State){
                    btn4State = false;
                    btn4.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn5State){
                    btn5State = false;
                    btn5.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn5State){
                    btn5State = false;
                    btn5.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn6State){
                    btn6State = false;
                    btn6.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn6State){
                    btn6State = false;
                    btn6.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn7State){
                    btn7State = false;
                    btn7.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn7State){
                    btn7State = false;
                    btn7.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("봐봐","버튼상태는"+btn8State);
                if(btn8State){
                    btn8State = false;
                    btn8.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn8State){
                    btn8State = false;
                    btn8.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("봐봐","상태는?"+btn9State);
                if(btn9State){
                    btn9State = false;
                    btn9.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score++;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }else if(!btn9State){
                    btn9State = false;
                    btn9.setImageURI(Uri.parse("android.resource://com.example.dudugi/drawable/circle_nonclick"));
                    score--;
                    txtScore.setText("SCORE : "+Integer.toString(score*10));
                    Log.e("봐봐","제 점수는요"+score);
                }
            }
        });

    }

    public void Cancel(View view) {
        Intent intent = new Intent();
        intent.putExtra("gold",score*10);
        setResult(RESULT_GAME_PUNCH,intent);
        finish();
    }



    public class Play extends Thread{
        ImageView btn;
        int num;
        /*Boolean state;*/
        Random rnd = new Random();
        Play(ImageView btn,int num){
            this.btn = btn;
            this.num = num;
            /*this.state = state;*/
        }
        @Override
        public void run() {
            super.run();
            while(true){
                try {
                    Thread.sleep(3000);
                    int number = rnd.nextInt(3);
                    /*Log.e("봐봐","숫자는?"+number);*/
                    if(number==1){
                        switch (num){
                            case 1:
                                btn1State = true;
                                break;
                            case 2:
                                btn2State = true;
                                break;
                            case 3:
                                btn3State = true;
                                break;
                            case 4:
                                btn4State = true;
                                break;
                            case 5:
                                btn5State = true;
                                break;
                            case 6:
                                btn6State = true;
                                break;
                            case 7:
                                btn7State = true;
                                break;
                            case 8:
                                btn8State = true;
                                break;
                            case 9:
                                btn9State = true;
                                break;
                        }
                        /*mHandler.sendEmptyMessage(1);*/
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = btn;
                        mHandler.sendMessage(msg);
                        /*mHandler.obtainMessage(2,btn);*/

                        Thread.sleep(10000);
                        btn1State = false;
                        btn2State = false;
                        btn3State = false;
                        btn4State = false;
                        btn5State = false;
                        btn6State = false;
                        btn7State = false;
                        btn8State = false;
                        btn9State = false;
                        Message msg2 = mHandler.obtainMessage();
                        msg2.what = 2;
                        msg2.obj = btn;
                        mHandler.sendMessage(msg2);



                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    //로그생성용 메소드
    public void setLog(String content) {
        Log.i(TAG, content);
        /*private String TAG = "ActivityMain";*/
    }
}
