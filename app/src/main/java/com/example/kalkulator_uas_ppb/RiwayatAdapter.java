package com.example.kalkulator_uas_ppb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    private ArrayList<RiwayatHitung> listRiwayat;
    private Context context;
    private SharedPreferences sharedPreferences;

    public RiwayatAdapter(ArrayList<RiwayatHitung> listRiwayat, Context context, SharedPreferences sharedPreferences) {
        this.listRiwayat = listRiwayat;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public RiwayatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.riwayat_list, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.ViewHolder holder, int position) {
        RiwayatHitung riwayatHitung = listRiwayat.get(position);
        holder.riwayat.setText(riwayatHitung.getRiwayat());
    }

    @Override
    public int getItemCount() {
        return listRiwayat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView riwayat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            riwayat = itemView.findViewById(R.id.tv_riwayat);

            itemView.setOnLongClickListener(view -> {

                int p = getLayoutPosition();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Hapus Riwayat?")
                        .setMessage("Ingin Hapus Riwayat?")
                        .setPositiveButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                        .setNegativeButton("Yes", ((dialogInterface, i) -> {
                            String id = listRiwayat.get(p).getId();
                            sharedPreferences.edit().remove(id).commit();
                            for (int u = 0; u < listRiwayat.size(); u++) {
                                if (id.equalsIgnoreCase(listRiwayat.get(u).getId())) {
                                    listRiwayat.remove(u);
                                    notifyItemRemoved(u);
                                    notifyItemChanged(u);
                                    notifyItemRangeChanged(u, listRiwayat.size());
                                }
                            }
                        }));
                AlertDialog dialog = alert.create();
                dialog.show();

                return true;
            });
        }
    }
}