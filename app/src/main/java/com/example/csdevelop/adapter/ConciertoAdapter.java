package com.example.csdevelop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csdevelop.R;
import com.example.csdevelop.model.Concierto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//al crear la primera linea se añaden los metodos del viewHolder y despues se crea el constructor

public class ConciertoAdapter extends FirestoreRecyclerAdapter<Concierto, ConciertoAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    //constructor
    public ConciertoAdapter(@NonNull FirestoreRecyclerOptions<Concierto> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Concierto concierto) {
        //una vez hecho link en el oncreate y despues en el viewHolder hacemos esto
        //para recuperar los datos
        holder.nombre.setText(concierto.getNombre());
        holder.fecha.setText(concierto.getFecha());
        holder.hora.setText(concierto.getHora());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //aqui conectamos el adapter con la vista de un solo registro: concierto row
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.concierto_row, parent, false);
       return new ViewHolder(v);
    }

    //creamos esto para que no se ralle y despues generamos el constructor para que no se ralle
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, fecha, hora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //declaramos los elementos de la vista del registro para manipularlos
            nombre = itemView.findViewById(R.id.nombreConcierto);
            fecha = itemView.findViewById(R.id.fechaConcierto);
            hora = itemView.findViewById(R.id.horaConcierto);
        }
    }
}
