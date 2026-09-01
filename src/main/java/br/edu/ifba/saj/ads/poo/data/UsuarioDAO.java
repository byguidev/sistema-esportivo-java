package br.edu.ifba.saj.ads.poo.data;

import br.edu.ifba.saj.ads.poo.model.Usuario;

public class UsuarioDAO extends GenericDAOImpl<Usuario, Long> {

    public UsuarioDAO() {
        super(Long.class);
    }
}
