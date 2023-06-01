package com.example.csdevelop.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csdevelop.R;
import com.example.csdevelop.adapter.GruposAdapter;
import com.example.csdevelop.login.LogIn;
import com.example.csdevelop.perfil.MiPerfil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class PerfilFragment extends Fragment {

    Button logout, btonPerfil, btonTwitter, btonInsta, btonWeb;
    ImageView imgFotoPerfil;
    TextView nombreUsuario;

    RecyclerView rv;

    GruposAdapter adapter;

    FirebaseFirestore firestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_perfil, container, false);



        logout=vista.findViewById(R.id.volverAtras);
        btonPerfil = vista.findViewById(R.id.btonEditar);
        imgFotoPerfil = vista.findViewById(R.id.imgFotoPerfil2);
        nombreUsuario = vista.findViewById(R.id.nombreUsuario);
        btonTwitter = vista.findViewById(R.id.twitterButton);
        btonInsta = vista.findViewById(R.id.instagramButton);
        btonWeb = vista.findViewById(R.id.webButton);

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

        onResume();

        btonTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://twitter.com/ConciertosSolo?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"));
                startActivity(intent);
            }
        });

        btonInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://instagram.com/conciertos.solo?igshid=MzRlODBiNWFlZA=="));
                startActivity(intent);
            }
        });

        btonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://conciertossolo.com/"));
                startActivity(intent);
            }
        });




        return vista;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Llamar al método de actualización aquí
        loadProfilePhoto();
    }

    private void loadProfilePhoto() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("usuarios").document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String fotoPerfilURL = documentSnapshot.getString("foto_perfil");
                            String nomUsuario = documentSnapshot.getString("nombre");

                            nombreUsuario.setText(nomUsuario.toUpperCase());
                            if (fotoPerfilURL != null && !fotoPerfilURL.isEmpty()) {
                                Picasso.get().load(fotoPerfilURL).into(imgFotoPerfil);
                            } else {
                                imgFotoPerfil.setImageResource(R.drawable.foto_perfil);
                            }
                        } else {
                            imgFotoPerfil.setImageResource(R.drawable.foto_perfil);
                        }
                    }
                });
    }

    private void goLogin(){
        Intent is =new Intent(getContext(), LogIn.class);
        //para no ejecutar actividades innecesarias
        is.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(is);
    }
}