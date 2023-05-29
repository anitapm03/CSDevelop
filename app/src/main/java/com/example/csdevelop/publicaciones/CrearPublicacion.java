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
    private static final int COD_SEL_IMAGE = 300;
    private static final int COD_SEL_Storage = 200;
    private Uri image_url;

    FirebaseAuth mauth;
    FirebaseFirestore mfirestore;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;

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
        //XFaTmJtvW9Wp4hLgxNPrJ3TQ5rF2
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
                //añadimos una publicacion nueva
                databaseReference.push().setValue(new Publicacion(txtPubli.getText().toString(), nombreUsuario, "1", id));

                //cerramos el activity
                Intent intent = new Intent(CrearPublicacion.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //aqui hay que manejar la imagen que es lo chungo

                uploadPhoto();
            }
        });


        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearPublicacion.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void uploadPhoto(){

        //para abrir la galeria
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("image_url", "requestCode - RESULT_OK: "+requestCode+" "+RESULT_OK);
        if(resultCode == RESULT_OK) {
            if (requestCode == COD_SEL_IMAGE) {
                image_url = data.getData();
                subirPhoto(image_url);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url){
        progressDialog.setMessage("Cargando imagen");
        progressDialog.show();
        String ruta = storage_path + "" + photo + "" + mauth.getUid() + "" + idd;

        StorageReference reference = storageReference.child(ruta);

        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", downloadUri);
                            //aqui se añade a firestore

                            Publicacion p = new Publicacion(txtPubli.getText().toString(),nombreUsuario,downloadUri, "2" ,id );
                            databaseReference.push().setValue(p);

                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void getImg(String id){
        

    }

    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}