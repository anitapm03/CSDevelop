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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//al crear la primera linea se añaden los metodos del viewHolder y despues se crea el constructor

public class ConciertoAdapter extends FirestoreRecyclerAdapter<Concierto, ConciertoAdapter.ViewHolder> implements View.OnClickListener{//nuevo

    private View.OnClickListener listener1;
    private static final int MAX_LONG=13;


    public ConciertoAdapter(@NonNull FirestoreRecyclerOptions<Concierto> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Concierto concierto) {
        if (concierto.getNombre().length()<MAX_LONG){
            holder.nombre.setText(concierto.getNombre());
        } else {
            String nombreCorto= concierto.getNombre().substring(0, MAX_LONG) + "...";
            holder.nombre.setText(nombreCorto);
        }

        holder.fecha.setText(concierto.getFecha());
        holder.hora.setText(concierto.getHora());

        String img = concierto.getImagen();
        Glide.with(holder.itemView.getContext()).load(img).into(holder.foto);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.concierto_row, parent, false);
       v.setOnClickListener(this);

       return new ViewHolder(v);
    }

    //nuevo
    public void setOnClickListener(View.OnClickListener listener1){
        this.listener1=listener1;

    }
    //nuevo
    @Override
    public void onClick(View v) {
        if (listener1!=null){
            listener1.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, fecha, hora;
        ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreConcierto);
            fecha = itemView.findViewById(R.id.fechaConcierto);
            hora = itemView.findViewById(R.id.horaConcierto);
            foto = itemView.findViewById(R.id.fotoConcierto);


        }

    }
}
