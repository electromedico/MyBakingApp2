package com.example.alex.mybakingapp2.WidgetUtils;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.alex.mybakingapp2.R;
import com.example.alex.mybakingapp2.model.Ingredient;
import com.example.alex.mybakingapp2.model.Recipe;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    List<Ingredient> ingredients;

    public ListRemoteViewsFactory(Context context) {
        this.context = context;

    }

    @Override
    public void onCreate() {
        context.getApplicationContext();

    }

    @Override
    public void onDataSetChanged() {
        if (context instanceof WidgetConfigurationActivity){
            WidgetConfigurationActivity activity= (WidgetConfigurationActivity) context;
        }
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

    public void setData(Recipe r){
        ingredients = r.getIngredients();
    }

}
