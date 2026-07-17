package br.edu.ifba.saj.ads.poo.data;

// camada de dados: armazena as entidades em memória usando listas

import br.edu.ifba.saj.ads.poo.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// repositório central que guarda competições, atletas, inscrições e resultados
// as listas ficam em memória e são perdidas quando a aplicação fecha
public class RepositorioAtividades {
    // lista de competições cadastradas
    private List<Competicao> listaCompeticoes = new ArrayList<>();
    // lista de atletas cadastrados
    private List<Atleta> listaAtletas = new ArrayList<>();
    // lista de inscrições realizadas
    private List<Inscricao> listaInscricoes = new ArrayList<>();
    // lista de resultados publicados
    private List<Resultado> listaResultados = new ArrayList<>();

    // adiciona uma nova competição à lista
    public void salvarCompeticao(Competicao c) {
        listaCompeticoes.add(c);
    }

    // substitui uma competição existente pelo id
    public boolean atualizarCompeticao(Competicao competicao) {
        for (int i = 0; i < listaCompeticoes.size(); i++) {
            if (listaCompeticoes.get(i).getId() == competicao.getId()) {
                listaCompeticoes.set(i, competicao);
                return true;
            }
        }

        return false;
    }

    // remove a competição com o id informado
    public boolean removerCompeticao(long id) {
        return listaCompeticoes.removeIf(competicao -> competicao.getId() == id);
    }

    // busca uma competição pelo id retornando um optional
    public Optional<Competicao> buscarCompeticaoPorId(long id) {
        return listaCompeticoes.stream().filter(competicao -> competicao.getId() == id).findFirst();
    }

    // adiciona um novo atleta à lista
    public void salvarAtleta(Atleta a) {
        listaAtletas.add(a);
    }

    // substitui um atleta existente pelo id
    public boolean atualizarAtleta(Atleta atleta) {
        for (int i = 0; i < listaAtletas.size(); i++) {
            if (listaAtletas.get(i).getId() == atleta.getId()) {
                listaAtletas.set(i, atleta);
                return true;
            }
        }

        return false;
    }

    // remove o atleta com o id informado
    public boolean removerAtleta(long id) {
        return listaAtletas.removeIf(atleta -> atleta.getId() == id);
    }

    // busca um atleta pelo id retornando um optional
    public Optional<Atleta> buscarAtletaPorId(long id) {
        return listaAtletas.stream().filter(atleta -> atleta.getId() == id).findFirst();
    }

    // adiciona uma nova inscrição à lista
    public void salvarInscricao(Inscricao i) {
        listaInscricoes.add(i);
    }

    // substitui uma inscrição existente pelo id
    public boolean atualizarInscricao(Inscricao inscricao) {
        for (int i = 0; i < listaInscricoes.size(); i++) {
            if (listaInscricoes.get(i).getId() == inscricao.getId()) {
                listaInscricoes.set(i, inscricao);
                return true;
            }
        }

        return false;
    }

    // remove a inscrição com o id informado
    public boolean removerInscricao(long id) {
        return listaInscricoes.removeIf(inscricao -> inscricao.getId() == id);
    }

    // busca uma inscrição pelo id retornando um optional
    public Optional<Inscricao> buscarInscricaoPorId(long id) {
        return listaInscricoes.stream().filter(inscricao -> inscricao.getId() == id).findFirst();
    }

    // adiciona um novo resultado à lista
    public void salvarResultado(Resultado r) {
        listaResultados.add(r);
    }

    // substitui um resultado existente pelo id
    public boolean atualizarResultado(Resultado resultado) {
        for (int i = 0; i < listaResultados.size(); i++) {
            if (listaResultados.get(i).getId() == resultado.getId()) {
                listaResultados.set(i, resultado);
                return true;
            }
        }

        return false;
    }

    // remove o resultado com o id informado
    public boolean removerResultado(long id) {
        return listaResultados.removeIf(resultado -> resultado.getId() == id);
    }

    // busca um resultado pelo id retornando um optional
    public Optional<Resultado> buscarResultadoPorId(long id) {
        return listaResultados.stream().filter(resultado -> resultado.getId() == id).findFirst();
    }

    // retorna uma cópia da lista de competições para evitar alterações externas
    public List<Competicao> listarCompeticoes() {
        return new ArrayList<>(listaCompeticoes);
    }

    // retorna uma cópia da lista de atletas
    public List<Atleta> listarAtletas() {
        return new ArrayList<>(listaAtletas);
    }

    // retorna uma cópia da lista de inscrições
    public List<Inscricao> listarInscricoes() {
        return new ArrayList<>(listaInscricoes);
    }

    // retorna uma cópia da lista de resultados
    public List<Resultado> listarResultados() {
        return new ArrayList<>(listaResultados);
    }
}
