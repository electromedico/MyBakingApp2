package com.example.alex.mybakingapp2.WidgetUtils;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.alex.mybakingapp2.R;
import com.example.alex.mybakingapp2.model.Ingredient;
import com.example.alex.mybakingapp2.model.Recipe;
import com.google.gson.Gson;

import java.util.List;

import static com.example.alex.mybakingapp2.IngredientsWidget2.WIDGET_ID_KEY;
import static com.example.alex.mybakingapp2.IngredientsWidget2ConfigureActivity.PREFS_NAME;
import static com.example.alex.mybakingapp2.IngredientsWidget2ConfigureActivity.PREF_PREFIX_KEY;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(),intent);
    }

}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    List<Ingredient> ingredients;
    private int mAppWidgetId;


    public ListRemoteViewsFactory(Context context,Intent intent) {
        this.context = context;
        if (intent.getExtras()!=null){
            mAppWidgetId= Integer.parseInt(intent.getStringExtra(WIDGET_ID_KEY));
        }


    }

    @Override
    public void onCreate() {
        context.getApplicationContext();

    }

    @Override
    public void onDataSetChanged() {
        Log.d("onDataSetChanged","onDataSetChanged");
        String gsonString = loadTitlePref(context, mAppWidgetId);
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(gsonString, Recipe.class);
        ingredients=recipe.getIngredients();



    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null) return 0;
        else return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (ingredients != null || ingredients.size() !=0){
            Ingredient ingredient = ingredients.get(position);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
            views.setTextViewText(R.id.quantity_tv,ingredient.getMeasure());
            views.setTextViewText(R.id.ingredient_tv,ingredient.getIngredient());
            return views;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

}
