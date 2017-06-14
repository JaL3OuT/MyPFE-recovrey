package com.pfe.mjihe.mypfe.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Rapport;
import com.pfe.mjihe.mypfe.models.User;

public class RapportView extends Activity implements View.OnClickListener {
   private TextView dateV,rapportV,latVR,langVR;
   private ImageView imagerapport ;
   private Button vmmapVR;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String gov, local, comun,rappass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport_view);
        initView();
        initFirebase();
        getadressAdmin();
    }
    private void initView (){
        dateV= (TextView) findViewById(R.id.dateRapportView);
        rapportV = (TextView) findViewById(R.id.rapportViewRapport);
        latVR = (TextView) findViewById(R.id.latViewRapport);
        langVR = (TextView) findViewById(R.id.langViewRapport);
        imagerapport = (ImageView) findViewById(R.id.rapportViewimg);
        vmmapVR = (Button) findViewById(R.id.mapViewRepport);
        vmmapVR.setVisibility(View.INVISIBLE);
        vmmapVR.setOnClickListener(this);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        rappass = b.getString("rapport_index");
    }
    @Override
    public void onClick(View v) {
    }
    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
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
        mRef.child("Rapport").child(gov).child(comun).child(local).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Rapport mRapport = dataSnapshot.child(rappass).getValue(Rapport.class);
                dateV.setText(mRapport.getDate().toString());
                String sImage = mRapport.getUrl();
                Glide.with(getApplicationContext()).load(sImage).into(imagerapport);
                rapportV.setText(mRapport.getRapport().toString());
                if((null != String.valueOf(mRapport.getLati())) &&null!=String.valueOf(mRapport.getLangi()) )
                {
                    langVR.setText(String.valueOf(mRapport.getLangi()));
                    latVR.setText(String.valueOf(mRapport.getLati()));

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
