package com.example.csdevelop.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.csdevelop.R;

public class HolderPublicacion extends RecyclerView.ViewHolder{

    TextView textoPublicacion, nombreUsuario;
    ImageView fotoUsuario, fotoPublicacion;
    public HolderPublicacion(View itemView) {
        super(itemView);

        textoPublicacion = (TextView) itemView.findViewById(R.id.textoPublicacion);
        nombreUsuario = (TextView) itemView.findViewById(R.id.nombreConciertoRow);
        fotoUsuario = (ImageView) itemView.findViewById(R.id.imgFotoConciertoGrupo);
        fotoPublicacion = (ImageView) itemView.findViewById(R.id.imagenPublicacion);
    }

    public TextView getTextoPublicacion() {
        return textoPublicacion;
    }

    public void setTextoPublicacion(TextView textoPublicacion) {
        this.textoPublicacion = textoPublicacion;
    }

    public TextView getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(TextView nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public ImageView getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(ImageView fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public ImageView getFotoPublicacion() {
        return fotoPublicacion;
    }

    public void setFotoPublicacion(ImageView fotoPublicacion) {
        this.fotoPublicacion = fotoPublicacion;
    }
}
