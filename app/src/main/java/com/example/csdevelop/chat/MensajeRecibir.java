package com.example.csdevelop.chat;

public class MensajeRecibir extends Mensaje{
    private Long hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String usuarioEnvia, String textoMensaje, String urlFoto, String tipo, String idUsuario, Long hora) {
        super(usuarioEnvia, textoMensaje, urlFoto, tipo, idUsuario);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
