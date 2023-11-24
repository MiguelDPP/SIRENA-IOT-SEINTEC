package com.seintec.sirenaiotseintec.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.seintec.sirenaiotseintec.R;
import com.seintec.sirenaiotseintec.models.Device;
import com.seintec.sirenaiotseintec.models.Hour;
import com.seintec.sirenaiotseintec.models.Schedule;
import com.seintec.sirenaiotseintec.utils.Database;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {
    private ArrayList<Hour> localDataSet;
    private Schedule schedule;

    private ConstraintLayout progressView;

    public void setProgressView(ConstraintLayout progressView) {
        this.progressView = progressView;
    }

    //Variable Callback de metodo

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

        if(holder.hour.getType().equals("Entrada")) {
            holder.imageLeft.setImageResource(R.drawable.entrada_icon);
        }else if (holder.hour.getType().equals("Salida")) {
            holder.imageLeft.setImageResource(R.drawable.salida_icon);
        }else if (holder.hour.getType().equals("CambioHora")) {
            holder.imageLeft.setImageResource(R.drawable.alarm_icon_30);
        } else {
            holder.imageLeft.setImageResource(R.drawable.descanso_icon);
        }

        holder.imageRight.setImageResource(R.drawable.delete_icon);

        holder.imageRight.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Eliminar");
            builder.setMessage("¿Está seguro de eliminar este horario?");
            builder.setPositiveButton("Si", (dialog, which) -> {
                Hour hour = localDataSet.get(position);
                localDataSet.remove(position);
                if (hour.getType().equals("Entrada")) {
                    schedule.getEntrada().remove(hour.getIndex());
                } else if (hour.getType().equals("Salida")) {
                    schedule.getSalida().remove(hour.getIndex());
                } else if (hour.getType().equals("CambioHora")) {
                    schedule.getCambioHora().remove(hour.getIndex());
                } else {
                    schedule.getDescanso().remove(hour.getIndex());
                }

                progressView.setVisibility(View.VISIBLE);

                Database.saveInformationDatabase("devices/"+ Device.getUserLogin().getMac()+"/schedules", schedule, schedule.getName())
                        .thenAccept(aVoid -> {
                            Toast.makeText(v.getContext(), "Se actualizo correctamente", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            progressView.setVisibility(View.GONE);
                        });
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.show();


        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
