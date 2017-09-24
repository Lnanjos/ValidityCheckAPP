package com.validycheck;

public class Secao {

    public Secao(int idSecao, String nomeSecao) {
        this.idSecao = idSecao;
        this.nomeSecao = nomeSecao;
    }

    private int idSecao;
    private String nomeSecao;

    public int getIdSecao() {
        return idSecao;
    }

    public void setIdSecao(int idSecao) {
        this.idSecao = idSecao;
    }

    public String getNomeSecao() {
        return nomeSecao;
    }

    public void setNomeSecao(String nomeSecao) {
        this.nomeSecao = nomeSecao;
    }
}
