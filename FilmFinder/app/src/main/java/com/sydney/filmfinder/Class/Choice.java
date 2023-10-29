package com.sydney.filmfinder.Class;

import com.google.gson.annotations.SerializedName;

public class Choice {
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }
}
