package com.example.claimfast2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class clientUserProfile extends AppCompatActivity {

    private static final String TAG = "Policy Details";

    private static final String KEY_NAME = "Name";
    private static final String KEY_AGE = "Age";
    private static final String KEY_DOB = "DOB";

    private TextView textViewDetails;
    private EditText policyID;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_user_profile);

        textViewDetails = findViewById(R.id.txt_load_details);
        policyID = findViewById(R.id.txt_policyId);
    }

    public void loadDetails(View v){
        String policy_id = policyID.getText().toString();
        DocumentReference policyDoc =  db.collection("policy_details").document(policy_id);
        policyDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    textViewDetails.setText("Loading...");
                    String name = documentSnapshot.getString(KEY_NAME);
                    String age = documentSnapshot.getString(KEY_AGE);
                    String dob = documentSnapshot.getString(KEY_DOB);
                    textViewDetails.setText("Name: "+name +"\n"+"Age: "+age +"\n"+"Date of birth: "+dob);
                }else{
                    new SweetAlertDialog(clientUserProfile.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Policy ID is invalid, Please enter again")
                            .setConfirmText("Ok").show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new SweetAlertDialog(clientUserProfile.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(Log.d(TAG, e.toString()))
                                .setConfirmText("Ok").show();
                    }
                });

    }
}
