package br.edu.ifba.saj.ads.poo.model;

// classe que representa um atleta inscrito em competições
public class Atleta extends AbstractModel<Long> {
    // nome completo do atleta
    private String nome;
    // categoria em que o atleta compete (ex.: adulto, juvenil)
    private String categoria;

    // construtor que cria um atleta e chama o construtor da superclasse
    public Atleta(String nome, String categoria) {
        super();
        this.nome = nome;
        this.categoria = categoria;
    }

    // retorna o nome do atleta
    public String getNome() {
        return nome;
    }

    // atualiza o nome do atleta
    public void setNome(String nome) {
        this.nome = nome;
    }

    // retorna a categoria do atleta
    public String getCategoria() {
        return categoria;
    }

    // atualiza a categoria do atleta
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // representação textual usada em listas e tabelas (mostra o nome)
    @Override
    public String toString() {
        return this.getNome();
    }
}
