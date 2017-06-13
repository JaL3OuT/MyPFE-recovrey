package com.pfe.mjihe.mypfe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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

public class CodePinWallet extends Activity {

    final int PIN_LENGTH = 4;
    int leng;
    private SharedPreferences pref1;
    private SharedPreferences.Editor edit1;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextView pinbox1, pinbox2, pinbox3, pinbox0;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnDel, btnOk;
    private TextView[] pinboxarray;
    private String userEntered;
    View.OnClickListener pinButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Button pressedButton = (Button) v;


            if (leng < PIN_LENGTH) {
                activeButton();
                userEntered = userEntered + pressedButton.getText();
                Log.v("PinView", "User entered=" + userEntered);
                //Update pin boxes
                pinboxarray[userEntered.length() - 1].setText("*");
                if (userEntered.length() == PIN_LENGTH) {
                    //Check if entered PIN is correct
                    deactiveButton();
                    btnOk.setEnabled(true);

                }
            }
        }
    };
    private String pin;
    private int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_pin_wallet);
        Intent intent = getIntent();
        Bundle bn = intent.getExtras();
        code = bn.getInt("codeIntent");
        getWalletPin();
        initView();
    }
    private void initView() {
        userEntered = "";
        leng = userEntered.length();
        Log.v("Pinlength", String.valueOf(leng));
        pinbox0 = (TextView) findViewById(R.id.box0);
        pinbox1 = (TextView) findViewById(R.id.box1);
        pinbox2 = (TextView) findViewById(R.id.box2);
        pinbox3 = (TextView) findViewById(R.id.box3);
        pinboxarray = new TextView[PIN_LENGTH];
        pinboxarray[0] = pinbox0;
        pinboxarray[1] = pinbox1;
        pinboxarray[2] = pinbox2;
        pinboxarray[3] = pinbox3;
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btn0 = (Button) findViewById(R.id.btn0);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnOk.setEnabled(false);
        btn1.setOnClickListener(pinButtonHandler);
        btn2.setOnClickListener(pinButtonHandler);
        btn3.setOnClickListener(pinButtonHandler);
        btn4.setOnClickListener(pinButtonHandler);
        btn5.setOnClickListener(pinButtonHandler);
        btn6.setOnClickListener(pinButtonHandler);
        btn7.setOnClickListener(pinButtonHandler);
        btn8.setOnClickListener(pinButtonHandler);
        btn9.setOnClickListener(pinButtonHandler);
        btn0.setOnClickListener(pinButtonHandler);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = pref1.getString("PINW", null);

                if (pin.equals(userEntered)) {
                    Log.v("Pin", pin);
                    if (code == 1){
                        Intent in = new Intent(getApplicationContext(), WalletActivity.class);
                        startActivity(in);
                        finish();
                    }else if (code == 0){
                        User u = new User();
                        Wallet w = new Wallet();
                        u.setWallet(w);
                        mRef.child("user").child(mUser.getUid()).child("wallet").setValue(w).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(in);
                                finish();
                            }
                        });

                    }

                } else {
                    Toast.makeText(CodePinWallet.this, "Code PIN incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          activeButton();
                                          if (userEntered.length() > 0) {
                                              activeButton();
                                              btnDel.setEnabled(true);
                                              userEntered = userEntered.substring(0, userEntered.length() - 1);
                                              pinboxarray[userEntered.length()].setText("_");
                                          }
                                      }
                                  }
        );

    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void getWalletPin() {
        initFirebase();
        mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("HelloUser", "onDataChange: " + user.getWallet().getExiste());
                pref1 = getSharedPreferences("PREF", MODE_PRIVATE);
                edit1 = pref1.edit();
                edit1.putString("PINW", user.getWallet().getPinWallet());
                edit1.apply();
                Log.d("HelloUser", "onDataChange: " + user.getWallet().getPinWallet());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void activeButton() {
        btn0.setEnabled(true);
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);
        btn4.setEnabled(true);
        btn5.setEnabled(true);
        btn6.setEnabled(true);
        btn7.setEnabled(true);
        btn8.setEnabled(true);
        btn9.setEnabled(true);
    }

    private void deactiveButton() {
        btn0.setEnabled(false);
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        btn5.setEnabled(false);
        btn6.setEnabled(false);
        btn7.setEnabled(false);
        btn8.setEnabled(false);
        btn9.setEnabled(false);
    }
}
