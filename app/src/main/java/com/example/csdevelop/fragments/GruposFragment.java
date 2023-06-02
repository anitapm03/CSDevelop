package com.example.csdevelop.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class GruposFragment extends Fragment {
    RecyclerView rv;
    GruposAdapter adapter;
    FirebaseFirestore firestore;

    public GruposFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //creamos una vista con lo que habia en el return y ya podemos programar
        View vista=inflater.inflate(R.layout.fragment_grupos, container, false);

        if(!Internet.isNetworkAvailable(getContext())){
            Internet.showNoInternetAlert(getContext());
        }

        firestore=FirebaseFirestore.getInstance();

        rv=vista.findViewById(R.id.rvGrupos);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = firestore.collection("conciertos");
        FirestoreRecyclerOptions<Concierto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Concierto>().setQuery(query, Concierto.class).build();

        List<Concierto> listaConciertos = new ArrayList<>();

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

        /*if (fragmentListener!= null){
            fragmentListener.onFragmentFinish();
        }*/

    }

    /*para que termine el activity y no explote
    private FragmentListener fragmentListener;
    public interface FragmentListener{
        void onFragmentFinish();
    }

    public void onAttach(Context context){
        super.onAttach(context);
        try{
            fragmentListener = (FragmentListener) context;

        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " debe implementar Fragment Listener");
        }
    }*/
}