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
        builder.setTitle(context.getString(R.string.noInternet));
        builder.setMessage(context.getString(R.string.conexion));
        builder.setPositiveButton(context.getString(R.string.aceptar), (dialog, which) -> {
            if (context instanceof Activity) {
                ((Activity) context).finishAffinity(); // Cerrar la aplicación
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
