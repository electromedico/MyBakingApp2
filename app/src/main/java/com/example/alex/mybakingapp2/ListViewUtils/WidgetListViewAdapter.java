package com.example.alex.mybakingapp2.ListViewUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.HashMap;

public class WidgetListViewAdapter extends ArrayAdapter {

    private final Context context;
    private final String[] values;


    HashMap<String, String> mIdMap = new HashMap<String, String>();

    public WidgetListViewAdapter(@NonNull Context context, int resource, String[] values) {
        super(context, resource);
        this.context = context;
        this.values = values;

    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }


}
