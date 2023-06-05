package com.example.csdevelop.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.csdevelop.R;

public class HolderFav extends RecyclerView.ViewHolder{

    private TextView nombre;
    private ImageView foto;

    public HolderFav(View itemView) {
        super(itemView);

        nombre = itemView.findViewById(R.id.nombreConciertoRow1);
        foto = itemView.findViewById(R.id.imgFotoConciertoGrupo1);

    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public ImageView getFoto() {
        return foto;
    }

    public void setFoto(ImageView foto) {
        this.foto = foto;
    }
}
