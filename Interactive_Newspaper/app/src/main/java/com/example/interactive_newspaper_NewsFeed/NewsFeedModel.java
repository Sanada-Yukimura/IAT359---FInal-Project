package com.example.interactive_newspaper_NewsFeed;

public class NewsFeedModel {
    String url;
    String title;
    String descrip;

    public NewsFeedModel(String title, String url, String descrip){
        this.url = url;
        this.title = title;
        this.descrip = descrip;
    }
}
