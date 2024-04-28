package com.example.flash_com;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button sendButton;
    Button receiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.sendBtn);
        receiveButton = findViewById(R.id.ReceiveBtn);

        sendButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,SendActivity.class);
            startActivity(intent);
        });

        receiveButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,ReceiveActivity.class);
            startActivity(intent);
        });
    }
}