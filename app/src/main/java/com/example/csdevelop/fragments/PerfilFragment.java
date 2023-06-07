package com.example.csdevelop.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.Internet;
import com.example.csdevelop.R;
import com.example.csdevelop.adapter.ConciertosFavsAdapter;
import com.example.csdevelop.login.LogIn;
import com.example.csdevelop.model.Concierto;
import com.example.csdevelop.perfil.MiPerfil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PerfilFragment extends Fragment {

    Button logout, btonPerfil, btonTwitter, btonInsta, btonWeb;
    ImageView imgFotoPerfil;
    TextView nombreUsuario;
    private ProgressBar progressBar;
    ArrayList<Concierto> favsList;

    List<String> nombresFavs = new ArrayList<>();
    RecyclerView rv;

    ConciertosFavsAdapter adapter;

    FirebaseFirestore firestore;

    List<Object> arrayValues;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);

        if (!Internet.isNetworkAvailable(getContext())) {
            Internet.showNoInternetAlert(getContext());
        }

        logout = vista.findViewById(R.id.volverAtras);
        btonPerfil = vista.findViewById(R.id.btonEditar);
        imgFotoPerfil = vista.findViewById(R.id.imgFotoPerfil2);
        nombreUsuario = vista.findViewById(R.id.nombreUsuario);
        btonTwitter = vista.findViewById(R.id.twitterButton);
        btonInsta = vista.findViewById(R.id.instagramButton);
        btonWeb = vista.findViewById(R.id.webButton);
        progressBar = vista.findViewById(R.id.progressBar);
        rv = vista.findViewById(R.id.conciertosRecyclerView);

        // Recogemos los datos del usuario
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = mAuth.getCurrentUser().getUid(); // ID del usuario

        firestore = FirebaseFirestore.getInstance();

        rv.setHasFixedSize(true);
        favsList = new ArrayList<>();
        adapter = new ConciertosFavsAdapter(getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        CollectionReference collectionRef = firestore.collection("usuarios");
        Query q = collectionRef.whereEqualTo(FieldPath.documentId(), id);
        List<Concierto> listaConciertos = new ArrayList<>();
        List<String> listaConciertos1 = new ArrayList<>();

        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Object arrayField = document.get("misFavoritos");
                        System.out.println(arrayField);
                        if (arrayField instanceof List) {
                            arrayValues = (List<Object>) arrayField;
                            for (Object valor : arrayValues) {
                                String nombre = valor.toString();
                                listaConciertos1.add(nombre);
                            }

                            adapter.setConciertos(listaConciertos1);
                            adapter.notifyDataSetChanged();
                            rv.setAdapter(adapter);
                        }
                    }
                }
            }
        });



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

    public void lanzarDetalle(Concierto concierto) {
        Intent intent = new Intent(getContext(), DetalleConcierto.class);
        intent.putExtra("concierto", concierto);
        startActivity(intent);

        if (fragmentListener != null) {
            fragmentListener.onFragmentFinish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Llamar al método de actualización aquí
        loadProfilePhoto();
    }

    private void loadProfilePhoto() {
        progressBar.setVisibility(View.VISIBLE);
        imgFotoPerfil.setVisibility(View.GONE);

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
                                progressBar.setVisibility(View.GONE);
                                imgFotoPerfil.setVisibility(View.VISIBLE);
                                Picasso.get().load(fotoPerfilURL).into(imgFotoPerfil);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                imgFotoPerfil.setVisibility(View.VISIBLE);
                                imgFotoPerfil.setImageResource(R.drawable.foto_perfil);
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            imgFotoPerfil.setVisibility(View.VISIBLE);
                            imgFotoPerfil.setImageResource(R.drawable.foto_perfil);
                        }
                    }
                });
    }

    private void goLogin() {
        Intent is = new Intent(getContext(), LogIn.class);
        //para no ejecutar actividades innecesarias
        is.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(is);
    }

    //para que termine el activity y no explote
    private PruebaFragment.FragmentListener fragmentListener;

    public interface FragmentListener {
        void onFragmentFinish();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentListener = (PruebaFragment.FragmentListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar Fragment Listener");
        }
    }
}