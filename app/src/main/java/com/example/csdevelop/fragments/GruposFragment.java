package com.example.csdevelop.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.Internet;
import com.example.csdevelop.R;
import com.example.csdevelop.adapter.ConciertoAdapter;
import com.example.csdevelop.adapter.GruposAdapter;
import com.example.csdevelop.chat.ChatActivity;
import com.example.csdevelop.model.Concierto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class GruposFragment extends Fragment {
    RecyclerView rv;
    GruposAdapter adapter;
    FirebaseFirestore firestore;

    CollectionReference coleccionUsuarios;

    public GruposFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista=inflater.inflate(R.layout.fragment_grupos, container, false);

        if(!Internet.isNetworkAvailable(getContext())){
            Internet.showNoInternetAlert(getContext());
        }

        firestore=FirebaseFirestore.getInstance();

        rv=vista.findViewById(R.id.rvGrupos);

        //recogemos los datos del usuario
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = mAuth.getCurrentUser().getUid();//id del usuario


        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = firestore.collection("conciertos");
        FirestoreRecyclerOptions<Concierto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Concierto>().setQuery(query, Concierto.class).build();
        adapter = new GruposAdapter(firestoreRecyclerOptions);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int posicion =rv.getChildAdapterPosition(v);
                Concierto c=adapter.getItem(posicion);
                lanzarChat(c);
            }
        });

        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);


        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void lanzarChat(Concierto concierto){
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("concierto", concierto);
        startActivity(intent );
    }
}