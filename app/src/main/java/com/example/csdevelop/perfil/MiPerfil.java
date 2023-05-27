package com.example.csdevelop.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MiPerfil extends AppCompatActivity {

    Button btonActualizar, btonSalir, btnCambiarFoto, btnCambiarContra, btnGuardarContra;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    Dialog dialogSubirFoto;
    final int CODIGO_RESPUESTA_GALERIA = 3;
    Uri uri;
    ImageView imgFotoPerfil1, imgFotoPerfil2;
    Button btnEliminar, btnCancelar, btnGaleria;
    TextView txtPassAntigua, txtPassNueva;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        final EditText txtNombre = findViewById(R.id.edtUsuario);
        //final EditText txtpassAntigua = findViewById(R.id.edtPassword);
        //final EditText txtpassNueva = findViewById(R.id.edtPasswordC);


        btonActualizar = findViewById(R.id.btonActualizar);
        btonSalir = findViewById(R.id.btonSalir);
        btnCambiarFoto = findViewById(R.id.btonEditarFoto);
        imgFotoPerfil1 = findViewById(R.id.imgFotoPerfil2);
        btnCambiarContra = findViewById(R.id.btonCambiarContrasena);
        txtPassAntigua = findViewById(R.id.edtContrasenaAntigua);
        txtPassNueva = findViewById(R.id.edtContrasenaNueva);
        btnGuardarContra = findViewById(R.id.btonGuardarContrasena);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();



        String userId = currentUser.getUid();
        DocumentReference userRef = db.collection("usuarios").document(userId);
        storageReference = FirebaseStorage.getInstance().getReference();
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String username = document.getString("nombre");
                        email = document.getString("email");
                        password = document.getString("contraseña");
                        txtNombre.setText(username);
                        String fotoPerfilURL = document.getString("foto_perfil");

                        if (fotoPerfilURL != null && !fotoPerfilURL.isEmpty()) {
                            Picasso.get().load(fotoPerfilURL).into(imgFotoPerfil1);
                        } else {
                            // El usuario no tiene foto de perfil, establecer la imagen por defecto
                            imgFotoPerfil1.setImageResource(R.drawable.foto_perfil);
                        }
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

        btnCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCajasContrasena();
            }
        });

        btnGuardarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String newPassword = txtPassNueva.getText().toString();
                                    String contraseñaAntigua = txtPassAntigua.getText().toString();
                                    if(contraseñaAntigua.equals(password)){
                                        user.updatePassword(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Contraseña antigua incorrecta", Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    // Ocurrió un error en el inicio de sesión
                                    Toast.makeText(getApplicationContext(), "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });


        btonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfil.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirFoto();
            }
        });

    }


    private void mostrarCajasContrasena() {
        int visibility = txtPassAntigua.getVisibility();

        if (visibility == View.VISIBLE) {
            txtPassAntigua.setVisibility(View.GONE);
            txtPassNueva.setVisibility(View.GONE);
            btnGuardarContra.setVisibility(View.GONE);
        } else {
            txtPassAntigua.setVisibility(View.VISIBLE);
            txtPassNueva.setVisibility(View.VISIBLE);
            btnGuardarContra.setVisibility(View.VISIBLE);
        }
    }


    private void subirFoto(){
        dialogSubirFoto = new Dialog(MiPerfil.this, android.R.style.Theme_Dialog);
        dialogSubirFoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSubirFoto.setContentView(R.layout.layout_subir_foto); ///////////////////////////////////////////////////////

        imgFotoPerfil2 = (ImageView) dialogSubirFoto.findViewById(R.id.imgFotoPerfil2);
        btnEliminar = dialogSubirFoto.findViewById(R.id.btonEliminar);
        btnCancelar = dialogSubirFoto.findViewById(R.id.btonCancelar2);
        btnGaleria = dialogSubirFoto.findViewById(R.id.btonGaleria);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("usuarios").document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String fotoPerfilURL = documentSnapshot.getString("foto_perfil");

                            if (fotoPerfilURL != null && !fotoPerfilURL.isEmpty()) {
                                Picasso.get().load(fotoPerfilURL).into(imgFotoPerfil2);
                            } else {
                                // El usuario no tiene foto de perfil, establecer la imagen por defecto
                                imgFotoPerfil2.setImageResource(R.drawable.foto_perfil);

                            }
                        }
                    }
                });

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,CODIGO_RESPUESTA_GALERIA);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón de Cancelar
                dialogSubirFoto.dismiss(); // Cierra el diálogo
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference referenciaFotoPerfil = storageReference.child("Perfil").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                referenciaFotoPerfil.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseFirestore.getInstance().collection("usuarios")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("foto_perfil", FieldValue.delete())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Imagen de perfil eliminada", Toast.LENGTH_SHORT).show();
                                        imgFotoPerfil2.setImageResource(R.drawable.foto_perfil);
                                        imgFotoPerfil1.setImageResource(R.drawable.foto_perfil);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error al eliminar la referencia en Firestore", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al eliminar la imagen en Firebase Storage", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        dialogSubirFoto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogSubirFoto.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSubirFoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSubirFoto.show();

    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == CODIGO_RESPUESTA_GALERIA){
            if(data!=null && data.getData() != null){
                uri = data.getData();
                try{
                    imgFotoPerfil2.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri));
                    imgFotoPerfil1.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri));

                    UploadTask uploadTask;
                    final StorageReference refe = storageReference.child("Perfil").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
                    uploadTask = refe.putFile(uri);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                                throw task.getException();
                            return refe.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Map<String,Object> map = new HashMap<>();
                                map.put("foto_perfil", task.getResult().toString());

                                FirebaseFirestore.getInstance().collection("usuarios").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });

                }catch(Exception e){
                    Log.e("Error",""+e.toString());
                }
            }
        }
    }

}