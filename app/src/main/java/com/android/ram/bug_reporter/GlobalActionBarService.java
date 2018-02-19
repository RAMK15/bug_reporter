package com.android.ram.bug_reporter;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class GlobalActionBarService extends AccessibilityService {
    FrameLayout mLayout;
    SharedPreferences UIdata;
    SharedPreferences.Editor UIdataeditor;
    SharedPreferences EventsCapturedata;
    SharedPreferences.Editor EventsCapturedataeditor;
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // Create an overlay and display the action bar
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayout = new FrameLayout(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.action_bar, mLayout);
        wm.addView(mLayout, lp);
        configurePowerButton();
        configureScrollButton();
        configureVolumeButton();
        configureSwipeButton();


    }

    @Override
    protected boolean onGesture(int gestureId) {
        Toast.makeText(GlobalActionBarService.this,"onGesture"+gestureId,Toast.LENGTH_LONG).show();
        return super.onGesture(gestureId);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Toast.makeText(GlobalActionBarService.this,"onKeyEvent"+event.toString(),Toast.LENGTH_LONG).show();
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Toast.makeText(GlobalActionBarService.this,"accessibility event :"+accessibilityEvent.toString(),Toast.LENGTH_LONG).show();
        EventsCapturedata=getSharedPreferences("EventsCapturedData",Context.MODE_PRIVATE);
        Boolean isRecording=EventsCapturedata.getBoolean("recording",false);
        if(isRecording){
            Set<String> CapturedEventsSet=new HashSet<String>();
            CapturedEventsSet=EventsCapturedata.getStringSet("EventsCapturedData",CapturedEventsSet);
            CapturedEventsSet.add(accessibilityEvent.toString());
            EventsCapturedataeditor=EventsCapturedata.edit();
            EventsCapturedataeditor.remove("EventsCapturedData");
            //Set<String> CapturedEventsSet=new HashSet<String>();
            EventsCapturedataeditor.putStringSet("EventsCapturedData",CapturedEventsSet);
            EventsCapturedataeditor.commit();
        }
    }

    public  void  startRecordingEvents(){
        Toast.makeText(GlobalActionBarService.this,"start recording events",Toast.LENGTH_LONG).show();
        EventsCapturedata=getSharedPreferences("EventsCapturedData",Context.MODE_PRIVATE);
        EventsCapturedataeditor=EventsCapturedata.edit();
        EventsCapturedataeditor.remove("EventsCapturedData");
        Set<String> CapturedEventsSet=new HashSet<String>();
        EventsCapturedataeditor.putStringSet("EventsCapturedData",CapturedEventsSet);
        EventsCapturedataeditor.putBoolean("recording",true);
        EventsCapturedataeditor.commit();
    }
    public  void  stopRecordingEvents(){
        Toast.makeText(GlobalActionBarService.this,"stop recording events",Toast.LENGTH_LONG).show();
        EventsCapturedata=getSharedPreferences("EventsCapturedData",Context.MODE_PRIVATE);
        EventsCapturedataeditor=EventsCapturedata.edit();
        EventsCapturedataeditor.putBoolean("recording",false);
        EventsCapturedataeditor.commit();
    }

    @Override
    public void onInterrupt() {

    }

    private void configurePowerButton() {
        Button powerButton = (Button) mLayout.findViewById(R.id.power);
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
                AccessibilityNodeInfo root = getRootInActiveWindow();
                findAllUIElements(root);
            }
        });
    }
    private AccessibilityNodeInfo findScrollableNode(AccessibilityNodeInfo root) {
        Deque<AccessibilityNodeInfo> deque = new ArrayDeque<>();
        deque.add(root);

        while (!deque.isEmpty()) {
            AccessibilityNodeInfo node = deque.removeFirst();
            if (node.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)) {
                return node;
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                deque.addLast(node.getChild(i));
            }
        }

        return null;
    }
    private AccessibilityNodeInfo findAllUIElements(AccessibilityNodeInfo root) {
        Deque<AccessibilityNodeInfo> deque = new ArrayDeque<>();
        UIdata=getSharedPreferences("UIdata", Context.MODE_PRIVATE);
        UIdataeditor=UIdata.edit();
        UIdataeditor.remove("UIdata");
        Set<String> UIElementsSet=new HashSet<String>();
        UIdataeditor.commit();
        deque.add(root);
        while (!deque.isEmpty()) {
            AccessibilityNodeInfo node = deque.removeFirst();
            UIElementsSet.add(node.toString()+"\n========================\n");
            for (int i = 0; i < node.getChildCount(); i++) {
                deque.addLast(node.getChild(i));
            }
        }
        UIdataeditor.putStringSet("UIdata",UIElementsSet );
        UIdataeditor.commit();
        return null;
    }

    private void configureScrollButton() {
        Button scrollButton = (Button) mLayout.findViewById(R.id.scroll);
        scrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessibilityNodeInfo scrollable = findScrollableNode(getRootInActiveWindow());
                if (scrollable != null) {
                    scrollable.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD.getId());
                }
            }
        });
    }
    private void configureVolumeButton() {
        Button volumeUpButton = (Button) mLayout.findViewById(R.id.volume_up);
        volumeUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecordingEvents();
            }
        });
    }
    private void configureSwipeButton() {
        Button swipeButton = (Button) mLayout.findViewById(R.id.swipe);
        swipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecordingEvents();
            }
        });
    }
}
