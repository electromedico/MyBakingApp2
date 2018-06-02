package com.example.alex.mybakingapp2.UtilsRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.mybakingapp2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class ViewHolderStep extends RecyclerView.ViewHolder {
    @BindView(R.id.step_id_tv)
    TextView stepIdTv;
    @BindView(R.id.step_description_tv)
    TextView stepTv;
    @BindView(R.id.thumbnail_im)
    ImageView imageTv;

    private String imageURL;

    public ViewHolderStep(View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);

    }

    public TextView getStepIdTv() {
        return stepIdTv;
    }

    public TextView getStepTv() {
        return stepTv;
    }

    public ImageView getImageTv() {
        return imageTv;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
