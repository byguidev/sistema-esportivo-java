package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import br.edu.ifba.saj.ads.poo.model.Resultado;

// interface específica para o serviço de resultados
public interface ResultadoServiceI extends GenericService<Resultado, Long> {

    // usados quando a competição ou o atleta são excluídos
    void removerPorCompeticao(Competicao competicao);

    void removerPorAtleta(Atleta atleta);
}
