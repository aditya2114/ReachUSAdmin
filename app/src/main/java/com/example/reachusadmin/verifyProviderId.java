package com.example.reachusadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class verifyProviderId extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    ImageView idProof;
    Button verify;
    Bundle extras;
    String providerUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_provider_id);

        extras = getIntent().getExtras();
        if (extras != null) {
            providerUserId=extras.getString("provideruserId");
        }
        Log.d("Data", providerUserId+"");
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://reachus-95f46.appspot.com").child(providerUserId).child("Idproof.jpg");
        verify=findViewById(R.id.verify);
        idProof=findViewById(R.id.idProof);
        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        Log.d("storageRef", storageReference+"");
        try {
            final File localfile=File.createTempFile("providerIdProof","jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    idProof.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Exception",e.getMessage()+"");
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference=fStore.collection("Services").document("userId"+providerUserId);
                Map<String, Object> verified=new HashMap<>();
                verified.put("isIdVerified",true);
                documentReference.set(verified,SetOptions.merge());
                Toast.makeText(verifyProviderId.this,"Id Verified",Toast.LENGTH_LONG).show();
            }
        });
    }
}