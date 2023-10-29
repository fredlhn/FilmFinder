package com.sydney.filmfinder.Class;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieRecommendationResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("choices")
    private List<Choice> choices;

    public String getCompletionText() {
        if (choices != null && !choices.isEmpty()) {
            return choices.get(0).getText().trim();  // trim() to remove any leading or trailing newlines or spaces
        }
        return null;
    }
}
