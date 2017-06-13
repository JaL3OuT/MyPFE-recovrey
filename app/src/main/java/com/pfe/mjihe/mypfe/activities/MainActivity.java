package com.pfe.mjihe.mypfe.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.fragments.Traveaux_client_Fragment;
import com.pfe.mjihe.mypfe.fragments.AccueilFragment;
import com.pfe.mjihe.mypfe.fragments.FacturesFragment;
import com.pfe.mjihe.mypfe.fragments.RepportFragment;
import com.pfe.mjihe.mypfe.fragments.WalletFragment;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.utils.MyService;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static FragmentManager fm;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private String testexist;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("attendez svp !!!!");
        dialog.setMessage("attendez svp !!!!");
        dialog.show();
        initInstance();
        Intent in = new Intent(MainActivity.this, MyService.class);
        startService(in);
        getWalletTest();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        final View v = navigationView.getHeaderView(0);
        mRef.child("user").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("TAG", "onDataChange: " + dataSnapshot.getChildrenCount());
                ((TextView) v.findViewById(R.id.headernom)).setText(dataSnapshot.child("nom").getValue().toString());
                ((TextView) v.findViewById(R.id.headermail)).setText(dataSnapshot.child("email").getValue().toString());
                dialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        fm.beginTransaction().replace(R.id.container, new AccueilFragment()).commit();
    }
    private void initInstance() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        fm = getFragmentManager();
        mUser = mAuth.getCurrentUser();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout) {
            mAuth.signOut();
            Intent in = new Intent(MainActivity.this, LoginActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.accueil) {
            fm.beginTransaction().replace(R.id.container, new AccueilFragment()).commit();
        }

        if (id == R.id.wallet) {
            testexist = pref.getString("IS_EXIST", "No");
            if (testexist.equals("true")) {
                Toast.makeText(getApplicationContext(), testexist, Toast.LENGTH_LONG).show();
                if (testexist.equals("true")) {
                    Intent in = new Intent(getApplicationContext(), CodePinWallet.class);
                    in.putExtra("codeIntent", 1);
                    startActivity(in);}
            } else {
                fm.beginTransaction().replace(R.id.container, new WalletFragment()).commit();
            }
        }
        if (id == R.id.rapportadd) {
            fm.beginTransaction().replace(R.id.container, new RepportFragment()).commit();
        }
        if (id == R.id.factures) {
            fm.beginTransaction().replace(R.id.container, new FacturesFragment()).commit();
        }
        if (id == R.id.nav_Traveau)
        {
            fm.beginTransaction().replace(R.id.container , new Traveaux_client_Fragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getWalletTest() {
        mRef.child("user").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                Log.d("HelloUser", "onDataChange: " + user.getWallet().getExiste());
                pref = getSharedPreferences("PREF", MODE_PRIVATE);
                edit = pref.edit();
                edit.putString("IS_EXIST", user.getWallet().getExiste());
                edit.apply();
                //Log.d("HelloUser", "onDataChange: " + user.getWallet().getExiste());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
