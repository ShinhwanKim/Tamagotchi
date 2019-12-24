package com.example.tamagotchi;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class TaskWash extends AsyncTask<Void,Integer,Void> {
    private String TAG = "TaskWash";
    ImageView imgWash;
    ImageView imgDdong1;
    ImageView imgDdong2;
    ImageView imgDdong3;
    String uri;

    @Override
    protected void onPreExecute() {
        setLog("onPreExecute");
        super.onPreExecute();
        imgWash = ActivityMain.imgWater;
        imgWash.setVisibility(View.VISIBLE);
        uri = "android.resource://com.example.tamagotchi/drawable/watering";
        imgDdong1=ActivityMain.imgDdong1;
        imgDdong2=ActivityMain.imgDdong2;
        imgDdong3=ActivityMain.imgDdong3;
        ActivityMain.showerState = true;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        setLog("doInBackground");
        try {
            for (int j=0; j<3; j++){
                for(int i =1; i<10; i++){
                    publishProgress(i,j);
                    Thread.sleep(70);
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
        imgWash.setImageURI(Uri.parse(uri+values[0].toString()));
        if(values[0]==9){
            switch (values[1]){
                case 0:
                    imgDdong1.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    imgDdong2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    imgDdong3.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        setLog("onPostExecute");
        super.onPostExecute(aVoid);
        imgWash.setVisibility(View.INVISIBLE);
        ActivityMain.showerState = false;
    }
    //로그생성용 메소드
    public void setLog(String content) {
        Log.i(TAG, content);
    }
}
