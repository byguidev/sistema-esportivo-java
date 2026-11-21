package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.model.Usuario;

public interface UsuarioServiceI extends GenericService<Usuario, Long> {

    // devolve o usuário se login e senha baterem, ou null
    Usuario autenticar(String login, String senha);
}
