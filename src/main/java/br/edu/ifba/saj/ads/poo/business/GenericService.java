package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.model.AbstractModel;
import java.util.List;

// contrato da comunicação Apresentação -> Negócio: espelha o GenericDAO e acrescenta a validação
public interface GenericService<T extends AbstractModel<ID>, ID> {
    ID salvar(T entidade) throws RegraDeNegocioException;
    void atualizar(T entidade) throws RegraDeNegocioException;
    T buscarPorId(ID id);
    void deletar(ID id);
    List<T> buscarTodos();
    void validar(T entidade) throws RegraDeNegocioException;
}
