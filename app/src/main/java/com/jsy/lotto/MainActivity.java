package com.jsy.lotto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Random;

import javax.xml.transform.Templates;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    TextView num1,num2,num3,num4,num5,num6,tvguel;
    Button btnGetNum;
    DecimalFormat formatter = new DecimalFormat("###,###");
    public static int drwNo,drwNo1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num1 = (TextView)findViewById(R.id.num1);
        num2 = (TextView)findViewById(R.id.num2);
        num3 = (TextView)findViewById(R.id.num3);
        num4 = (TextView)findViewById(R.id.num4);
        num5 = (TextView)findViewById(R.id.num5);
        num6 = (TextView)findViewById(R.id.num6);
        tvguel = (TextView)findViewById(R.id.tvguel);

        btnGetNum = (Button)findViewById(R.id.btnGetNum);
        myHelper = new myDBHelper(this);

        btnGetNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number[]=new int[7];
                /*for(int i=1;i<7;i++){
                    Random r = new Random();
                    num[i]=r.nextInt(45)+1;
                    for(int j=1;j<i;j++){
                        if(num[i]==num[j]){
                            i--;
                        }
                    }


                }*/

               /* for (int i = 1; i < 7; i++) {
                    for (int j = i + 1; j < 7; j++) {
                        if (num[i] > num[j]) {
                            int non = num[i];
                            num[i] = num[j];
                            num[j] = non;
                        }
                    }
                }*/
/*num[1] = 5;
num[2] = 9;
num[3] = 14;
num[4] = 26;
num[5] = 30;
num[6] = 43;*/
                Random r = new Random();
                int num=r.nextInt(946)+1;

                /*num1.setText(num[1]+"");
                num2.setText(num[2]+"");
                num3.setText(num[3]+"");
                num4.setText(num[4]+"");
                num5.setText(num[5]+"");
                num6.setText(num[6]+"");*/


                sqlDB = myHelper.getReadableDatabase();
                Cursor  cursor;
                //cursor = sqlDB.rawQuery("select * from round where drwtNo1="+num[1]+" and drwtNo2="+num[2]+" and drwtNo3="+num[3]+" and drwtNo4="+num[4]+" and drwtNo5="+num[5]+" and drwtNo6="+num[6]+";",null);
                cursor = sqlDB.rawQuery("select * from round where drwNo="+num+";",null);

                while (cursor.moveToNext()){
                    int drwNo = cursor.getInt(0);
                    long firstWinamnt = cursor.getLong(1);
                    number[1] = cursor.getInt(2);
                    number[2] = cursor.getInt(3);
                    number[3] = cursor.getInt(4);
                    number[4] = cursor.getInt(5);
                    number[5] = cursor.getInt(6);
                    number[6] = cursor.getInt(7);
                    num1.setText(number[1]+"");
                    num2.setText(number[2]+"");
                    num3.setText(number[3]+"");
                    num4.setText(number[4]+"");
                    num5.setText(number[5]+"");
                    num6.setText(number[6]+"");
                    //Log.i("drwNotest",drwNo+"");
                    tvguel.setText(drwNo+"회차 1등 번호입니다.\n 당첨금은 "+formatter.format(firstWinamnt)+"입니다.");

                        break;
                    }



                cursor.close();
                sqlDB.close();

            }
        });




    }


    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "lotto", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}