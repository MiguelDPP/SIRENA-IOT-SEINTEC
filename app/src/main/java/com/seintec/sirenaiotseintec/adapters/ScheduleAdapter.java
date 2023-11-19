package com.seintec.sirenaiotseintec.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.seintec.sirenaiotseintec.EditScheduleActivity;
import com.seintec.sirenaiotseintec.R;
import com.seintec.sirenaiotseintec.models.Schedule;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private ArrayList<Schedule> localDataSet;

    public void setLocalDataSet(ArrayList<Schedule> localDataSet) {
        this.localDataSet = localDataSet;
    }

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
    public ScheduleAdapter() {
        localDataSet = new ArrayList<>();
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
        holder.txtSubtitle.setText("Activo: " + (holder.schedule.isActivated()? "Si" : "No"));
        //Cambiar la imagenleft
        holder.imageRight.setImageResource(R.drawable.delete_icon);
        if (holder.schedule.isActivated()) {
            holder.itemView.setBackground(holder.itemView.getResources().getDrawable(R.drawable.bg_rounded_active));
            //Cambiar el color del svg de las imagenes
            holder.imageLeft.setColorFilter(holder.itemView.getResources().getColor(R.color.white));
            holder.imageRight.setColorFilter(holder.itemView.getResources().getColor(R.color.white));
            holder.txtTitle.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.txtSubtitle.setTextColor(holder.itemView.getResources().getColor(R.color.white));
        }
        if (holder.schedule.isPredetermined()) {
            holder.imageRight.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(v.getContext(), EditScheduleActivity.class);
            intent.putExtra("schedule", holder.schedule);
            v.getContext().startActivity(intent);
        });

        holder.imageRight.setOnClickListener((v)->{
            //Eliminar el elemento
            Toast.makeText(v.getContext(), "Eliminar", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
