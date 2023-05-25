package com.example.csdevelop.perfil;

public class Usuario {

    String nombreUsuario, passAntigua, passnueva;

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
}
