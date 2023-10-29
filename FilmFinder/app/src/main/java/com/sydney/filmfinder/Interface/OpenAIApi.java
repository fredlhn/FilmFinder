package com.sydney.filmfinder.Interface;


import com.sydney.filmfinder.Class.MovieRecommendationRequest;
import com.sydney.filmfinder.Class.MovieRecommendationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OpenAIApi {

    @POST("/v1/completions")  // Replace 'path_to_endpoint' with the appropriate endpoint for fetching recommendations.
    Call<MovieRecommendationResponse> getMovieRecommendation(
            @Header("Authorization") String apiKey,
            @Body MovieRecommendationRequest request
    );
}

