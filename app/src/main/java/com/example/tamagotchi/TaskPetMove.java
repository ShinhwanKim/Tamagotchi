package com.example.tamagotchi;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class TaskPetMove extends AsyncTask<Integer,Integer,Integer> {
    private String TAG = "TaskPetMove";
    ImageView imgPet;

    @Override
    protected void onPreExecute() {
        setLog("onPreExecute");
        super.onPreExecute();
        imgPet = ActivityMain.imgPet;
        imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move1"));
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        setLog("doInBackground");
        try {
            Thread.sleep(200);
            for(int i =0; i<11; i++){
                setLog("돌아간다"+i);
                publishProgress(i);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        setLog("onProgressUpdate");
        switch (values[0]){
            case 0:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move2"));
                break;
            case 1:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move3"));
                break;
            case 2:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move4"));
                break;
            case 3:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move1"));
                break;
            case 4:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move2"));
                break;
            case 5:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move3"));
                break;
            case 6:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move4"));
                break;
            case 7:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move1"));
                break;
            case 8:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move2"));
                break;
            case 9:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move3"));
                break;
            case 10:
                imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/move4"));
                break;
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        imgPet.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/mypet"));
        super.onPostExecute(integer);
    }

    //로그생성용 메소드
    public void setLog(String content) {
        Log.i(TAG, content);

    }
}
