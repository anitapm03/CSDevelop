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

public class GruposAdapter extends FirestoreRecyclerAdapter<Concierto, GruposAdapter.ViewHolder> implements View.OnClickListener{


    private View.OnClickListener listener;

    public GruposAdapter(@NonNull FirestoreRecyclerOptions<Concierto> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull GruposAdapter.ViewHolder holder, int position, @NonNull Concierto concierto) {
        //una vez hecho link en el oncreate y despues en el viewHolder hacemos esto
        //para recuperar los datos
        holder.nombre.setText(concierto.getNombre());

        String img = concierto.getImagen();
        Glide.with(holder.itemView.getContext()).load(img).into(holder.foto);

    }

    @NonNull
    @Override
    public GruposAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //aqui conectamos el adapter con la vista de un solo registro: chats row
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_row, parent, false);
        v.setOnClickListener(this);

        return new GruposAdapter.ViewHolder(v);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;

    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    //creamos esto para que no se ralle y despues generamos el constructor para que no se ralle
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //declaramos los elementos de la vista del registro para manipularlos
            nombre = itemView.findViewById(R.id.nombreConciertoRow);

            foto = itemView.findViewById(R.id.imgFotoConciertoGrupo);


        }

    }

}
/*


 */