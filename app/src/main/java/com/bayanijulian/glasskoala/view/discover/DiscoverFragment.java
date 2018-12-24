package com.bayanijulian.glasskoala.view.discover;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.database.DatabaseIO;
import com.bayanijulian.glasskoala.database.UserIO;
import com.bayanijulian.glasskoala.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {
    public static final String TAG = DiscoverFragment.class.getSimpleName();

    private SearchView search;
    private RecyclerView discoverList;
    private User currentUser;
    private FirebaseFirestore database;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment getInstance(User currentUser) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(User.LABEL, currentUser);

        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        currentUser = Objects.requireNonNull(getArguments()).getParcelable(User.LABEL);

        search = view.findViewById(R.id.fragment_discover_search);

        discoverList = view.findViewById(R.id.fragment_discover_list);
        discoverList.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));

        database = FirebaseFirestore.getInstance();

        UserIO.getAll(database, new DatabaseIO.ListListener<User>() {
            @Override
            public void onComplete(List<User> users) {
                updateList(users);
            }
        });
        return view;
    }

    private void updateList(List<User> users) {
        final DiscoverAdapter discoverAdapter = new DiscoverAdapter(currentUser, users);
        discoverList.setAdapter(discoverAdapter);
        //filters friends
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                discoverAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                discoverAdapter.getFilter().filter(newText);
                Log.d(TAG,"Search changed to " + newText);
                return true;
            }
        });
    }
}
