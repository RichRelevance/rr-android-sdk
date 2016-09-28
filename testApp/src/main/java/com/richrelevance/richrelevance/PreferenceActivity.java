package com.richrelevance.richrelevance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.richrelevance.Callback;
import com.richrelevance.Error;
import com.richrelevance.RichRelevance;
import com.richrelevance.userPreference.ActionType;
import com.richrelevance.userPreference.FieldType;
import com.richrelevance.userPreference.UserPreferenceResponseInfo;

import java.util.ArrayList;
import java.util.List;

public class PreferenceActivity extends AppCompatActivity implements ActionBar.TabListener, PreferenceListFragment.LoadingListener {

    List<String> likedProducts, dislikedProducts;

    PreferenceAdapter pagerAdapter;

    ViewPager viewPager;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.rr_logo);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        pagerAdapter = new PreferenceAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for(int i = 0; i < pagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(pagerAdapter.getPageTitle(i)).setTabListener(this));
        }

        getPreferences(FieldType.PRODUCT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preference, menu);
        Utils.changeDrawableColor(menu.findItem(R.id.action_refresh).getIcon(), getResources().getColor(R.color.Primary_Light_Sub));
        Utils.changeDrawableColor(menu.findItem(R.id.action_clear).getIcon(), getResources().getColor(R.color.Primary_Light_Sub));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {

            case R.id.action_refresh:
                getPreferences(FieldType.PRODUCT);
                return true;

            case R.id.action_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want clear all the current user preferences?").setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearProducts();
                    }
                }).setNegativeButton("Cancel", null);
                builder.create().show();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void startLoading() {
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stopLoading() {
        if(progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void getPreferences(FieldType fieldType) {
        startLoading();
        RichRelevance.buildGetUserPreferences(fieldType).setCallback(new Callback<UserPreferenceResponseInfo>() {
            @Override
            public void onResult(final UserPreferenceResponseInfo result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result != null) {
                            PreferenceAdapter pagerAdapter = PreferenceActivity.this.pagerAdapter;
                            likedProducts = result.getProducts().getLikes();
                            dislikedProducts = result.getProducts().getDislikes();
                            ((PreferenceListFragment) pagerAdapter.instantiateItem(viewPager, 0)).loadProducts(likedProducts);
                            ((PreferenceListFragment) pagerAdapter.instantiateItem(viewPager, 1)).loadProducts(dislikedProducts);
                            stopLoading();
                        }
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

    private void clearProducts() {
        startLoading();
        List<String> allProducts = new ArrayList<>();
        if(likedProducts != null) { allProducts.addAll(likedProducts); }
        if(dislikedProducts != null) { allProducts.addAll(dislikedProducts); }
        RichRelevance.buildTrackUserPreference(FieldType.PRODUCT, ActionType.NEUTRAL, allProducts.toArray(new String[allProducts.size()])).setCallback(new Callback<UserPreferenceResponseInfo>() {
            @Override
            public void onResult(UserPreferenceResponseInfo result) {
                try {
                    Thread.sleep(5000);//Introduce delay to allow the server to make updates on its database
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        getPreferences(FieldType.PRODUCT);
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

    public class PreferenceAdapter extends FragmentStatePagerAdapter {
        private final String LIKE = "Likes";

        private final String DISLIKE = "Dislikes";

        public PreferenceAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return new PreferenceListFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                default:
                case 0:
                    return LIKE;
                case 1:
                    return DISLIKE;
            }
        }
    }
}
