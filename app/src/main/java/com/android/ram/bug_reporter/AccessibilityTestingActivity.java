package com.android.ram.bug_reporter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class AccessibilityTestingActivity extends AppCompatActivity {
    TextView t;
    SharedPreferences UIdata;
    SharedPreferences.Editor UIdataeditor;
    Set<String> UIElementsSet=new HashSet<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_testing);
        t=(TextView)findViewById(R.id.accessibility_textview);
        UIdata=getSharedPreferences("UIdata", Context.MODE_PRIVATE);
        UIdataeditor=UIdata.edit();
        Set<String> temp=new HashSet<String>();
        UIElementsSet=UIdata.getStringSet("UIdata", temp);
        String text="";
        for (String s:UIElementsSet){
            text=text+s;
        }
        t.setText(text);
    }
}
