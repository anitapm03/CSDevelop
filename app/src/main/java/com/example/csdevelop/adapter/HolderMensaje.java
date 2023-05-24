package com.example.csdevelop.adapter;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csdevelop.R;

public class HolderMensaje extends RecyclerView.ViewHolder {

    private TextView enviadoMensaje; //cuerpo del mensaje que enviamos nosotros
    private ImageView mensajeFoto;
    //private TextView horaMensajeEnviado;

    private ImageView entregando;
    private ImageView entregado;


    //antiguo, controla los mensajes otros
    private TextView usuarioMensaje;
    private TextView recibeMensaje;
    private TextView horaMensaje;
    private ImageView fotoMensaje;

    public HolderMensaje(View itemView) {
        super(itemView);
        //mensajes propios
        //enviadoMensaje = (TextView) itemView.findViewById(R.id.enviadoMensaje);
        mensajeFoto = (ImageView) itemView.findViewById(R.id.mensajeFoto);
        //horaMensajeEnviado = (TextView) itemView.findViewById(R.id.horaMensajeEnviado);
        entregando = (ImageView) itemView.findViewById(R.id.entregando);
        entregado = (ImageView) itemView.findViewById(R.id.entregado);

        //los antiguos
        usuarioMensaje = (TextView) itemView.findViewById(R.id.usuarioMensaje);
        recibeMensaje = (TextView) itemView.findViewById(R.id.recibeMensaje);
        fotoMensaje = (ImageView) itemView.findViewById(R.id.mensajeFoto);
        horaMensaje = (TextView) itemView.findViewById(R.id.horaMensaje);
    }

    public TextView getUsuarioMensaje() {
        return usuarioMensaje;
    }

    public void setUsuarioMensaje(TextView usuarioMensaje) {
        this.usuarioMensaje = usuarioMensaje;
    }

    public TextView getRecibeMensaje() {
        return recibeMensaje;
    }

    public void setRecibeMensaje(TextView recibeMensaje) {
        this.recibeMensaje = recibeMensaje;
    }

    public ImageView getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(ImageView fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }

    public TextView getHoraMensaje() {
        return horaMensaje;
    }

    public void setHoraMensaje(TextView horaMensaje) {
        this.horaMensaje = horaMensaje;
    }


    //getters y setters nuevos
    /*public TextView getEnviadoMensaje() {
        return enviadoMensaje;
    }

    public void setEnviadoMensaje(TextView enviadoMensaje) {
        this.enviadoMensaje = enviadoMensaje;
    }*/

    public ImageView getMensajeFoto() {
        return mensajeFoto;
    }

    public void setMensajeFoto(ImageView mensajeFoto) {
        this.mensajeFoto = mensajeFoto;
    }

    /*public TextView getHoraMensajeEnviado() {
        return horaMensajeEnviado;
    }

    public void setHoraMensajeEnviado(TextView horaMensajeEnviado) {
        this.horaMensajeEnviado = horaMensajeEnviado;
    }*/

    public ImageView getEntregando() {
        return entregando;
    }

    public void setEntregando(ImageView entregando) {
        this.entregando = entregando;
    }

    public ImageView getEntregado() {
        return entregado;
    }

    public void setEntregado(ImageView entregado) {
        this.entregado = entregado;
    }
}
