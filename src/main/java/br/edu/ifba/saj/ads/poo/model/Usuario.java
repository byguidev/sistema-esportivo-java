package br.edu.ifba.saj.ads.poo.model;

// usuário do sistema, usado no login e no controle de acesso
public class Usuario extends AbstractModel<Long> {
    private String login;
    private String senha;
    private Perfil perfil;

    public Usuario(String login, String senha, Perfil perfil) {
        super();
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    // é o toString que o JavaFX usa para renderizar o usuário em tabelas e combos
    @Override
    public String toString() {
        return this.getLogin();
    }
}
