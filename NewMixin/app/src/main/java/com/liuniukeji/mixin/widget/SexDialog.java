package com.liuniukeji.mixin.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import com.liuniukeji.mixin.R;


/**
 * 弹出提示
 */
public class SexDialog extends Dialog implements OnClickListener {
    private LayoutInflater factory;
    private TextView sureView;
    private TextView noView;
    private RadioButton maleView;
    private RadioButton femaleView;
    private RadioButton scretView;
    private String msex;
    private Context context;


    public SexDialog(Context context, String sex) {
        super(context, R.style.mydialogstyle);
        this.msex = sex;
        this.context = context;
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(factory.inflate(R.layout.sex_dialog, null));
        sureView = (TextView) findViewById(R.id.yes);
        noView = (TextView) findViewById(R.id.no);
        maleView = (RadioButton) findViewById(R.id.rb_male);
        femaleView = (RadioButton) findViewById(R.id.rb_female);
        scretView = findViewById(R.id.rb_secret);
        switch (msex){
            case "0":
                scretView.setChecked(true);
                break;
            case "1":
                femaleView.setChecked(true);
                break;
            case "2":
                maleView.setChecked(true);
                break;
        }


        sureView.setOnClickListener(this);
        noView.setOnClickListener(this);
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay();
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        this.setCanceledOnTouchOutside(false);
        window.setWindowAnimations(R.style.mydialogstyle);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.yes) {
            if(scretView.isChecked()){
                msex = "0";
            }
            if(femaleView.isChecked()){
                msex = "1";
            }

            if(maleView.isChecked()){
                msex = "2";
            }
            onRight(msex);
            this.dismiss();
        } else if (i == R.id.no) {
            onLeft();
            this.dismiss();
        }
    }

    public void onLeft() {

    }

    public void onRight(String msex){

    }

}
