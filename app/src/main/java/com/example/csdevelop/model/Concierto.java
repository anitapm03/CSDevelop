package com.example.csdevelop.model;

import java.io.Serializable;
import java.util.List;

public class Concierto implements Serializable {
    String enlace, fecha, hora, idArtista, idSala, nombre, imagen;
    Boolean isFavorito;
    private List<Concierto> conciertosList;
    //constructor
    public Concierto(){}

    //constructor completo
    public Concierto(String enlace, String fecha, String hora, String idArtista, String idSala, String nombre, String imagen) {
        this.enlace = enlace;
        this.fecha = fecha;
        this.hora = hora;
        this.idArtista = idArtista;
        this.idSala = idSala;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public Concierto(String nombre) {
    }

    //getters y setters
    public String getEnlace() {
        return enlace;
    }
    public boolean isFavorito() {
        return isFavorito;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
