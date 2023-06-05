package com.example.csdevelop.perfil;

import java.util.List;

public class Usuario {

    String nombreUsuario, passAntigua, passnueva;
    private List<String> misFavoritos, misGrupos;
    public Usuario(){

    }

    public Usuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Usuario(String nombreUsuario, String passAntigua, String passnueva) {
        this.nombreUsuario = nombreUsuario;
        this.passAntigua = passAntigua;
        this.passnueva = passnueva;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassAntigua() {
        return passAntigua;
    }

    public void setPassAntigua(String passAntigua) {
        this.passAntigua = passAntigua;
    }

    public String getPassnueva() {
        return passnueva;
    }

    public void setPassnueva(String passnueva) {
        this.passnueva = passnueva;
    }

    String foto_perfil;

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public List<String> getMisFavoritos() {
        return misFavoritos;
    }

    public void setMisFavoritos(List<String> misFavoritos) {
        this.misFavoritos = misFavoritos;
    }

    public List<String> getMisGrupos() {
        return misGrupos;
    }

    public void setMisGrupos(List<String> misGrupos) {
        this.misGrupos = misGrupos;
    }
}
