package com.example.alex.mybakingapp2.UtilsRecyclerView;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.mybakingapp2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class ViewHolderStep extends RecyclerView.ViewHolder{

    @BindView(R.id.description_step_layout)
    TextView stepIdTv;
    @BindView(R.id.imageView_step_layout)
    ImageView imageTv;
    @BindView(R.id.step_cl)
    ConstraintLayout constraintLayout;



    private Integer key;

    public ViewHolderStep(View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public TextView getStepIdTv() {
        return stepIdTv;
    }

    public ImageView getImageTv() {
        return imageTv;
    }

}
