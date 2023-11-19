package com.seintec.sirenaiotseintec.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.seintec.sirenaiotseintec.R;
import com.seintec.sirenaiotseintec.models.Hour;
import com.seintec.sirenaiotseintec.models.Schedule;

import java.util.ArrayList;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {
    private ArrayList<Hour> localDataSet;
    private Schedule schedule;

    public void setLocalDataSet(ArrayList<Hour> localDataSet) {
        this.localDataSet = localDataSet;
    }

    public ArrayList<Hour> getLocalDataSet() {
        return localDataSet;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView txtTitle;
        public TextView txtSubtitle;

        Hour hour;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageLeft = itemView.findViewById(R.id.imageLeft);
            imageRight = itemView.findViewById(R.id.imageRight);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtSubtitle = itemView.findViewById(R.id.txtSubtitle);
        }
    }

    public HoursAdapter() {
        localDataSet = new ArrayList<>();
    }

    public HoursAdapter(ArrayList<Hour> dataSet) {
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
        holder.hour = localDataSet.get(position);
        holder.txtTitle.setText(holder.hour.getType());
        holder.txtSubtitle.setText(holder.hour.getHour());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
