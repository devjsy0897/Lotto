package com.jsy.lotto;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoadingActivity extends Activity {
    int drwNo,drwNo1,firstWinamnt,drwtNo1,drwtNo2,drwtNo3,drwtNo4,drwtNo5,drwtNo6,bnusNo;
    TextView tvloading;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

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

        myHelper = new myDBHelper(this);
        sqlDB=myHelper.getWritableDatabase();
        myHelper.onUpgrade(sqlDB, 1,2);
        sqlDB.close();



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

        for(int i=800;i<1000;i++) {
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
            //Log.i("drwNotest",drwNo+"");

            if(drwNo==drwNo1) {
                //Log.i("drwNotest2", drwNo + " " + drwNo1);
                break;
            }
            drwNo1=drwNo;
            //Log.i("drwNotest1",drwNo+"");



        }
    }
    //get number ↑

    void jParsing(String data){

        try {

            myHelper = new myDBHelper(this);
            JSONObject jObject = new JSONObject(data);
            //Log.i("drwNotest",drwNo+"");
            drwNo = jObject.getInt("drwNo");
            firstWinamnt = jObject.getInt("firstWinamnt");
            drwtNo1 = jObject.getInt("drwtNo1");
            drwtNo2 = jObject.getInt("drwtNo2");
            drwtNo3 = jObject.getInt("drwtNo3");
            drwtNo4 = jObject.getInt("drwtNo4");
            drwtNo5 = jObject.getInt("drwtNo5");
            drwtNo6 = jObject.getInt("drwtNo6");
            bnusNo = jObject.getInt("bnusNo");
            //Log.i("drwNotest1",drwNo+"");
            sqlDB=myHelper.getWritableDatabase();
            //Log.i("drwNotest2",drwNo+"");

            //Log.i("drwNotest3",drwNo+"");
            //myHelper.onCreate(sqlDB);

            Log.i("drwNotest4",drwNo+"");

            Log.i("drwNotest5",drwNo+"");

            sqlDB.execSQL("insert into round values( "+drwNo+","+firstWinamnt+","+drwtNo1+","+drwtNo2+","+drwtNo3+","+drwtNo4+","+drwtNo5+","+drwtNo6+","+bnusNo+" );");
            Log.i("drwNotest6",drwNo+"");
            sqlDB.close();
            Log.i("drwNotest7",drwNo+"");

        }catch (Exception e){ Log.i("mytagcatch",e.getLocalizedMessage());}

    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context){
            super(context,"lotto",null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table round ( drwNo integer primary key, firstWinamnt text, " +
                    "drwtNo1 integer, drwtNo2 integer, drwtNo3 integer, drwtNo4 integer, drwtNo5 integer, drwtNo6 integer, bnusNo integer);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists round");
            onCreate(db);
        }
    }
}