package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordmanager.cryptage.Encrypt;
import com.example.passwordmanager.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar2;

    private FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private final CollectionReference usersRef = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();

        TextView toLoginView = (TextView) findViewById(R.id.toLoginView);
        toLoginView.setOnClickListener(this);


        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.passwordEditText);

        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);

    }

    @Override
    public void onClick(View v) {
        final int idToLogIn = R.id.toLoginView;
        final int idRegister = R.id.registerButton;
        switch (v.getId()){
            case idToLogIn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case idRegister:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("You must enter a email address");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("You must enter a valid email address");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("You must enter a password");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("You must enter a valid password more than 6 char");
            editTextPassword.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        if(task.getResult() != null) {
                            FirebaseUser user = task.getResult().getUser();
                            if(user != null) {
                                String userId = user.getUid();

//                              ENCRYPT
                                String passwordHash = Encrypt.encrypt(password);
//                                Log.d("testHash", "passwordHash: " +passwordHash);

                                UserModel passwordManagerApp = new UserModel(email, passwordHash, "passwordManagerApplication");
                                db.collection(userId).document().set(passwordManagerApp)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("uId","registerUser:" + userId);
                                            startActivity(new Intent(RegisterUserActivity.this, MainPageActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(RegisterUserActivity.this, "Error", Toast.LENGTH_LONG).show());
                            } else {
                                Toast.makeText(RegisterUserActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterUserActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}