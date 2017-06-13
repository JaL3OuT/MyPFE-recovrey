package com.pfe.mjihe.mypfe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
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
import com.pfe.mjihe.mypfe.models.Wallet;
public class WalletActivity extends Activity implements View.OnClickListener {
    private String sommeWallet;
    private Button recharge, sup;
    private TextView msg;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initinstance();
        setContentView(R.layout.activity_wallet);
        initView();
    }
    public void onClick(View v) {
        Intent in = new Intent(getApplicationContext(), CodePinWallet.class);
        in.putExtra("codeIntent", 0);
        startActivity(in);
        finish();
    }
    private void initinstance() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }
    private void initView() {
        msg = (TextView) findViewById(R.id.msg);
        mRef.child("user").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getWallet().getExiste().equals("true")) {
                    sommeWallet = user.getWallet().getSolde().toString();
                    msg.setText(sommeWallet);
                } else {
                    Toast.makeText(getApplicationContext(), "HelloWallet", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        recharge = (Button) findViewById(R.id.recharge);
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Recharger_Wallet.class);
                startActivity(i);
            }
        });
        sup = (Button) findViewById(R.id.supprimer);
        sup.setOnClickListener(this);
    }
}
