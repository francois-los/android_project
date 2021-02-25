package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    private Button forgortbtn;

    private ProgressBar progressBar;

    private FirebaseAuth fbAuth;

    public ForgottenPasswordActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);

        fbAuth = FirebaseAuth.getInstance();

        forgortbtn = (Button) findViewById(R.id.forgottenButton);
        forgortbtn.setOnClickListener(this);

        email = (EditText)findViewById(R.id.emailEditText);

        progressBar = (ProgressBar)findViewById(R.id.progressBar3);

    }

    @Override
    public void onClick(View v) {
        final int idAddData = R.id.addDataButton;
        switch (v.getId()){
            case R.id.forgottenButton:
                reinitialization();
                break;
        }
    }

    private void reinitialization(){
        String emailAddress = email.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if(emailAddress.isEmpty()){
            email.setError("You must enter your email address");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            email.setError("You must enter a valid email address");
            email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

//        Log.d("TAGReset", "reinitialization: " + emailAddress);


        fbAuth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Log.d("TagReset", "Email sent.");
                    Toast.makeText(ForgottenPasswordActivity.this, "Your reset email has been send !", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(ForgottenPasswordActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }


}
