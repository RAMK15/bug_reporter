package com.android.ram.bug_reporter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class AccessibilityTestingActivity extends AppCompatActivity {
    TextView t;
    SharedPreferences UIdata;
    SharedPreferences.Editor UIdataeditor;
    Set<String> UIElementsSet=new HashSet<String>();
    SharedPreferences EventsCapturedata;
    SharedPreferences.Editor EventsCapturedataeditor;
    Set<String> CapturedEventsSet=new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_testing);
        t=(TextView)findViewById(R.id.accessibility_textview);
        UIdata=getSharedPreferences("UIdata", Context.MODE_PRIVATE);
        EventsCapturedata=getSharedPreferences("EventsCapturedData",Context.MODE_PRIVATE);
        Set<String> temp=new HashSet<String>();
        UIElementsSet=UIdata.getStringSet("UIdata", temp);
        CapturedEventsSet=EventsCapturedata.getStringSet("EventsCapturedData",temp);
        String text="";
        for (String s:UIElementsSet){
            text=text+s;
        }
        t.setText(text);
        t.append("\n=========log========\n");
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line+"\n");
            }
            t.append(log.toString());
        }
        catch (IOException e) {}


    }
}
