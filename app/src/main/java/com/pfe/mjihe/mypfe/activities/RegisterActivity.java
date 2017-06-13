package com.pfe.mjihe.mypfe.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.models.Wallet;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bRegister;

    private EditText nom;
    private EditText prenom, cin;
    private EditText email;
    private EditText password;
    private EditText repassword;
    private Spinner gov, comm, loc;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private ProgressDialog  mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initInstance();
        initListener();
    }

    private void initListener() {
        bRegister.setOnClickListener(this);
    }

    private void initInstance() {
        mAuth= FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Chargment");
        mDialog.setMessage("Attendez SVP !!");
    }

    private void initView() {
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        cin = (EditText) findViewById(R.id.cin);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        email = (EditText) findViewById(R.id.email);
        bRegister = (Button) findViewById(R.id.register);
        gov = (Spinner) findViewById(R.id.gov);
        gov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choixSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        comm = (Spinner) findViewById(R.id.commun);
        comm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choixSpinner1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loc = (Spinner) findViewById(R.id.localite);


    }
    @Override
    public void onClick(View v) {
        if (!isEmpty()){
            mDialog.show();
            createUser(email.getText().toString(), password.getText().toString());
        }
    }
    private boolean isEmpty(){
        boolean empty = false;

        if (TextUtils.isEmpty(nom.getText())){
            nom.setError("Champs Vide");
            empty = true;
        }
        if (TextUtils.isEmpty(prenom.getText())){
            prenom.setError("Champs Vide");
            empty = true;
        }
        if (TextUtils.isEmpty(email.getText())){
            email.setError("Champs Vide");
            empty = true;
        }

        if (TextUtils.isEmpty(password.getText())){
            password.setError("Champs Vide");
            empty = true;
        }

        if (TextUtils.isEmpty(repassword.getText())){
            repassword.setError("Champs Vide");
            empty = true;
        }
        if (TextUtils.isEmpty(cin.getText())) {
            repassword.setError("Champs Vide");
            empty = true;
        }

        return empty;
    }

    private void createUser(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.e("Done", authResult.toString());
                        addUser(authResult.getUser().getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Not DOne", e.toString());
                    }
                });
    }

    private void addUser(String uid) {
        String tUser = "user";
        User u = new User();
        Wallet w = new Wallet();
        u.setEmail(email.getText().toString());
        u.setGouvernorat(gov.getSelectedItem().toString());
        u.setComunn(comm.getSelectedItem().toString());
        u.setLocalite(loc.getSelectedItem().toString());
        u.setNom(nom.getText().toString());
        u.setPrenom(prenom.getText().toString());
        u.setCIN(cin.getText().toString());
        u.setType(tUser);
        u.setWallet(w);
        mRef.child("user").child(uid).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                Toast.makeText(RegisterActivity.this, task.isSuccessful()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void choixSpinner() {
        String choi = gov.getSelectedItem().toString();
        switch (choi) {
            case "Ariana":
                ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.communAriana, android.R.layout.simple_spinner_item);
                staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comm.setAdapter(staticAdapter);
                break;
            case "Tunis":
                ArrayAdapter<CharSequence> staticAdapter1 = ArrayAdapter.createFromResource(this, R.array.communTunis, android.R.layout.simple_spinner_item);
                staticAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comm.setAdapter(staticAdapter1);
                break;
        }
    }

    private void choixSpinner1() {
        String choi1 = comm.getSelectedItem().toString();
        switch (choi1) {
            case "Soukra":
                ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.localiteSokra, android.R.layout.simple_spinner_item);
                staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                loc.setAdapter(staticAdapter);
                break;
            case "Ariana":
                ArrayAdapter<CharSequence> staticAdapter1 = ArrayAdapter.createFromResource(this, R.array.localiteAriana, android.R.layout.simple_spinner_item);
                staticAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                loc.setAdapter(staticAdapter1);
                break;

        }

    }

}
