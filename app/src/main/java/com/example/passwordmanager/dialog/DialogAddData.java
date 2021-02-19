package com.example.passwordmanager.dialog;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.passwordmanager.Md5;
import com.example.passwordmanager.R;
import com.example.passwordmanager.cryptage.Encrypt;
import com.example.passwordmanager.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogAddData extends DialogFragment {
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText editTextUrl;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressBar progressBar3;


    public DialogAddData() {

    }

    public static DialogAddData newInstance() {

        Bundle args = new Bundle();
        DialogAddData fragment = new DialogAddData();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
    }

    @Override
    public void onResume() {
        if(getDialog() != null) {
            //User able to dismiss dialog :
            //getDialog().setCancelable(false);

            Window window = getDialog().getWindow();
            Point size = new Point();

            // Store dimensions of the screen in `size`
            window.getWindowManager().getDefaultDisplay().getSize(size);

            // Set the width of the dialog to the dimensions of the screen
            window.setLayout((int) (0.75 * size.x), (int) (0.5 * size.y));
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(R.drawable.dialog_inset);
        }
        super.onResume();
    }

    private void init(View view) {

        mAuth = FirebaseAuth.getInstance();

        Button uploadData =  view.findViewById(R.id.uploadData);
        uploadData.setOnClickListener(v -> uploadData());

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextUrl = view.findViewById(R.id.editTextUrl);

        progressBar3 = view.findViewById(R.id.progressBar3);
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


        //                                TEST ENCRYPT
        String passwordHash = Encrypt.encrypt(password);
        Log.d("testHash", "passwordHash: " +passwordHash);


//        byte [] md5input=password.getBytes();
//        BigInteger md5Data= null;
//        try{
//            md5Data= new BigInteger(1, Md5.encryptMD5(md5input));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//
//        String  passwordHash= md5Data.toString(16);
//        Log.d("passwordHash", "onComplete: "+ email+" "+passwordHash+" "+url);

        UserModel passwordManagerApp = new UserModel(email, passwordHash, url);

        String userId = mAuth.getCurrentUser().getUid();
        db.collection(userId).document().set(passwordManagerApp)
                .addOnSuccessListener(aVoid -> {
//                Log.d("uId","registerUser:" + userId);
                    getDialog().dismiss();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show());

    }
}
