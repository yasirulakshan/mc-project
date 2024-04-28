package com.example.flash_com;

import android.app.Activity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class ReceiveActivity extends Activity {

    Button scanButton;
    TextView previousMessageTextView;
//    Button button2;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        addListenerOnButton();

        previousMessageTextView = findViewById(R.id.previous_message);
        try{
            String data = getIntent().getStringExtra("key");

            if(!data.isEmpty()){
                previousMessageTextView.setText(data);
            }
        }
        catch (Exception e){
            System.out.println("Exception triggered");
        }



    }

    public void addListenerOnButton() {
        final Context context = this;

        scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(arg0 -> {
            Intent intent = new Intent(context, CameraScreen.class);
            startActivity(intent);

        });

    }
}