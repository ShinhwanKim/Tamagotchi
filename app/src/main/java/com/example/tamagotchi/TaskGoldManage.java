package com.example.tamagotchi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

//매개 변수(순서대로) : 벌어들인 금액,
public class TaskGoldManage extends AsyncTask<Integer,Integer,Boolean> {
    TextView txtgetGold;
    TextView txtloseGold;
    TextView txtGold;
    final static int GETGOLD = 1;
    final static int LOSEGOLD = 0;
    int currentGold;
    int goldValue;
    int manageGold;
    Context context;
    Animation goldAnimation;
    Animation GetAnimation;
    Animation LoseAnimation;

    public TaskGoldManage(Context context){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        txtgetGold = ActivityMain.txtGetGold;
        txtloseGold = ActivityMain.txtLoseGold;
        currentGold = ActivityMain.myPet.getGold();

        txtGold = ActivityMain.txtGold;
        manageGold = currentGold;
        goldAnimation = AnimationUtils.loadAnimation(context,R.anim.getgold);

    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        goldValue = integers[1];
        Boolean result = null;
        switch (integers[0]){
            case GETGOLD:
                for(int i=0;i<=goldValue;i++){
                    try {
                        publishProgress(GETGOLD,i);
                        result = true;
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case LOSEGOLD:
                for(int i=0;i<=goldValue;i++){
                    try {
                        publishProgress(LOSEGOLD,i);
                        result = false;
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(values[1]==1){
            switch (values[0]){
                case GETGOLD:
                    txtgetGold.setVisibility(View.VISIBLE);
                    txtgetGold.setText("+ "+goldValue);
                    txtgetGold.startAnimation(goldAnimation);
                    break;
                case LOSEGOLD:
                    txtloseGold.setVisibility(View.VISIBLE);
                    txtloseGold.setText("- "+goldValue);
                    txtloseGold.startAnimation(goldAnimation);

                    break;
            }
        }
        switch (values[0]){
            case GETGOLD:
                txtGold.setText(Integer.toString(manageGold));
                manageGold++;
                break;
            case LOSEGOLD:
                txtGold.setText(Integer.toString(manageGold));
                manageGold--;
                break;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean){
            ActivityMain.myPet.setGold(currentGold+goldValue);
        }else {
            ActivityMain.myPet.setGold(currentGold-goldValue);
        }
        txtgetGold.setVisibility(View.INVISIBLE);
        txtloseGold.setVisibility(View.INVISIBLE);
    }
}
