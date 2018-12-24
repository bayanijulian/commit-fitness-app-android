package com.bayanijulian.glasskoala.view.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.model.User;
import com.bayanijulian.glasskoala.view.goal.CreateGoalActivity;
import com.bayanijulian.glasskoala.view.goal.GoalsActivity;
import com.bayanijulian.glasskoala.view.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    public static final String TAG = ProfileFragment.class.getSimpleName();

    private User currentUser;

    private ImageView profileImg;
    private TextView nameTxt;
    private Button goalsSeeAllBtn;
    private Button createGoalBtn;
    private BroadcastReceiver userReceiver;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment getInstance(User currentUser) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(User.LABEL, currentUser);

        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (getArguments() != null) {
            this.currentUser = getArguments().getParcelable(User.LABEL);
        }

        profileImg = view.findViewById(R.id.fragment_profile_img);
        Picasso.get().load(currentUser.getProfileImg()).noPlaceholder().into(profileImg);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfileDetailActivity();
            }
        });

        nameTxt = view.findViewById(R.id.fragment_profile_tv_name);
        nameTxt.setText(currentUser.getName());

        // receives from the main activity if the user is updated
        userReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (MainActivity.CURRENT_USER_UPDATE.equals(intent.getAction())) {
                    currentUser = intent.getParcelableExtra(MainActivity.CURRENT_USER_UPDATE);
                    Picasso.get().load(currentUser.getProfileImg()).noPlaceholder().into(profileImg);
                    nameTxt.setText(currentUser.getName());
                }
            }
        };


        goalsSeeAllBtn = view.findViewById(R.id.fragment_profile_btn_see_all);
        goalsSeeAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoalsActivity();
            }
        });

        createGoalBtn = view.findViewById(R.id.fragment_profile_btn_create_goal);
        createGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateGoalActivity();
            }
        });

        return view;
    }

    private void startCreateGoalActivity() {
        Intent createGoalActivity = new Intent(getActivity(), CreateGoalActivity.class);
        //startActivityForResult(createGoalActivity, MainActivity.RC_CREATE_GOAL);

        Objects.requireNonNull(getActivity())
                .startActivityForResult(createGoalActivity, MainActivity.RC_CREATE_GOAL);
    }

    private void startGoalsActivity() {
        Intent goalsActivity = new Intent(getActivity(), GoalsActivity.class);
        goalsActivity.putExtra(User.LABEL, currentUser);
        startActivity(goalsActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter userFilter = new IntentFilter(MainActivity.CURRENT_USER_UPDATE);
        Objects.requireNonNull(getContext()).registerReceiver(userReceiver, userFilter);
    }

//    public void onStop() {
//        super.onStop();
//        Objects.requireNonNull(getContext()).unregisterReceiver(userReceiver);
//    }

    private void startProfileDetailActivity() {
        Intent profileSettingsActivity = new Intent(getActivity(), ProfileSettingsActivity.class);
        profileSettingsActivity.putExtra(User.LABEL, currentUser);
        startActivity(profileSettingsActivity);
    }
}
