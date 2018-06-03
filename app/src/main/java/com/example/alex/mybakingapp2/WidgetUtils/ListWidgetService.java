package com.example.alex.mybakingapp2.WidgetUtils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.alex.mybakingapp2.R;
import com.example.alex.mybakingapp2.StepListActivity;
import com.example.alex.mybakingapp2.model.Ingredient;
import com.example.alex.mybakingapp2.model.Recipe;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import static com.example.alex.mybakingapp2.IngredientsWidget2.WIDGET_ID_KEY;
import static com.example.alex.mybakingapp2.IngredientsWidget2ConfigureActivity.PREF_PREFIX_KEY;
import static com.example.alex.mybakingapp2.IngredientsWidget2ConfigureActivity.loadTitlePref;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(),intent);
    }

}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private List<Ingredient> ingredients;
    private int mAppWidgetId;
    private final Gson gson = new Gson();
    private Recipe recipe;


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
        recipe = gson.fromJson(gsonString, Recipe.class);
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
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_layout);
            views.setTextViewText(R.id.quantity_widget_tv,ingredient.getQuantity()+" "+ingredient.getMeasure());
            views.setTextViewText(R.id.ingredient_widget_tv,ingredient.getIngredient());


            Intent appIntent = new Intent(context, StepListActivity.class);
            appIntent.putExtra(WIDGET_ID_KEY,String.valueOf(mAppWidgetId));
            appIntent.putExtra(PREF_PREFIX_KEY+mAppWidgetId,
                    (Serializable)recipe);

            views.setOnClickFillInIntent(R.id.linear_layout_widget, appIntent);

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

}
