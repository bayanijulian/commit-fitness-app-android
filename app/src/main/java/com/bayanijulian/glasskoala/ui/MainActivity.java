package com.bayanijulian.glasskoala.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.model.Goal;
import com.bayanijulian.glasskoala.util.DatabaseIO;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_LOGIN = 43278;
    private static final int RC_CREATE_GOAL = 3245;

    private FirebaseUser currentUser;
    private FloatingActionButton createGoalFab;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticate();

        createGoalFab = findViewById(R.id.activity_main_fab_create_goal);
        createGoalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCreateGoalActivity = new Intent(v.getContext(), CreateGoalActivity.class);
                startActivityForResult(startCreateGoalActivity, RC_CREATE_GOAL);
            }
        });

        viewPager = findViewById(R.id.activity_main_view_pager);
        PagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabLayout = findViewById(R.id.activity_main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN) {
            if (resultCode == RESULT_OK) {
                authenticate();
                Log.d(TAG, "Login Success, User ID is " + currentUser.getUid());
            } else {
                Log.d(TAG, "Login Failure");
            }
        } else if (requestCode == RC_CREATE_GOAL) {
            if (resultCode == RESULT_OK && data != null) {
                Log.d(TAG, "New goal created. Attempting to write to database.");
                Goal newGoal = data.getParcelableExtra(Goal.LABEL);
                newGoal.setUserId(currentUser.getUid());
                DatabaseIO.addGoal(newGoal);
            }
        }
    }

    private void login() {
        // choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_LOGIN);
    }

    private void authenticate() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "No current user. Going to login activity.");
            login();
        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 2;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int currentPage) {
            switch (currentPage) {
                case 0:
                    return new GroupsFragment();
                case 1:
                    return new GoalsFragment();
                default:
                    throw new IllegalStateException();
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int currentPage) {
            switch(currentPage) {
                case 0:
                    return "Groups";
                case 1:
                    return "Goals";
                default:
                    throw new IllegalStateException();
            }
        }
    }

}
