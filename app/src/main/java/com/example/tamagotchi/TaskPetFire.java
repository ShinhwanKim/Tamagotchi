package com.example.tamagotchi;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TaskPetFire extends AsyncTask<Void,Integer,Void> {
    private String TAG = "TaskPetFire";
    ImageView imgPet;
    ImageView imgFire;
    String uri;

    @Override
    protected void onPreExecute() {
        setLog("onPreExecute");
        super.onPreExecute();
        imgFire = ActivityMain.imgFire;
        imgPet = ActivityMain.imgPet;
        imgFire.setVisibility(View.VISIBLE);
        imgPet.setVisibility(View.INVISIBLE);

        uri = "android.resource://com.example.tamagotchi/drawable/piriefire";
    }

    @Override
    protected Void doInBackground(Void... voids) {
        setLog("doInBackground");
        try {
            for(int i =1; i<38; i++){
                /*setLog("돌아간다"+i);*/
                publishProgress(i);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        setLog("onProgressUpdate");
        imgFire.setImageURI(Uri.parse(uri+values[0].toString()));
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        setLog("onPostExecute");
        super.onPostExecute(aVoid);
        imgFire.setVisibility(View.INVISIBLE);
        imgPet.setVisibility(View.VISIBLE);
    }

    //로그생성용 메소드
    public void setLog(String content) {
        Log.i(TAG, content);
    }
}
