package com.bayanijulian.glasskoala.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.model.Goal;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.bayanijulian.glasskoala.model.User;
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
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticate();

//        createGoalFab = findViewById(R.id.activity_main_fab_create_goal);
//        createGoalFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent startCreateGoalActivity = new Intent(v.getContext(), CreateGoalActivity.class);
//                startActivityForResult(startCreateGoalActivity, RC_CREATE_GOAL);
//            }
//        });

        viewPager = findViewById(R.id.activity_main_view_pager);
        navigation = findViewById(R.id.activity_main_navigation);

        setupPagerAdapter();
        setupViewPagerNavigation();
        setupViewPagerAnimation();

        toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
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
                DatabaseIO.getGoalIO().create(newGoal);
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
        } else {
            updateUser();
        }
    }

    private void updateUser() {
        String id = this.currentUser.getUid();
        String name = this.currentUser.getDisplayName();
        String phoneNumber = this.currentUser.getPhoneNumber();
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);

    }

    private void setupPagerAdapter() {
        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int currentPage) {
                switch (currentPage) {
                    case 0:
                        return new HomeFragment();
                    case 1:
                        return new GoalsFragment();
                    case 2:
                        return new ProfileFragment();
                    default:
                        throw new IllegalStateException();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        viewPager.setAdapter(pagerAdapter);
    }

    private void setupViewPagerNavigation() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private MenuItem previous;
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int currentPage) {
                if(previous == null) {
                    // set previous  to first page when first loaded
                    previous = navigation.getMenu().getItem(0);
                }

                previous.setChecked(false);

                // set the new page to checked
                navigation.getMenu().getItem(currentPage).setChecked(true);

                // update the new page to be previous
                previous = navigation.getMenu().getItem(currentPage);


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_main_item_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.menu_main_item_goals:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.menu_main_item_profile:
                        viewPager.setCurrentItem(2);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setupViewPagerAnimation() {
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setTranslationX(-1 * position * page.getWidth());

                if (Math.abs(position) < 0.5) {
                    page.setVisibility(View.VISIBLE);
                    page.setScaleX(1 - Math.abs(position));
                    page.setScaleY(1 - Math.abs(position));
                } else if (Math.abs(position) > 0.5) {
                    page.setVisibility(View.GONE);
                }
            }
        });
    }
}
