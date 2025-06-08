package com.example.coletacerta.model;

public class Solicitacao {
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String observacoes;
    private String materiais;
    private String imagemUrl;

    public Solicitacao() {
        // Construtor vazio exigido pelo Firebase
    }

    public Solicitacao(String logradouro, String numero, String bairro, String cidade, String cep,
                       String observacoes, String materiais, String imagemUrl) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.observacoes = observacoes;
        this.materiais = materiais;
        this.imagemUrl = imagemUrl;
    }

    // Getters e Setters

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getMateriais() { return materiais; }
    public void setMateriais(String materiais) { this.materiais = materiais; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
}
