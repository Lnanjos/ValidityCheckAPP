package com.validycheck.domain;

public class Secao {

    private Long codigo;
    private String nomeSecao;

    public Secao() {
    }

    public Secao(Long codigo, String nomeSecao) {
        this.codigo = codigo;
        this.nomeSecao = nomeSecao;
    }

    public Secao(String nomeSecao) {
        this.nomeSecao = nomeSecao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNomeSecao() {
        return nomeSecao;
    }

    public void setNomeSecao(String nomeSecao) {
        this.nomeSecao = nomeSecao;
    }
}
