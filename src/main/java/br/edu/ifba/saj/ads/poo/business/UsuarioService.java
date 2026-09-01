package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.GenericDAO;
import br.edu.ifba.saj.ads.poo.model.Usuario;
import br.edu.ifba.saj.ads.poo.util.SessaoUsuario;

import java.util.Objects;

public class UsuarioService extends GenericServiceImpl<Usuario, Long> implements UsuarioServiceI {

    public UsuarioService(GenericDAO<Usuario, Long> dao) {
        super(dao);
    }

    @Override
    public Usuario autenticar(String login, String senha) {
        // procura entre os usuários cadastrados um que bata login e senha
        return dao.buscarTodos().stream()
            .filter(usuario -> usuario.getLogin().equals(login) && usuario.getSenha().equals(senha))
            .findFirst()
            .orElse(null);
    }

    @Override
    public void validar(Usuario usuario) throws RegraDeNegocioException {
        if (usuario == null) {
            throw new RegraDeNegocioException("Dados do usuário são obrigatórios.");
        }

        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new RegraDeNegocioException("Login do usuário é obrigatório.");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new RegraDeNegocioException("Senha do usuário é obrigatória.");
        }

        if (usuario.getPerfil() == null) {
            throw new RegraDeNegocioException("Perfil do usuário é obrigatório.");
        }

        // login precisa ser único; ignora o próprio usuário quando é uma edição
        boolean loginJaExiste = dao.buscarTodos().stream()
            .anyMatch(u -> !Objects.equals(u.getId(), usuario.getId())
                && u.getLogin().equalsIgnoreCase(usuario.getLogin()));

        if (loginJaExiste) {
            throw new RegraDeNegocioException("Já existe um usuário com este login.");
        }
    }

    @Override
    public void deletar(Long id) {
        // impede excluir a própria conta enquanto ela está logada, senão a sessão fica inválida
        Usuario logado = SessaoUsuario.getUsuarioLogado();
        if (logado != null && logado.getId().equals(id)) {
            throw new RegraDeNegocioException("Não é possível excluir o usuário logado no momento.");
        }

        super.deletar(id);
    }
}
