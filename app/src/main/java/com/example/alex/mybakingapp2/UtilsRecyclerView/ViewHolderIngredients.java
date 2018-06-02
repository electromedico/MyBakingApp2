package com.example.alex.mybakingapp2.UtilsRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alex.mybakingapp2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class ViewHolderIngredients extends RecyclerView.ViewHolder {
    @BindView(R.id.ingredient_tv)
    TextView ingredientTv;
    @BindView(R.id.quantity_tv)
    TextView quantityTv;

    public ViewHolderIngredients(View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public TextView getIngredientTv() {
        return ingredientTv;
    }

    public TextView getQuantityTv() {
        return quantityTv;
    }

}
