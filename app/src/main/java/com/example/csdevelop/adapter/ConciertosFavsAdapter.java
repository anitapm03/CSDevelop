package com.example.csdevelop.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csdevelop.R;
import com.example.csdevelop.model.Concierto;
import com.example.csdevelop.perfil.Usuario;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConciertosFavsAdapter extends RecyclerView.Adapter<ConciertosFavsAdapter.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    private List<String> listConci = new ArrayList<>();
    private Context c;
    private static final int MAX_LONG=23;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ConciertosFavsAdapter(Context c) {
        this.c = c;
    }


    public void addConcierto(String concierto) {
        listConci.add(concierto);
        notifyItemInserted(listConci.size());
    }

    public void setFavoritos(List<String> favoritos) {
        this.listConci = favoritos;
        notifyDataSetChanged();
    }

    public void removeConcierto(int position) {
        listConci.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (listConci.isEmpty()) {
            holder.nombreHolder.setText(c.getString(R.string.noConciertosFavs));
            holder.fotoHolder.setImageResource(View.GONE);
            holder.btnBorrar.setVisibility(View.GONE);
        } else {
            String nombreConcierto = listConci.get(position);
            if (nombreConcierto.length() < MAX_LONG) {
                holder.nombreHolder.setText(nombreConcierto);
            } else {
                String nombreCorto = nombreConcierto.substring(0, MAX_LONG) + "...";
                holder.nombreHolder.setText(nombreCorto);
            }
            getImageUrlFromConcierto(nombreConcierto, holder);
            holder.btnBorrar.setVisibility(View.VISIBLE);
            final int pos = position;
            holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeConcierto(pos);
                    removeFromFavorites(nombreConcierto);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conciertos_favs_row, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }


    public int getItemCount() {
        if (listConci.isEmpty()) {
            return 1;
        } else {
            return listConci.size();
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setConciertos(List<String> conciertos) {
        listConci = conciertos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreHolder;
        private ImageView fotoHolder;
        private Button btnBorrar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreHolder = itemView.findViewById(R.id.nombreConciertoRow1);
            fotoHolder = itemView.findViewById(R.id.imgFotoConciertoGrupo1);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);

        }
    }

    private void getImageUrlFromConcierto(String concierto, final ViewHolder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference conciertosRef = db.collection("conciertos");
        conciertosRef
                .whereEqualTo("nombre", concierto)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            String imageUrl = querySnapshot.getDocuments().get(0).getString("imagen");
                            if (imageUrl != null) {
                                Glide.with(c).load(imageUrl).into(holder.fotoHolder);
                            }
                        }
                    } else {
                    }
                });
    }

    private void removeFromFavorites(String concierto) {
        CollectionReference usuariosRef = db.collection("usuarios");

        usuariosRef.whereArrayContains("misFavoritos", concierto)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> usuarios = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot usuario : usuarios) {
                        String usuarioId = usuario.getId();

                        List<String> misFavoritos = usuario.toObject(Usuario.class).getMisFavoritos();

                        misFavoritos.remove(concierto);

                        usuariosRef.document(usuarioId)
                                .update("misFavoritos", misFavoritos)
                                .addOnSuccessListener(aVoid -> {
                                    setFavoritos(misFavoritos);
                                })
                                .addOnFailureListener(e -> {
                                });
                    }
                })
                .addOnFailureListener(e -> {
                });
    }

}