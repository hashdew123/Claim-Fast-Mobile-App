package com.example.claimfast2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class clientHomePage extends AppCompatActivity {

    private Button reportAccident;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_page);

        reportAccident = (Button) findViewById(R.id.bReportAccident);

        reportAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(clientHomePage.this, clientMap.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
