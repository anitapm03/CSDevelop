package com.example.csdevelop.chat;

import java.util.Map;

public class MensajeEnviar extends Mensaje{
    private Map hora;

    public MensajeEnviar(){}

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String usuarioEnvia, String textoMensaje, String tipo, String idUsuario, Map hora) {
        super(usuarioEnvia, textoMensaje, tipo, idUsuario);
        this.hora = hora;
    }

    public MensajeEnviar(String usuarioEnvia, String textoMensaje, String urlFoto, String tipo, String idUsuario, Map hora) {
        super(usuarioEnvia, textoMensaje, urlFoto, tipo, idUsuario);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
