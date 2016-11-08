package com.richrelevance.richrelevance;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class UserChooserActivity extends AppCompatActivity {

    public static final String KEY_SELECTED_USER = "KEY_SELECTED_USER";
    private static final String KEY_PRESELECTED_USER = "KEY_PRESELECTED_USER";

    public static Intent createUserChooserActivityIntent(Activity activity, User selectedUser) {
        Intent intent = new Intent(activity, UserChooserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PRESELECTED_USER, selectedUser);
        intent.putExtras(bundle);
        return intent;
    }

    private FloatingActionButton fab;

    private final UserListAdapter adapter = new UserListAdapter();

    private User getPreselectedUser() {
        return getIntent().getParcelableExtra(KEY_PRESELECTED_USER);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_chooser);

        fab = (FloatingActionButton) findViewById(R.id.fabSelectUser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserChoiceResult(adapter.getSelectedUser());
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setUserList(getPreselectedUser(), User.listAll(User.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.done:
                sendUserChoiceResult(adapter.getSelectedUser());
                return true;
            case R.id.addUser:
                launchCreateUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchCreateUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(R.layout.dialog_create_user);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog d = (Dialog) dialog;
                EditText nameField = (EditText) d.findViewById(R.id.name);
                EditText userIdField = (EditText) d.findViewById(R.id.userId);

                User user = new User(nameField.getText().toString(), userIdField.getText().toString());
                adapter.addUser(user);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final Dialog dialog = builder.create();

        dialog.show();
    }

    private void sendUserChoiceResult(User user) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_SELECTED_USER, user);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
