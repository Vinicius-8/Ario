package com.oytu.ario;

public class M_Alarme {
    private int codigo;
    private String hora;
    private int toque;
    private int ativado;

    public M_Alarme(int codigo, String hora, int toque, int ativado) {
        this.codigo = codigo;
        this.hora = hora;
        this.toque = toque;
        this.ativado = ativado;
    }

    public M_Alarme(String hora, int toque, int ativado) {
        this.hora = hora;
        this.toque = toque;
        this.ativado = ativado;
    }

    public M_Alarme() {
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

    public int getToque() {
        return toque;
    }

    public void setToque(int toque) {
        this.toque = toque;
    }

    public int getAtivado() {
        return ativado;
    }

    public void setAtivado(int ativado) {
        this.ativado = ativado;
    }
}
