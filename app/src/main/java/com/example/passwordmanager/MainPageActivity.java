package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.passwordmanager.dialog.DialogAddData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        FloatingActionButton addDataButton = findViewById(R.id.addDataButton);
        addDataButton.setOnClickListener(this);

        TextView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(this);

        getUserData();
    }

    @Override
    public void onClick(View v) {
        final int  idLogOut = R.id.logout;
        final int idRefresh = R.id.refresh;
        final int idAddData = R.id.addDataButton;
        switch (v.getId()){
            case idLogOut:
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
                break;
            case idRefresh:
                startActivity(new Intent(MainPageActivity.this, MainPageActivity.class));
                break;
            case idAddData:
                DialogAddData dialog = DialogAddData.newInstance();
                dialog.show(getSupportFragmentManager(), "dialogAddData");
                break;
        }
    }



    private void getUserData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            final String userId = user.getUid();
            db.collection(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Map> userData = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("dataUser", document.getId() + " => " + document.getData());
                                    Map<String, Object> map = document.getData();
                                    userData.add(map);
                                }

                                //
                                for (int i = 0 ; i < userData.size() ; i++)
                                    Log.d("listDataUser" , userData.get(i).toString());
                                //

                            } else {
                                Log.d("dataUser", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

    }
}
