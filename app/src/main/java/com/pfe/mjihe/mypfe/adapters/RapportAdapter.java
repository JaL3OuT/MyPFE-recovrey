package com.pfe.mjihe.mypfe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Rapport;

import java.util.List;

/**
 * Created by Mjihe on 30/05/2017.
 */

public class RapportAdapter extends RecyclerView.Adapter<RapportAdapter.MyViewHolder> {
    private List<Rapport> rapportList;

    public RapportAdapter(List<Rapport> rapportList) {
        this.rapportList = rapportList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rapport_row, parent, false);
        return new MyViewHolder(itemView);
    }
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Rapport rapport = rapportList.get(position);
        holder.date.setText(rapport.getDate());
        holder.raport.setText(rapport.getRapport());
        holder.uid.setText(rapport.getRuidUid());
        // if (rapport.getLocation()== null)
        //{
        //  holder.latlan.setText("pas de localisation");
        //}
    }
    public int getItemCount() {
        return rapportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date, raport, uid, latlan;
        public MyViewHolder(View View) {
            super(View);
            date = (TextView) View.findViewById(R.id.date);
            raport = (TextView) View.findViewById(R.id.raportview);
            uid = (TextView) View.findViewById(R.id.uid);
            latlan = (TextView) View.findViewById(R.id.latlan);
        }
    }

}
