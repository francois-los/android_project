package com.example.passwordmanager.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.passwordmanager.R;
import com.example.passwordmanager.model.UserModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WebSiteAdapter extends RecyclerView.Adapter<WebSiteAdapter.ViewHolder> {
    private final List<UserModel> webSiteList;
    private final OnDataListener onDataListener;

    public WebSiteAdapter(List<UserModel> webSiteList, OnDataListener listener) {
        this.webSiteList = webSiteList;

//        Log.d("dataTestReception", "WebSiteAdapter: "+webSiteList);
        this.onDataListener = listener;
    }

    @NonNull
    @Override
    public WebSiteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view, onDataListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WebSiteAdapter.ViewHolder holder, int position) {
        holder.setData(webSiteList.get(position));
    }

    @Override
    public int getItemCount() {
        return webSiteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView urlTextView;
        private final OnDataListener onDataListener;

        public ViewHolder(@NonNull View itemView, OnDataListener listener) {
            super(itemView);
            onDataListener = listener;
            urlTextView = itemView.findViewById(R.id.urlTextView);
            itemView.setOnClickListener(this);
        }

        //A modifier avec le layout
        public void setData(UserModel userModel) {
            urlTextView.setText(userModel.getUrl());
            Log.d("TAG", "setData: test");
        }

        @Override
        public void onClick(View v) {
            onDataListener.onDataClick(getAdapterPosition());
        }
    }

    public interface OnDataListener {
        void onDataClick(int position);
    }
}
