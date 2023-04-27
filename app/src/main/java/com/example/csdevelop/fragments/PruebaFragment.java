package com.example.csdevelop.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.R;
import com.example.csdevelop.adapter.ConciertoAdapter;
import com.example.csdevelop.model.Concierto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class PruebaFragment extends Fragment {

    RecyclerView rv;
    ConciertoAdapter adapter;
    FirebaseFirestore firestore;

    public PruebaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //creamos una vista con lo que habia en el return y ya podemos programar
        View vista=inflater.inflate(R.layout.fragment_prueba, container, false);

        firestore=FirebaseFirestore.getInstance();


        rv=vista.findViewById(R.id.rvConciertos);

        //rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setLayoutManager(new GridLayoutManager(getContext(),2));
        Query query = firestore.collection("conciertos");
        FirestoreRecyclerOptions<Concierto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Concierto>().setQuery(query, Concierto.class).build();

        List<Concierto> listaConciertos = new ArrayList<>();


        adapter = new ConciertoAdapter(firestoreRecyclerOptions);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Seleccion",Toast.LENGTH_SHORT).show();
                int posicion =rv.getChildAdapterPosition(v);
                Concierto c=adapter.getItem(posicion);
                lanzarDetalle(c);
            }
        });

        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        // Inflate the layout for this fragment
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

    public void lanzarDetalle(Concierto concierto){
        Intent intent = new Intent(getContext(), DetalleConcierto.class);
        intent.putExtra("concierto", concierto);
        startActivity(intent );
    }
}













