package com.richrelevance.richrelevance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.richrelevance.Callback;
import com.richrelevance.Error;
import com.richrelevance.RichRelevance;
import com.richrelevance.recommendations.Placement;
import com.richrelevance.recommendations.PlacementResponse;
import com.richrelevance.recommendations.PlacementResponseInfo;
import com.richrelevance.recommendations.RecommendedProduct;
import com.richrelevance.richrelevance.model.CardModel;
import com.richrelevance.richrelevance.model.Orientations;
import com.richrelevance.richrelevance.view.CardContainer;
import com.richrelevance.richrelevance.view.SimpleCardStackAdapter;
import com.richrelevance.userPreference.ActionType;
import com.richrelevance.userPreference.FieldType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CardContainer cardContainer;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageRR = (ImageView) findViewById(R.id.image_rr);
        Utils.changeDrawableColor(imageRR.getDrawable(), getResources().getColor(R.color.Text_Dark));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.rr_logo);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        resetStack(this.getApplication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Utils.changeDrawableColor(menu.findItem(R.id.action_refresh).getIcon(), getResources().getColor(R.color.Primary_Light_Sub));
        Utils.changeDrawableColor(menu.findItem(R.id.action_preference).getIcon(), getResources().getColor(R.color.Primary_Light_Sub));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                resetStack(this.getApplication());
                return true;
            case R.id.action_preference:
                Intent intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startLoading() {
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void stopLoading() {
        if(progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void resetStack(final Context context) {
        startLoading();

        // Create a "RecommendationsForPlacements" builder for the "add to cart" placement type.
        // Placement placement = new Placement(Placement.PlacementType.ADD_TO_CART, "prod1");
        RichRelevance.buildRecommendationsForPlacements(new Placement(Placement.PlacementType.ADD_TO_CART, "prod1"), new Placement(Placement.PlacementType.HOME, "prod2"))
                // Attach a callback
                .setCallback(new Callback<PlacementResponseInfo>() {
                    @Override
                    public void onResult(PlacementResponseInfo result) {
                        List<CardModel> cards = new ArrayList<>();

                        for(PlacementResponse placement : result.getPlacements()) {
                            for(final RecommendedProduct recommendedProduct : placement.getRecommendedProducts()) {
                                CardModel card = new CardModel(recommendedProduct.getName(), recommendedProduct.getBrand(), recommendedProduct.getImageUrl(), recommendedProduct.getPriceCents());

                                card.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
                                    @Override
                                    public void onLike() {
                                        RichRelevance.buildTrackUserPreference(FieldType.PRODUCT, ActionType.LIKE, recommendedProduct.getId()).execute();
                                    }

                                    @Override
                                    public void onDislike() {
                                        RichRelevance.buildTrackUserPreference(FieldType.PRODUCT, ActionType.DISLIKE, recommendedProduct.getId()).execute();
                                    }

                                    @Override
                                    public void onNeutral() {
                                        RichRelevance.buildTrackUserPreference(FieldType.PRODUCT, ActionType.NEUTRAL, recommendedProduct.getId()).execute();
                                    }
                                });
                                cards.add(card);
                            }
                        }

                        final SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(context, cards);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cardContainer = (CardContainer) findViewById(R.id.cardLayoutView);
                                cardContainer.setOrientation(Orientations.Orientation.Disordered);
                                cardContainer.setAdapter(adapter);
                                stopLoading();
                            }
                        });
                    }

                    @Override
                    public void onError(final Error error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stopLoading();
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).execute();
    }

    public void like(View view) {
        cardContainer.likeFling();
    }

    public void dislike(View view) {
        cardContainer.dislikeFling();
    }
}
