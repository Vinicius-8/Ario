package com.oytu.ario;

public class M_Wifi {

    private int codigo;
    private String hora;
    private String horaDesat;
    private int ativado;

    public M_Wifi(int codigo, String hora, String horaDesat, int ativado) {
        this.codigo = codigo;
        this.hora = hora;
        this.horaDesat = horaDesat;
        this.ativado = ativado;
    }

    public M_Wifi(String hora, String horaDesat, int ativado) {
        this.hora = hora;
        this.horaDesat = horaDesat;
        this.ativado = ativado;
    }

    public M_Wifi() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getAtivado() {
        return ativado;
    }

    public void setAtivado(int ativado) {
        this.ativado = ativado;
    }

    public String getHoraDesat() {
        return horaDesat;
    }

    public void setHoraDesat(String horaDesat) {
        this.horaDesat = horaDesat;
    }
}
