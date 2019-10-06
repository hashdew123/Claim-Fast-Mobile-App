package com.example.claimfast2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class claimDriverForm extends AppCompatActivity {
    private static final String TAG = "claimDriverForm";

    private static final String KEY_Driver_Name = "Driver Name";
    private static final String KEY_License_no = "License Number";
    private static final String KEY_License_Exp = "License Expire Date";
    private static final String KEY_Categories = "Categories";
    private static final String KEY_NIC = "NIC";

    private EditText Driver_Name;
    private EditText License_no;
    private EditText License_Exp;
    private EditText Categories;
    private EditText NIC;

    private Button next;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_driver_form);

        Driver_Name = findViewById(R.id.driverName);
        License_no = findViewById(R.id.licenceNo);
        License_Exp = findViewById(R.id.LicenceExp);
        Categories = findViewById(R.id.category);
        NIC = findViewById(R.id.NIC);

//        next = (Button) findViewById(R.id.next);

//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(claimDriverForm.this, claimDamageForm.class);
//                startActivity(intent);
//                finish();
//                return;
//            }
//        });

    }

    public void SaveDriverClaim(View v){
        String dname = Driver_Name.getText().toString();
        String license_No = License_no.getText().toString();
        String license_exp = License_Exp.getText().toString();
        String categories = Categories.getText().toString();
        String nic = NIC.getText().toString();

        Map<String, Object> driverClaim = new HashMap<>();
        driverClaim.put(KEY_Driver_Name, dname);
        driverClaim.put(KEY_License_no, license_No);
        driverClaim.put(KEY_License_Exp, license_exp);
        driverClaim.put(KEY_Categories, categories);
        driverClaim.put(KEY_NIC, nic);

        db.collection("Claims").document(  "Driver Claim Details").set(driverClaim)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(claimDriverForm.this,"Successful",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(claimDriverForm.this, claimDamageForm.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(claimDriverForm.this,"Error",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

    }
}
