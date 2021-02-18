package com.example.passwordmanager.model;

import java.util.ArrayList;
import java.util.Map;

public class PasswordStorageModel {

    //    private Map<String, Map<String, String>> webSiteInLogs;

    //    public PasswordStorageModel(Map<String, Map<String, String>> WebSiteList){
    //        this.webSiteInLogs = WebSiteList;
    //    }

    //    public Map<String, Map<String, String>> getWebSiteName() { return webSiteInLogs; }


    private ArrayList<Map<String, String>> webSiteInLogs;

    public PasswordStorageModel(){
        //Constructor for Firebase
    }

    public PasswordStorageModel(ArrayList<Map<String, String>> WebSiteList){
        this.webSiteInLogs = WebSiteList;
    }

    public ArrayList<Map<String, String>> getWebSiteName() { return webSiteInLogs; }

}
