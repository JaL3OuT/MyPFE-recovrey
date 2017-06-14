package com.pfe.mjihe.mypfe.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.pfe.mjihe.mypfe.adapters.RapportAdapter;
import com.pfe.mjihe.mypfe.admin.RapportView;
import com.pfe.mjihe.mypfe.models.Rapport;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.utils.DividerItemDecoration;
import com.pfe.mjihe.mypfe.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class rapport extends android.support.v4.app.Fragment {

    private String gov, local, comun;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private View rootview;
    private RecyclerView recycler;
    private RapportAdapter mAdapter;
    private List<Rapport> mRapportList = new ArrayList<>();

    public rapport() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.content_raport, container, false);
        initView();
        initFirebase();
        getadressAdmin();

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void initView() {
        recycler = (RecyclerView) rootview.findViewById(R.id.recycler_viewRapport);
        mAdapter = new RapportAdapter(mRapportList);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(mLayoutmanager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recycler.setAdapter(mAdapter);
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), mRapportList.get(position).getDate(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity() , RapportView.class);
                i.putExtra("rapport_index",String.valueOf(mRapportList.get(position).getRtimstemp()));
                startActivity(i);
            }
        });
    }
    public void getadressAdmin() {
        mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                local = user.getLocalite();
                gov = user.getGouvernorat();
                comun = user.getComunn();
                rapportData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void rapportData() {
        initFirebase();
        mRapportList.removeAll(mRapportList);
        mRef.child("Rapport").child(gov).child(comun).child(local).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rapportSnapshot : dataSnapshot.getChildren()) {
                    Rapport mRapport = rapportSnapshot.getValue(Rapport.class);
                    mRapportList.add(mRapport);
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
