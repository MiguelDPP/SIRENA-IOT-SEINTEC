package com.seintec.sirenaiotseintec.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.seintec.sirenaiotseintec.R;
import com.seintec.sirenaiotseintec.models.Schedule;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private ArrayList<Schedule> localDataSet;

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView txtTitle;
        public TextView txtSubtitle;

        Schedule schedule;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageLeft = itemView.findViewById(R.id.imageLeft);
            imageRight = itemView.findViewById(R.id.imageRight);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtSubtitle = itemView.findViewById(R.id.txtSubtitle);
        }
    }
    public ScheduleAdapter(ArrayList<Schedule> dataSet) {
        localDataSet = dataSet;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.schedule = localDataSet.get(position);
        holder.txtTitle.setText(holder.schedule.getName());
        holder.txtSubtitle.setText("Hora: " + holder.schedule.getHour());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
