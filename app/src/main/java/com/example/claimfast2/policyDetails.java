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

public class policyDetails extends AppCompatActivity {
    private static final String TAG = "Policy Details";

    private static final String KEY_POLICYID = "PolicyID";
    private static final String KEY_NAME = "fullName";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_NIC = "nicNo";
    private static final String KEY_DOB = "dateOfBirth";
    private static final String KEY_CONTACTNO = "contactNo";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_REGNAME = "regName";
    private static final String KEY_VEHICOLOR = "vehicleColor";
    private static final String KEY_ENGNO = "engineNo";
    private static final String KEY_CHANO = "chassisNo";
    private static final String KEY_MANU = "manufacturer";
    private static final String KEY_MODEL = "Model";
    private static final String KEY_YEAR= "year";
    private static final String KEY_ENGCAPACTIY = "engineCapacity";
    private static final String KEY_ABSOWNER = "absoluteOwner";
    private static final String KEY_FINANCIALRIGHTS = "financialRights";
    private static final String KEY_CURRENTDAMAGE = "currentDamages";
    private static final String KEY_PRESENTVALUE= "presentValue";
    private static final String KEY_EXPIREDON= "expiredOn";
    private static final String KEY_COMMENCEDON = "commencedFrom";
    private static final String KEY_NATUREDIS= "natureDisaster";
    private static final String KEY_TERCOVER= "terrorismCover";

    private TextView textViewDetails;
    private EditText policyID;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_details);

        textViewDetails = findViewById(R.id.txt_load_details);
        policyID = findViewById(R.id.txt_policyId);



    }

    public void loadDetails(View v){
            String policy_id = policyID.getText().toString();
            DocumentReference policyDoc =  db.collection("Policy_Details").document(policy_id);
            policyDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                String policyId = documentSnapshot.getString(KEY_POLICYID);
                                String name = documentSnapshot.getString(KEY_NAME);
                                String address = documentSnapshot.getString(KEY_ADDRESS);
                                String nic = documentSnapshot.getString(KEY_NIC);
                                String dob = documentSnapshot.getString(KEY_DOB);
                                String contactNo = documentSnapshot.getString(KEY_CONTACTNO);
                                String email = documentSnapshot.getString(KEY_EMAIL);
                                String vehicleColor = documentSnapshot.getString(KEY_VEHICOLOR);
                                String engineNo = documentSnapshot.getString(KEY_ENGNO);
                                String chassisNo = documentSnapshot.getString(KEY_CHANO);
                                String manufacturer = documentSnapshot.getString(KEY_MANU);
                                String model = documentSnapshot.getString(KEY_MODEL);
                                String regName = documentSnapshot.getString(KEY_REGNAME);
                                String year = documentSnapshot.getString(KEY_YEAR);
                                String engineCapacity = documentSnapshot.getString(KEY_ENGCAPACTIY);
                                String absOwner = documentSnapshot.getString(KEY_ABSOWNER);
                                String financialRights = documentSnapshot.getString(KEY_FINANCIALRIGHTS);
                                String currentDamage = documentSnapshot.getString(KEY_CURRENTDAMAGE);
                                String presentValue = documentSnapshot.getString(KEY_PRESENTVALUE);
                                String expireOn = documentSnapshot.getString(KEY_EXPIREDON);
                                String commencedOn = documentSnapshot.getString(KEY_COMMENCEDON);
                                Boolean naturalDisaster = documentSnapshot.getBoolean(KEY_NATUREDIS);
                                Boolean terrorismCover = documentSnapshot.getBoolean(KEY_TERCOVER);
                                textViewDetails.setText(
                                        "policy Id: "+ policyId +"\n"+
                                                "Full Name: "+name +"\n" +
                                                "address: "+address + "\n" +
                                                "NIC: "+ nic +"\n" +
                                                "Date of Birth: "+dob +"\n"+
                                                "Contact No: "+contactNo +"\n" +
                                                "Email: "+ email +"\n"+
                                                "Vehicle Color: "+vehicleColor +"\n"+
                                                "Engine No: "+engineNo + "\n"+
                                                "Chassis No: "+ chassisNo +"\n"+
                                                "Manufacturer: "+manufacturer +"\n"+
                                                "Vehicle model: "+model + "\n" +
                                                "Registered Name: " + regName + "\n"+
                                                "Registration year" + year + "\n" +
                                                "Engine Capacity: "+ engineCapacity +"\n"+
                                                "Absolute Owner: "+absOwner +"\n"+
                                                "Financial Rights: "+financialRights +"\n"+
                                                "Current Damage: "+ currentDamage +"\n"+
                                                "Present Value: "+presentValue +"\n"+
                                                "Expire On: "+expireOn +"\n"+
                                                "Commenced On: "+ commencedOn +"\n"+
                                                "Natural Disaster: "+naturalDisaster +"\n"+
                                                "Terrorism Cover: "+terrorismCover
                                );
                            }else{
                                new SweetAlertDialog(policyDetails.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Policy ID is invalid, Please enter again")
                                        .setConfirmText("Ok").show();
                            }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new SweetAlertDialog(policyDetails.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(Log.d(TAG, e.toString()))
                                    .setConfirmText("Ok").show();
                        }
                    });

    }
}

