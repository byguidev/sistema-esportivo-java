package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.model.Usuario;

// interface específica para o serviço de usuários
public interface UsuarioServiceI extends GenericService<Usuario, Long> {

    // confere login e senha; devolve o usuário se bater, ou null se não achar ou a senha estiver errada
    Usuario autenticar(String login, String senha);
}
