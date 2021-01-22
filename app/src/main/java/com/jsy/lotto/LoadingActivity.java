package com.jsy.lotto;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoadingActivity extends Activity {
    public static int drwNo,drwNo1;
    TextView tvloading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        tvloading = (TextView)findViewById(R.id.tvloading);
       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);*/

        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(1000);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);

//애니메이션 시작
        tvloading.startAnimation(mAnimation);





        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    getNum();
                    Log.i("drwNotest2", drwNo + " " + drwNo1);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        t.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    //get number ↓
    //https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=861

    public void getNum() throws IOException {

        for(int i=500;i<1000;i++) {
            StringBuilder urlBuilder = new StringBuilder("https://www.dhlottery.co.kr/common.do?method=getLottoNumber"); /*URL*/
            urlBuilder.append("&" + URLEncoder.encode("drwNo", "UTF-8") + "=" + URLEncoder.encode(i+"", "UTF-8")); /*페이지번호*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();


            jParsing(sb.toString());
            Log.i("drwNotest",drwNo+"");

            if(drwNo==drwNo1) {
                Log.i("drwNotest2", drwNo + " " + drwNo1);
                break;
            }
            drwNo1=drwNo;
            Log.i("drwNotest1",drwNo+"");


        }
    }
    //get number ↑

    void jParsing(String data){

        try {
            JSONObject jObject = new JSONObject(data);

            drwNo = jObject.getInt("drwNo");

        }catch (Exception e){ Log.i("mytagcatch",e.getLocalizedMessage());}

    }
}