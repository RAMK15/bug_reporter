package com.android.ram.bug_reporter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                addLayout("button","click","x=1,y=2");
            }
        });
        /*FloatingActionButton fab=findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLayout(i++);
            }
        });*/
        mLineraLayout=(ViewGroup)findViewById(R.id.events_sequence);
    }
    private void addLayout(int i){
        View layout= LayoutInflater.from(this).inflate(R.layout.steps_layout,mLineraLayout,false);
        TextView t=(TextView)layout.findViewById(R.id.step_no);
        t.setText("step "+i);
        mLineraLayout.addView(layout);
    }
    private void addLayout(String type,String action,String loc){
        View layout= LayoutInflater.from(this).inflate(R.layout.steps_layout,mLineraLayout,false);
        TextView t=(TextView)layout.findViewById(R.id.step_no);
        t.setText("step "+i);
        EditText et_type=(EditText)layout.findViewById(R.id.class_type);
        EditText et_action=(EditText)layout.findViewById(R.id.action_type);
        EditText et_loc=(EditText)layout.findViewById(R.id.location);
        et_action.setText(action);
        et_type.setText(type);
        et_loc.setText(loc);
        mLineraLayout.addView(layout);
    }
    private void init_bug_report(){
        EditText device_name=findViewById(R.id.DeviceInfo);
        EditText os_name=findViewById(R.id.OSInfo);
        EditText appname=findViewById(R.id.AppName);
        EditText orient=findViewById(R.id.Orientation);
    }
}
