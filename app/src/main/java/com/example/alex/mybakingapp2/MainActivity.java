package com.example.alex.mybakingapp2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.alex.mybakingapp2.IdlingResource.SimpleIdlingResource;
import com.example.alex.mybakingapp2.NetworkUtils.GetRecepiesController;
import com.example.alex.mybakingapp2.NetworkUtils.OnTaskCompleted;
import com.example.alex.mybakingapp2.UtilsRecyclerView.RecipeOnClickListener;
import com.example.alex.mybakingapp2.UtilsRecyclerView.RecyclerViewMainAdapter;
import com.example.alex.mybakingapp2.model.Recipe;

import java.io.Serializable;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted,RecipeOnClickListener {


    private RecyclerViewMainAdapter mainAdapter;
    private HashMap<String,Recipe> recipeHashMap;
    private RecyclerView recyclerView;

    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        GetRecepiesController getRecepiesController = new GetRecepiesController(this);
        getRecepiesController.start();

        getIdlingResource();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mainAdapter = new RecyclerViewMainAdapter(this);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        outState.putParcelable(getString(R.string.manager_state),manager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {

            //Restore state of the RecyclerView
            String keyState = getString(R.string.manager_state);
            if(savedInstanceState.containsKey(keyState)){

                Parcelable state =savedInstanceState.getParcelable(keyState);

                LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                manager.onRestoreInstanceState(state);

            }
        }

    }

    @Override
    public void onTaskCompleted(Recipe[] recipes) {
        recipeHashMap = new HashMap<>();
        for (Recipe recipe:recipes){
            recipeHashMap.put(recipe.getName(),recipe);
        }
        mainAdapter.setDataset(recipes);

    }

    @Override
    public void onListItemClick(String key) {
        Recipe recipe = recipeHashMap.get(key);

        Intent intent = new Intent(this, StepListActivity.class);
        intent.putExtra(StepListActivity.ARG_ITEM_ID, (Serializable) recipe);

        this.startActivity(intent);

    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){

        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


}
