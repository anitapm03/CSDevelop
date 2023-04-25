package com.example.csdevelop.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.csdevelop.R;
import com.example.csdevelop.adapter.ConciertoAdapter;
import com.example.csdevelop.model.Concierto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = firestore.collection("conciertos");
        FirestoreRecyclerOptions<Concierto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Concierto>().setQuery(query, Concierto.class).build();

        adapter = new ConciertoAdapter(firestoreRecyclerOptions);
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
}













