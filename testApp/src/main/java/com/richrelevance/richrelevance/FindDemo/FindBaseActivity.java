package com.richrelevance.richrelevance.FindDemo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.richrelevance.richrelevance.ClientConfigurationManager;
import com.richrelevance.richrelevance.R;
import com.richrelevance.richrelevance.User;
import com.richrelevance.richrelevance.UserChooserActivity;

import static com.richrelevance.richrelevance.UserChooserActivity.createUserChooserActivityIntent;

public abstract class FindBaseActivity extends AppCompatActivity {

    protected abstract void loadActivity();

    private static final int SELECT_USER_RESULT = 989;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                startActivityForResult(createUserChooserActivityIntent(this, ClientConfigurationManager.getInstance().getSelectedUser()), SELECT_USER_RESULT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_USER_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                User selectedUser = data.getParcelableExtra(UserChooserActivity.KEY_SELECTED_USER);
                ClientConfigurationManager.getInstance().setUser(selectedUser);
                ClientConfigurationManager.getInstance().createConfiguration(this);
                loadActivity();
            }
        }
    }
}
