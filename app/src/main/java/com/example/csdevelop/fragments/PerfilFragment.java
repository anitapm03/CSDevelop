package com.example.csdevelop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.example.csdevelop.login.LogIn;
import com.google.firebase.auth.FirebaseAuth;


public class PerfilFragment extends Fragment {

    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_perfil, container, false);



        logout=vista.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                goLogin();
            }
        });


        // Inflate the layout for this fragment
        return vista;
    }

    private void goLogin(){
        Intent is =new Intent(getContext(), LogIn.class);
        //para no ejecutar actividades innecesarias
        is.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(is);
    }
}