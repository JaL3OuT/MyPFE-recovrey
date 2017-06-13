package com.pfe.mjihe.mypfe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.pfe.mjihe.mypfe.models.User;

public class Recharger_Wallet extends Activity implements View.OnClickListener {
    private EditText aRecharger;
    private TextView solde;
    private Button valider;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String sommeWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharger__wallet);
        initinstance();
        initview();
    }

    private void initview() {
        valider = (Button) findViewById(R.id.rvalidRecharge);
        aRecharger = (EditText) findViewById(R.id.sommeET);
        solde = (TextView) findViewById(R.id.soldeActuel);
        valider.setOnClickListener(this);
        mRef.child("user").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getWallet().getExiste().equals("true")) {
                    sommeWallet = user.getWallet().getSolde().toString();
                    solde.setText(sommeWallet.toString());

                } else {
                    Toast.makeText(getApplicationContext(), "HelloWallet", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initinstance() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    @Override
    public void onClick(View v) {
        if (!isEmpty()) {
            double soldwallet = Double.parseDouble(sommeWallet);
            double charge = Double.parseDouble(String.valueOf(aRecharger.getText()));
            double nsolde = soldwallet + charge;
            mRef.child("user").child(mUser.getUid()).child("wallet").child("solde").setValue(nsolde).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Recharger_Wallet.this, "Recharger avec succé avec succé ", Toast.LENGTH_SHORT).show();
                    initview();
                }
            });
        }
    }

    private boolean isEmpty() {
        boolean empty = false;
        if (TextUtils.isEmpty(aRecharger.getText())) {
            aRecharger.setError("champs vide");
            empty = true;
        }
        return empty;
    }
}
