package br.edu.ifba.saj.ads.poo.presentation;

import br.edu.ifba.saj.ads.poo.business.FabricaDeServicos;
import br.edu.ifba.saj.ads.poo.business.UsuarioServiceI;
import br.edu.ifba.saj.ads.poo.model.Usuario;
import br.edu.ifba.saj.ads.poo.util.SessaoUsuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    // usa a mesma fábrica de sempre, assim o usuário validado aqui é o mesmo que existe nas outras telas
    private final FabricaDeServicos fabrica = FabricaDeServicos.getInstance();
    private final UsuarioServiceI usuarioService = fabrica.getUsuarioService();

    @FXML private TextField login;
    @FXML private PasswordField senha;

    // confere usuário e senha; se bater, abre a tela principal no lugar da tela de login
    @FXML private void onEntrar() {
        Usuario usuario = usuarioService.autenticar(login.getText(), senha.getText());

        if (usuario == null) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de login",
                "Não foi possível entrar", "Login ou senha inválidos.");
            return;
        }

        SessaoUsuario.logar(usuario);
        abrirTelaPrincipal();
    }

    private void abrirTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/Main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) login).getScene().getWindow();
            stage.setTitle("Sistema Esportivo");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro",
                "Não foi possível abrir o sistema", e.getMessage());
        }
    }
}
