package com.example.csdevelop.registro;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.csdevelop.Internet;
import com.example.csdevelop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Handler;

public class Registro extends AppCompatActivity {
    Button btnVolver,btnRG;
    TextInputEditText edtMail, edtPassword, edtUsuario, edtPasswordC;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    TextView alerta;
    AwesomeValidation awesomeValidation;
    boolean existe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        if(!Internet.isNetworkAvailable(this)){
            Internet.showNoInternetAlert(this);
        }
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // estilo basico de validacion
        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.txtInputMail, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.txtInputPassword, ".{8,}",R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.txtInputUsuario, ".{3,}",R.string.invalid_username );

        //intent de registro recibido
        Intent rR= getIntent();

        edtMail =findViewById(R.id.edtMail);
        edtPassword =findViewById(R.id.edtPassword);
        edtPasswordC =findViewById(R.id.edtPasswordC);
        edtUsuario =findViewById(R.id.edtUsuario);
        btnVolver=findViewById(R.id.btnVolver);
        btnRG=findViewById(R.id.btnRG);
        alerta = findViewById(R.id.alertaRegistro);

        btnRG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtMail.getText().toString();
                String pasw = edtPassword.getText().toString();
                String paswC = edtPasswordC.getText().toString();
                String nombreUsuario = edtUsuario.getText().toString();

                if(awesomeValidation.validate()){
                        if(pasw.equals(paswC)){
                            registrarUsuario(email, nombreUsuario, pasw);
                        }else{
                            alerta.setText(getString(R.string.contraNoCoinciden));
                            alerta.setTextColor(ContextCompat.getColor(Registro.this, R.color.rojo));
                            quitarTextView();
                        }
                }

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public interface UsernameExistenceCallback {
        void onUsernameExists(boolean exists);
        void onUsernameExistenceCheckFailed(Exception exception);
    }

    private void checkUsernameExistence(String username, UsernameExistenceCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuariosRef = db.collection("usuarios");

        usuariosRef.whereEqualTo("nombre", username)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            callback.onUsernameExists(true);
                        } else {
                            callback.onUsernameExists(false);
                        }
                    } else {
                        callback.onUsernameExistenceCheckFailed(task.getException());
                    }
                });
    }

    private void registrarUsuario(String email, String nombreUsuario, String pasw) {
        checkUsernameExistence(nombreUsuario, new UsernameExistenceCallback() {
            @Override
            public void onUsernameExists(boolean exists) {
                if (exists) {
                    alerta.setText(getString(R.string.nombreNoDisponible));
                    alerta.setTextColor(ContextCompat.getColor(Registro.this, R.color.rojo));
                    quitarTextView();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, pasw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = firebaseAuth.getCurrentUser().getUid();
                                Map<String, Object> map = new HashMap<>();
                                map.put("nombre", nombreUsuario);
                                map.put("id", id);
                                map.put("email", email);
                                map.put("contraseña", pasw);

                                firestore.collection("usuarios").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        alerta.setText(getString(R.string.registroExitoso));
                                        alerta.setTextColor(ContextCompat.getColor(Registro.this, R.color.verde));
                                        esperarAntesDeFinalizar();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        alerta.setText(getString(R.string.errorRegistro));
                                        alerta.setTextColor(ContextCompat.getColor(Registro.this, R.color.rojo));
                                        quitarTextView();                                    }
                                });
                            } else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastDeError(errorCode);
                            }
                        }
                    });
                }
            }

            @Override
            public void onUsernameExistenceCheckFailed(Exception exception) {
            }
        });
    }


    private void dameToastDeError(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(Registro.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(Registro.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(Registro.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(Registro.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                edtMail.setError("La dirección de correo electrónico está mal formateada.");
                edtMail.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                alerta.setText(getString(R.string.contraIncorrecta));
                alerta.setTextColor(ContextCompat.getColor(this,R.color.rojo));
                quitarTextView();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                alerta.setText(getString(R.string.emailExiste));
                alerta.setTextColor(ContextCompat.getColor(this,R.color.rojo));
                quitarTextView();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                alerta.setText(getString(R.string.emailExiste));
                alerta.setTextColor(ContextCompat.getColor(this,R.color.rojo));
                quitarTextView();
                break;

            case "ERROR_USER_DISABLED":
                alerta.setText(getString(R.string.cuentaInhabilitada));
                alerta.setTextColor(ContextCompat.getColor(this,R.color.rojo));
                quitarTextView();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(Registro.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                edtPassword.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                edtPassword.requestFocus();
                break;

        }
    }

    private void quitarTextView(){
        CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                alerta.setText("");
            }
        };

        countDownTimer.start();
    }

    private void esperarAntesDeFinalizar() {
        Handler handler = new Handler();
        int tiempoEspera = 2000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, tiempoEspera);
    }

}