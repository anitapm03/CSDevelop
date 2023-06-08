package com.example.csdevelop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
//import androidx.paging.PageFetcherSnapshotState;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csdevelop.R;
import com.example.csdevelop.chat.Mensaje;
import com.example.csdevelop.chat.MensajeRecibir;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class MensajesAdapter extends RecyclerView.Adapter<HolderMensaje> {

    //constantes para saber si el mensaje es mio o de otro
    public static final int MENSAJE_OTROS=1;
    public static final int MENSAJE_PROPIO=0;
    Boolean soloMios =false;
    FirebaseUser fUser;

    private List<MensajeRecibir> listMensaje = new ArrayList<>();
    private Context c;

    public MensajesAdapter(Context c) {
        this.c = c;
    }

    public void addMensaje(MensajeRecibir m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == MENSAJE_PROPIO){
            View v =LayoutInflater.from(c).inflate(R.layout.chat_item_user,parent,false);
            return new HolderMensaje(v);
        } else {
            View v = LayoutInflater.from(c).inflate(R.layout.chat_item_other,parent,false);
            return new HolderMensaje(v);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        if(soloMios){
            holder.getEntregado().setVisibility(View.VISIBLE);

            Long codigoHora = listMensaje.get(position).getHora();
            Date d = new Date(codigoHora);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

            holder.getHoraMensaje().setText(sdf.format(d));
            holder.getRecibeMensaje().setText(listMensaje.get(position).getUsuarioEnvia());

        } else {
            holder.getRecibeMensaje().setText(listMensaje.get(position).getUsuarioEnvia());
        }


        // antiguo
        holder.getUsuarioMensaje().setText(listMensaje.get(position).getTextoMensaje());


        if (listMensaje.get(position).getTipo().equals("2")){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getRecibeMensaje().setVisibility(View.VISIBLE);

            String urlFoto = listMensaje.get(position).getUrlFoto();
            Glide.with(holder.itemView.getContext())
                    .load(urlFoto)
                    .into(holder.getFotoMensaje());

        } else if (listMensaje.get(position).getTipo().equals("1")){
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getRecibeMensaje().setVisibility(View.VISIBLE);
        }

        Long codigoHora = listMensaje.get(position).getHora();
        Date d = new Date(codigoHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        holder.getHoraMensaje().setText(sdf.format(d));

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(listMensaje.get(position).getIdUsuario().equals(fUser.getUid())){
            soloMios=true;
            return MENSAJE_PROPIO;
        }else{
            soloMios=false;
            return MENSAJE_OTROS;
        }
    }
}
