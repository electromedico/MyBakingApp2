package com.example.alex.mybakingapp2;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.alex.mybakingapp2.NetworkUtils.GetRecepiesController;
import com.example.alex.mybakingapp2.NetworkUtils.OnTaskCompleted;
import com.example.alex.mybakingapp2.UtilsRecyclerView.RecipeOnClickListener;
import com.example.alex.mybakingapp2.UtilsRecyclerView.RecyclerViewMainAdapter;
import com.example.alex.mybakingapp2.model.Recipe;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * The configuration screen for the {@link IngredientsWidget2 IngredientsWidget2} AppWidget.
 */
public class IngredientsWidget2ConfigureActivity extends Activity implements OnTaskCompleted,RecipeOnClickListener {

    private static final String PREFS_NAME = "com.example.alex.mybakingapp2.IngredientsWidget2";
    public static final String PREF_PREFIX_KEY = "appwidget_";
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private RecyclerViewMainAdapter mainAdapter;
    private HashMap<String,Recipe> recipeHashMap;
    private RecyclerView recyclerView;


    public IngredientsWidget2ConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    private static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    public static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return null;
        }
    }


    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.recipe_list);

        recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        GetRecepiesController getRecepiesController = new GetRecepiesController(this);
        getRecepiesController.start();

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);


        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

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
        final Context context = IngredientsWidget2ConfigureActivity.this;

        // When the button is clicked, store the string locally
        String widgetText = key;
        Gson gson = new Gson();
        String json = gson.toJson(recipeHashMap.get(key));
        saveTitlePref(context, mAppWidgetId, json);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetId,R.id.widget_list_view);
        IngredientsWidget2.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
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

}

