package br.edu.ifba.saj.ads.poo.util;

import br.edu.ifba.saj.ads.poo.model.Usuario;

// guarda o usuário logado no momento (é um app desktop de um usuário só por vez, então um "estático" resolve)
public class SessaoUsuario {
    private static Usuario usuarioLogado;

    private SessaoUsuario() {
    }

    public static void logar(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void deslogar() {
        usuarioLogado = null;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    // "sistema" cobre o que roda antes do login, como a criação do admin inicial
    public static String getLoginAtual() {
        return usuarioLogado != null ? usuarioLogado.getLogin() : "sistema";
    }
}
