package com.pfe.mjihe.mypfe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.admin.MainAdmin;

public class SplashAppScreen extends Activity {
    private ImageView splash;
    private FirebaseAuth mAuth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_app_screen);

        initInstance();
    }

    private void initInstance() {
        splash = (ImageView) findViewById(R.id.splashimage);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            getUserDetail();
        } else {
            startActivity(new Intent(SplashAppScreen.this, LoginActivity.class));
            finish();
        }
    }
    private void getUserDetail() {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("user").child(mAuth.getCurrentUser().getUid());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("MyTag", "onDataChange: " + dataSnapshot.child("type").getValue().toString());
                if (dataSnapshot.child("type").getValue().toString().equals("user")) {
                    startActivity(new Intent(SplashAppScreen.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashAppScreen.this, MainAdmin.class));
                    finish();
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
