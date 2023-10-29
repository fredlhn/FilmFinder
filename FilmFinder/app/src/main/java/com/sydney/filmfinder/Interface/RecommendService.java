package com.sydney.filmfinder.Interface;

import com.sydney.filmfinder.Class.MovieRecommendation;
import com.sydney.filmfinder.Class.UserInput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RecommendService {
    @POST("/v1/completions")
    Call<MovieRecommendation> getRecommendation(@Body UserInput userInput);

}
