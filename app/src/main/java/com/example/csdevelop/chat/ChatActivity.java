package com.example.csdevelop.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.example.csdevelop.adapter.MensajesAdapter;
import com.example.csdevelop.model.Concierto;
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

//import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    //tipos de mensaje
    private static final String TYPE_TEXT="1";
    private static final String TYPE_PIC="2";
    //ID CHAT GLOBAL
    String id_chat_global;

   // CircleImageView fotoConciertoChat;
    EditText eText;
    TextView nombreEventoChat;
    Button volver;
    ImageButton enviarMensaje, addImagen;

    RecyclerView rvMensajes;

    ArrayList<MensajeRecibir> msgList;
    private MensajesAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

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
                databaseReference.push().setValue(new MensajeEnviar(eText.getText().toString(), nombreUsuario, TYPE_TEXT, id,ServerValue.TIMESTAMP));
                eText.setText("");
            }
        });

        addImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una imagen"),PHOTO_SEND);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    //Uri u = Uri.parse(url);
                    // NO FUNCIONA

                    //Uri u = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();
                    MensajeEnviar m = new MensajeEnviar("ha enviado una imagen: ",nombreUsuario,url, TYPE_PIC ,id , ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);

                }
            });
        }
    }



    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}