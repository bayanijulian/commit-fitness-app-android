package com.bayanijulian.glasskoala.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bayanijulian.glasskoala.R;
import com.bayanijulian.glasskoala.model.Goal;
import com.bayanijulian.glasskoala.util.LocationValidator;

import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {
    private static final String TAG = GoalsAdapter.class.getSimpleName();
    private List<Goal> goals;

    public GoalsAdapter(List<Goal> goals) {
        this.goals = goals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View goalItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_goal, viewGroup, false);
        return new ViewHolder(goalItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Goal goal = goals.get(i);
        viewHolder.locationTxt.setText(goal.getLocation().getName());
        viewHolder.dateTxt.setText(goal.getDate());
        viewHolder.timeTxt.setText(goal.getStartTime());
        String durationOutput = String.valueOf(goal.getDuration());
        viewHolder.durationTxt.setText(durationOutput);
        final LocationValidator.Listener listener = new LocationValidator.Listener() {
            @Override
            public void onComplete(boolean isValid) {
                if(isValid)
                    viewHolder.checkInBtn.setText("SUCESS");
                else
                    viewHolder.checkInBtn.setText("NOT CLOSE ENOUGH");
            }
        };
        viewHolder.checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Check In Button Clicked!");
                LocationValidator.updateUserLocation(v.getContext(), goal.getLocation().getUserLocation(),listener);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView locationTxt;
        public TextView dateTxt;
        public TextView timeTxt;
        public TextView durationTxt;
        public Button checkInBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.locationTxt = itemView.findViewById(R.id.item_goal_tv_location);
            this.dateTxt = itemView.findViewById(R.id.item_goal_tv_date);
            this.timeTxt = itemView.findViewById(R.id.item_goal_tv_time);
            this.durationTxt = itemView.findViewById(R.id.item_goal_tv_duration);
            this.checkInBtn = itemView.findViewById(R.id.item_goal_btn_check_in);
        }
    }
}
