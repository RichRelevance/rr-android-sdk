package com.richrelevance.richrelevance;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListingViewHolder> {

    private User selectedUser;

    private List<User> userList = new ArrayList<>();

    @Override
    public UserListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(UserListingViewHolder.LAYOUT_RESOURCE, parent, false);
        return new UserListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListingViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(User selectedUser, List<User> userList) {
        if (userList == null) {
            userList = new ArrayList<>();
        }
        this.userList = userList;
        if (userList.contains(selectedUser)) {
            this.selectedUser = selectedUser;
        }
        notifyDataSetChanged();
    }

    public void addUser(User user) {
        if (user != null) {
            userList.add(user);
            notifyDataSetChanged();
        }
    }

    public User getSelectedUser() {
        return selectedUser;
    }

//    public void setSelectedUser(User selectedUser) {
//        int position = userList.indexOf(selectedUser);
//        onUserClicked(position);
//    }

    private void onUserClicked(int position) {
//        if (position == -1) {
//            if(selectedUser != null) {
//                notifyItemChanged(userList.indexOf(selectedUser));
//            }
//            selectedUser = null;
//        } else {
            if(selectedUser != null) {
                notifyItemChanged(userList.indexOf(selectedUser));
            }
            if (selectedUser == userList.get(position)) {
                selectedUser = null;
            } else {
                selectedUser = userList.get(position);
            }
            notifyItemChanged(position);
//        }
    }

    protected class UserListingViewHolder extends RecyclerView.ViewHolder {

        private static final int LAYOUT_RESOURCE = R.layout.listing_user;

        private View view;
        private TextView userIdTextView;

        public UserListingViewHolder(View itemView) {
            super(itemView);

            userIdTextView = (TextView) itemView.findViewById(R.id.userIdTextView);
            view = itemView;
        }

        public void bind(final User user) {
            userIdTextView.setText(user.getName() + "(" + user.getUserID() + ")");

            view.setSelected(user == selectedUser);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    onUserClicked(position);
                }
            });
        }
    }
}
