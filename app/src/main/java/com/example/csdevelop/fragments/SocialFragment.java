package com.example.csdevelop.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.Internet;
import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.example.csdevelop.adapter.GruposAdapter;
import com.example.csdevelop.adapter.PublicacionAdapter;
import com.example.csdevelop.chat.MensajeRecibir;
import com.example.csdevelop.model.Concierto;
import com.example.csdevelop.publicaciones.CrearPublicacion;
import com.example.csdevelop.publicaciones.Publicacion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class SocialFragment extends Fragment {

    Button addPub;
    RecyclerView rvPublicaciones;
    PublicacionAdapter adapter;
    FirebaseFirestore firestore;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    ArrayList<Publicacion> publicacionesArr;

    public SocialFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_social, container, false);

        if(!Internet.isNetworkAvailable(getContext())){
            Internet.showNoInternetAlert(getContext());
        }

        addPub = v.findViewById(R.id.addPub);
        rvPublicaciones = v.findViewById(R.id.rvPublicaciones);

        firestore=FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("publicaciones/");

        rvPublicaciones.setHasFixedSize(true);
        publicacionesArr =new ArrayList<>();
        adapter = new PublicacionAdapter(getContext());

        //layout para que aparezcan arriba las publicaciones nuevas
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvPublicaciones.setLayoutManager(layoutManager);

        //rvPublicaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPublicaciones.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Publicacion p = snapshot.getValue(Publicacion.class);
                adapter.addPublicacion(p);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarPublicacion();
            }
        });

        return v;
    }

    private void lanzarPublicacion() {

        Intent intent = new Intent(getContext(), CrearPublicacion.class);
        startActivity(intent );

        if (fragmentListener!= null){
            fragmentListener.onFragmentFinish();
        }
    }

    private void setScrollbar(){
        rvPublicaciones.scrollToPosition(0);
    }

    //para que termine el activity y no explote
    private PruebaFragment.FragmentListener fragmentListener;
    public interface FragmentListener{
        void onFragmentFinish();
    }

    public void onAttach(Context context){
        super.onAttach(context);
        try{
            fragmentListener = (PruebaFragment.FragmentListener) context;

        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " debe implementar Fragment Listener");
        }
    }


}