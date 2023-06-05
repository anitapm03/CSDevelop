package com.example.csdevelop.publicaciones;

import android.net.Uri;

import java.io.Serializable;

public class Publicacion implements Serializable {
    private String nombreUsuario, textoPublicacion, urlFotoPublicacion, tipoPublicacion, idUsuarioPublicacion;
    public Publicacion(){}

    public Publicacion(String nombreUsuario, String textoPublicacion, String tipoPublicacion, String idUsuarioPublicacion) {
        this.nombreUsuario = nombreUsuario;
        this.textoPublicacion = textoPublicacion;
        this.tipoPublicacion = tipoPublicacion;
        this.idUsuarioPublicacion = idUsuarioPublicacion;
    }

    public Publicacion(String nombreUsuario, String textoPublicacion, String urlFotoPublicacion, String tipoPublicacion, String idUsuarioPublicacion) {
        this.nombreUsuario = nombreUsuario;
        this.textoPublicacion = textoPublicacion;
        this.urlFotoPublicacion = urlFotoPublicacion;
        this.tipoPublicacion = tipoPublicacion;
        this.idUsuarioPublicacion = idUsuarioPublicacion;
    }



    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTextoPublicacion() {
        return textoPublicacion;
    }

    public void setTextoPublicacion(String textoPublicacion) {
        this.textoPublicacion = textoPublicacion;
    }

    public String getUrlFotoPublicacion() {
        return urlFotoPublicacion;
    }

    public void setUrlFotoPublicacion(String urlFotoPublicacion) {
        this.urlFotoPublicacion = urlFotoPublicacion;
    }

    public String getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(String tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }

    public String getIdUsuarioPublicacion() {
        return idUsuarioPublicacion;
    }

    public void setIdUsuarioPublicacion(String idUsuarioPublicacion) {
        this.idUsuarioPublicacion = idUsuarioPublicacion;
    }
}
