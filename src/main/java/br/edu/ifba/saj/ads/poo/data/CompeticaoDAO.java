package br.edu.ifba.saj.ads.poo.data;

import br.edu.ifba.saj.ads.poo.model.Competicao;

// acessa os dados de competições
public class CompeticaoDAO extends GenericDAOImpl<Competicao, Long> {

    public CompeticaoDAO() {
        super(Long.class);
    }
}
