package com.pfe.mjihe.mypfe.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Factures;

import java.util.List;

/**
 * Created by Mjihe on 12/06/2017.
 */

public class FacturesAdapters extends RecyclerView.Adapter<FacturesAdapters.MyViewHolder> {
    private List<Factures> facturesList;

    public FacturesAdapters(List<Factures> facturesList) {
        this.facturesList = facturesList;
    }

    public FacturesAdapters.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_factures, parent, false);
        return new FacturesAdapters.MyViewHolder(itemView);
    }

    public int getItemCount() {
        if (facturesList == null)
            return 0;
        return facturesList.size();
    }

    public void onBindViewHolder(FacturesAdapters.MyViewHolder holder, int position) {
        Factures factures = facturesList.get(position);
        holder.id.setText(factures.getnLot());
        holder.dat.setText(factures.getDate());
        holder.somme.setText(String.valueOf(factures.getSomme()));
        if (factures.isPaymentF() == true) {
            holder.payF.setBackgroundColor(Color.GREEN);
        } else {
            holder.payF.setBackgroundColor(Color.RED);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView id, dat, somme;
        private LinearLayout payF;

        public MyViewHolder(View View) {
            super(View);
            id = (TextView) View.findViewById(R.id.nlotF);
            dat = (TextView) View.findViewById(R.id.datrF);
            somme = (TextView) View.findViewById(R.id.sommeF);
            payF = (LinearLayout) View.findViewById(R.id.paymentF);

        }
    }
}
