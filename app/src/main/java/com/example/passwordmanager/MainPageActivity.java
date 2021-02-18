package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
//import android.view.LayoutInflater;
import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private TextView logout;
    private TextView refresh;
    private FloatingActionButton addDataButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        addDataButton = findViewById(R.id.addDataButton);
        addDataButton.setOnClickListener(this);

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(this);

        mListView = findViewById(R.id.list);
    }

/*    @Override
    public void onDataRetrieved(String pass_info) {

    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout : startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
                break;
            case R.id.refresh: startActivity(new Intent(MainPageActivity.this, MainPageActivity.class));
                break;
            case R.id.addDataButton:startActivity(new Intent(MainPageActivity.this, AddDataActivity.class));
                break;
        }
    }
}
