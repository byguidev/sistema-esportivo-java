package br.edu.ifba.saj.ads.poo.data;

import br.edu.ifba.saj.ads.poo.model.AbstractModel;
import br.edu.ifba.saj.ads.poo.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class GenericDAOImpl<T extends AbstractModel<ID>, ID> implements GenericDAO<T, ID> {
    protected final Map<ID, T> banco = new HashMap<>();

    protected final IdGenerator idGenerator = new IdGenerator();

    private final Class<ID> tipoId;

    public GenericDAOImpl(Class<ID> tipoId) {
        this.tipoId = tipoId;
    }
    
    @Override
    public ID salvar(T entidade) {
        ID id = entidade.getId();

        // entidade nova: ganha id e data de criação
        if (id == null) {
            id = idGenerator.gerarNovoId(tipoId);
            entidade.setId(id);
            entidade.setCreatedAt(LocalDateTime.now());
        }

        entidade.setUpdatedAt(LocalDateTime.now());

        banco.put(id, entidade);
        return id;
    }

    @Override
    public void atualizar(T entidade) {
        if (!banco.containsKey(entidade.getId())) {
            throw new NoSuchElementException("Registro não encontrado para atualização.");
        }

        entidade.setUpdatedAt(LocalDateTime.now());
        banco.put(entidade.getId(), entidade);
    }

    @Override
    public T buscarPorId(ID id) {
        return banco.get(id);
    }

    @Override
    public void deletar(ID id) {
        if (!banco.containsKey(id)) {
            throw new NoSuchElementException("Registro não encontrado para remoção.");
        }

        banco.remove(id);
    }

    @Override
    public List<T> buscarTodos() {
        return new ArrayList<>(banco.values());
    }
}
