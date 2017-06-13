package com.pfe.mjihe.mypfe.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.pfe.mjihe.mypfe.models.Factures;
import com.pfe.mjihe.mypfe.models.User;

public class FacturesView extends Activity implements View.OnClickListener {
    private String numFactures;
    private TextView id, dat, somme;
    private Button payerF;
    private LinearLayout payF;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Double sommeWallet;
    private Factures mfactures;
    private String userCin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factures_view);
        Intent i1 = getIntent();
        Bundle b = i1.getExtras();
        numFactures = (String) b.get("num Facture");
        initview();
        initFirebase();
        getDetail();

    }

    private void initview() {
        payerF = (Button) findViewById(R.id.payerFacture);
        payerF.setOnClickListener(this);
        id = (TextView) findViewById(R.id.nlotFV);
        dat = (TextView) findViewById(R.id.datrFV);
        somme = (TextView) findViewById(R.id.sommeFV);
        payF = (LinearLayout) findViewById(R.id.paymentFV);
        id.setText(numFactures);
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void getDetail() {
        mRef.child("user").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userCin = dataSnapshot.child("cin").getValue().toString();
                mRef.child("factures").child(userCin.toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mfactures = dataSnapshot.child(numFactures).getValue(Factures.class);
                        id.setText(mfactures.getnLot());
                        dat.setText(mfactures.getDate());
                        somme.setText(String.valueOf(mfactures.getSomme()));
                        if (mfactures.isPaymentF() == true) {
                            payerF.setVisibility(View.INVISIBLE);
                            payF.setBackgroundColor(Color.GREEN);
                        } else {
                            payerF.setVisibility(View.VISIBLE);
                            payF.setBackgroundColor(Color.RED);
                        }
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

    @Override
    public void onClick(View v) {
        mRef.child("user").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String gov, commun;
                gov = user.getGouvernorat().toString();
                commun = user.getComunn().toString();
                if (user.getWallet().getExiste().equals("true")) {
                    sommeWallet = user.getWallet().getSolde();
                    if (sommeWallet < mfactures.getSomme()) {
                        Toast.makeText(FacturesView.this, "Veuillez recharger votre Wallet ", Toast.LENGTH_SHORT).show();
                    } else {
                        mRef.child("factures").child(userCin.toString()).child(numFactures).child("paymentF").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        mRef.child("Rapport").child(gov).child(commun).child("Lot").child(numFactures).child("payment").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                        Double nSolde = sommeWallet - mfactures.getSomme();
                        mRef.child("user").child(mUser.getUid()).child("wallet").child("solde").setValue(nSolde).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(FacturesView.this, "Factures payer avec succé ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Vous n'avez pas de Wallet voyer créé un ou utuliser DigiCassh", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}