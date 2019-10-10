package com.example.claimfast2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Claim_images extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE =256;
    private Button bSelectImages;
    private RecyclerView uploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private updateImageListAdapter UpdateImageListAdapter;

    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_images);

        mStorage = FirebaseStorage.getInstance().getReference();

        bSelectImages = (Button) findViewById(R.id.btn_selectImages);
        uploadList = (RecyclerView) findViewById(R.id.v_uploadList);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        UpdateImageListAdapter = new updateImageListAdapter(fileNameList,fileDoneList);

        //Recycler View

        uploadList.setLayoutManager(new LinearLayoutManager(this));
        uploadList.setHasFixedSize(true);
        uploadList.setAdapter(UpdateImageListAdapter);



        bSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                int totalSelectedItems = data.getClipData().getItemCount();

                for(int i=0; i<totalSelectedItems; i++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("Uploading");
                    UpdateImageListAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("Images").child(fileName);

                    final int finalI =i;
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           fileDoneList.remove(finalI);
                           fileDoneList.add(finalI, "done");

                           UpdateImageListAdapter.notifyDataSetChanged();


                        }
                    });

                }
                //Toast.makeText(Claim_images.this,"Selected Multiple Files", Toast.LENGTH_SHORT).show();
            }else if(data.getData()!= null){
                Toast.makeText(Claim_images.this,"Selected single File", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
