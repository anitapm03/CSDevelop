package com.example.csdevelop.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csdevelop.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConciertosFavsAdapter extends RecyclerView.Adapter<ConciertosFavsAdapter.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    private List<String> listConci = new ArrayList<>();
    private Context c;

    public ConciertosFavsAdapter(Context c) {
        this.c = c;
    }

    public void addConcierto(String concierto) {
        listConci.add(concierto);
        notifyItemInserted(listConci.size());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String concierto = listConci.get(position);

        // Set the data to the views
        holder.nombreHolder.setText(concierto);
        getImageUrlFromConcierto(concierto, holder);
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
        return listConci.size();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreHolder = itemView.findViewById(R.id.nombreConciertoRow1);
            fotoHolder = itemView.findViewById(R.id.imgFotoConciertoGrupo1);

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
                        // Handle the error
                    }
                });
    }
}
