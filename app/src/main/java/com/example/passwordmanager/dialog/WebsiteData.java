package com.example.passwordmanager.dialog;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.passwordmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WebsiteData extends DialogFragment {

    public WebsiteData() {

    }

    public static WebsiteData newInstance() {
        Bundle args = new Bundle();
        WebsiteData fragment = new WebsiteData();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.datavisualization, container, false);
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


    }

}
