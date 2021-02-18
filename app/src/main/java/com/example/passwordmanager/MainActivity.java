package com.example.passwordmanager;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    Login var
    private TextView register;


//        fingerPrint login
    private TextView textView;
    private ImageView imageView;
    private FingerprintManager fingerprintManager;
    private FingerprintManager.AuthenticationCallback authenticationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        login with email Adress
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
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
        }
    }
}