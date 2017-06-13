package com.pfe.mjihe.mypfe.admin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Lot;
import com.pfe.mjihe.mypfe.models.User;

public class LotView extends Activity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String gov, comun;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText lat, lang, taxe, cin;
    private Spinner pay;
    private Button mod, eng;
    private String numlotv;
    private MapView mapLotv;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean permission = false;
    private TextView numLot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lot_view);
        Intent i1 = getIntent();
        Bundle b = i1.getExtras();
        numlotv = (String) b.get("num lot");
        Log.e("Tag", "num lot pass" + numlotv);
        initview();
        numLot.setText(numlotv);
        mapLotv.onCreate(savedInstanceState);
        mapLotv.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        initFirebase();
        getadressAdmin();
    }

    private void initview() {
        numLot = (TextView) findViewById(R.id.mNumLot);
        mapLotv = (MapView) findViewById(R.id.mapView_lot);
        lat = (EditText) findViewById(R.id.ajouterLatLot);
        lang = (EditText) findViewById(R.id.ajouterLangLot);
        taxe = (EditText) findViewById(R.id.taxe);
        cin = (EditText) findViewById(R.id.mCin);
        pay = (Spinner) findViewById(R.id.pay);
        mod = (Button) findViewById(R.id.modlot);
        eng = (Button) findViewById(R.id.enrlot);
        mod.setOnClickListener(this);
        eng.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.modlot) {
            eng.setVisibility(View.VISIBLE);
            lat.setClickable(true);
            lang.setClickable(true);
            taxe.setClickable(true);
            cin.setClickable(true);
        }

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
        mRef.child("Region").child(gov).child(comun).child("Lot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lot mlot = dataSnapshot.child(numlotv).getValue(Lot.class);
                String slat = String.valueOf(mlot.getLatlot());
                Log.e("Tag", "lot  " + slat);
                lat.setText(slat);
                Log.e("Tag", lat.getText().toString());
                String slang = String.valueOf(mlot.getLaglot());
                lang.setText(slang);
                Log.e("Tag", lang.getText().toString());
                String staxe = String.valueOf(mlot.getTaxe());
                taxe.setText(staxe);
                String scin = String.valueOf(mlot.getCin());
                cin.setText(scin);
                boolean bpay = mlot.getPayment();
                if (bpay == true) {
                    pay.setSelection(1);
                } else {
                    pay.setSelection(0);
                }
                LatLng addressI = new LatLng(Double.valueOf(mlot.getLatlot()), Double.valueOf(mlot.getLaglot()));
                Log.e("tag", "adress " + addressI.toString());
                onMapReady(map);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(addressI, 13));
                map.addMarker(new MarkerOptions()
                        .position(addressI)
                        .title(numlotv).snippet(scin));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setCompassEnabled(true);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermission();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e("TAG", "onConnected: " + String.valueOf(mLastLocation.getLatitude()));
        } else {
            Toast.makeText(getApplication(), "No Location Found", Toast.LENGTH_SHORT).show();
            initGps();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Need Location Permission");
            builder.setMessage("This app needs location permission.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission = true;
                mGoogleApiClient.reconnect();
            } else {
                checkPermission();
            }
        }
    }

    void initGps() {
        LocationManager service = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    public void onResume() {
        mapLotv.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapLotv.onLowMemory();
    }

}
