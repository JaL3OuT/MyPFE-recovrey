package com.pfe.mjihe.mypfe.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.pfe.mjihe.mypfe.models.Municipalite;
import com.pfe.mjihe.mypfe.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccueilFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private TextView nomm, infom;
    private ImageView logom;
    private MapView mapview;
    private View rootview;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean permission = false;
    private String slocal;
    private String gov, comu, snom, sinfo, slogo;
    private Double mlat;
    private Double mlang;
    private LatLng local;//= new LatLng(0,0);
    private Municipalite mmunici;

    public AccueilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_accueil, container, false);
        initfirebase();
        mapview = (MapView) rootview.findViewById(R.id.mapView1);
        mapview.onCreate(savedInstanceState);
        mapview.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        initview();
        getadressuseer();
        return rootview;
    }
    public void onResume() {
        mapview.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapview.onLowMemory();
    }

    private void initview() {
        nomm = (TextView) rootview.findViewById(R.id.nomM);
        infom = (TextView) rootview.findViewById(R.id.infomun);
        logom = (ImageView) rootview.findViewById(R.id.imageView2);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Need Location Permission");
            builder.setMessage("This app needs location permission.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermission();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e("TAG", "onConnected: " + String.valueOf(mLastLocation.getLatitude()));
        } else {
            Toast.makeText(getActivity(), "No Location Found", Toast.LENGTH_SHORT).show();
            initGps();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    void initGps() {
        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void initfirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mUser = mAuth.getCurrentUser();
    }

    private void getadressuseer() {
        initfirebase();
        mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                gov = user.getGouvernorat();
                comu = user.getLocalite();
                getdata();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getdata() {
        mRef.child("Region").child(gov).child(comu).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mmunici = dataSnapshot.getValue(Municipalite.class);
                Log.d("adress", "user1: " + mmunici.toString());
                snom = mmunici.getNom();
                slocal = snom;
                String ilat, ilang;
                Log.d("adress", "user1: " + String.valueOf(mmunici.getLat()));
                mlat = Double.parseDouble(String.valueOf(mmunici.getLat()));
                mlang = Double.parseDouble(String.valueOf(mmunici.getLang()));
                Log.d("adress", "user1: " + mlat);
                local = new LatLng(mlat, mlang);
                nomm.setText(snom);
                sinfo = mmunici.getInfo();
                infom.setText(sinfo);
                slogo = mmunici.getLogo();
                Glide.with(getActivity()).load(slogo).into(logom);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 13));
                map.addMarker(new MarkerOptions()
                        .position(local)
                        .title(slocal).snippet("Municipalité de " + slocal));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
