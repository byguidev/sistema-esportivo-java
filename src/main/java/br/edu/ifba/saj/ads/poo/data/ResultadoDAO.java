package br.edu.ifba.saj.ads.poo.data;

import br.edu.ifba.saj.ads.poo.model.Resultado;

// DAO para gerenciar os resultados das competições
public class ResultadoDAO extends GenericDAOImpl<Resultado, Long> {

    public ResultadoDAO() {
        super(Long.class);
    }
}
