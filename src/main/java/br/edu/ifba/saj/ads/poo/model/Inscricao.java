package br.edu.ifba.saj.ads.poo.model;

// liga um atleta a uma competição
public class Inscricao extends AbstractModel<Long> {
    private Atleta atleta;
    private Competicao competicao;

    public Inscricao(Atleta a, Competicao c) {
        super();
        this.atleta = a;
        this.competicao = c;
    }

    public Atleta getAtleta() {
        return atleta;
    }

    public void setAtleta(Atleta atleta) {
        this.atleta = atleta;
    }

    public Competicao getCompeticao() {
        return competicao;
    }

    public void setCompeticao(Competicao competicao) {
        this.competicao = competicao;
    }

    // é o toString que o JavaFX usa para renderizar a inscrição em tabelas e combos
    @Override
    public String toString() {
        return this.atleta.getNome() + " - " + this.competicao.getNome();
    }
}
