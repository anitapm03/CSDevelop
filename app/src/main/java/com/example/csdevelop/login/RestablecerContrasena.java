package com.example.csdevelop.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.csdevelop.Internet;
import com.example.csdevelop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class RestablecerContrasena extends AppCompatActivity {

    Button btonEnviar, btonVolver;
    EditText editTextEmail;
    TextView alerta;
    FirebaseAuth mAuth;
    private String email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser;

    DocumentReference userRef;

    AwesomeValidation awesomeValidation;

    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_contrasena);

        if(!Internet.isNetworkAvailable(this)){
            Internet.showNoInternetAlert(this);
        }

        btonEnviar = findViewById(R.id.btnEnviar);
        btonVolver = findViewById(R.id.btonVolver);
        editTextEmail = findViewById(R.id.email);
        alerta = findViewById(R.id.alertaRestablecer);
        mAuth = FirebaseAuth.getInstance();

        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.email, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Espere un momento...")
                .setCancelable(false);
        mDialog = builder.create();

        btonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                if (awesomeValidation.validate()) {
                    resetPassword();
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

    private void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDialog.show();

                    alerta.setText("El correo ha sido enviado");
                    String codigoColor = "#01B706";
                    int color = Color.parseColor(codigoColor);
                    alerta.setTextColor(color);
                    quitarTextView();

                    CollectionReference usuariosRef = db.collection("usuarios");
                    Query query = usuariosRef.whereEqualTo("email", email);
                    mDialog.dismiss();
                } else {
                    alerta.setText("El correo introducido no está asociado a ninguna cuenta");
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
                alerta.setText("");
            }
        };

        countDownTimer.start();
    }
}