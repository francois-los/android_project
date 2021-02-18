package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.passwordmanager.dialog.DialogAddData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import androidx.fragment.app.Fragment;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

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
}
