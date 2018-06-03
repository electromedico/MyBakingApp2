package com.example.alex.mybakingapp2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.alex.mybakingapp2.UtilsRecyclerView.RecyclerViewRecipeAdapter;
import com.example.alex.mybakingapp2.model.Recipe;
import com.example.alex.mybakingapp2.model.Step;

import java.io.Serializable;

import static com.example.alex.mybakingapp2.IngredientsWidget2.WIDGET_ID_KEY;
import static com.example.alex.mybakingapp2.IngredientsWidget2ConfigureActivity.PREF_PREFIX_KEY;

/**
 * An activity representing a list of Recipe. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    RecyclerView recyclerView;
    private RecyclerViewRecipeAdapter recipeAdapter;
    private Recipe recipe;
    private StepListActivity mParentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        mParentActivity= this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        Bundle bundle =getIntent().getExtras();

        if (bundle != null){
            if (bundle.containsKey(WIDGET_ID_KEY)){
                String widgetId=bundle.getString(WIDGET_ID_KEY);
                recipe = (Recipe) bundle.getSerializable(PREF_PREFIX_KEY+widgetId);
            }
            else{
                recipe = (Recipe) getIntent().getSerializableExtra(StepListActivity.ARG_ITEM_ID);
            }
            setTitle(recipe.getName());
        }


        recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recipeAdapter = new RecyclerViewRecipeAdapter(this);
        recyclerView.setAdapter(recipeAdapter);
        recipeAdapter.setmDataset(recipe);
    }



    @Override
    public void onClick(View v) {
    int tag = (int) v.getTag();
    Object item=recipeAdapter.getItemAtPosition(tag);
    if(item instanceof Step){
        Step s = (Step) item;
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(StepDetailFragment.ARG_ITEM_ID, s);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            Context context = this;
            Intent intent = new Intent(context, StepDetailActivity.class);
            intent.putExtra(StepDetailFragment.ARG_ITEM_ID,(Serializable) s);

            context.startActivity(intent);
        }
    }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        outState.putParcelable(getString(R.string.manager_state),manager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {

            //Restore state of the RecyclerView
            String keyState = getString(R.string.manager_state);
            if(savedInstanceState.containsKey(keyState)){

                Parcelable state =savedInstanceState.getParcelable(keyState);

                LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                manager.onRestoreInstanceState(state);

            }
        }
    }
}
