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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.passwordmanager.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button uploadData;
    private EditText editTextUrl;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressBar progressBar3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        mAuth = FirebaseAuth.getInstance();

        uploadData = (Button) findViewById(R.id.uploadData);
        uploadData.setOnClickListener(this);

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextUrl = (EditText)findViewById(R.id.editTextUrl);

        progressBar3 = (ProgressBar)findViewById(R.id.progressBar3);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uploadData:
                uploadData();
                break;
        }
    }

    private void uploadData() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String url = editTextUrl.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("You must enter a email address");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("You must enter a password");
            editTextPassword.requestFocus();
            return;
        }
        if(url.isEmpty()){
            editTextUrl.setError("you must enter a Url");
            editTextPassword.requestFocus();
            return;
        }

        progressBar3.setVisibility(View.VISIBLE);

        byte [] md5input=password.getBytes();
        BigInteger md5Data= null;
        try{
            md5Data= new BigInteger(1, md5.encryptMD5(md5input));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String  md5Str= md5Data.toString(16);
        Log.d("passwordHash", "onComplete: "+ email+" "+md5Str+" "+url);

        UserModel passwordManagerApp = new UserModel(email, md5Str, url);

        String userId = mAuth.getCurrentUser().getUid();
        db.collection(userId).document().set(passwordManagerApp)
            .addOnSuccessListener(aVoid -> {
//                Log.d("uId","registerUser:" + userId);
                startActivity(new Intent(AddDataActivity.this, MainPageActivity.class));
                finish();
            })
            .addOnFailureListener(e ->{
                    Toast.makeText(AddDataActivity.this, "Error", Toast.LENGTH_LONG).show();
            });

    }
}
