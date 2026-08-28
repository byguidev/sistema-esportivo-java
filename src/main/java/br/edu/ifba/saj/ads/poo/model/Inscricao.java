package br.edu.ifba.saj.ads.poo.model;

// classe que representa a inscrição de um atleta em uma competição
public class Inscricao extends AbstractModel<Long> {
    // atleta inscrito
    private Atleta atleta;
    // competição alvo da inscrição
    private Competicao competicao;

    // chama o construtor da superclasse e cria uma inscrição associando atleta e competição
    public Inscricao(Atleta a, Competicao c) {
        super();
        this.atleta = a;
        this.competicao = c;
    }

    // retorna o atleta da inscrição
    public Atleta getAtleta() {
        return atleta;
    }

    // atualiza o atleta da inscrição
    public void setAtleta(Atleta atleta) {
        this.atleta = atleta;
    }

    // retorna a competição da inscrição
    public Competicao getCompeticao() {
        return competicao;
    }

    // atualiza a competição da inscrição
    public void setCompeticao(Competicao competicao) {
        this.competicao = competicao;
    }

    // representação textual usada em combos (atleta - competição)
    @Override
    public String toString() {
        return this.atleta.getNome() + " - " + this.competicao.getNome();
    }
}
