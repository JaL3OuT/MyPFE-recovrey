package com.pfe.mjihe.mypfe.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.activities.AddWalletFragment;

public class WalletFragment extends Fragment {


    private TextView msg;
    private FloatingActionButton actionButton;
    private View rootView;

    public WalletFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        msg = (TextView) rootView.findViewById(R.id.msg);
        actionButton = (FloatingActionButton) rootView.findViewById(R.id.addWallet);
        initView();
        return rootView;
    }

    private void initView() {
        actionButton.setVisibility(View.VISIBLE);
        msg.setText("vous n'avez pas de wallet sur le boutton pour créé une ");
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AddWalletFragment.class);
                startActivityForResult(in, 101);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {

        }
    }
}

