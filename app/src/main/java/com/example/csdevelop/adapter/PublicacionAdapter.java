package com.example.csdevelop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csdevelop.R;
import com.example.csdevelop.login.LogIn;
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
    private static final String TYPE_PIC="2";


    private List<Publicacion> listPublicaciones = new ArrayList<>();

    private Context c;
    private String enlaceFoto;
    public PublicacionAdapter (Context c){this.c = c;}

    public void addPublicacion(Publicacion p){
        listPublicaciones.add(p);
        notifyItemInserted(listPublicaciones.size());
    }

    public void setEnlaceFoto(String enlaceFoto) {
        this.enlaceFoto = enlaceFoto;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HolderPublicacion onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.publicacion_row,parent,false);
        return new HolderPublicacion(v);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderPublicacion holder, int position) {
        holder.getNombreUsuario().setText(listPublicaciones.get(position).getNombreUsuario());
        holder.getTextoPublicacion().setText(listPublicaciones.get(position).getTextoPublicacion());

        String userId = listPublicaciones.get(position).getIdUsuarioPublicacion();
        if (userId != null) {
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
        }

        if (listPublicaciones.get(position).getTipoPublicacion().equals(TYPE_PIC)) {
            String enlaceFoto = listPublicaciones.get(position).getUrlFotoPublicacion();
            if (enlaceFoto != null && !enlaceFoto.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(enlaceFoto)
                        .into(holder.getFotoPublicacion());
            }
        }
    }


    @Override
    public int getItemCount() {
        return listPublicaciones.size();
    }

}