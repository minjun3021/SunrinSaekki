package com.kmj.sunrinsaekki;

public class UserData {

    private String name;
    private String profileURL;
    private String id;

    public UserData(String name, String profileURL, String id) {
        this.name = name;
        this.profileURL = profileURL;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
