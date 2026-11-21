package br.edu.ifba.saj.ads.poo.model;

// atleta que pode ser inscrito em competições
public class Atleta extends AbstractModel<Long> {
    private String nome;
    // ex.: adulto, juvenil
    private String categoria;

    public Atleta(String nome, String categoria) {
        super();
        this.nome = nome;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // é o toString que o JavaFX usa para renderizar o atleta em tabelas e combos
    @Override
    public String toString() {
        return this.getNome();
    }
}
