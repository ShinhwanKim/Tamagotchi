package com.example.tamagotchi;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class TaskWeather extends AsyncTask<Void,Double,Void> {
    private String TAG = "TaskWeather";
    JSONArray JAItem;
    Double percentRain;
    Double humidity;
    Double skyState;
    Double temperature;
    Double wind;

    @Override
    protected Void doInBackground(Void... voids) {
        setLog("doInBackground");
        String nx="92";
        String ny="131";
        String baseDate = "20190525";
        String baseTime = "0500";
        String serviceKey="LdYe5Lkt9gAf4GBrGCFHAqiHeVQiIILYR62PNpydYqWA8gnCfOTvyv6XDQLSuuhQ%2FsUSVaZSBv9uAxFcUxrUmA%3D%3D";
        String urlStr ="http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?" + "ServiceKey="
                +serviceKey + "&base_date=" + baseDate + "&base_time=" + baseTime + "&nx="+ nx + "&ny=" + ny + "&_type=json";
        setLog("안됨1");
        try {
            URL url = new URL("http://naver.com");
            /*URL url = new URL(urlStr);*/
            BufferedReader bf ;
            String line=null;
            String result="";
            setLog("안됨2");
            bf = new BufferedReader(new InputStreamReader(url.openStream()));
            setLog("안됨3");
            line=bf.readLine();
            setLog("안됨4");
            JSONObject obj = new JSONObject(line);
            setLog(obj.toString());
            setLog("안됨5");
            setLog("안됨6"+obj.getJSONObject("response").toString());
            JSONObject JsonResponse = obj.getJSONObject("response");
            JSONObject JsonBody = JsonResponse.getJSONObject("body");
            JSONObject JsonItem = JsonBody.getJSONObject("items");

            JAItem = (JSONArray) JsonItem.getJSONArray("item");
            setLog("안됨7"+JAItem.toString());

            JSONObject JsonInfo;
            Log.e("안녕","안됨6"+JAItem.getJSONObject(0));
            Log.e("안녕","안됨7"+JAItem.length());
            for(int i=0;i<JAItem.length();i++){
                JsonInfo = JAItem.getJSONObject(i);
                String base_Date = JsonInfo.getString("baseDate");
                String fcst_Time = JsonInfo.getString("fcstTime");
                Double fcstValue = JsonInfo.getDouble("fcstValue");
                String category = JsonInfo.getString("category");
                setLog("배열의 "+i+"번째 요소");
                setLog("category= "+category);
                setLog("fcstValue= "+fcstValue);

                switch (category){
                    case "POP": //강수확률
                        percentRain = fcstValue;
                        break;
                    case "REH": //습도
                        humidity = fcstValue;
                        break;
                    case "SKY": //하늘구름 상태
                        skyState = fcstValue;
                        break;
                    case "T3H": //3시간 기온
                        temperature = fcstValue;
                        break;
                    case "WSD": //풍속
                        wind = fcstValue;
                        break;
                }
            }
            publishProgress(percentRain,humidity,skyState,temperature,wind);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        setLog("onProgressUpdate");
        super.onProgressUpdate(values);
        ActivityMain.txtWeather.setText("현재 날씨 상태입니다."+"\n비올 확률:"+values[0].toString()
        +"\n습도:"+values[1].toString()+"\n구름상태:"+values[2].toString()+"\n현재 온도:"+values[3].toString()
        +"\n바람세기:"+values[4].toString());
    }
    public void setLog(String content) {
        Log.i(TAG, content);
        /*private String TAG = "ActivityMain";*/
    }
}
