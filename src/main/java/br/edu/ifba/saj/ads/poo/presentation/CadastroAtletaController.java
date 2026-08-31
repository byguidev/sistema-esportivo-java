package br.edu.ifba.saj.ads.poo.presentation;

import br.edu.ifba.saj.ads.poo.business.GenericService;
import br.edu.ifba.saj.ads.poo.model.Atleta;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CadastroAtletaController {

    GenericService<Atleta, Long> servico;
    private Atleta atletaEditando;

    @FXML private TextField nome;
    @FXML private TextField categoria;
    @FXML private Button submitBtn;

    public void setServico(GenericService<Atleta, Long> servico) {
        this.servico = servico;
    }

    public void setAtletaEditando(Atleta atleta) {
        this.atletaEditando = atleta;
        if (atleta != null) {
            nome.setText(atleta.getNome());
            categoria.setText(atleta.getCategoria());
            submitBtn.setText("Salvar Alterações");
        }
    }

    public void salvarAtleta() throws Exception {
        if (atletaEditando == null) {
            // novo cadastro
            Atleta novoAtleta = new Atleta(nome.getText(), categoria.getText());
            servico.salvar(novoAtleta);
            return;
        }

        // atualizar existente
        atletaEditando.setNome(nome.getText());
        atletaEditando.setCategoria(categoria.getText());
        servico.atualizar(atletaEditando);
    }

    public void onSubmit() {
        try {
            salvarAtleta();
            MainController.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso",
                "Operação Realizada", "O atleta foi salvo com sucesso!");

            if (atletaEditando != null) {
                fecharJanela();
            } else {
                nome.clear();
                categoria.clear();
            }
            atletaEditando = null;
        } catch(Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de validação",
                "Não foi possível salvar", e.getMessage());
        }
    }

    private void fecharJanela() {
        ((Node) submitBtn).getScene().getWindow().hide();
    }
}
