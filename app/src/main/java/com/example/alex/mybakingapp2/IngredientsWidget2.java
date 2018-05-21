package com.example.alex.mybakingapp2;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.alex.mybakingapp2.WidgetUtils.ListWidgetService;



/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidget2ConfigureActivity IngredientsWidget2ConfigureActivity}
 */
public class IngredientsWidget2 extends AppWidgetProvider {
    public static String WIDGET_ID_KEY="widget_id";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        //Set the ListWidgetService to act as the adapter for the listview
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(WIDGET_ID_KEY,String.valueOf(appWidgetId));
        views.setRemoteAdapter(R.id.widget_list_view,intent);


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

