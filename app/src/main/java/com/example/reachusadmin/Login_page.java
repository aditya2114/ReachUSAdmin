package com.example.reachusadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_page extends AppCompatActivity {

    private static final String TAG = "Email Password";
    EditText email,password;
    CheckBox show_pass;
    String s_email,s_pass;
    Button login,forgot_pass,signup;
    private long pressedTime;
    private FirebaseAuth mAuth=null;
    FirebaseUser fuser;
    ProgressBar pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        show_pass = findViewById(R.id.show_password);
        email = findViewById(R.id.l_email);
        password = findViewById(R.id.l_password);
        login = findViewById(R.id.login_button);
        forgot_pass = findViewById(R.id.forgotpasword_button);
        signup = findViewById(R.id.Sign_up_button);
        pg = findViewById(R.id.pg1);

        mAuth = FirebaseAuth.getInstance();

        show_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    password.setTransformationMethod(null);
                }
                else
                {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pg.setVisibility(View.VISIBLE);
                pg.setIndeterminate(true);
                signIn();

            }
        });
    }
    private void signIn() {

        Log.d(TAG,"signin"+email);
        if (!validateform())
        {
            pg.setVisibility(View.GONE);
            return;


        }

        s_email = email.getText().toString();
        s_pass = password.getText().toString();

        if(s_email.equals("sangaveaditya2003@gmail.com") ||s_email.equals("ksanket114402@gmail.com")){
            mAuth.signInWithEmailAndPassword(s_email,s_pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                if(mAuth.getCurrentUser().isEmailVerified()){
                                    pg.setVisibility(View.GONE);
                                    Toast.makeText(Login_page.this,"Sign-In Sucessfull",Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Login_page.this,MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    pg.setVisibility(View.GONE);
                                    Toast.makeText(Login_page.this,"Please Verify your Email First",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                pg.setVisibility(View.GONE);

                                Log.w(TAG,"Failed to Sign-in",task.getException());
                                Toast.makeText(Login_page.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(Login_page.this,"You are not an Admin",Toast.LENGTH_LONG);
        }

    }
    private boolean validateform() {
        boolean valid = true;
        String em = email.getText().toString();
        if(TextUtils.isEmpty(em)){
            email.setError("Field Empty");
            valid = false;
        }

        else
        {
            email.setError(null);
        }

        String pw = password.getText().toString();
        if(TextUtils.isEmpty(pw)){
            password.setError("Field Empty");
            valid = false;
        }
        else
        {
            password.setError(null);
        }
        return valid;
    }
}
