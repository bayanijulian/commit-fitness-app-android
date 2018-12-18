package com.bayanijulian.glasskoala;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bayanijulian.glasskoala.model.Goal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {
    private List<Goal> goals;

    public GoalsAdapter(Goal [] goals) {
        this.goals = new ArrayList<>();
        this.goals.addAll(Arrays.asList(goals));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View goalItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_goal, viewGroup, false);
        return new ViewHolder(goalItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Goal goal = goals.get(i);
        viewHolder.locationTxt.setText(goal.getLocation().getName());
        viewHolder.dateTxt.setText(goal.getDate());
        viewHolder.timeTxt.setText(goal.getStartTime());
        String durationOutput = String.valueOf(goal.getDuration()) + " minutes";
        viewHolder.durationTxt.setText(durationOutput);
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
