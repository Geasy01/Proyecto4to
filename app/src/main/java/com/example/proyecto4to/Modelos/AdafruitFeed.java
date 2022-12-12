package com.example.proyecto4to.Modelos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AdafruitFeed {
    private final int status;
    private final String message;

    @SerializedName("data")
    private final ArrayList<FeedData> ListFeedData;

    public AdafruitFeed(int status, String message, ArrayList<FeedData> listFeedData) {
        this.status = status;
        this.message = message;
        ListFeedData = listFeedData;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<FeedData> getListFeedData() {
        return ListFeedData;
    }
}
