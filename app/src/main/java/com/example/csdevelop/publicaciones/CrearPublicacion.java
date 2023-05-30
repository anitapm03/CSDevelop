package com.example.csdevelop.publicaciones;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.example.csdevelop.chat.ChatActivity;
import com.example.csdevelop.chat.MensajeEnviar;
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

    private static final int PHOTO_SEND=1;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    //para la imagen
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String storage_path = "publicaciones-img/*";

    //sacar datos del usuario para indicar quien envia la publi
    FirebaseFirestore firestore;
    CollectionReference coleccionUsuarios;
    FirebaseAuth firebaseAuth;
    String id;
    String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        txtPubli = findViewById(R.id.txtPubli);
        volver = findViewById(R.id.volverAtras);
        publicar = findViewById(R.id.publicar);
        addImg = findViewById(R.id.addFoto);
        fotoPubli = findViewById(R.id.fotoPubli);


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
                    databaseReference.push().setValue(new Publicacion(txtPubli.getText().toString(), nombreUsuario, TYPE_TEXT, id));
                } else {
                    databaseReference.push().setValue(new Publicacion(txtPubli.getText().toString(), nombreUsuario, enlaceFoto,TYPE_PIC, id));
                }




                //cerramos el activity
                Intent intent = new Intent(CrearPublicacion.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una imagen"),PHOTO_SEND);
                fotoPubli.setVisibility(View.VISIBLE);
                //getPic();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("publicaciones");
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                    enlaceFoto = url;

                    //Opcion de cambiar el boton a añadir imagen y publicar
                    //Publicacion p = new Publicacion(txtPubli.getText().toString(),nombreUsuario,url,TYPE_PIC,id);

                    //databaseReference.push().setValue(p);

                }
            });
        }

    }



    private void getImg(String id){
        

    }

    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);

        String fragment = "social";
        intent.putExtra("fragment", fragment );

        startActivity(intent);

        finish();
    }
}