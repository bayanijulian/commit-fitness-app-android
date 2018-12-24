package com.bayanijulian.glasskoala.view.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.bayanijulian.glasskoala.database.GoalIO;
import com.bayanijulian.glasskoala.database.UserIO;
import com.bayanijulian.glasskoala.model.Goal;
import com.bayanijulian.glasskoala.model.User;
import com.bayanijulian.glasskoala.view.discover.DiscoverFragment;
import com.bayanijulian.glasskoala.view.SignInActivity;
import com.bayanijulian.glasskoala.view.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int RC_CREATE_GOAL = 3245;
    public static final String CURRENT_USER_UPDATE = "current user updated";

    private User currentUser;
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private Toolbar toolbar;

    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private DiscoverFragment discoverFragment;

    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // gets the current user from sign in activity
        Intent fromSignIn = getIntent();
        currentUser = fromSignIn.getParcelableExtra(User.LABEL);

        database = FirebaseFirestore.getInstance();

        viewPager = findViewById(R.id.activity_main_view_pager);
        navigation = findViewById(R.id.activity_main_navigation);

        setupPagerAdapter();
        setupViewPagerNavigation();
        setupViewPagerAnimation();

        toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        // listener to check if user is signed out randomly
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startSignInActivity();
                } else {
                    UserIO.get(database, user, new DatabaseIO.SingleListener<User>() {
                        @Override
                        public void onComplete(User data) {
                            Intent updateUser = new Intent();
                            updateUser.putExtra(CURRENT_USER_UPDATE, currentUser);
                            sendBroadcast(updateUser);
                        }
                    });
                }
            }
        };


    }

    private void startSignInActivity() {
        Intent signInActivity = new Intent(this, SignInActivity.class);
        startActivity(signInActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CREATE_GOAL) {
            if (resultCode == RESULT_OK && data != null) {
                Log.d(TAG, "New goal created. Attempting to write to database.");
                Goal newGoal = data.getParcelableExtra(Goal.LABEL);
                newGoal.setUserId(currentUser.getId());
                GoalIO.create(database, newGoal);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // automatically fetches new current user
        auth.addAuthStateListener(authStateListener);
        UserIO.get(database, auth.getCurrentUser(), new DatabaseIO.SingleListener<User>() {
            @Override
            public void onComplete(User data) {
                Intent updateUser = new Intent();
                updateUser.putExtra(CURRENT_USER_UPDATE, currentUser);
                sendBroadcast(updateUser);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    private void setupPagerAdapter() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavigationPagerAdapter pagerAdapter = new NavigationPagerAdapter(fragmentManager);
        this.homeFragment = HomeFragment.getInstance();
        this.profileFragment = ProfileFragment.getInstance(currentUser);
        this.discoverFragment = DiscoverFragment.getInstance(currentUser);

        pagerAdapter.setHomeFragment(this.homeFragment);
        pagerAdapter.setProfileFragment(this.profileFragment);
        pagerAdapter.setDiscoverFragment(this.discoverFragment);
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
                    case R.id.menu_main_item_profile:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.menu_main_item_discover:
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
