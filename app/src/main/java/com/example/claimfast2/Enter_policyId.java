package com.example.claimfast2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.protobuf.Empty;

public class Enter_policyId extends AppCompatActivity {

    public static final String POLICY_ID = "com.claimfast.app.POLICY_ID";
    private Button enter;
    private EditText policyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_policy_id);

        policyId = (EditText) findViewById(R.id.txt_policyid);
        enter = (Button) findViewById(R.id.btn_enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pol_ID = policyId.getText().toString();
                System.out.println("this is the pol_ID 1 " + pol_ID);
                Intent intent = new Intent(Enter_policyId.this, claimDriverForm.class);
                    intent.putExtra(POLICY_ID, pol_ID);
                    System.out.println("this is the pol ID " + pol_ID);
                    startActivity(intent);
                    finish();
                    return;
                }
        });
    }


    public void goBack(View v){
        Intent intent = new Intent(Enter_policyId.this, agentHomePage.class);
        startActivity(intent);
        finish();
        return;
    }


}
