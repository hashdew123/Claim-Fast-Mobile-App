package com.example.claimfast2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class selectUser extends AppCompatActivity {

    LinearLayout linear1,linear2;
    private Button bClient,bAgent;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            linear1.setVisibility(View.VISIBLE);
            linear2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        linear1 = (LinearLayout) findViewById(R.id.linear1);
        linear2 = (LinearLayout) findViewById(R.id.linear2);

        handler.postDelayed(runnable, 3000);

        bClient = (Button) findViewById(R.id.bClient);
        bAgent = (Button) findViewById(R.id.bAgent);

        bClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectUser.this, clientLogin.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        bAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectUser.this, agentLogin.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }
}
