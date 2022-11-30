package com.example.proyecto4to;

import android.view.Display;

public class Modelo {

    private int imageView;
    private String textview;

    Modelo(int imageView, String textview){
        this.imageView = imageView;
        this.textview = textview;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getTextview() {
        return textview;
    }

    public void setTextview(String textview) {
        this.textview = textview;
    }
}
