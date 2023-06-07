package com.example.csdevelop.publicaciones;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.csdevelop.Internet;
import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.example.csdevelop.chat.ChatActivity;
import com.example.csdevelop.chat.MensajeEnviar;
import com.example.csdevelop.login.LogIn;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CrearPublicacion extends AppCompatActivity {
    private static final String TYPE_TEXT="1";
    private static final String TYPE_PIC="2";

    String enlaceFoto = "";

    EditText txtPubli;
    Button addImg, publicar, volver;
    ImageView fotoPubli;
    Uri uri;
    final int CODIGO_RESPUESTA_GALERIA = 3;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    //para la imagen
    private FirebaseStorage storage;
    private StorageReference storageReference;

    //sacar datos del usuario para indicar quien envia la publi
    FirebaseFirestore firestore;
    CollectionReference coleccionUsuarios;
    FirebaseAuth firebaseAuth;
    String id;
    ImageView fotoSeleccionada;
    String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        if(!Internet.isNetworkAvailable(this)){
            Internet.showNoInternetAlert(this);
        }

        Glide.with(this)
                .setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .applyDefaultRequestOptions(RequestOptions.noTransformation())
                .applyDefaultRequestOptions(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .applyDefaultRequestOptions(RequestOptions.skipMemoryCacheOf(true))
                .applyDefaultRequestOptions(RequestOptions.overrideOf(250, 250));

        txtPubli = findViewById(R.id.txtPubli);
        volver = findViewById(R.id.volverAtras);
        publicar = findViewById(R.id.publicar);
        addImg = findViewById(R.id.addFoto);
        fotoPubli = findViewById(R.id.fotoPubli);
        fotoSeleccionada = findViewById(R.id.imageView);

        //instanciamos lo de la base de datos
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("publicaciones");
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //recogemos los datos del usuario
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        id = mAuth.getCurrentUser().getUid();
        firestore= FirebaseFirestore.getInstance();
        coleccionUsuarios= firestore.collection("usuarios");

        coleccionUsuarios.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        nombreUsuario= document.getString("nombre");

                    }
                } else {
                    Toast.makeText(CrearPublicacion.this, "Error con el Usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //meter una condicion que me diga si tiene imagen o no

                if (enlaceFoto.length() == 0){
                    //añadimos una publicacion nueva de texto porque no hay enlace
                    if (!txtPubli.getText().toString().equals("")){
                        databaseReference.push().setValue(new Publicacion( nombreUsuario, txtPubli.getText().toString(), TYPE_TEXT, id));
                    }

                } else {
                    databaseReference.push().setValue(new Publicacion(nombreUsuario,txtPubli.getText().toString(), uri.toString(),TYPE_PIC, id));
                }

                //cerramos el activity
                Intent intent = new Intent(CrearPublicacion.this, MainActivity.class);

                String fragment = "social";
                intent.putExtra("fragment", fragment );

                startActivity(intent);

                finish();
            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,CODIGO_RESPUESTA_GALERIA);
            }
        });


        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearPublicacion.this, MainActivity.class);

                String fragment = "social";
                intent.putExtra("fragment", fragment );

                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_RESPUESTA_GALERIA) {
            if (data != null && data.getData() != null) {
                uri = data.getData();
                try {
                    final StorageReference refe = storage.getReference().child("imagenes_social")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                    UploadTask uploadTask = refe.putFile(uri);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Obtener la URL de descarga del archivo subido
                            return refe.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                uri = task.getResult();
                                enlaceFoto = uri.toString();
                                Glide.with(CrearPublicacion.this).load(enlaceFoto).into(fotoSeleccionada);

                            } else {
                                // Error al obtener la URL de descarga
                                Log.e("Error", task.getException().toString());
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("Error", "" + e.toString());
                }
            }
        }
    }



    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);

        String fragment = "social";
        intent.putExtra("fragment", fragment );

        startActivity(intent);

        finish();
    }
}