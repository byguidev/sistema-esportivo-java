package br.edu.ifba.saj.ads.poo.model;

// classe que representa um usuário do sistema (login e controle de acesso)
public class Usuario extends AbstractModel<Long> {
    // login usado para entrar no sistema
    private String login;
    // senha do usuário (texto puro, projeto simples pra fins acadêmicos)
    private String senha;
    // perfil de acesso: COMUM ou ADMIN
    private Perfil perfil;

    // construtor que cria um usuário e chama o construtor da superclasse
    public Usuario(String login, String senha, Perfil perfil) {
        super();
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    // retorna o login do usuário
    public String getLogin() {
        return login;
    }

    // atualiza o login do usuário
    public void setLogin(String login) {
        this.login = login;
    }

    // retorna a senha do usuário
    public String getSenha() {
        return senha;
    }

    // atualiza a senha do usuário
    public void setSenha(String senha) {
        this.senha = senha;
    }

    // retorna o perfil do usuário
    public Perfil getPerfil() {
        return perfil;
    }

    // atualiza o perfil do usuário
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    // representação textual usada em listas e tabelas (mostra o login)
    @Override
    public String toString() {
        return this.getLogin();
    }
}
