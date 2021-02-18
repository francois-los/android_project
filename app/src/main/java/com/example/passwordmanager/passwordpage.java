package com.example.passwordmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class PasswordPage extends Fragment implements dataListner {

    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View RootView = inflater.inflate(R.layout.main, container, false);
        mListView = (ListView) RootView.findViewById(R.id.list);

        return RootView;
    }
    @Override
    public void onDataRetrieved(String pass_info) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Integer.parseInt(pass_info));
        mListView.setAdapter(adapter);
    }
}
