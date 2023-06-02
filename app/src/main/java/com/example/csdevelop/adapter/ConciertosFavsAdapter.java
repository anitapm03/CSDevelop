package com.example.csdevelop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csdevelop.R;
import com.example.csdevelop.model.Concierto;

import java.util.List;

public class ConciertosFavsAdapter extends RecyclerView.Adapter<ConciertosFavsAdapter.ConciertoViewHolder> {

    private View.OnClickListener listener;

    private List<Concierto> conciertos;

    public ConciertosFavsAdapter(List<Concierto> conciertos) {
        this.conciertos = conciertos;
    }

    @Override
    public void onBindViewHolder(@NonNull ConciertoViewHolder holder, int position) {
        Concierto concierto = conciertos.get(position);

        // Set the data to the views
        holder.nombre.setText(concierto.getNombre());

        String img = concierto.getImagen();
        Glide.with(holder.itemView.getContext()).load(img).into(holder.foto);
    }

    @NonNull
    @Override
    public ConciertoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conciertos_favs_row, parent, false);
        return new ConciertoViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return conciertos.size();
    }

    public class ConciertoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, hora;
        ImageView foto;

        public ConciertoViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreConcierto);
        }
    }
}
