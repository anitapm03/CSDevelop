package com.example.csdevelop.adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class ConciertosFavsAdapter extends RecyclerView.Adapter<HolderFav>  implements View.OnClickListener{

    private View.OnClickListener listener;

    private List<Concierto> listConci = new ArrayList<>();
    private Context c;

    public ConciertosFavsAdapter(Context c) {
        this.c = c;
    }

    public void addConcierto (Concierto concierto){
        listConci.add(concierto);
        notifyItemInserted(listConci.size());
    }
    @Override
    public void onBindViewHolder(@NonNull HolderFav holder, int position) {
        //Concierto concierto = conciertos.get(position);

        // Set the data to the views
        holder.getNombre().setText(listConci.get(position).getNombre());

        String img = listConci.get(position).getImagen();
        Glide.with(holder.itemView.getContext()).load(img).into(holder.getFoto());
    }

    @NonNull
    @Override
    public HolderFav onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conciertos_favs_row, parent, false);
        view.setOnClickListener(this);
        return new HolderFav(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;

    }

    public int getItemCount(){
        return listConci.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }



    /*public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, hora;
        ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreConciertoRow1);
            foto = itemView.findViewById(R.id.imgFotoConciertoGrupo1);
        }
    }*/
}
