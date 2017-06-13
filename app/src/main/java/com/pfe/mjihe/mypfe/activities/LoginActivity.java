package com.pfe.mjihe.mypfe.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.admin.MainAdmin;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    String etat;
    private FirebaseAuth mAuth;
    private Button bLogin;
    private Button bRegister;
    private TextView tEmail;
    private TextView tPassword;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        testInternet();
        initInstance();
        initView();
        initListener();
    }
    private void initListener() {
        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }
    private void initView() {
        tEmail = (TextView) findViewById(R.id.email);
        tPassword = (TextView) findViewById(R.id.password);
        bLogin = (Button) findViewById(R.id.login);
        bRegister = (Button) findViewById(R.id.register);
    }

    private void initInstance() {
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            getUserDetail();
        }

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Chargment");
        mDialog.setMessage("Attendez SVP !!");
    }
    private boolean isEmpty() {
        boolean mempty = false;
        if (TextUtils.isEmpty(tEmail.getText())) {
            tEmail.setError("Champs Vide");
            mempty = true;
        }
        if (TextUtils.isEmpty(tPassword.getText())) {
            tPassword.setError("Champs Vide");
            mempty = true;
        }
        return mempty;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.register) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } else {
            if (!isEmpty()) {
                mDialog.show();
                mAuth.signInWithEmailAndPassword(tEmail.getText().toString(), tPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Loged IN", Toast.LENGTH_SHORT).show();
                            getUserDetail();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email ou password erronée", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void getUserDetail() {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("user").child(mAuth.getCurrentUser().getUid());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                Log.e("MyTag", "onDataChange: " + dataSnapshot.child("type").getValue().toString());
                if (dataSnapshot.child("type").getValue().toString().equals("user")) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, MainAdmin.class));
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void testInternet() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        etat = activeNetwork.toString();
        Log.e("MyTag", "connected: " + etat);
        Toast.makeText(this, etat, Toast.LENGTH_SHORT).show();
    }
}