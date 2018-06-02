package com.example.alex.mybakingapp2.UtilsRecyclerView;

import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;


import com.example.alex.mybakingapp2.R;

import butterknife.BindView;
import butterknife.ButterKnife;


class ViewHolderRecipesList extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.recipe_name)
    TextView recipeName;
    private final RecipeOnClickListener listener;

    public ViewHolderRecipesList(View itemView, RecipeOnClickListener l) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        listener=l;
        itemView.setOnClickListener(this);

    }

    public TextView getRecipeName() {
        return recipeName;
    }

    @Override
    public void onClick(View v) {
        String key = getRecipeName().getText().toString();
        listener.onListItemClick(key);
    }
}
