package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.GenericDAO;
import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import br.edu.ifba.saj.ads.poo.model.Inscricao;

import java.util.List;
import java.util.Objects;

public class InscricaoService extends GenericServiceImpl<Inscricao, Long> implements InscricaoServiceI {

    public InscricaoService(GenericDAO<Inscricao, Long> dao) {
        super(dao);
    }

    @Override
    public List<Inscricao> buscarPorCompeticao(Competicao competicao) {
        // se competição for null, retorna lista vazia pra evitar erro
        if (competicao == null) {
            return List.of();
        }

        // filtra todas as inscrições para apenas as da competição especificada
        return dao.buscarTodos().stream()
            .filter(inscricao -> inscricao.getCompeticao().getId().equals(competicao.getId()))
            .toList();
    }

    @Override
    public List<Inscricao> buscarPorAtleta(Atleta atleta) {
        if (atleta == null) {
            return List.of();
        }

        return dao.buscarTodos().stream()
            .filter(inscricao -> inscricao.getAtleta().getId().equals(atleta.getId()))
            .toList();
    }

    @Override
    public void removerPorCompeticao(Competicao competicao) {
        buscarPorCompeticao(competicao).forEach(inscricao -> dao.deletar(inscricao.getId()));
    }

    @Override
    public void removerPorAtleta(Atleta atleta) {
        buscarPorAtleta(atleta).forEach(inscricao -> dao.deletar(inscricao.getId()));
    }

    @Override
    public void validar(Inscricao inscricao) throws RegraDeNegocioException {
        // verifica se tem todos os dados necessários
        if (inscricao == null || inscricao.getAtleta() == null || inscricao.getCompeticao() == null) {
            throw new RegraDeNegocioException("Sem dados válidos para inscrição.");
        }

        // busca as inscrições da competição pra verificar limite, tirando da conta
        // a própria inscrição (senão uma edição bate no registro dela mesma)
        List<Inscricao> outrasInscricoes = buscarPorCompeticao(inscricao.getCompeticao()).stream()
            .filter(i -> !Objects.equals(i.getId(), inscricao.getId()))
            .toList();

        // verifica se ainda há vagas
        if (outrasInscricoes.size() >= inscricao.getCompeticao().getLimite()) {
            throw new RegraDeNegocioException("Competição completa. Não é possível realizar novas inscrições.");
        }

        // verifica se o atleta já está inscrito nesta competição
        boolean jaInscrito = outrasInscricoes.stream()
            .anyMatch(i -> i.getAtleta().getId().equals(inscricao.getAtleta().getId()));

        if (jaInscrito) {
            throw new RegraDeNegocioException("Atleta já inscrito nesta competição.");
        }
    }
}
