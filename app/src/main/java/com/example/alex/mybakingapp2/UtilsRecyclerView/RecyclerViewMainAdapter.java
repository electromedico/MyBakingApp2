package com.example.alex.mybakingapp2.UtilsRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.mybakingapp2.IngredientsWidget2ConfigureActivity;
import com.example.alex.mybakingapp2.R;
import com.example.alex.mybakingapp2.RecipeListActivity;
import com.example.alex.mybakingapp2.model.Recipe;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter {
    private Recipe[] recipes;
    private final Context context;

    public RecyclerViewMainAdapter(Context c) {
        this.context = c;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_list_content,parent,false);
        if(context instanceof RecipeListActivity){
            viewHolder= new ViewHolderRecipesList(view,(RecipeListActivity)context);
        }
        else{
            viewHolder= new ViewHolderRecipesList(view,(IngredientsWidget2ConfigureActivity)context);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderRecipesList viewHolder =(ViewHolderRecipesList)holder;
        viewHolder.getRecipeName().setText(recipes[position].getName());
    }



    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.length;

    }

    public void setDataset(Recipe[] dataset){
        recipes = dataset;
        notifyDataSetChanged();
    }
}
