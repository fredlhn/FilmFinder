package com.sydney.filmfinder.Interface;


import com.sydney.filmfinder.Class.MovieRecommendationRequest;
import com.sydney.filmfinder.Class.MovieRecommendationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OpenAIApi {

    @POST("completions")
    Call<MovieRecommendationResponse> getMovieRecommendation(
            @Header("Content-Type") String contentType,
            @Header("Authorization") String apiKey,
            @Body MovieRecommendationRequest request
    );
}

