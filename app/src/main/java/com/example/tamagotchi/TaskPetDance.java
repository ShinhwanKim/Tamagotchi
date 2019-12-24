package com.example.tamagotchi;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class TaskPetDance extends AsyncTask<Void,Integer,Void> {
    private String TAG = "TaskPetDance";
    ImageView imgPet;
    String uri;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        imgPet = ActivityMain.imgPet;
        uri = "android.resource://com.example.tamagotchi/drawable/dance";
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            for(int j = 0;j<3; j++){
                for(int i =1; i<33; i++){
                    setLog("돌아간다"+i);
                    publishProgress(i);
                    Thread.sleep(50);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        uri = uri+values[0].toString();
        imgPet.setImageURI(Uri.parse(uri));
        uri = "android.resource://com.example.tamagotchi/drawable/dance";
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/mypet"));
        super.onPostExecute(aVoid);
    }

    //로그생성용 메소드
    public void setLog(String content) {
        Log.i(TAG, content);
        /*private String TAG = "TaskPetDance";*/
    }
}
