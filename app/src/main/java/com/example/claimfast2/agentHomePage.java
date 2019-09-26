package com.example.claimfast2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class agentHomePage extends AppCompatActivity {

    private Button btn_accidentMap;
    private Button btn_submitclaim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_home_page);

        btn_accidentMap = (Button) findViewById(R.id.accidentMap);

        btn_accidentMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(agentHomePage.this, agentMap.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
