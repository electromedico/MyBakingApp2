package com.example.alex.mybakingapp2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.alex.mybakingapp2.WidgetUtils.ListWidgetService;
import com.example.alex.mybakingapp2.model.Recipe;
import com.google.gson.Gson;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidget2ConfigureActivity IngredientsWidget2ConfigureActivity}
 */
public class IngredientsWidget2 extends AppWidgetProvider {
    public static final String WIDGET_ID_KEY="widget_id";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        //we get the objet
        String recepiJSON = IngredientsWidget2ConfigureActivity.loadTitlePref(context, appWidgetId);
        Gson gson = new Gson();

        if (recepiJSON!=null) {
            Recipe recipe = gson.fromJson(recepiJSON,Recipe.class);
            views.setTextViewText(R.id.widget_title,recipe.getName());

        }

        //Set the ListWidgetService to act as the adapter for the listview
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(WIDGET_ID_KEY,String.valueOf(appWidgetId));
        views.setRemoteAdapter(R.id.widget_list_view,intent);

        //Set the recipeactivity to launch when clicked;
        Intent appIntent = new Intent(context, StepListActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_list_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidget2ConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

