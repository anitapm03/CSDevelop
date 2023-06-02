package com.example.csdevelop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Internet {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showNoInternetAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No hay conexión a internet");
        builder.setMessage("Por favor, asegúrese de tener una conexión a internet activa.");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            if (context instanceof Activity) {
                ((Activity) context).finishAffinity(); // Cerrar la aplicación
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
