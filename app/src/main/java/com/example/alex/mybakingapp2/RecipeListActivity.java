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


/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements OnTaskCompleted,RecipeOnClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerViewMainAdapter mainAdapter;
    private RecipeListActivity mParentActivity;
    private HashMap<String,Recipe> recipeHashMap;
    private RecyclerView recyclerView;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mParentActivity=this;


        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

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
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(RecipeDetailFragment.ARG_ITEM_ID,recipe);
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, (Serializable) recipe);

            this.startActivity(intent);
        }
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
