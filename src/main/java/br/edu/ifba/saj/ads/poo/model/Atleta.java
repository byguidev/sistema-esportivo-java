package br.edu.ifba.saj.ads.poo.model;

// classe que representa um atleta inscrito em competições
public class Atleta {
    // contador estático para gerar ids únicos automaticamente
    private static long contador = 0;
    // identificador único do atleta
    private long id;
    // nome completo do atleta
    private String nome;
    // categoria em que o atleta compete (ex.: adulto, juvenil)
    private String categoria;

    // construtor que cria um atleta gerando o id automaticamente
    public Atleta(String nome, String categoria) {
        this.id = ++contador;
        this.nome = nome;
        this.categoria = categoria;
    }

    // retorna o id do atleta
    public long getId() {
        return id;
    }

    // permite redefinir o id (uso em cenários de carga/restauro)
    public void setId(long id) {
        this.id = id;
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
