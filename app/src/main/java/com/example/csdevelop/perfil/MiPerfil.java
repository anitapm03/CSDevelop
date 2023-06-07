package com.example.csdevelop.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.csdevelop.Internet;
import com.example.csdevelop.R;
import com.example.csdevelop.login.LogIn;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MiPerfil extends AppCompatActivity {

    Button btonActualizar,btonVolver, btnCambiarFoto, btnCambiarContra, btnGuardarContra, btnEliminar, btnCancelar, btnGaleria;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    StorageReference storageReference;
    DocumentReference userRef;
    Dialog dialogSubirFoto;
    final int CODIGO_RESPUESTA_GALERIA = 3;
    Uri uri;
    ImageView imgFotoPerfil1, imgFotoPerfil2;
    TextView txtPassActual, txtPassNueva, txtConfirmarPassNueva, txtAlerta, txtAlerta2;
    String email, password, userId;
    AuthCredential credential;
    DocumentSnapshot document;

    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        if(!Internet.isNetworkAvailable(this)){
            Internet.showNoInternetAlert(this);
        }

        final EditText txtNombre = findViewById(R.id.edtUsuario);

        btonActualizar = findViewById(R.id.btonActualizar);
        btonVolver = findViewById(R.id.btonVolver);
        btnCambiarFoto = findViewById(R.id.btonEditarFoto);
        imgFotoPerfil1 = findViewById(R.id.imgFotoPerfil2);
        btnCambiarContra = findViewById(R.id.btonCambiarContrasena);
        txtPassActual = findViewById(R.id.edtContrasenaAntigua);
        txtPassNueva = findViewById(R.id.edtContrasenaNueva);
        txtConfirmarPassNueva = findViewById(R.id.edtContrasenaNueva2);
        btnGuardarContra = findViewById(R.id.btonGuardarContrasena);
        txtAlerta = findViewById(R.id.alerta);
        txtAlerta2 = findViewById(R.id.alerta2);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        // estilo basico de validacion
        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.edtContrasenaNueva, ".{8,}",R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.edtUsuario, ".{3,15}",R.string.invalid_username );


        userId = currentUser.getUid();
        userRef = db.collection("usuarios").document(userId);
        storageReference = FirebaseStorage.getInstance().getReference();
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        String username = document.getString("nombre");
                        email = document.getString("email");
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



        btonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    String nombre = txtNombre.getText().toString();
                    comprobarNombreUsuarioBBDD(nombre);
                }
            }
        });


        txtPassActual.setVisibility(View.GONE);
        txtPassNueva.setVisibility(View.GONE);
        txtConfirmarPassNueva.setVisibility(View.GONE);
        btnGuardarContra.setVisibility(View.GONE);
        btnCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPassActual.setText("");
                txtPassNueva.setText("");
                txtConfirmarPassNueva.setText("");
                mostrarCajasContrasena();
            }
        });

        btnGuardarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPassActual.getText().toString().isEmpty()){
                    txtAlerta.setText(getString(R.string.contraseñaActual));
                    txtAlerta.setTextColor(Color.RED);
                    quitarTextView();
                }else if(awesomeValidation.validate()) {
                    cambiarContraseña();
                }
            }
        });

        btnCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirFoto();
            }
        });

    }

    private void comprobarNombreUsuarioBBDD(final String nombre) {
        CollectionReference usuariosRef = db.collection("usuarios");

        Query query = usuariosRef.whereEqualTo("nombre", nombre);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        // El nombre no existe en la base de datos, se puede actualizar
                        userRef.update("nombre", nombre)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        txtAlerta2.setText(getString(R.string.nombreActualizado));
                                        String colorHex = "#01B706";
                                        int colorInt = Color.parseColor(colorHex);
                                        txtAlerta2.setTextColor(colorInt);
                                        quitarTextView();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        txtAlerta2.setText(getString(R.string.errorNombreActualizado));
                                        txtAlerta2.setTextColor(Color.RED);
                                        quitarTextView();
                                    }
                                });
                    } else {
                        // El nombre ya existe en la base de datos, no se puede actualizar
                        txtAlerta2.setText(getString(R.string.nombreExiste));
                        txtAlerta2.setTextColor(Color.RED);
                        quitarTextView();
                    }
                } else {
                    txtAlerta2.setText(getString(R.string.errorNombreActualizado));
                    txtAlerta2.setTextColor(Color.RED);
                    quitarTextView();
                }
            }
        });
    }


    private void quitarTextView(){
        CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                txtAlerta2.setText("");
                txtAlerta.setText("");
            }
        };

        countDownTimer.start();
    }

    private void mostrarCajasContrasena() {
        int visibility = txtPassActual.getVisibility();

        if (visibility == View.VISIBLE) {
            txtPassActual.setVisibility(View.GONE);
            txtPassNueva.setVisibility(View.GONE);
            txtConfirmarPassNueva.setVisibility(View.GONE);
            btnGuardarContra.setVisibility(View.GONE);
        } else {
            txtPassActual.setVisibility(View.VISIBLE);
            txtPassNueva.setVisibility(View.VISIBLE);
            txtConfirmarPassNueva.setVisibility(View.VISIBLE);
            btnGuardarContra.setVisibility(View.VISIBLE);
        }
    }


    private void subirFoto(){
        dialogSubirFoto = new Dialog(MiPerfil.this, android.R.style.Theme_Dialog);
        dialogSubirFoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSubirFoto.setContentView(R.layout.layout_subir_foto);

        imgFotoPerfil2 = (ImageView) dialogSubirFoto.findViewById(R.id.imgFotoPerfil2);
        btnEliminar = dialogSubirFoto.findViewById(R.id.btonEliminar);
        btnCancelar = dialogSubirFoto.findViewById(R.id.btonCancelar2);
        btnGaleria = dialogSubirFoto.findViewById(R.id.btonGaleria);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("usuarios").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String fotoPerfilURL = documentSnapshot.getString("foto_perfil");

                            if (fotoPerfilURL != null && !fotoPerfilURL.isEmpty()) {
                                Picasso.get().load(fotoPerfilURL).into(imgFotoPerfil2);
                            } else {
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
                dialogSubirFoto.dismiss();
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
                                        txtAlerta2.setText(getString(R.string.fotoEliminada));
                                        txtAlerta2.setTextColor(Color.RED);
                                        imgFotoPerfil2.setImageResource(R.drawable.foto_perfil);
                                        imgFotoPerfil1.setImageResource(R.drawable.foto_perfil);
                                        quitarTextView();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        txtAlerta2.setText(getString(R.string.errorFotoEliminada));
                                        txtAlerta2.setTextColor(Color.RED);
                                        quitarTextView();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        txtAlerta2.setText(getString(R.string.errorFotoEliminada));
                        txtAlerta2.setTextColor(Color.RED);
                        quitarTextView();
                    }
                });
            }

        });


        dialogSubirFoto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogSubirFoto.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSubirFoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSubirFoto.show();

    }

    private void cambiarContraseña(){
        password = txtPassActual.getText().toString();
        credential = EmailAuthProvider.getCredential(email, password);
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String newPassword = txtPassNueva.getText().toString();
                            if(newPassword.equals(txtConfirmarPassNueva.getText().toString())){
                                if(!newPassword.equals(password)){
                                    currentUser.updatePassword(newPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> updateTask) {
                                                    if (updateTask.isSuccessful()) {
                                                        txtAlerta.setText(getString(R.string.contraActualizada));
                                                        String colorHex = "#01B706";
                                                        int colorInt = Color.parseColor(colorHex);
                                                        txtAlerta.setTextColor(colorInt);
                                                        quitarTextView();
                                                    } else {
                                                        txtAlerta.setText(getString(R.string.errorContraAct));
                                                        txtAlerta.setTextColor(Color.RED);
                                                        quitarTextView();
                                                    }
                                                }
                                            });
                                }else {
                                    txtAlerta.setText(getString(R.string.mismaContraseña));
                                    txtAlerta.setTextColor(Color.RED);
                                    quitarTextView();
                                }
                            }else{
                                txtAlerta.setText(getString(R.string.contraNoCoinciden));
                                txtAlerta.setTextColor(Color.RED);
                                quitarTextView();
                            }

                        } else {
                            txtAlerta.setText(getString(R.string.contraActualIncorrecta));
                            txtAlerta.setTextColor(Color.RED);
                            quitarTextView();

                        }
                    }
                });
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
                                                txtAlerta2.setText(getString(R.string.fotoActualizada));
                                                txtAlerta.setTextColor(Color.GREEN);
                                                quitarTextView();
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