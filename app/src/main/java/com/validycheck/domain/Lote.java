package com.validycheck.domain;

import java.util.Date;

/**
 * Created by Samsung on 21/10/2017.
 */

public class Lote {

    private Produto produto;
    private Date validade;

    public Lote(Produto produto, Date validade) {
        this.produto = produto;
        this.validade = validade;
    }

    public Lote() {

    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }
}
