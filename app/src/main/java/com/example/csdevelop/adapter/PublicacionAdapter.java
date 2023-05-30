package com.example.csdevelop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csdevelop.R;
import com.example.csdevelop.publicaciones.Publicacion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<HolderPublicacion> {
    FirebaseUser fUser;
    private static final String TIPO_TEXTO="1";
    private static final String TIPO_IMAGEN="2";


    private List<Publicacion> listPublicaciones = new ArrayList<>();

    private Context c;

    public PublicacionAdapter (Context c){this.c = c;}

    public void addPublicacion(Publicacion p){
        listPublicaciones.add(p);
        notifyItemInserted(listPublicaciones.size());
    }

    @NonNull
    @Override
    public HolderPublicacion onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.publicacion_row,parent,false);
        return new HolderPublicacion(v);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderPublicacion holder, int position) {
        //rellenar los campos
        holder.getNombreUsuario().setText(listPublicaciones.get(position).getTextoPublicacion());
        holder.getTextoPublicacion().setText(listPublicaciones.get(position).getNombreUsuario());
        //foto perfil
        String userId= listPublicaciones.get(position).getIdUsuarioPublicacion();

        FirebaseFirestore.getInstance().collection("usuarios").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String fotoPerfilURL = documentSnapshot.getString("foto_perfil");

                            if (fotoPerfilURL != null && !fotoPerfilURL.isEmpty()) {

                                Glide.with(c).load(fotoPerfilURL).into(holder.getFotoUsuario());

                            } else {

                                Glide.with(c).load(R.drawable.foto_perfil).into(holder.getFotoUsuario());
                            }
                        } else {
                            Glide.with(c).load(R.drawable.foto_perfil).into(holder.getFotoUsuario());
                        }
                    }
                });


        //si tipo mensaje es == 1 significa que es una publicacion de solo texto
        //si no, es de tipo foto y se realiza lo siguiente
        if (listPublicaciones.get(position).getTipoPublicacion().equals(TIPO_IMAGEN)){
            holder.getFotoPublicacion().setVisibility(View.VISIBLE);

            Glide.with(c).load(listPublicaciones.get(position).getUrlFotoPublicacion()).into(holder.getFotoPublicacion());

        } else if (listPublicaciones.get(position).getTipoPublicacion().equals(TIPO_TEXTO)){
            holder.getFotoPublicacion().setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return listPublicaciones.size();
    }

    private void loadProfilePhoto() {

    }
}
