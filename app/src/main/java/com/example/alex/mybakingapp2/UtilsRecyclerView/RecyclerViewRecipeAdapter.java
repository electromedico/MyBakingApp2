package com.example.alex.mybakingapp2.UtilsRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.mybakingapp2.R;
import com.example.alex.mybakingapp2.StepListActivity;
import com.example.alex.mybakingapp2.model.Ingredient;
import com.example.alex.mybakingapp2.model.Label;
import com.example.alex.mybakingapp2.model.Recipe;
import com.example.alex.mybakingapp2.model.Step;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Object> items = new ArrayList<>();
    private final Context context;

    private final int INGREDIENT_CL =0;
    private final int STEP_CL = 1;
    private final int LABEL_CL=2;


    public RecyclerViewRecipeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder ;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType){
            case INGREDIENT_CL:
                view=inflater.inflate(R.layout.ingredient_layout,parent,false);
                viewHolder= new ViewHolderIngredients(view);
                break;

            case STEP_CL:
                view=inflater.inflate(R.layout.step_layout,parent,false);
                viewHolder= new ViewHolderStep(view);
                break;

            case LABEL_CL:
                view=inflater.inflate(R.layout.label_layout,parent,false);
                viewHolder= new ViewHolderLabel(view);
                break;
            default:
                viewHolder= null;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()){
            case INGREDIENT_CL:
                ViewHolderIngredients viewHolderIngredients= (ViewHolderIngredients) holder;
                configureIngredients(viewHolderIngredients,position);
                break;

            case STEP_CL:
                ViewHolderStep viewHolderStep = (ViewHolderStep) holder;
                configureSteps(viewHolderStep,position);
                break;

            case LABEL_CL:
                ViewHolderLabel viewHolderLabel= (ViewHolderLabel) holder;
                configureLabels(viewHolderLabel,position);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        if (items==null || items.size()==0)return 0;
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = items.get(position);
        if ( o instanceof Ingredient){
            return INGREDIENT_CL;
        }
        else if (o instanceof Step){
            Step s = (Step) (items.get(position));
           return STEP_CL;
           }
        else{
            return LABEL_CL;
        }
    }

    public void setmDataset(Recipe recipe){

        //Insert Ingredients Label
        items.add(new Label(Label.LABEL_INGREDIENTS));

        //retrieve list of ingredients and add to the items list
        Object[] ingredients = recipe.getIngredients().toArray();
        for (Object ingredient : ingredients){
            items.add(ingredient);
        }

        //Insert Steps Label
        items.add(new Label(Label.LABEL_STEPS));

        //retrieve list of steps and add them to the items list
        Object[] steps = recipe.getSteps().toArray();
        for ( Object step : steps){
            items.add(step);
        }

        notifyDataSetChanged();


    }

    private void configureSteps(ViewHolderStep viewHolderStep, int position){
        Step step = (Step) items.get(position);
        String stepString;

        if (step.getId()==0){
            stepString=step.getShortDescription();
            viewHolderStep.getStepIdTv().setText(stepString);

        }
        else {
            stepString=context.getString(R.string.step_label)+" "+step.getId()+" "+step.getShortDescription();
            viewHolderStep.getStepIdTv().setText(stepString);

        }

        if (step.getVideoURL()!= null && !step.getVideoURL().isEmpty()){
            viewHolderStep.getImageTv().setImageDrawable(context.getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
        }
        else{
            viewHolderStep.getImageTv().setImageDrawable(context.getDrawable(R.drawable.ic_image_black_24dp));
        }
        viewHolderStep.itemView.setTag(position);
        viewHolderStep.itemView.setOnClickListener((StepListActivity) context );

    }

    private void configureLabels(ViewHolderLabel holderLabel, int position){
        if (position==0){
            holderLabel.getLabelTv().setText(Label.LABEL_INGREDIENTS);
        }
        else {
            holderLabel.getLabelTv().setText(Label.LABEL_STEPS);
        }
    }



    private void configureIngredients(ViewHolderIngredients viewHolderIngredients, int position){
        Ingredient i = (Ingredient) items.get(position);
        viewHolderIngredients.getIngredientTv().setText(i.getIngredient());
        viewHolderIngredients.getQuantityTv().setText(
               String.valueOf(i.getQuantity()) +
                        " " + i.getMeasure());

    }


    /**
     * local class to manage the label viewholder
     * */
    private class  ViewHolderLabel extends RecyclerView.ViewHolder{
        private TextView labelTv;

        ViewHolderLabel(View itemView) {
            super(itemView);
            labelTv= itemView.findViewById(R.id.label_tv);
        }

        TextView getLabelTv() {
            return labelTv;
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public Object getItemAtPosition(int position){
        return items.get(position) ;
    }
}
