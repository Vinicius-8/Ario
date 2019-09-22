package com.oytu.ario;

/**
 * Classe que tem 'ligação' com celula/tupla do listview
 * é necessária para definir os dados de cada tupla
 */
public class CelulaAlarme {
    private String hora;
    private int toque_index;
    private int ativado;

    public CelulaAlarme(String hora, int toque_index, int ativado) {
        this.hora = hora;
        this.toque_index = toque_index;
        this.ativado = ativado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getToque_index() {
        return toque_index;
    }

    public void setToque_index(int toque_index) {
        this.toque_index = toque_index;
    }

    public int getAtivado() {
        return ativado;
    }

    public void setAtivado(int ativado) {
        this.ativado = ativado;
    }
}
