package com.pfe.mjihe.mypfe.admin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.adapters.TraveauxAdapter;
import com.pfe.mjihe.mypfe.admin.AjoutTraveau;
import com.pfe.mjihe.mypfe.admin.MapTraveauxActivity;
import com.pfe.mjihe.mypfe.admin.TraveauView;
import com.pfe.mjihe.mypfe.models.Traveaux;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.utils.DividerItemDecoration;
import com.pfe.mjihe.mypfe.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Traveaux_admin_fragment extends Fragment {

    private View rootview;
    private Button ajouTraveau, bmaptrave;
    private String gov, comun;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView recyclertraveaux;
    private TraveauxAdapter mTraveauxAdapter;
    private List<Traveaux> mTraveauListe = new ArrayList<>();

    public Traveaux_admin_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_traveaux_admin_fragment, container, false);

        initFirebase();
        initview();
        getadressAdmin();
        return rootview;
    }
    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void initview() {
        ajouTraveau = (Button) rootview.findViewById(R.id.ajoutTraveaux);
        ajouTraveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(), AjoutTraveau.class);
                startActivity(i1);
            }
        });
        bmaptrave = (Button) rootview.findViewById(R.id.maptraveaux);
        bmaptrave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(), MapTraveauxActivity.class);
                startActivity(i1);
            }
        });
        recyclertraveaux = (RecyclerView) rootview.findViewById(R.id.recyclerTraveau);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());
        recyclertraveaux.setLayoutManager(mLayoutmanager);
        recyclertraveaux.setItemAnimator(new DefaultItemAnimator());
        recyclertraveaux.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mTraveauxAdapter = new TraveauxAdapter(mTraveauListe);
        recyclertraveaux.setAdapter(mTraveauxAdapter);

        ItemClickSupport.addTo(recyclertraveaux).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), mTraveauListe.get(position).getIdtraveau().toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), TraveauView.class);
                i.putExtra("id traveau", mTraveauListe.get(position).getIdtraveau().toString());
                startActivity(i);
            }
        });
    }

    public void getadressAdmin() {
        initFirebase();
        mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                gov = user.getGouvernorat();
                comun = user.getComunn();
                traveuData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void traveuData() {
        initFirebase();
        mTraveauListe.removeAll(mTraveauListe);
        mRef.child("Region").child(gov).child(comun).child("traveaux").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("TAG", "children count " + dataSnapshot.getChildrenCount());
                for (DataSnapshot traveauSnapshot : dataSnapshot.getChildren()) {
                    Traveaux mTraveaux = traveauSnapshot.getValue(Traveaux.class);
                    Log.e("TAG", "id TRaveau : " + mTraveaux.getIdtraveau());
                    mTraveauListe.add(mTraveaux);
                }
                mTraveauxAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
