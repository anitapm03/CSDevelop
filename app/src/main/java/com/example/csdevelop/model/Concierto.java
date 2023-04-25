package com.example.csdevelop.model;

public class Concierto {
    String enlace, fecha, hora, idArtista, idSala, nombre;

    //constructor
    public Concierto(){}

    //constructor completo
    public Concierto(String enlace, String fecha, String hora, String idArtista, String idSala, String nombre) {
        this.enlace = enlace;
        this.fecha = fecha;
        this.hora = hora;
        this.idArtista = idArtista;
        this.idSala = idSala;
        this.nombre = nombre;
    }

    //getters y setters
    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(String idArtista) {
        this.idArtista = idArtista;
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
