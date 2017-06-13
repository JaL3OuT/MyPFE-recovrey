package com.pfe.mjihe.mypfe.admin.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.adapters.LotAdapter;
import com.pfe.mjihe.mypfe.admin.Ajoutlot;
import com.pfe.mjihe.mypfe.admin.LotView;
import com.pfe.mjihe.mypfe.admin.mapLotActivity;
import com.pfe.mjihe.mypfe.models.Factures;
import com.pfe.mjihe.mypfe.models.Lot;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.utils.DividerItemDecoration;
import com.pfe.mjihe.mypfe.utils.ItemClickSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapTabedCitoyen extends Fragment {
    private String gov, comun;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private View rootview;
    private Button aCarte, ajouLot;
    private FloatingActionButton nottifier;
    private RecyclerView recyclerlot;
    private LotAdapter mlotAdapter;
    private Lot mlot;
    private User nUser;
    private List<Lot> mLotList = new ArrayList<>();
    private List<Lot> nLotList = new ArrayList<>();
    private List<User> nUserList = new ArrayList<>();
    private long datestamp;
    private String date;
    private ProgressDialog mDialog;


    public MapTabedCitoyen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_map_tabed_citoyen, container, false);
        initView();
        getadressAdmin();
        getuserlist();
        getadressAdmin1();
        return rootview;
    }

    private void initView() {
        aCarte = (Button) rootview.findViewById(R.id.mapLot);
        aCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(), mapLotActivity.class);
                startActivity(i1);
            }
        });
        ajouLot = (Button) rootview.findViewById(R.id.ajoutLot);
        ajouLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Ajoutlot.class);
                startActivity(i);
            }
        });
        mlotAdapter = new LotAdapter(mLotList);
        nottifier = (FloatingActionButton) rootview.findViewById(R.id.notiffier);
        nottifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                comparerCin();
            }
        });
        recyclerlot = (RecyclerView) rootview.findViewById(R.id.recycler_lot);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());
        recyclerlot.setLayoutManager(mLayoutmanager);
        recyclerlot.setItemAnimator(new DefaultItemAnimator());
        recyclerlot.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerlot.setAdapter(mlotAdapter);
        ItemClickSupport.addTo(recyclerlot).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), mLotList.get(position).getNumlot().toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LotView.class);
                i.putExtra("num lot", mLotList.get(position).getNumlot().toString());
                startActivity(i);
                // do it
            }
        });
        mDialog = new ProgressDialog(getActivity());
        mDialog.setTitle("Chargment");
        mDialog.setMessage("Attendez SVP !!");
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void getadressAdmin() {
        initFirebase();
        mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                gov = user.getGouvernorat();
                comun = user.getComunn();
                lotData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void lotData() {
        initFirebase();
        mLotList.removeAll(mLotList);
        mRef.child("Region").child(gov).child(comun).child("Lot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot lotSnapshot : dataSnapshot.getChildren()) {
                    Lot mlot = lotSnapshot.getValue(Lot.class);
                    mLotList.add(mlot);
                }
                mlotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getuserlist() {
        nUserList.removeAll(nUserList);
        mRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    nUser = userSnapshot.getValue(User.class);
                    gov = nUser.getGouvernorat();
                    comun = nUser.getComunn();
                    Log.e("TAG", "NUSER " + String.valueOf(nUser.getCIN()));
                    nUserList.add(nUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getadressAdmin1() {
        initFirebase();
        mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                gov = user.getGouvernorat();
                comun = user.getComunn();
                lotList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void lotList() {
        mRef.child("Region").child(gov).child(comun).child("Lot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot lotSnapshot : dataSnapshot.getChildren()) {
                    mlot = lotSnapshot.getValue(Lot.class);
                    Log.e("TAG", "LOT " + String.valueOf(mlot.getCin()));
                    nLotList.add(mlot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void comparerCin() {
        for (int i = 0; i < nLotList.size(); i++) {
            Log.e("TAG", "i = : " + i);
            Log.e("TAG", "nlot CIN = : " + nLotList.get(i).getCin());
            for (int j = 0; j < nUserList.size(); j++) {
                Log.e("TAG", "USER CIN  = : " + nUserList.get(j).getCIN());
                if (nLotList.get(i).getCin().equals(nUserList.get(j).getCIN())) {
                    Log.e("TAG", "CIN : exist = " + nUserList.get(j).getCIN());
                    getdate();
                    Factures mfac = new Factures(String.valueOf(nLotList.get(i).getNumlot()), date, Boolean.valueOf(nLotList.get(i).getPayment()),
                            Double.valueOf(nLotList.get(i).getTaxe()), Double.valueOf(nLotList.get(i).getLatlot()), Double.valueOf(nLotList.get(i).getLaglot()));
                    mRef.child("factures").child(nUserList.get(j).getCIN().toString()).child(nLotList.get(i).getNumlot().toString()).setValue(mfac).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            Toast.makeText(getActivity(), "Notification envoyer ", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }
    }

    private void getdate() {
        datestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date netDate = (new Date(datestamp));
        date = sdf.format(netDate);
    }

}
