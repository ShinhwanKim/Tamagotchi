package com.example.tamagotchi;

import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadWeather extends Thread{
    private String TAG = "ThreadWeather";
    JSONArray JAItem;
    Double percentRain;
    Double humidity;
    Double skyState;
    Double temperature;
    Double wind;
    @Override
    public void run() {
        super.run();

        try {
            long now = System.currentTimeMillis();
            Date dDate = new Date(now);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH");
            String getTime = sdf.format(dDate);
            Log.e("로그","측정한 현재 날짜는 1 "+getTime);

            String[] nowDate = new String[3];
            nowDate[0]=getTime.split(",")[0];
            int MM = Integer.parseInt(getTime.split(",")[1]);
            nowDate[1]=Integer.toString(MM);
            int dd = Integer.parseInt(getTime.split(",")[2]);
            nowDate[2]=Integer.toString(dd);
            /*String baseDate = nowDate[0]+nowDate[1]+nowDate[2];*/
            String baseDate = getTime.split(",")[0]+getTime.split(",")[1]+getTime.split(",")[2];
            String currentDate = getTime.split(",")[0]+"년 "+MM+"월 "+dd+"일";
            int HH = Integer.parseInt(getTime.split(",")[3]);
            if(HH<2){
                HH=23;
            }else if(HH>=2 && HH<5){
                HH=2;
            }else if(HH>=5 && HH<8){
                HH=5;
            }else if(HH>=8 && HH<11){
                HH=8;
            }else if(HH>=11 && HH<14){
                HH=11;
            }else if(HH>=14 && HH<17){
                HH=14;
            }else if(HH>=17 && HH<20){
                HH=17;
            }else if(HH>=20 && HH<23){
                HH=20;
            }else if(HH>=23 && HH<=24){
                HH=23;
            }

            Log.e("로그","정리된 현재 날짜는 1 "+baseDate);
            Log.e("로그","측정된 현재 날짜는 1 "+HH);

            setLog("doInBackground");
            String nx="60";
            String ny="125";
            String baseTime=null;
            if(HH<10){
                baseTime="0"+HH+"00";
            }else if(HH>=10){
                baseTime=HH+"00";
            }
            Log.e("로그","수정된 현재 날짜는 1 "+baseTime);
            /*String baseTime = "0500";*/
            String serviceKey="LdYe5Lkt9gAf4GBrGCFHAqiHeVQiIILYR62PNpydYqWA8gnCfOTvyv6XDQLSuuhQ%2FsUSVaZSBv9uAxFcUxrUmA%3D%3D";
            String urlStr ="http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?" + "ServiceKey="
                    +serviceKey + "&base_date=" + baseDate + "&base_time=" + baseTime + "&nx="+ nx + "&ny=" + ny + "&_type=json";
            setLog("안됨1");
            /*11= new URL("http://naver.com");*/
            URL url = new URL(urlStr);
            BufferedReader bf ;
            String line=null;
            String result="";
            setLog("안됨2");
            bf = new BufferedReader(new InputStreamReader(url.openStream()));
            setLog("안됨3");
            line=bf.readLine();
            setLog(line);
            setLog("안됨4");
            JSONObject obj = new JSONObject(line);
            setLog("안됨5");
            setLog(obj.toString());
            setLog("안됨6");
            setLog("안됨7"+obj.getJSONObject("response").toString());
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
            Double[] weatherData = new Double[5];
            weatherData[0]=percentRain;
            weatherData[1]=humidity;
            weatherData[2]=skyState;
            weatherData[3]=temperature;
            weatherData[4]=wind;

            Message msgOn = ActivityMain.weatherHandler.obtainMessage();
            msgOn.what=1;
            msgOn.obj=weatherData;
            ActivityMain.weatherHandler.sendMessage(msgOn);

            Thread.sleep(6000);
            Message msgOff = ActivityMain.weatherHandler.obtainMessage();
            msgOff.what=2;
            ActivityMain.weatherHandler.sendMessage(msgOff);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setLog(String content) {
        Log.i(TAG, content);
        /*private String TAG = "ActivityMain";*/
    }
}
