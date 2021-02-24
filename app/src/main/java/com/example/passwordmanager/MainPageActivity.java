package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webSiteList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        adapter = new WebSiteAdapter(webSiteList, this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        FloatingActionButton addDataButton = findViewById(R.id.addDataButton);
        addDataButton.setOnClickListener(this);

        getUserData();
    }

    @Override
    public void onClick(View v) {
        final int idAddData = R.id.addDataButton;
        switch (v.getId()){
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
            //            final String userId = "jq4c4xyqKOgxvNlU6vGQfXoMxS72";
            final String userId = user.getUid();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int refreshId = R.id.refresh;
        final int exitId = R.id.exit;
        switch (item.getItemId()) {
            case refreshId:
                startActivity(new Intent(MainPageActivity.this, MainPageActivity.class));
                this.overridePendingTransition(0,0);
                finish();
                return true;
            case exitId:
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDataClick(int position) {
        UserModel userModel = webSiteList.get(position);
        WebsiteData dialog2 = WebsiteData.newInstance(userModel.getUrl(),userModel.getEmail(),userModel.getPassword());
        dialog2.show(getSupportFragmentManager(), "websiteData");
    }
}
