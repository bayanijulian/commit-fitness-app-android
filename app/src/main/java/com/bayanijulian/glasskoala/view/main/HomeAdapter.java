package com.bayanijulian.glasskoala.view.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private static final String TAG = HomeAdapter.class.getSimpleName();
    private List<User> users;

    public HomeAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_home, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = users.get(i);
        viewHolder.setView(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView nameTxt;
        public ImageView profileImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.nameTxt = itemView.findViewById(R.id.item_home_txt_name);
            this.profileImg = itemView.findViewById(R.id.item_home_img_profile);
        }

        public void setView(final User user) {
            nameTxt.setText(user.getName());

            boolean hasProfileImg = user.getProfileImg() != null && !user.getProfileImg().isEmpty();
            if (hasProfileImg) {
                Picasso.get().load(user.getProfileImg())
                        .fit()
                        .centerCrop()
                        .noPlaceholder()
                        .into(profileImg);

            }
        }
    }
}
