package com.example.csdevelop.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.csdevelop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class RestablecerContrasena extends AppCompatActivity {

    Button btonEnviar, btonVolver;
    EditText editTextEmail;
    TextView alerta;
    FirebaseAuth mAuth;
    private String email;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_contrasena);

        btonEnviar = findViewById(R.id.btnEnviar);
        btonVolver = findViewById(R.id.btonVolver);
        editTextEmail = findViewById(R.id.email);
        alerta = findViewById(R.id.alertaRestablecer);
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        btonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                if(!email.isEmpty()){
                    resetPassword();
                }else{
                    alerta.setText("Introduzca un email");
                }

            }
        });

        btonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mDialog.setMessage("Espere un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    alerta.setText("El correo ha sido enviado");
                    String codigoColor = "#3C7C32";
                    int color = Color.parseColor(codigoColor);
                    alerta.setTextColor(color);
                    mDialog.dismiss();
                }else{
                    alerta.setText("El correo introducido no está asociado a ninguna cuenta");
                    String codigoColor = "#FA000F";
                    int color = Color.parseColor(codigoColor);
                    alerta.setTextColor(color);
                    mDialog.dismiss();
                }

            }
        });

    }
}