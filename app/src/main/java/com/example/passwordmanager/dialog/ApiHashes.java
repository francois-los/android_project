package com.example.passwordmanager.dialog;

import com.google.gson.annotations.SerializedName;

public class ApiHashes {
    @SerializedName("url")
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
