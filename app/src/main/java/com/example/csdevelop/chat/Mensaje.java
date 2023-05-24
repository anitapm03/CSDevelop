package com.example.csdevelop.chat;

public class Mensaje {
    private String usuarioEnvia, textoMensaje, urlFoto, tipo, idUsuario;

    public Mensaje(){}

    public Mensaje(String usuarioEnvia, String textoMensaje, String tipo, String idUsuario) {
        this.usuarioEnvia = usuarioEnvia;
        this.textoMensaje = textoMensaje;
        this.tipo = tipo;
        this.idUsuario = idUsuario;
    }

    public Mensaje(String usuarioEnvia, String textoMensaje, String urlFoto, String tipo, String idUsuario){
        this.usuarioEnvia = usuarioEnvia;
        this.textoMensaje = textoMensaje;
        this.urlFoto = urlFoto;
        this.tipo = tipo;
        this.idUsuario = idUsuario;
    }

    public String getUsuarioEnvia() {
        return usuarioEnvia;
    }

    public void setUsuarioEnvia(String usuarioEnvia) {
        this.usuarioEnvia = usuarioEnvia;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public void setTextoMensaje(String textoMensaje) {
        this.textoMensaje = textoMensaje;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
