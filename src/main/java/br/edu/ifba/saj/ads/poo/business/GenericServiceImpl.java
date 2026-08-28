package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.GenericDAO;
import br.edu.ifba.saj.ads.poo.model.AbstractModel;
import java.util.List;

public abstract class GenericServiceImpl<T extends AbstractModel<ID>, ID> implements GenericService<T, ID> {
    protected final GenericDAO<T, ID> dao;

    protected GenericServiceImpl(GenericDAO<T, ID> dao) {
        this.dao = dao;
    }

    @Override
    public ID salvar(T entidade) throws RegraDeNegocioException {
        validar(entidade);
        return dao.salvar(entidade);
    }

    @Override
    public void atualizar(T entidade) throws RegraDeNegocioException {
        validar(entidade);
        dao.atualizar(entidade);
    }

    @Override
    public T buscarPorId(ID id) {
        return dao.buscarPorId(id);
    }

    @Override
    public void deletar(ID id) {
        dao.deletar(id);
    }

    @Override
    public List<T> buscarTodos() {
        return dao.buscarTodos();
    }
}
