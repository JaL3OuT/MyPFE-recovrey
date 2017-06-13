package com.pfe.mjihe.mypfe.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.pfe.mjihe.mypfe.activities.MainActivity;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.models.Wallet;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet_SoldeFragment extends Fragment implements View.OnClickListener {
    private String sommeWallet;
    private Button recharge, sup;
    private TextView msg;
    private View rootview;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public Wallet_SoldeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.wallet_soldefragment, container, false);
        initinstance();
        initView();
        return rootview;
    }

    @Override
    public void onClick(View v) {
        User u = new User();
        Wallet w = new Wallet();
        u.setWallet(w);
        mRef.child("user").child(mUser.getUid()).child("wallet").setValue(w).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
    }

    private void initinstance() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    private void initView() {
        recharge = (Button) rootview.findViewById(R.id.recharge);
        sup = (Button) rootview.findViewById(R.id.supprimer);
        msg = (TextView) rootview.findViewById(R.id.msg);
        sup.setOnClickListener(this);
        mRef.child("user").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sommeWallet = dataSnapshot.child("wallet").child("solde").getValue().toString();
                Log.e("connect", "onDataChange2: " + sommeWallet);
                msg.setText(sommeWallet);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
