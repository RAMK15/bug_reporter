package com.android.ram.bug_reporter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BugReportFormActivity extends AppCompatActivity {
    private ViewGroup mLineraLayout;
    Button add_steps_button;
    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report_form);
        add_steps_button=(Button)findViewById(R.id.add_steps);
        add_steps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLayout("button","click","x=1,y=2","description");
            }
        });
        mLineraLayout=(ViewGroup)findViewById(R.id.events_sequence);
        init_bug_report();
    }
    private void addLayout(int i){
        View layout= LayoutInflater.from(this).inflate(R.layout.steps_layout,mLineraLayout,false);
        TextView t=(TextView)layout.findViewById(R.id.step_no);
        t.setText("step "+i);
        mLineraLayout.addView(layout);
    }
    private void addLayout(String type,String action,String loc,String descr){
        View layout= LayoutInflater.from(this).inflate(R.layout.steps_layout,mLineraLayout,false);
        TextView t=(TextView)layout.findViewById(R.id.step_no);
        t.setText("step "+i++);
        EditText et_type=(EditText)layout.findViewById(R.id.class_type);
        EditText et_action=(EditText)layout.findViewById(R.id.action_type);
        EditText et_loc=(EditText)layout.findViewById(R.id.location);
        EditText et_descr=(EditText)layout.findViewById(R.id.descr);
        et_action.setText(action);
        et_type.setText(type);
        et_loc.setText(loc);
        et_descr.setText(descr);
        mLineraLayout.addView(layout);
    }
    private void addLayout(String s){
        int i=0,a=0,b=0,c=0,d=0;
        // String eventDetail=(nstep+1)+"#"+eventType+"#"+className+"#"+TextPart+"#"+eventLoc;
        while(i<s.length()){
            if(a==0 && s.charAt(i)=='#')
                a=i;
            else if(b==0 && s.charAt(i)=='#')
                b=i;
            else if(c==0 && s.charAt(i)=='#')
                c=i;
            else if(d==0 && s.charAt(i)=='#')
                d=i;
            i++;
        }
        String eventType=s.substring(a+1,b);
        String className=s.substring(b+1,c);
        String TextPart=s.substring(c+1,d);
        String eventLoc=s.substring(d+1);
        addLayout(className,eventType,eventLoc,TextPart);
    }
    private void init_bug_report(){
        EditText device_name=findViewById(R.id.DeviceName);
        EditText device_manufacturer=findViewById(R.id.DeviceManufacturer);
        EditText device_brand=findViewById(R.id.DeviceBrand);
        EditText device=findViewById(R.id.Device);
        EditText os_name=findViewById(R.id.OSInfo);
        EditText appname=findViewById(R.id.AppName);
        EditText orientation=findViewById(R.id.Orientation);
        EditText apilevel=findViewById(R.id.API);

        String deviceName = android.os.Build.MODEL;
        String deviceMan = android.os.Build.MANUFACTURER;
        String brand = android.os.Build.BRAND;
        String bootLoaderVersion = Build.BOOTLOADER;
        String device_ = android.os.Build.DEVICE;
        String os_version = System.getProperty("os.version");
        int api=android.os.Build.VERSION.SDK_INT;

        String rotation="";
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            rotation+="portrait";
        else
            rotation+="landscape";

        device_name.setText(deviceName);
        device_manufacturer.setText(deviceMan);
        device_brand.setText(brand);
        device.setText(device_);
        os_name.setText(System.getProperty("os.version"));
        apilevel.setText(""+api);
        orientation.setText(rotation);

        String st="Debug-infos:";
        st += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        st += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        st += "\n Device: " + android.os.Build.DEVICE;
        st += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";

        Toast.makeText(BugReportFormActivity.this,st,Toast.LENGTH_LONG).show();
        appname.setText(st);
        SharedPreferences EventsCapturedata;
        SharedPreferences.Editor EventsCapturedataeditor;
        EventsCapturedata=getSharedPreferences("EventsCapturedData", Context.MODE_PRIVATE);
        Set<String> CapturedEventsSet=new HashSet<String>();
        CapturedEventsSet=EventsCapturedata.getStringSet("EventsCapturedData",CapturedEventsSet);
        int nstep=EventsCapturedata.getInt("step",0);
        List<String> list_of_events=new ArrayList<String>(nstep);
        for (int i=0;i<nstep;i++)
            list_of_events.add("");
        for (String s:CapturedEventsSet){
            int k=0;
            String temp="";
            while(s.charAt(k)!='#'){
                temp+=s.charAt(k++);
            }
            int index=Integer.parseInt(temp);
            list_of_events.set(index-1,s);
        }
        int k=0;
        for(String s:list_of_events){
            addLayout(s);
        }


    }
}