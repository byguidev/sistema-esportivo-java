package br.edu.ifba.saj.ads.poo.data;

import br.edu.ifba.saj.ads.poo.model.Atleta;

public class AtletaDAO extends GenericDAOImpl<Atleta, Long> {

    public AtletaDAO() {
        super(Long.class);
    }
}
