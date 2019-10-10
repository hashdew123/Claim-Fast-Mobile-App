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

public class claimDamageForm extends AppCompatActivity {
    private static final String TAG = "claimDamageForm";


    private static final String KEY_Damaage_Cause = "Cause of damage";
    private static final String KEY_Knocked_On = "Knocked on";
    private static final String KEY_Accident_Place = "Place of Accident";
    private static final String KEY_notes = "Special Notes";
    private static final String KEY_TH_Damage_Nature = "Third party nature of damage";
    private static final String KEY_TH_claiment = "Third party claiment";
    private static final String KEY_TH_Claim_Amount = "Third party Claimed amount";

    private EditText DamaageCause;
    private EditText KnockedOn;
    private EditText AccidentPlace;
    private EditText Notes;
    private EditText th_DamageNature;
    private EditText th_claiment;
    private EditText th_claimAmount;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_damage_form);

        DamaageCause = findViewById(R.id.causeDamage);
        KnockedOn = findViewById(R.id.knockedOn);
        AccidentPlace = findViewById(R.id.placeOfAccident);
        Notes = findViewById(R.id.specialNotes);
        th_DamageNature = findViewById(R.id.natureOfDamage);
        th_claiment = findViewById(R.id.thirdPartyClaiment);
        th_claimAmount = findViewById(R.id.amountClaimed);
    }

    public void SaveDamageDetail(View v){

        String damageCause = DamaageCause.getText().toString();
        String licenseNo = KnockedOn.getText().toString();
        String accidentPlace = AccidentPlace.getText().toString();
        String notes = Notes.getText().toString();
        String damageNature = th_DamageNature.getText().toString();
        String claiment = th_claiment.getText().toString();
        String claimAmount = th_claimAmount.getText().toString();


        Map<String, Object> damageDetails = new HashMap<>();
        damageDetails.put(KEY_Damaage_Cause, damageCause);
        damageDetails.put(KEY_Knocked_On, licenseNo);
        damageDetails.put(KEY_Accident_Place, accidentPlace);
        damageDetails.put(KEY_notes, notes);
        damageDetails.put(KEY_TH_Damage_Nature, damageNature);
        damageDetails.put(KEY_TH_claiment, claiment);
        damageDetails.put(KEY_TH_Claim_Amount, claimAmount);

        //Get policy ID from policy ID page
        Intent intent = getIntent();
        String policy_Id = intent.getStringExtra(claimDriverForm.POLICY_ID);
       //Toast.makeText(claimDamageForm.this,"policy ID |-"+ policy_Id + "-| okay",Toast.LENGTH_SHORT).show();

        db.collection(policy_Id).document("Driver Damage Details").set(damageDetails, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(claimDamageForm.this,"Successful",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(claimDamageForm.this, Claim_images.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(claimDamageForm.this,"Error",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

    }
}

