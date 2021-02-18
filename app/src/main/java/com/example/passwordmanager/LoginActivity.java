package com.example.passwordmanager;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

//    Login var
    private TextView register;
    private Button loginbtn;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Intent intent;

//logIn
    private TextView email;
    private TextView password;

    //        fingerPrint login
    private TextView textView;
    private ImageView imageView;

    private FingerprintManager fingerprintManager;
    private FingerprintManager.AuthenticationCallback authenticationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                intent = new Intent(LoginActivity.this, PasswordPage.class);
            }
        };
//        login with email Adress
        loginbtn = (Button) findViewById(R.id.loginButton);
        loginbtn.setOnClickListener(this);


        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

//        fingerPrint login
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.image1);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        authenticationCallback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                textView.setText("error");
                imageView.setImageResource(R.drawable.image2);
                super.onAuthenticationError(errorCode, errString);
            }
            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                textView.setText("help");
                imageView.setImageResource(R.drawable.image1);
                super.onAuthenticationHelp(helpCode, helpString);
            }
            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                textView.setText("easy win");
                imageView.setImageResource(R.drawable.image3);
                super.onAuthenticationSucceeded(result);
                setContentView(R.layout.main);
            }
            @Override
            public void onAuthenticationFailed() {
                textView.setText("failed");
                imageView.setImageResource(R.drawable.image2);
                super.onAuthenticationFailed();
            }
        };
    }

    public void scanButton(View view){
        fingerprintManager.authenticate(null, null, 0, authenticationCallback, null);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.loginButton :
                email = (EditText)findViewById(R.id.email);
                String mail = email.getText().toString().trim();
                password = (EditText)findViewById(R.id.password);
                String pass = password.getText().toString().trim();
                if(mail.isEmpty()){
                    email.setError("You must enter a email address");
                    email.requestFocus();
                    break;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    email.setError("You must enter a valid email address");
                    email.requestFocus();
                    break;
                }
                if(pass.isEmpty()){
                    password.setError("You must enter a password");
                    password.requestFocus();
                    break;
                }
                if(pass.length()<6){
                    password.setError("You must enter a valid password more than 6 char");
                    password.requestFocus();
                    break;
                }
                Log.d("emailpass", mail+" "+pass);
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterUser.class));
                break;
        }
    }
}