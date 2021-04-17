package com.example.myapp.network.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeImage {
    @SerializedName("image")
    @Expose
    private String image;

    private String userName;


    public ChangeImage() {
    }

    public ChangeImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
