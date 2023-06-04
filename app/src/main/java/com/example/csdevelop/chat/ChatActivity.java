package com.example.csdevelop.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.Internet;
import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.example.csdevelop.adapter.MensajesAdapter;
import com.example.csdevelop.model.Concierto;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    //tipos de mensaje
    private static final String TYPE_TEXT="1";
    private static final String TYPE_PIC="2";
    //ID CHAT GLOBAL
    String id_chat_global;

    Uri uri;
   // CircleImageView fotoConciertoChat;
    EditText eText;
    TextView nombreEventoChat;
    Button volver;
    ImageButton enviarMensaje, addImagen;

    final int CODIGO_RESPUESTA_GALERIA = 3;
    RecyclerView rvMensajes;

    ArrayList<MensajeRecibir> msgList;
    private MensajesAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;

    //sacar datos del usuario para indicar quien envia el msg
    FirebaseFirestore firestore;
    CollectionReference coleccionUsuarios, coleccionConciertos;
    FirebaseAuth firebaseAuth;
    String id;
    String nombreUsuario, referencia;
    private static final int PHOTO_SEND=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if(!Internet.isNetworkAvailable(this)){
            Internet.showNoInternetAlert(this);
        }

        Glide.with(this).setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)).applyDefaultRequestOptions(RequestOptions.noTransformation()).applyDefaultRequestOptions(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).applyDefaultRequestOptions(RequestOptions.skipMemoryCacheOf(true)).applyDefaultRequestOptions(RequestOptions.overrideOf(250, 250));

        volver=findViewById(R.id.volver);
        //fotoConciertoChat=findViewById(R.id.fotoConciertoChat);
        eText=findViewById(R.id.eText);
        nombreEventoChat=findViewById(R.id.nombreEventoChat);
        enviarMensaje=findViewById(R.id.enviarMensaje);
        rvMensajes=findViewById(R.id.rvMensajes);
        addImagen=findViewById(R.id.addImagen);

        //hacer un metodo que identifique el nombre del concierto
        //y cree una referencia chats/nombreEvento para acceder a los distintos chats
        referencia = "chats/";
        //recogemos el evento
        Concierto concierto = (Concierto) getIntent().getSerializableExtra("concierto");
        nombreEventoChat.setText(concierto.getNombre());
        referencia += concierto.getNombre();


        //instanciamos lo de la base de datos
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(referencia);
        storage = FirebaseStorage.getInstance();



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
                        String comprobacion = nombreUsuario;
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "Error con el Usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rvMensajes.setHasFixedSize(true);
        msgList = new ArrayList<>();

        adapter = new MensajesAdapter(this);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        //layout.setStackFromEnd(true);
        rvMensajes.setLayoutManager(layout);
        rvMensajes.setAdapter(adapter);



        enviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // adapter.addMensaje(new Mensaje(eText.getText().toString(), "Ana"));
                if(!eText.getText().toString().equals("")){
                    databaseReference.push().setValue(new MensajeEnviar(eText.getText().toString(), nombreUsuario, TYPE_TEXT, id,ServerValue.TIMESTAMP));
                    eText.setText("");
                }

            }
        });

        addImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,CODIGO_RESPUESTA_GALERIA);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        //para que se añada autom
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MensajeRecibir m = snapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);

                String fragment = "grupos";
                intent.putExtra("fragment", fragment );

                startActivity(intent);

                finish();
            }
        });

    }//fin oncreate

    //para que baje autom cuando la pantalla se llene de msgs
    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_RESPUESTA_GALERIA) {
            if (data != null && data.getData() != null) {
                uri = data.getData();
                try {
                    final StorageReference refe = storage.getReference().child("imagenes_chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
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
                                Uri downloadUri = task.getResult();

                                // Crear un mensaje con la URL de descarga y enviarlo al chat
                                MensajeEnviar m = new MensajeEnviar("Ha enviado una imagen: ", nombreUsuario, downloadUri.toString(), TYPE_PIC, id, ServerValue.TIMESTAMP);
                                databaseReference.push().setValue(m);
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

        String fragment = "grupos";
        intent.putExtra("fragment", fragment );

        startActivity(intent);

        finish();
    }
}