package com.example.csdevelop.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.R;
import com.example.csdevelop.adapter.ConciertoAdapter;
import com.example.csdevelop.model.Concierto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class BuscarFragment extends Fragment {

    RecyclerView rv;
    ConciertoAdapter adapter;
    FirebaseFirestore firestore;

    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_buscar, container, false);

        firestore=FirebaseFirestore.getInstance();

        rv=vista.findViewById(R.id.rv);
        SearchView searchView = vista.findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Introduzca artista, grupo, lugar...");

        setUpRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchTerm = newText.toLowerCase();


                Query newQuery;
                if (isNumeric(searchTerm)) {
                    newQuery = firestore.collection("conciertos")
                            .orderBy("fecha")
                            .startAt(searchTerm)
                            .endAt(searchTerm + "\uf8ff");
                } else {
                    if (!searchTerm.isEmpty()) {
                        searchTerm = searchTerm.substring(0, 1).toUpperCase() + searchTerm.substring(1);
                    }

                    newQuery = firestore.collection("conciertos")
                            .orderBy("nombre")
                            .startAt(searchTerm)
                            .endAt(searchTerm + "\uf8ff");
                }

                FirestoreRecyclerOptions<Concierto> newOptions =
                        new FirestoreRecyclerOptions.Builder<Concierto>()
                                .setQuery(newQuery, Concierto.class)
                                .build();

                adapter.updateOptions(newOptions);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return vista;
    }


    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setUpRecyclerView() {
        Query originalQuery = firestore.collection("conciertos")
                .orderBy("nombre")
                .orderBy("fecha")
                .orderBy("hora");

        FirestoreRecyclerOptions<Concierto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Concierto>()
                        .setQuery(originalQuery, Concierto.class)
                        .build();


        adapter = new ConciertoAdapter(firestoreRecyclerOptions);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));

        rv.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = rv.getChildAdapterPosition(v);
                Concierto c = adapter.getItem(posicion);
                lanzarDetalle(c);
            }
        });
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

    public void lanzarDetalle(Concierto concierto) {
        Intent intent = new Intent(getContext(), DetalleConcierto.class);
        intent.putExtra("concierto", concierto);
        startActivity(intent);
    }
}
