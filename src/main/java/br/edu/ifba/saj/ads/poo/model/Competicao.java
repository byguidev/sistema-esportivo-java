package br.edu.ifba.saj.ads.poo.model;

import java.time.LocalDate;

// classe que representa uma competição esportiva cadastrada no sistema
public class Competicao {
    // contador estático usado para gerar ids únicos
    private static long contador = 0;
    // identificador único da competição
    private long id;
    // nome da competição
    private String nome;
    // data em que a competição será realizada
    private LocalDate data;
    // número máximo de atletas que podem se inscrever
    private int limiteParticipantes;

    // cria uma nova competição com id gerado automaticamente
    public Competicao(String nome, LocalDate data, int limiteParticipantes) {
        this.id = ++contador;
        this.nome = nome;
        this.data = data;
        this.limiteParticipantes = limiteParticipantes;
    }

    // retorna o id da competição
    public long getId() {
        return id;
    }

    // redefine o id da competição
    public void setId(long id) {
        this.id = id;
    }

    // retorna o nome da competição
    public String getNome() {
        return nome;
    }

    // atualiza o nome da competição
    public void setNome(String nome) {
        this.nome = nome;
    }

    // retorna a data da competição
    public LocalDate getData() {
        return data;
    }

    // atualiza a data da competição
    public void setData(LocalDate data) {
        this.data = data;
    }

    // retorna o limite de participantes (atalho para limiteParticipantes)
    public int getLimite() {
        return limiteParticipantes;
    }

    // atualiza o limite de participantes
    public void setLimite(int limiteParticipantes) {
        this.limiteParticipantes = limiteParticipantes;
    }

    // representação textual da competição usada em combos e tabelas
    @Override
    public String toString() {
        return this.getNome();
    }
}
