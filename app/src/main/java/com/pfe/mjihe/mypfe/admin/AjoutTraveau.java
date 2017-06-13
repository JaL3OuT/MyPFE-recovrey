package com.pfe.mjihe.mypfe.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.pfe.mjihe.mypfe.models.Traveaux;
import com.pfe.mjihe.mypfe.models.User;

public class AjoutTraveau extends Activity {
    private Button ajouTrave;
    private EditText idT, dateT, dureeT, budgetT, latT, langT;
    private ProgressDialog mDialog;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Traveaux mTraveau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_traveau);
        initFirebase();
        initview();
    }
    private void initview() {
        idT = (EditText) findViewById(R.id.idTraveET);
        dateT = (EditText) findViewById(R.id.dateTraveauET);
        dureeT = (EditText) findViewById(R.id.dureeTraveauEt);
        budgetT = (EditText) findViewById(R.id.budgetTraveauET);
        latT = (EditText) findViewById(R.id.traveaulatEt);
        langT = (EditText) findViewById(R.id.traveauLangET);
        ajouTrave = (Button) findViewById(R.id.ajouterT);
        ajouTrave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {
                    mDialog.show();
                    mTraveau = new Traveaux(idT.getText().toString(), dateT.getText().toString(), Double.valueOf(budgetT.getText().toString()), dureeT.getText().toString(), Double.valueOf(latT.getText().toString()), Double.valueOf(langT.getText().toString()));
                    mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            String gov = user.getGouvernorat();
                            String comun = user.getComunn();
                            mRef.child("Region").child(gov).child(comun).child("traveaux").child(idT.getText().toString()).setValue(mTraveau).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDialog.dismiss();
                                    Toast.makeText(AjoutTraveau.this, "Traveau ajouter ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Chargment");
        mDialog.setMessage("Attendez SVP !!");
    }


    private boolean isEmpty() {
        boolean empty = false;
        if (TextUtils.isEmpty(idT.getText())) {
            idT.setError("champs vide ");
            empty = true;
        }
        if (TextUtils.isEmpty(dateT.getText())) {
            dateT.setError("champs vide ");
            empty = true;
        }
        if (TextUtils.isEmpty(dureeT.getText())) {
            dureeT.setError("champs vide ");
            empty = true;
        }
        if (TextUtils.isEmpty(budgetT.getText())) {
            budgetT.setError("champs vide ");
            empty = true;
        }
        if (TextUtils.isEmpty(latT.getText())) {
            latT.setError("champs vide ");
            empty = true;
        }
        if (TextUtils.isEmpty(langT.getText())) {
            langT.setError("champs vide ");
            empty = true;
        }
        return empty;
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

}
