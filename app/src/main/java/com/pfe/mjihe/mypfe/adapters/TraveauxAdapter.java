package com.pfe.mjihe.mypfe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Traveaux;

import java.util.List;

/**
 * Created by Mjihe on 11/06/2017.
 */
public class TraveauxAdapter extends RecyclerView.Adapter<TraveauxAdapter.MyViewHolder> {
    private List<Traveaux> traveauList;

    public TraveauxAdapter(List<Traveaux> traveauList) {
        this.traveauList = traveauList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_travaux, parent, false);

        return new TraveauxAdapter.MyViewHolder(itemView);
    }

    public int getItemCount() {
        if (traveauList == null)
            return 0;
        return traveauList.size();
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Traveaux traveaux = traveauList.get(position);
        holder.budget.setText(String.valueOf(traveaux.getBudget()));
        holder.id.setText(traveaux.getIdtraveau());
        holder.dat.setText(traveaux.getDate());
        holder.dure.setText(traveaux.getDuré());

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView id, dat, dure, budget;

        public MyViewHolder(View View) {
            super(View);
            id = (TextView) View.findViewById(R.id.idtravRow);
            dat = (TextView) View.findViewById(R.id.dateTravrow);
            dure = (TextView) View.findViewById(R.id.dureetravrow);
            budget = (TextView) View.findViewById(R.id.budgettravrow);
        }
    }
}
