package com.example.reachusadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    Button verify,denied;
    TextView Name,age;
    EditText reason;
    Bundle extras;
    String providerUserId,Email,message;
    ProgressDialog pg;
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
        denied = findViewById(R.id.deny);
        reason = findViewById(R.id.reasonforc);
        Name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        pg = new ProgressDialog(this);
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setIndeterminate(true);
        pg.setMessage("Loading Customers Data...");
        pg.show();

        loaddata();
        Log.d("storageRef", storageReference+"");
        try {
            final File localfile=File.createTempFile("providerIdProof","jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    idProof.setImageBitmap(bitmap);
                    pg.dismiss();
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

        denied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "Dear user your account verification for ReachUs Service Provider is Denied Because :"+reason.getText().toString();
                sendmail();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference=fStore.collection("Services").document("userId"+providerUserId);
                Map<String, Object> verified=new HashMap<>();
                verified.put("isIdVerified",true);
                documentReference.set(verified,SetOptions.merge());
                message="Congratulations dear user your account for service provider is succesfully verifed and you will start recieving orders now..All THE BEST";
                sendmail();
                Toast.makeText(verifyProviderId.this,"Id Verified",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void sendmail() {
        String sub="ReachUs Account Verification";
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ Email});
        email.putExtra(Intent.EXTRA_SUBJECT,sub );
        email.putExtra(Intent.EXTRA_TEXT, message);
        //need this to prompts email client only
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    private void loaddata() {
        DocumentReference documentReference = fStore.collection("users").document(providerUserId);
        documentReference.collection("users").document("Info")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {if (task.isSuccessful()) {
                        DocumentSnapshot document=task.getResult();
                        if(document.exists()) {
                            Log.d("Email", document.getId() + " => " + document.getData());
                            Email = document.getString("Email");
                            Name.setText("Name :"+document.getString("fullName"));
                            age.setText("Age :"+document.getString("Age"));



                        }
                    } else {
                        Log.w("Email", "Error getting documents.", task.getException());
                    }

                            }
                });

    }
}