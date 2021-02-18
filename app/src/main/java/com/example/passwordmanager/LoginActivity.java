package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

//    Login var
    private TextView register;
    private Button loginbtn;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Intent intent;

//logIn
    private EditText email;
    private EditText password;

    private ProgressBar progressBar;

    //        fingerPrint login
    private TextView textView;
    private ImageView imageView;

    private FingerprintManager fingerprintManager;
    private FingerprintManager.AuthenticationCallback authenticationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                intent = new Intent(LoginActivity.this, MainPageActivity.class);
            }
        };
//        login with email Adress
        loginbtn = (Button) findViewById(R.id.loginButton);
        loginbtn.setOnClickListener(this);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

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
                loginUser();
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
                break;
        }
    }

    private void loginUser() {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(mail.isEmpty()){
            email.setError("You must enter a email address");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("You must enter a valid email address");
            email.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            password.setError("You must enter a password");
            password.requestFocus();
            return;
        }
        if(pass.length()<6){
            password.setError("You must enter a valid password more than 6 char");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mFirebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(LoginActivity.this, task -> {
            if(!task.isSuccessful()) {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthException e) {
                    getAuthError(task, LoginActivity.this, email, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, "Error, try again", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
            else {
                startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                finish();
            }
        });
    }

    private void getAuthError(@NonNull Task<AuthResult> t, Context context, EditText emailEt, EditText passwordEt) {
        String errorCode = ((FirebaseAuthException) Objects.requireNonNull(t.getException())).getErrorCode();

        switch (errorCode) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(context, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(context, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(context, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(context, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                emailEt.setError("The email address is badly formatted.");
                emailEt.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(context, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                passwordEt.setError("password is incorrect ");
                passwordEt.requestFocus();
                passwordEt.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(context, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(context, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(context, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(context, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                emailEt.setError("The email address is already in use by another account.");
                emailEt.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(context, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(context, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(context, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(context, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(context, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(context, "The given password is invalid.", Toast.LENGTH_LONG).show();
                passwordEt.setError("The password must have 6 characters at least");
                passwordEt.requestFocus();
                break;
        }
    }
}