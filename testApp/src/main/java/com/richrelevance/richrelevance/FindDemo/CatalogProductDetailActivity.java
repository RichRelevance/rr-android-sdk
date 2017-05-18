package com.richrelevance.richrelevance.FindDemo;


import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.richrelevance.RichRelevance;
import com.richrelevance.find.search.SearchResultProduct;
import com.richrelevance.recommendations.Placement;
import com.richrelevance.richrelevance.R;
import com.squareup.picasso.Picasso;

public class CatalogProductDetailActivity extends FindBaseActivity {

    private static final String KEY_PRODUCT = "KEY_PRODUCT";
    private static final String KEY_ADD_TO_CART_PARAMS = "KEY_ADD_TO_CART_PARAMS";

    public static Intent createCatalogProductDetailActivityIntent(Activity activity, SearchResultProduct product, String addToCartParams) {
        Intent intent = new Intent(activity, CatalogProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT, product);
        intent.putExtra(KEY_ADD_TO_CART_PARAMS, addToCartParams);
        return intent;
    }

    private SearchResultProduct product;

    private ImageView image;
    private TextView name;
    private TextView brand;
    private TextView price;
    private FloatingActionButton fabAddToCart;

    private SearchResultProduct getProduct() {
        if(product == null) {
            product = getIntent().getParcelableExtra(KEY_PRODUCT);
        }
        return product;
    }

    private String getAddToCartParams() {
        return getIntent().getStringExtra(KEY_ADD_TO_CART_PARAMS);
    }

    @Override
    protected void loadActivity() {
        setContentView(R.layout.activity_detail_catalog_product);

        image = (ImageView) findViewById(R.id.product_image);
        name = (TextView) findViewById(R.id.product_name);
        brand = (TextView) findViewById(R.id.product_brand);
        price = (TextView) findViewById(R.id.product_price);
        fabAddToCart = (FloatingActionButton) findViewById(R.id.fabAddToCart);

        loadProduct(getProduct());
    }

    private void loadProduct(SearchResultProduct product) {
        Picasso.with(image.getContext()).load(product.getImageId()).fit().centerCrop().into(image);
        name.setText(product.getName());
        brand.setText(product.getBrand());
        price.setText(convertCents(product.getSalesPriceCents()));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(product.getName());
        }
    }

    public String convertCents(int cents) {
        return String.format(getResources().getString(R.string.format), Integer.toString(cents / 100), Integer.toString(cents % 100)).replace(" ", "0");
    }

    // attached to fab
    public void addToCart(View view) {

        // rotation animation, fab clickable is set to false until animation is completed
        Animation rotate_fab = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_fab);
        fabAddToCart.startAnimation(rotate_fab);
        rotate_fab.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fabAddToCart.setClickable(false);
                RichRelevance.buildRecommendationsForPlacements()
                        .setPlacements(new Placement(Placement.PlacementType.ADD_TO_CART, "add_to_cart"))
                        .setAddToCartParams(getAddToCartParams())
                        .execute();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fabAddToCart.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // functionality
    }
}
