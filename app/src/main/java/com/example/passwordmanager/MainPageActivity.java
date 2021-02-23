package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.passwordmanager.adapter.WebSiteAdapter;
import com.example.passwordmanager.dialog.DialogAddData;
import com.example.passwordmanager.dialog.WebsiteData;
import com.example.passwordmanager.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener, WebSiteAdapter.OnDataListener {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private WebSiteAdapter adapter;
    List<UserModel> webSiteList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        webSiteList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);



        adapter = new WebSiteAdapter(webSiteList, this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


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
        final int idLogOut = R.id.logout;
        final int idRefresh = R.id.refresh;
        final int idAddData = R.id.addDataButton;
//        final int boutonProvisoir = R.id.boutonProvisoir;
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
//            case boutonProvisoir:
//                WebsiteData dialog2 = WebsiteData.newInstance();
//                dialog2.show(getSupportFragmentManager(), "websiteData");
//                break;
        }
    }


    private void getUserData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
//            final String userId = user.getUid();
            final String userId = "jq4c4xyqKOgxvNlU6vGQfXoMxS72";
            db.collection(userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<UserModel> userData = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        UserModel model = snapshot.toObject(UserModel.class);
                        userData.add(model);
                    }

                    //
                    for (int i = 0 ; i < userData.size() ; i++)
                        Log.d("listDataUser" , userData.get(i).toString());
                    //

                    this.runOnUiThread(() -> {
                        webSiteList.addAll(userData);
                        recyclerView.post(() -> adapter.notifyDataSetChanged());
                    });
                });
        }
    }

    @Override
    public void onDataClick(int position) {
//        Log.d("TAG", "onDataClick: " + webSiteList.get(position).getUrl());
        UserModel userModel = webSiteList.get(position);
        WebsiteData dialog2 = WebsiteData.newInstance(userModel.getUrl(),userModel.getEmail(),userModel.getPassword());
        dialog2.show(getSupportFragmentManager(), "websiteData");
    }
}
