package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.GenericDAO;
import br.edu.ifba.saj.ads.poo.model.AbstractModel;

public interface GenericService<T extends AbstractModel<ID>, ID> extends GenericDAO<T, ID> {
    ID salvar(T entidade) throws RegraDeNegocioException;
    void atualizar(T entidade) throws RegraDeNegocioException;
    void validar(T entidade) throws RegraDeNegocioException;
}
