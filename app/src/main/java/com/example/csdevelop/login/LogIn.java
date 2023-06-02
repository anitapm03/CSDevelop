package com.example.csdevelop.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.csdevelop.MainActivity;
import com.example.csdevelop.R;
import com.example.csdevelop.chat.ChatActivity;
import com.example.csdevelop.registro.Registro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {
    Button btnIS, btnR, btnOlvideContraseña;
    TextInputEditText edtMail, edtPassword;
    TextView alerta;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_CSDevelop);
        super.onCreate(savedInstanceState);


        if (!isNetworkAvailable()) {
            showNoInternetAlert();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        //codigo para comprobar que tenemos sesion iniciada
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            iniciarSesion();
        }
        setContentView(R.layout.activity_log_in);

        // estilo basico de validacion
        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);
        //awesomeValidation.addValidation(this,R.id.txtInputMail, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.txtInputUsuario, ".{1,}",R.string.invalid_usernameLogin);
        awesomeValidation.addValidation(this,R.id.txtInputPassword, ".{1,}",R.string.invalid_passwordLogin);


        edtMail=findViewById(R.id.edtMail);
        edtPassword=findViewById(R.id.edtPassword);
        btnIS=findViewById(R.id.btnRG);
        btnR=findViewById(R.id.btnR);
        alerta = findViewById(R.id.alertaLogin);
        btnOlvideContraseña = findViewById(R.id.btnOlvideContraseña);

        btnIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(awesomeValidation.validate()){

                    String mail=edtMail.getText().toString();
                    String pasw=edtPassword.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(mail,pasw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                iniciarSesion();


                            }else{
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeError(errorCode);
                            }
                        }
                    });
                }
            }
        });
        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse();
            }
        });

        btnOlvideContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, RestablecerContrasena.class));
            }
        });
    }

    public void iniciarSesion(){
        Intent is =new Intent(this, MainActivity.class);
        //para no ejecutar actividades innecesarias
        is.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(is);
    }

    public void registrarse(){
        Intent r = new Intent(this, Registro.class);
        startActivity(r);
    }


    private void dameToastdeError(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(LogIn.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(LogIn.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(LogIn.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                alerta.setText("Email inválido");
                alerta.setTextColor(ContextCompat.getColor(this,R.color.rojo));
                //Toast.makeText(LogIn.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                //edtMail.setError("La dirección de correo electrónico está mal formateada.");
                edtMail.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                alerta.setText("Contraseña incorrecta");
                alerta.setTextColor(ContextCompat.getColor(this,R.color.rojo));
                //Toast.makeText(LogIn.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                //edtPassword.setError("la contraseña es incorrecta ");
                edtPassword.requestFocus();
                edtPassword.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(LogIn.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(LogIn.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(LogIn.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(LogIn.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(LogIn.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                alerta.setText("No hay ninguna cuenta asociada a este usuario");
                alerta.setTextColor(ContextCompat.getColor(this,R.color.rojo));
                //Toast.makeText(LogIn.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(LogIn.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(LogIn.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(LogIn.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                edtPassword.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                edtPassword.requestFocus();
                break;

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void showNoInternetAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No hay conexión a internet");
        builder.setMessage("Por favor, asegúrese de tener una conexión a internet activa.");
        builder.setPositiveButton("Aceptar", (dialog, which)->finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}