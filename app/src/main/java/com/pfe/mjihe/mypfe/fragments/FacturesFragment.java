package com.pfe.mjihe.mypfe.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.activities.FacturesView;
import com.pfe.mjihe.mypfe.adapters.FacturesAdapters;
import com.pfe.mjihe.mypfe.models.Factures;
import com.pfe.mjihe.mypfe.utils.DividerItemDecoration;
import com.pfe.mjihe.mypfe.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacturesFragment extends Fragment {
    private View rootview;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView recyclerfact;
    private FacturesAdapters mfacturesAdapters;
    private List<Factures> mFacturesListe = new ArrayList<>();

    public FacturesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_factures, container, false);
        initview();
        initFirebase();
        getFacturesList();
        return rootview;
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void initview() {
        recyclerfact = (RecyclerView) rootview.findViewById(R.id.recyclerfactures);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());
        recyclerfact.setLayoutManager(mLayoutmanager);
        recyclerfact.setItemAnimator(new DefaultItemAnimator());
        recyclerfact.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mfacturesAdapters = new FacturesAdapters(mFacturesListe);
        recyclerfact.setAdapter(mfacturesAdapters);
        ItemClickSupport.addTo(recyclerfact).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), mFacturesListe.get(position).getDate().toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), FacturesView.class);
                i.putExtra("num Facture", mFacturesListe.get(position).getnLot());
                startActivity(i);
            }
        });
    }

    private void getFacturesList() {
        mRef.child("user").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userCin = dataSnapshot.child("cin").getValue().toString();
                mFacturesListe.removeAll(mFacturesListe);
                mRef.child("factures").child(userCin.toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot facturesSnapshot : dataSnapshot.getChildren()) {
                            Factures mfactures = facturesSnapshot.getValue(Factures.class);
                            mFacturesListe.add(mfactures);
                        }
                        mfacturesAdapters.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
