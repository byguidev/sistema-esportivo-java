package br.edu.ifba.saj.ads.poo.model;

import java.time.LocalDate;

// competição esportiva cadastrada no sistema
public class Competicao extends AbstractModel<Long> {
    private String nome;
    private LocalDate data;
    private int limiteParticipantes;

    public Competicao(String nome, LocalDate data, int limiteParticipantes) {
        super();
        this.nome = nome;
        this.data = data;
        this.limiteParticipantes = limiteParticipantes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    // nome curto porque é lido em toda validação de vaga
    public int getLimite() {
        return limiteParticipantes;
    }

    public void setLimite(int limiteParticipantes) {
        this.limiteParticipantes = limiteParticipantes;
    }

    // é o toString que o JavaFX usa para renderizar a competição em tabelas e combos
    @Override
    public String toString() {
        return this.getNome();
    }
}
