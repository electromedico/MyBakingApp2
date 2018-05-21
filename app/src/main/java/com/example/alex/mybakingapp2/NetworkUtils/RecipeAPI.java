package com.example.alex.mybakingapp2.NetworkUtils;

import com.example.alex.mybakingapp2.model.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeAPI {

    String RECIPES_TAG = "baking.json";

    @GET(RECIPES_TAG)
    Call<Recipe[]> loadAll(@Query("adress") String status);
}
