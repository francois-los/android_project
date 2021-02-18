package com.example.passwordmanager.model;

import java.util.Map;

public class PasswordStorageModel {
    private Map<String, String> webSiteInLogs;

    public PasswordStorageModel(){
        //Constructor for Firebase
    }


//    Comment rendre le nom webSiteName Dynamique ??
    public PasswordStorageModel(Map<String, String> webSiteName){
        this.webSiteInLogs = webSiteName;
    }

    public Map<String, String> getWebSiteName() { return webSiteInLogs; }
}
