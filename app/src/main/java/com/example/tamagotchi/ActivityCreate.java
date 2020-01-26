package com.example.tamagotchi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;
/*이 액티비티는 나의 파이리(펫)를 최초 생성하는 액티비티이다. 만약 이미 생성한 파이리가 있다면
이 액티비티는 생략되고 바로 ActivityMain으로 이동한다. 이 과정에서 SharedPreference를 활용한다.
생성된 SharedPreference파일이 없다면(앱 최초실행) 이 액티비티에서 파이리의 이름을 설정하고
ActivityMain으로 이동한다.
 */
public class ActivityCreate extends AppCompatActivity {
    private String TAG = "ActivityCreate";
    private int hatch;
    int touchIndex = 0;

    private ConstraintLayout wholeLayout;
    private ImageView imgTouch;
    private ImageView imgEgg;

    private ktClassPet myPet;
    Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        //상태창 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Intent intent = new Intent(ActivityCreate.this,ActivityMain.class);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        shake = AnimationUtils.loadAnimation(this,R.anim.shake);

        //이미 생성된 파이리가 있는지 체크를 위해 "MyPetData" SP(= SharedPreference)파일 호출;
        SharedPreferences SPmyPetData = getSharedPreferences("MyPetData",0);
        final SharedPreferences.Editor edPetData = SPmyPetData.edit();
        String checkDataPresence = SPmyPetData.getString("MyPetData","EMPTY");

        //최초 게임 옵션 데이터(배경음,효과음 등)를 저장해주기 위해 "OptionData" SP(= SharedPreference)파일 호출;
        SharedPreferences SPoptionData = getSharedPreferences("OptionData",0);
        final SharedPreferences.Editor edOptionData = SPoptionData.edit();

        wholeLayout = findViewById(R.id.ac_create_constraint);
        imgTouch = findViewById(R.id.ac_create_img_wakeup);
        imgEgg = findViewById(R.id.ac_create_img_egg);

        /*AlertDialog.Builder createTutorial = new AlertDialog.Builder(ActivityCreate.this);*/
        final Dialog dialog = new Dialog(ActivityCreate.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tutorialbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        final CustomTextView doctorTalk = dialog.findViewById(R.id.dialog_tutorial_doctor_content);
        doctorTalk.setText("반갑네. 게임 진행을 원한다면 나를 터치해주게.");
        ConstraintLayout layout = dialog.findViewById(R.id.dialog_tutorial_layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (touchIndex){
                    case 0:
                        doctorTalk.setText("나의 이름은 오박사.");
                        touchIndex++;
                        break;
                    case 1:
                        doctorTalk.setText("모두로부터 포켓몬 박사라고 존경받고 있지.");
                        touchIndex++;
                        break;
                    case 2:
                        doctorTalk.setText("포켓몬 월드에 발을 처음 들인 자네에게 포켓몬 알을 선물할까 하네.");
                        touchIndex++;
                        break;
                    case 3:
                        doctorTalk.setText("알을 받으면 알을 잘 어루만지듯 터치해보게. 아주 시비로운 일이 벌어질게야");
                        touchIndex++;
                        break;
                    case 4:
                        doctorTalk.setText("자!! 여기 포켓몬 알!");
                        touchIndex++;
                        break;
                    case 5:
                        dialog.dismiss();
                        break;
                }

                return false;
            }
        });

        dialog.show();
        /*final EditText etxName = new EditText(ActivityCreate.this);
        createTutorial.setView(etxName);*/

        /*ImageView doctor = new ImageView(ActivityCreate.this);
        doctor.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/doctor"));
                createTutorial.setView(doctor);*/

        /*createTutorial.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        createTutorial.show();*/

        /*TouchView imgTap = new TouchView(ActivityCreate.this);*/

        //"MyPetData"이란 파일에 값이 없을때 "EMPTY". 이 때는 파이리를 최초 생성해줘야 한다.
        if(checkDataPresence.equals("EMPTY")){
            myPet = new ktClassPet();

            wholeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    setLog("터치터치");
                    vibrator.vibrate(200);
                    hatch += 1;
                    imgTouch.setVisibility(View.VISIBLE);

                    imgTouch.setX(event.getX());
                    imgTouch.setY(event.getY());

                    if(hatch==3){
                        imgEgg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg1"));
                        imgEgg.startAnimation(shake);
                    }else if(hatch==6){
                        imgEgg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg2"));
                        imgEgg.startAnimation(shake);
                    }else if(hatch==9){
                        imgEgg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg3"));
                        imgEgg.startAnimation(shake);
                    }else if(hatch==12){
                        imgEgg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg4"));
                        imgEgg.startAnimation(shake);
                    }else if(hatch==15){
                        imgEgg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg5"));
                        imgEgg.startAnimation(shake);
                    }else if(hatch==18){
                        imgEgg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg6"));
                        imgEgg.startAnimation(shake);
                    }
                    //나중에 쓰레드 사용해서 터치시 WAKEUP 이미지 뜨게하기 Thread.sleep 사용하니 sleep시킨 초만큼 메인
                    // 쓰레드가 일시정지됨
                    /*try {
                        Thread.sleep(1000);
                        imgTouch.setVisibility(View.INVISIBLE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    if(hatch>=18){
                        imgTouch.setVisibility(View.INVISIBLE);

                        //다이얼로그 EditText로 파이리 이름 정하기
                        AlertDialog.Builder createPet = new AlertDialog.Builder(ActivityCreate.this);
                        createPet.setTitle("파이리 이름 정하기");

                        final EditText etxName = new EditText(ActivityCreate.this);
                        createPet.setView(etxName);

                        createPet.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String name = etxName.getText().toString();
                                myPet.setName(name);
                                myPet.setHunger(50);
                                myPet.setStamina(50);
                                myPet.setHealth(50);
                                myPet.setEmotion(50);
                                myPet.setGold(500);
                                myPet.setGrowth(0);
                                dialog.dismiss();
                                /*setLog(myPet.name);*/

                                //입력한 파이리 이름 JSON형식으로 저장. 나머지 데이터는 기본값
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

                                //게임 옵션 데이터 기본값 모두 true 로 저장
                                JSONObject JOmyOptionData = new JSONObject();
                                try {
                                    JOmyOptionData.put("SoundState",true);
                                    JOmyOptionData.put("EffectSoundState",true);
                                    JOmyOptionData.put("NotificationState",true);
                                    JOmyOptionData.put("VibratorState",true);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                edOptionData.putString("OptionData",JOmyOptionData.toString());
                                edOptionData.commit();

                                /*IntentFilter filter = new IntentFilter();
                                filter.addAction("com.example.broadcast");
                                registerReceiver(broadcast,filter);*/
                                Intent intentService = new Intent(ActivityCreate.this,ServiceGrowing.class);
                                startService(intentService);


                                startActivity(intent);
                                finish();
                            }
                        });
                        createPet.show();
                    }
                    return false;
                }
            });
            setLog("생성생성");

        }else{//이미 생성한 파이리가 있다면 바로 ActivityMain으로 이동
            /*setLog("이미 만든 캐릭터가 있네요"+checkDataPresence);*/
            Intent intentService = new Intent(ActivityCreate.this,ServiceGrowing.class);
            startService(intentService);

            startActivity(intent);
            finish();
        }


    }


    //로그생성용 메소드
    public void setLog(String content){
        Log.i(TAG,content);
    }
}
