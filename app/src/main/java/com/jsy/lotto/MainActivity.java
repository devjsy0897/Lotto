package com.jsy.lotto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import java.util.Random;

import javax.xml.transform.Templates;

public class MainActivity extends AppCompatActivity {


    TextView num1,num2,num3,num4,num5,num6;
    Button btnGetNum;

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
        btnGetNum = (Button)findViewById(R.id.btnGetNum);

        btnGetNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num[]=new int[7];
                for(int i=1;i<7;i++){
                    Random r = new Random();
                    num[i]=r.nextInt(45)+1;
                    for(int j=1;j<i;j++){
                        if(num[i]==num[j]){
                            i--;
                        }
                    }


                }

                for (int i = 1; i < 7; i++) {
                    for (int j = i + 1; j < 7; j++) {
                        if (num[i] > num[j]) {
                            int non = num[i];
                            num[i] = num[j];
                            num[j] = non;
                        }
                    }
                }


                num1.setText(num[1]+"");
                num2.setText(num[2]+"");
                num3.setText(num[3]+"");
                num4.setText(num[4]+"");
                num5.setText(num[5]+"");
                num6.setText(num[6]+"");

            }
        });




    }


}