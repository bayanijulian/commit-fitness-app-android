package com.bayanijulian.glasskoala.view.goal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        viewHolder.startTimeTxt.setText(goal.getStartTime());
        viewHolder.endTimeTxt.setText(goal.getEndTime());


        final LocationValidator.Listener listener = new LocationValidator.Listener() {
            @Override
            public void onComplete(boolean isValid) {
                if (isValid)
                    viewHolder.statusTxt.setText("You got this! Only " + goal.getDuration() + " to go!");
                else
                    viewHolder.statusTxt.setText("NOT CLOSE ENOUGH");
            }
        };
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView locationTxt;
        public TextView dateTxt;
        public TextView startTimeTxt;
        public TextView endTimeTxt;
        public TextView statusTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.locationTxt = itemView.findViewById(R.id.goal_txt_location);
            this.dateTxt = itemView.findViewById(R.id.goal_txt_date);
            this.startTimeTxt = itemView.findViewById(R.id.goal_txt_start_time);
            this.endTimeTxt = itemView.findViewById(R.id.goal_txt_end_time);
            this.statusTxt = itemView.findViewById(R.id.goal_txt_status);
        }
    }
}
