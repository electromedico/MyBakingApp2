package com.example.alex.mybakingapp2.NetworkUtils;


import com.example.alex.mybakingapp2.model.Recipe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRecepiesController implements Callback<Recipe[]> {

    private final static String API_KEY = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private final OnTaskCompleted listener;

    public GetRecepiesController(OnTaskCompleted listener){this.listener=listener;}

    public void start(){
        Retrofit client = new Retrofit.Builder()
                        .baseUrl(API_KEY)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        RecipeAPI recipeAPI = client.create(RecipeAPI.class);
        Call<Recipe[]>  call = recipeAPI.loadAll(API_KEY);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Recipe[]> call, Response<Recipe[]> response) {
        if (response.isSuccessful()){
            Recipe[] recipes =response.body();
            listener.onTaskCompleted(recipes);

        }
    }

    @Override
    public void onFailure(Call<Recipe[]> call, Throwable t) {
        new Exception(t);
    }
}
