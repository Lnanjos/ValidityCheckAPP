package com.validycheck.domain;

public class Produto {

    private Long codigo;
    private String nomeProduto;
    private String codBarraProduto;
    private Secao secao;

    public Produto() {
    }

    public Produto(String nomeProduto, String codBarraProduto, Secao secao) {
        this.nomeProduto = nomeProduto;
        this.codBarraProduto = codBarraProduto;
        this.secao = secao;
    }

    public Produto(Long codigo, String nomeProduto, String codBarraProduto, Secao secao) {
        this.codigo = codigo;
        this.nomeProduto = nomeProduto;
        this.codBarraProduto = codBarraProduto;
        this.secao = secao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCodBarraProduto() {
        return codBarraProduto;
    }

    public void setCodBarraProduto(String codBarraProduto) {
        this.codBarraProduto = codBarraProduto;
    }

    public Secao getSecao() {
        return secao;
    }

    public void setSecao(Secao secao) {
        this.secao = secao;
    }
}
