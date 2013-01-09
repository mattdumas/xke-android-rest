package fr.xebia.xke.android.rest;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
    private String text;
    private String user;
    private String urlImage;

    public Tweet(String text, String user, String urlImage) {
        this.text = text;
        this.user = user;
        this.urlImage = urlImage;
    }

    public String getText() {
        return text;
    }

    public String getUser() {
        return user;
    }

    public String getUrlImage() {
        return urlImage;
    }
}
