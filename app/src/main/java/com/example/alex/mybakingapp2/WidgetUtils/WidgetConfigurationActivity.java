package com.example.alex.mybakingapp2.WidgetUtils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;

import com.example.alex.mybakingapp2.NetworkUtils.GetRecepiesController;
import com.example.alex.mybakingapp2.NetworkUtils.OnTaskCompleted;
import com.example.alex.mybakingapp2.R;
import com.example.alex.mybakingapp2.RecipeDetailActivity;
import com.example.alex.mybakingapp2.UtilsRecyclerView.RecipeOnClickListener;
import com.example.alex.mybakingapp2.UtilsRecyclerView.RecyclerViewMainAdapter;
import com.example.alex.mybakingapp2.model.Ingredient;
import com.example.alex.mybakingapp2.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

public class WidgetConfigurationActivity extends AppCompatActivity implements OnTaskCompleted,RecipeOnClickListener {


    private RecyclerViewMainAdapter mainAdapter;
    private HashMap<String,Recipe> recipeHashMap;
    private RecyclerView recyclerView;
    private int intentWidgetId;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            intentWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        GetRecepiesController getRecepiesController = new GetRecepiesController(this);
        getRecepiesController.start();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mainAdapter = new RecyclerViewMainAdapter(this);
        recyclerView.setAdapter(mainAdapter);
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
        recipe = recipeHashMap.get(key);
        configureWidget(recipe);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, intentWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();



    }

    private void configureWidget(Recipe recipe){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.ingredients_widget);


        appWidgetManager.updateAppWidget(intentWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(intentWidgetId, R.id.widget_list_view);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        outState.putParcelable(getString(R.string.manager_state),manager.onSaveInstanceState());
    }

    protected ArrayList<RemoteViews> simpleAdapter(Recipe recipe){
        ArrayList<RemoteViews> remoteViews = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()){
            RemoteViews views = new RemoteViews(this.getPackageName(),R.id.widget_ingredients_LL);
            views.setTextViewText(R.id.quantity_tv,ingredient.getMeasure());
            views.setTextViewText(R.id.ingredient_tv,ingredient.getIngredient());
            remoteViews.add(views);
        }
    return remoteViews;


    }
}
