package com.bayanijulian.glasskoala.view.discover;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.UserIO;
import com.bayanijulian.glasskoala.model.User;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder>
                             implements Filterable {
    private static final String TAG = DiscoverAdapter.class.getSimpleName();
    private List<User> users;
    private User currentUser;
    private List<User> usersFiltered;

    public DiscoverAdapter(User currentUser, List<User> users) {
        this.currentUser = currentUser;
        this.users = users;
        this.usersFiltered = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_user_search, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = usersFiltered.get(i);
        viewHolder.setView(currentUser, user);
    }

    @Override
    public int getItemCount() {
        return usersFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filter = constraint.toString().toLowerCase();

                if (filter.isEmpty()) {
                    usersFiltered = users;
                } else {
                    //modifies filtered list to only contain users with the filter
                    ArrayList<User> filteredList = new ArrayList<>();
                    for (User currentUser : users) {
                        // handles the case when there is no name
                        if (currentUser.getName() == null){
                            currentUser.setName("");
                        }

                        if (currentUser.getName().toLowerCase().contains(filter)) {
                            filteredList.add(currentUser);
                        }
                    }
                    usersFiltered = filteredList;
                }
                //passes the filtered list to results
                FilterResults filterResults = new FilterResults();
                filterResults.values = usersFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //updates the adapter
                //usersFiltered = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public ImageView profileImg;
        public TextView nameTxt;
        public ToggleButton followBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.profileImg = itemView.findViewById(R.id.item_user_search_img_profile);
            this.nameTxt = itemView.findViewById(R.id.item_user_search_name);
            this.followBtn = itemView.findViewById(R.id.item_user_search_btn_follow);
        }

        /**
         * Binds the user data to the views of the item
         *
         * @param currentUser Logged In User, used for only the follow button to get followers list
         * @param user user that needs to be represented in the list
         */
        public void setView(final User currentUser, final User user) {
            // load profile image
            boolean hasProfileImg = user.getProfileImg() != null && !user.getProfileImg().isEmpty();
            if (hasProfileImg) {
                //TODO toggle IMAGE or INITIAL state if they have image or not
                Picasso.get().load(user.getProfileImg())
                        .fit()
                        .centerCrop()
                        .noPlaceholder()
                        .into(profileImg);

            }
            nameTxt.setText(user.getName());
            if (currentUser.isFollowing(user)) {
                followBtn.setChecked(true);
            }
            followBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isFollowing) {
                    UserIO.updateFriendList(currentUser, user, isFollowing);
                }
            });
        }
    }

}
