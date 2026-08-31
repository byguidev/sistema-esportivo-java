package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.GenericDAO;
import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import br.edu.ifba.saj.ads.poo.model.Resultado;

import java.util.List;
import java.util.Objects;

public class ResultadoService extends GenericServiceImpl<Resultado, Long> implements ResultadoServiceI {

    public ResultadoService(GenericDAO<Resultado, Long> dao) {
        super(dao);
    }

    @Override
    public void removerPorCompeticao(Competicao competicao) {
        if (competicao == null) {
            return;
        }

        List<Resultado> daCompeticao = dao.buscarTodos().stream()
            .filter(r -> r.getCompeticao().getId().equals(competicao.getId()))
            .toList();

        daCompeticao.forEach(r -> dao.deletar(r.getId()));
    }

    @Override
    public void removerPorAtleta(Atleta atleta) {
        if (atleta == null) {
            return;
        }

        // sem um dos colocados o pódio fica incompleto, então sai inteiro
        List<Resultado> comOAtleta = dao.buscarTodos().stream()
            .filter(r -> r.getPrimeiroLugar().getId().equals(atleta.getId())
                || r.getSegundoLugar().getId().equals(atleta.getId())
                || r.getTerceiroLugar().getId().equals(atleta.getId()))
            .toList();

        comOAtleta.forEach(r -> dao.deletar(r.getId()));
    }

    @Override
    public void validar(Resultado resultado) throws RegraDeNegocioException {
        // precisa ter resultado e competição associada
        if (resultado == null || resultado.getCompeticao() == null) {
            throw new RegraDeNegocioException("É obrigatório selecionar uma competição.");
        }

        // verifica se já existe resultado para esta competição, ignorando o próprio
        // registro para não travar a edição
        boolean jaExiste = dao.buscarTodos().stream()
            .anyMatch(r -> r.getCompeticao().getId().equals(resultado.getCompeticao().getId())
                && !Objects.equals(r.getId(), resultado.getId()));

        if (jaExiste) {
            throw new RegraDeNegocioException("Já existe um resultado cadastrado para essa competição.");
        }
    }
}
