package br.edu.ifba.saj.ads.poo.presentation;

import br.edu.ifba.saj.ads.poo.business.GenericService;
import br.edu.ifba.saj.ads.poo.model.Perfil;
import br.edu.ifba.saj.ads.poo.model.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CadastroUsuarioController {

    GenericService<Usuario, Long> servico;
    private Usuario usuarioEditando;

    @FXML private TextField login;
    @FXML private PasswordField senha;
    @FXML private ComboBox<Perfil> comboBoxPerfil;
    @FXML private Button submitBtn;

    public void setServico(GenericService<Usuario, Long> servico) {
        this.servico = servico;
    }

    @FXML public void initialize() {
        comboBoxPerfil.setItems(FXCollections.observableArrayList(Perfil.values()));
    }

    public void setUsuarioEditando(Usuario usuario) {
        this.usuarioEditando = usuario;
        if (usuario != null) {
            login.setText(usuario.getLogin());
            senha.setText(usuario.getSenha());
            comboBoxPerfil.setValue(usuario.getPerfil());
            submitBtn.setText("Salvar Alterações");
        }
    }

    private void salvarUsuario() throws Exception {
        if (usuarioEditando == null) {
            // novo cadastro
            Usuario novoUsuario = new Usuario(login.getText(), senha.getText(), comboBoxPerfil.getValue());
            servico.salvar(novoUsuario);
            return;
        }

        // atualizar existente
        usuarioEditando.setLogin(login.getText());
        usuarioEditando.setSenha(senha.getText());
        usuarioEditando.setPerfil(comboBoxPerfil.getValue());
        servico.atualizar(usuarioEditando);
    }

    @FXML private void onSubmit() {
        try {
            salvarUsuario();
            MainController.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso",
                "Operação Realizada", "O usuário foi salvo com sucesso!");

            if (usuarioEditando != null) {
                fecharJanela();
            } else {
                login.clear();
                senha.clear();
                comboBoxPerfil.setValue(null);
            }
            usuarioEditando = null;
        } catch (Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de validação",
                "Não foi possível salvar", e.getMessage());
        }
    }

    private void fecharJanela() {
        ((Node) submitBtn).getScene().getWindow().hide();
    }
}
