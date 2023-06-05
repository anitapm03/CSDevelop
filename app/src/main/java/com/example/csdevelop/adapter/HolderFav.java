package com.example.csdevelop.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.csdevelop.R;

public class HolderFav extends RecyclerView.ViewHolder{

    private TextView nombreHolder;
    private ImageView fotoHolder;

    public HolderFav(View itemView) {
        super(itemView);

        nombreHolder = itemView.findViewById(R.id.nombreConciertoRow1);
        fotoHolder = itemView.findViewById(R.id.imgFotoConciertoGrupo1);

    }

    public TextView getNombreHolder() {
        return nombreHolder;
    }

    public void setNombreHolder(TextView nombreHolder) {
        this.nombreHolder = nombreHolder;
    }

    public ImageView getFotoHolder() {
        return fotoHolder;
    }

    public void setFotoHolder(ImageView fotoHolder) {
        this.fotoHolder = fotoHolder;
    }
}
