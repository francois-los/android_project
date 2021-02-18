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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordmanager.model.PasswordStorageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView toLoginView;
    private Button registerButton;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar2;

    private FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();

        toLoginView = (TextView) findViewById(R.id.toLoginView);
        toLoginView.setOnClickListener(this);


        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.passwordEditText);

        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toLoginView:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerButton:
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
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult() != null) {
                                FirebaseUser user = task.getResult().getUser();
                                if(user != null) {
                                    String userId = user.getUid();
//                                    UserModel newUser = new UserModel(email, password);

                                    Map<String, String> newUser = new HashMap<>();
                                    newUser.put("email", email);
                                    newUser.put("password", password);
                                    newUser.put("url", "passwordManagerApplication");
                                    PasswordStorageModel passwordManagerApp = new PasswordStorageModel(newUser);

                                    usersRef.document(userId).set(passwordManagerApp)
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("uId","registerUser:" + userId);


//                                                REDIRECTION VERS LA PAGE DES MDP + store Activity as Login


//                                    startActivity(new Intent(RegisterUser.this, ContentActivity.class));
//                                    finish();
                                            })
                                            .addOnFailureListener(e ->
                                                    Toast.makeText(RegisterUser.this, "Error", Toast.LENGTH_LONG).show());
                                } else {
                                    Toast.makeText(RegisterUser.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(RegisterUser.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}