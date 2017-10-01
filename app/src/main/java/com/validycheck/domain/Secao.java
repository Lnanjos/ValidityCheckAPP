package com.validycheck.domain;

public class Secao {

    public Secao(){}

    public Secao(int codigo, String nomeSecao) {
        this.codigo = codigo;
        this.nomeSecao = nomeSecao;
    }

    public Secao(String nomeSecao) {
        this.nomeSecao = nomeSecao;
    }

    private int codigo;
    private String nomeSecao;


    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomeSecao() {
        return nomeSecao;
    }

    public void setNomeSecao(String nomeSecao) {
        this.nomeSecao = nomeSecao;
    }
}
