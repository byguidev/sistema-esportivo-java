package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import br.edu.ifba.saj.ads.poo.model.Inscricao;

import java.util.List;

// interface específica para o serviço de inscrições
public interface InscricaoServiceI extends GenericService<Inscricao, Long> {

    List<Inscricao> buscarPorCompeticao(Competicao competicao);

    List<Inscricao> buscarPorAtleta(Atleta atleta);

    // usados quando a competição ou o atleta são excluídos
    void removerPorCompeticao(Competicao competicao);

    void removerPorAtleta(Atleta atleta);
}
