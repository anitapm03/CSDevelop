package com.example.csdevelop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.csdevelop.model.Concierto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.intellij.lang.annotations.Identifier;

public class DetalleConcierto extends AppCompatActivity {
    TextView nombreEvento, fechaEvento, horaEvento, nombreSala, direccionSala;
    Button entradas, volver, favorito;
    String enlace, foto, sala, nombreSalaString, direccionSalaString;
    ImageView fotoEvento;

    FirebaseFirestore firestore;
    CollectionReference coleccionSalas;

    int contador=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_concierto);

        nombreEvento=findViewById(R.id.nombreEvento);
        fechaEvento=findViewById(R.id.fechaEvento);
        horaEvento=findViewById(R.id.horaEvento);
        entradas=findViewById(R.id.enlace);
        volver=findViewById(R.id.logout);
        fotoEvento=findViewById(R.id.fotoEvento);
        favorito= findViewById(R.id.favorito);
        nombreSala=findViewById(R.id.nombreSala);
        direccionSala=findViewById(R.id.direccionSala);

        //recogemos el evento
        Concierto concierto = (Concierto) getIntent().getSerializableExtra("concierto");

        //colocamos la info
        nombreEvento.setText(concierto.getNombre());
        fechaEvento.setText(concierto.getFecha());
        horaEvento.setText(concierto.getHora());
        enlace= concierto.getEnlace();
        foto= concierto.getImagen();
        sala= concierto.getIdSala();
        //seleccionamos la sala para sacar nombre y direccion
        firestore= FirebaseFirestore.getInstance();
        coleccionSalas= firestore.collection("salas");
        coleccionSalas.document(sala).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        nombreSalaString= document.getString("nombreSala");
                        direccionSalaString= document.getString("direccion");
                        nombreSala.setText(nombreSalaString);
                        direccionSala.setText(direccionSalaString);
                    }
                } else {
                    Toast.makeText(DetalleConcierto.this, "Error con la sala", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //insertamos la imagen
        Glide.with(this)
                .load(foto)
                .into(fotoEvento);

        //boton que redirige a entradas
        entradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(enlace));
                getIntent().setPackage("com.android.chrome");
                startActivity(i);
            }
        });

        //volver al inicio
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleConcierto.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] iconos= {R.drawable.ic_fav,R.drawable.ic_no_fav};

                int indice = contador % iconos.length;

                favorito.setCompoundDrawablesWithIntrinsicBounds(iconos[indice],0,0,0);
                contador++;

                if (indice%2==0){
                    Toast.makeText(DetalleConcierto.this, "favorito", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DetalleConcierto.this, "no favorito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }


}