package br.edu.ifba.saj.ads.poo.model;

// classe que representa o resultado/pódio de uma competição
public class Resultado extends AbstractModel<Long> {
    // competição à qual o resultado se refere
    private Competicao competicao;
    // atleta que ficou em primeiro lugar
    private Atleta primeiroLugar;
    // atleta que ficou em segundo lugar
    private Atleta segundoLugar;
    // atleta que ficou em terceiro lugar
    private Atleta terceiroLugar;

    // chama o construtor da superclasse e cria um resultado associando competição e os três colocados
    public Resultado(Competicao competicao, Atleta primeiroLugar, Atleta segundoLugar, Atleta terceiroLugar) {
        super();
        this.competicao = competicao;
        this.primeiroLugar = primeiroLugar;
        this.segundoLugar = segundoLugar;
        this.terceiroLugar = terceiroLugar;
    }

    // retorna a competição do resultado
    public Competicao getCompeticao() {
        return competicao;
    }

    // atualiza a competição do resultado
    public void setCompeticao(Competicao competicao) {
        this.competicao = competicao;
    }

    // retorna o primeiro colocado
    public Atleta getPrimeiroLugar() {
        return primeiroLugar;
    }

    // atualiza o primeiro colocado
    public void setPrimeiroLugar(Atleta primeiroLugar) {
        this.primeiroLugar = primeiroLugar;
    }

    // retorna o segundo colocado
    public Atleta getSegundoLugar() {
        return segundoLugar;
    }

    // atualiza o segundo colocado
    public void setSegundoLugar(Atleta segundoLugar) {
        this.segundoLugar = segundoLugar;
    }

    // retorna o terceiro colocado
    public Atleta getTerceiroLugar() {
        return terceiroLugar;
    }

    // atualiza o terceiro colocado
    public void setTerceiroLugar(Atleta terceiroLugar) {
        this.terceiroLugar = terceiroLugar;
    }
}
