package com.example.csdevelop.fragments;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.example.csdevelop.login.LogIn;
import com.example.csdevelop.model.Concierto;
import com.example.csdevelop.perfil.MiPerfil;
import com.google.firebase.auth.FirebaseAuth;


public class PerfilFragment extends Fragment {

    Button logout, btonPerfil;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_perfil, container, false);



        logout=vista.findViewById(R.id.logout);
        btonPerfil = vista.findViewById(R.id.btonEditar);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                goLogin();
            }
        });

        btonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MiPerfil.class);
                startActivity(intent);

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