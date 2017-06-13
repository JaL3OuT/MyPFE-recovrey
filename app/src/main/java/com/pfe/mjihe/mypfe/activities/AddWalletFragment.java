package com.pfe.mjihe.mypfe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Wallet;

public class AddWalletFragment extends Activity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText edinar;
    private EditText pin;
    private EditText cin;
    private Button addWallet;
    private EditText pinWallet;

    private boolean cinValid, pinValid, pinWalletValid, edinarValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet_fragment);
        initView();
        initFirebase();
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void initView() {
        edinar = (EditText) findViewById(R.id.edinar);
        pin = (EditText) findViewById(R.id.pin);
        cin = (EditText) findViewById(R.id.cin);
        pinWallet = (EditText) findViewById(R.id.pinWallet);
        addWallet = (Button) findViewById(R.id.add);
        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });


        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO : add checked
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4 || s.length() > 4) {
                    pin.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    pinValid = false;
                    cin.setError("Vérifiez", getResources().getDrawable(R.drawable.ic_cancel));
                } else {
                    pin.setBackgroundColor(getResources().getColor(R.color.validText));
                    pinValid = true;

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edinar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4 || s.length() > 4) {
                    edinar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    edinarValid = false;
                    cin.setError("Vérifiez", getResources().getDrawable(R.drawable.ic_cancel));
                } else {
                    edinar.setBackgroundColor(getResources().getColor(R.color.validText));
                    edinarValid = true;

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 8 || s.length() > 8) {
                    cin.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    cinValid = false;
                    cin.setError("Vérifiez", getResources().getDrawable(R.drawable.ic_cancel));
                } else {
                    cin.setBackgroundColor(getResources().getColor(R.color.validText));
                    cinValid = true;

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pinWallet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 4 || s.length() > 4) {
                    pinWallet.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    pinWalletValid = false;
                    cin.setError("Vérifiez", getResources().getDrawable(R.drawable.ic_cancel));
                } else {
                    pinWallet.setBackgroundColor(getResources().getColor(R.color.validText));
                    pinWalletValid = true;


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    private void add() {


        if (cinValid && pinValid && pinWalletValid && edinarValid) {

            Wallet wallet = new Wallet();
            wallet.setCin(cin.getText().toString());
            wallet.setCodeEdinar(edinar.getText().toString());
            wallet.setPin(pin.getText().toString());
            wallet.setSolde(00.00);
            wallet.setPinWallet(pinWallet.getText().toString());
            wallet.setExiste("true");
            mRef.child("user").child(mUser.getUid()).child("wallet").setValue(wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddWalletFragment.this, "Wallet cree", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(AddWalletFragment.this, WalletActivity.class);
                        startActivityForResult(in, 101);
                        finish();

                    }
                }
            });
        } else {
            Toast.makeText(this, "Vérifier votre saisie", Toast.LENGTH_SHORT).show();
        }

        


    }
}
