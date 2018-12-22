package com.bayanijulian.glasskoala.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bayanijulian.glasskoala.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private Button logoutBtn;

    private ImageButton profileBtn;
    private TextView nameTxt;

    private FirebaseUser currentUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        logoutBtn = view.findViewById(R.id.fragment_profile_btn_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Logging out user.");
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });

        profileBtn = view.findViewById(R.id.fragment_profile_btn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfileDetailActivity();
            }
        });

        nameTxt = view.findViewById(R.id.fragment_profile_tv_name);

        String name = currentUser.getDisplayName();
        nameTxt.setText(name);


        return view;
    }


    private void startProfileDetailActivity() {
        Intent profileDetailActivity = new Intent(getActivity(), ProfileDetailActivity.class);
        startActivity(profileDetailActivity);
    }
}
