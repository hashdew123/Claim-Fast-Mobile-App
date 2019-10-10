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
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class claimDriverForm extends AppCompatActivity {
    private static final String TAG = "claimDriverForm";

    private static final String KEY_Driver_Name = "Driver Name";
    private static final String KEY_License_no = "License Number";
    private static final String KEY_License_Exp = "License Expire Date";
    private static final String KEY_Categories = "Categories";
    private static final String KEY_NIC = "NIC";
    private static final String KEY_TH_VehiNo = "Third party Vehicle No";
    private static final String KEY_TH_Name = "Third party Name";
    private static final String KEY_TH_Nic = "Third party Nic";
    private static final String KEY_TH_Address = "Third party Address";
    private static final String KEY_TH_ContactNo = "Third party Contact No";

    private EditText Driver_Name;
    private EditText License_no;
    private EditText License_Exp;
    private EditText Categories;
    private EditText NIC;

    private EditText th_VehiNo;
    private EditText th_name;
    private EditText th_nic;
    private EditText th_address;
    private EditText th_contactNo;

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

        th_VehiNo = findViewById(R.id.Th_vehiNo);
        th_name = findViewById(R.id.Th_name);
        th_nic = findViewById(R.id.Th_nic);
        th_address = findViewById(R.id.Th_address);
        th_contactNo = findViewById(R.id.Th_contactNo);


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

        String get_th_vehino = th_VehiNo.getText().toString();
        String get_th_name = th_name.getText().toString();
        String get_th_nic = th_nic.getText().toString();
        String get_th_address = th_address.getText().toString();
        String get_th_contactno = th_contactNo.getText().toString();

        Map<String, Object> driverClaim = new HashMap<>();
        driverClaim.put(KEY_Driver_Name, dname);
        driverClaim.put(KEY_License_no, license_No);
        driverClaim.put(KEY_License_Exp, license_exp);
        driverClaim.put(KEY_Categories, categories);
        driverClaim.put(KEY_NIC, nic);

        driverClaim.put(KEY_TH_VehiNo, get_th_vehino);
        driverClaim.put(KEY_TH_Name, get_th_name);
        driverClaim.put(KEY_TH_Nic, get_th_nic);
        driverClaim.put(KEY_TH_Address, get_th_address);
        driverClaim.put(KEY_TH_ContactNo, get_th_contactno);

        //Get policy ID from policy ID page
        Intent intent = getIntent();
        String policy_Id = intent.getStringExtra(Enter_policyId.POLICY_ID);
//        Toast.makeText(claimDriverForm.this,"Successful" + policy_Id+ "YES",Toast.LENGTH_SHORT).show();
//        System.out.println("This is the policy ID"+ policy_Id + "Got it");

        db.collection(policy_Id).document("Driver Claim Details").set(driverClaim, SetOptions.merge())
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
