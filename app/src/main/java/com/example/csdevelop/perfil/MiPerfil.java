package com.example.csdevelop.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.csdevelop.DetalleConcierto;
import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MiPerfil extends AppCompatActivity {

    Button btonActualizar, btonCancelar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        final EditText txtNombre = findViewById(R.id.edtUsuario);
        final EditText txtpassAntigua = findViewById(R.id.edtPassword);
        final EditText txtpassNueva = findViewById(R.id.edtPasswordC);


        btonActualizar = findViewById(R.id.btnActualizar);
        btonCancelar = findViewById(R.id.btnCancelar);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();




        String userId = currentUser.getUid();
        DocumentReference userRef = db.collection("usuarios").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String username = document.getString("nombre");
                        txtNombre.setText(username);
                    }
                }
            }
        });



        btonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DocumentReference userRef = db.collection("usuarios").document(userId);
                    userRef.update("nombre",txtNombre.getText().toString());


            }

        });


        btonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfil.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}