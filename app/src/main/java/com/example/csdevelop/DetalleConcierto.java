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
import com.example.csdevelop.chat.ChatActivity;
import com.example.csdevelop.model.Concierto;
import com.example.csdevelop.perfil.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetalleConcierto extends AppCompatActivity {
    TextView nombreEvento, fechaEvento, horaEvento, nombreSala, direccionSala;
    Button entradas, volver, favorito, unirse;
    String enlace, foto, sala, nombreSalaString, direccionSalaString;
    ImageView fotoEvento;

    FirebaseFirestore firestore;
    CollectionReference coleccionSalas;

    FirebaseAuth mAuth;
    String userId;

    DocumentReference conciertosRef;
    FirebaseFirestore db;
    Concierto concierto;

    int contador=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_concierto);

        if(!Internet.isNetworkAvailable(this)){
            Internet.showNoInternetAlert(this);
        }

        nombreEvento=findViewById(R.id.nombreEvento);
        fechaEvento=findViewById(R.id.fechaEvento);
        horaEvento=findViewById(R.id.horaEvento);
        entradas=findViewById(R.id.enlace);
        volver=findViewById(R.id.volverAtras);
        fotoEvento=findViewById(R.id.fotoEvento);
        favorito= findViewById(R.id.favorito);
        nombreSala=findViewById(R.id.nombreSala);
        direccionSala=findViewById(R.id.direccionSala);
        unirse=findViewById(R.id.unirse);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        conciertosRef = db.collection("usuarios").document(userId);
        //recogemos el evento
        concierto = (Concierto) getIntent().getSerializableExtra("concierto");

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

                String fragment = "inicio";
                intent.putExtra("fragment", fragment );

                startActivity(intent);

                finish();
            }
        });

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conciertosFavoritos();

            }
        });

        unirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetalleConcierto.this, ChatActivity.class);
                i.putExtra("concierto", concierto);
                startActivity(i);

                finish();

                conciertosGrupos();
            }
        });

        // Verificar si el concierto está en favoritos y establecer el ícono adecuado
        conciertosRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> misFavoritos = documentSnapshot.toObject(Usuario.class).getMisFavoritos();
                    if (misFavoritos != null && misFavoritos.contains(concierto.getNombre())) {
                        favorito.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fav, 0, 0, 0);
                        contador++;
                    }
                }
            }
        });

    }

    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);

        String fragment = "inicio";
        intent.putExtra("fragment", fragment );
        
        startActivity(intent);

        finish();
    }

    public void conciertosFavoritos(){
        int[] iconos = {R.drawable.ic_fav, R.drawable.ic_no_fav};

        int indice = contador % iconos.length;

        favorito.setCompoundDrawablesWithIntrinsicBounds(iconos[indice], 0, 0, 0);
        contador++;

        if (indice % 2 == 0) {
            Map<String, Object> actualizaciones = new HashMap<>();
            actualizaciones.put("misFavoritos", FieldValue.arrayUnion(concierto.getNombre()));

            conciertosRef.update(actualizaciones)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(DetalleConcierto.this, "Error al agregar el concierto a favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Map<String, Object> actualizaciones = new HashMap<>();
            actualizaciones.put("misFavoritos", FieldValue.arrayRemove(concierto.getNombre()));

            conciertosRef.update(actualizaciones)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(DetalleConcierto.this, "Error al eliminar el concierto de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void conciertosGrupos(){

            Map<String, Object> actualizaciones = new HashMap<>();
            actualizaciones.put("misGrupos", FieldValue.arrayUnion(concierto.getNombre()));

            conciertosRef.update(actualizaciones)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetalleConcierto.this, "Error al agregar el grupo", Toast.LENGTH_SHORT).show();
                        }
                    });
    }
}