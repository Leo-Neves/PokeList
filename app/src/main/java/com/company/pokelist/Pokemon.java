package com.company.pokelist;

import java.util.List;

public class Pokemon {
    private String nome;
    private String url;
    private String imagem_url;
    private int id;

    //Novos atributos
    private int ordem;
    private int altura;             //em centímetros
    private double peso;               //em gramas
    private List<String> tipos;
    private int base_exp;
    private List<String> outras_imagens_url;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagem_url() {
        return imagem_url;
    }

    public void setImagem_url(String imagem_url) {
        this.imagem_url = imagem_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public List<String> getTipos() {
        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }

    public int getBase_exp() {
        return base_exp;
    }

    public void setBase_exp(int base_exp) {
        this.base_exp = base_exp;
    }

    public List<String> getOutras_imagens_url() {
        return outras_imagens_url;
    }

    public void setOutras_imagens_url(List<String> outras_imagens_url) {
        this.outras_imagens_url = outras_imagens_url;
    }

}
