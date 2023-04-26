package com.example.csdevelop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.csdevelop.model.Concierto;

public class DetalleConcierto extends AppCompatActivity {
    TextView nombreEvento, fechaEvento, horaEvento;
    Button entradas, volver;
    String enlace, foto;
    ImageView fotoEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_concierto);

        nombreEvento=findViewById(R.id.nombreEvento);
        fechaEvento=findViewById(R.id.fechaEvento);
        horaEvento=findViewById(R.id.horaEvento);
        entradas=findViewById(R.id.enlace);
        volver=findViewById(R.id.volver);
        fotoEvento=findViewById(R.id.fotoEvento);

        //recogemos el evento
        Concierto concierto = (Concierto) getIntent().getSerializableExtra("concierto");

        //colocamos la info
        nombreEvento.setText(concierto.getNombre());
        fechaEvento.setText(concierto.getFecha());
        horaEvento.setText(concierto.getHora());
        enlace= concierto.getEnlace();
        foto= concierto.getImagen();

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
                finish();
            }
        });

    }
}