package com.bayanijulian.glasskoala.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.bayanijulian.glasskoala.model.User;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView homeList;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        homeList = view.findViewById(R.id.fragment_home_rv);
        homeList.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));

        updateHomeList();
        return view;
    }



    private void updateHomeList() {
        DatabaseIO.getUserIO().read(new DatabaseIO.Listener<User>() {
            @Override
            public void onComplete(List<User> data) {
                HomeAdapter homeAdapter = new HomeAdapter(data);
                homeList.setAdapter(homeAdapter);
            }
        });
    }

}
