package com.example.claimfast2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Claim_images extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE =1;
    private Button bSelectImages;
    private RecyclerView uploadList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_images);

        bSelectImages = (Button) findViewById(R.id.btn_selectImages);
        uploadList = (RecyclerView) findViewById(R.id.v_uploadList);

        bSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Pictures"),RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){

            if(data.getClipData()!= null){
                Toast.makeText(Claim_images.this,"Selected Multiple Files", Toast.LENGTH_SHORT).show();
            }else if(data.getData()!= null){
                Toast.makeText(Claim_images.this,"Selected single File", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
