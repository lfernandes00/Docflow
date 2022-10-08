package com.example.docflow;

public class ProfileDataModel {
    int id;
    String photo;

    public ProfileDataModel(int id, String photo) {
        this.id = id;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
