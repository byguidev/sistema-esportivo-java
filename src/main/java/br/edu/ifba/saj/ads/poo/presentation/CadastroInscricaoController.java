package br.edu.ifba.saj.ads.poo.presentation;

// controller da tela de inscrição de atletas em competições

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import br.edu.ifba.saj.ads.poo.model.*;
import br.edu.ifba.saj.ads.poo.business.ServicoAtividadesEsportivas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

// controla a tela que associa um atleta a uma competição
public class CadastroInscricaoController {
    // serviço de regras de negócio
    ServicoAtividadesEsportivas servico;

    // combos usados para escolher atleta e competição
    @FXML ComboBox<Atleta> comboBoxAtleta;
    @FXML ComboBox<Competicao> comboBoxCompeticao;

    // injeta o serviço e já popula os combos
    public void setServico(ServicoAtividadesEsportivas servico) {
        this.servico = servico;
        carregarDados();
    }

    // preenche os combos com atletas e competições do repositório
    public void carregarDados() {
        if (servico == null) {
            return;
        }

        ObservableList<Atleta> obsAtleta = FXCollections.observableArrayList(servico.listarAtletas());
        ObservableList<Competicao> obsCompeticao = FXCollections.observableArrayList(servico.listarCompeticoes());

        comboBoxAtleta.setItems(obsAtleta);
        comboBoxCompeticao.setItems(obsCompeticao);
    }

    // monta a inscrição a partir dos combos e delega ao serviço
    private void salvarInscricao() throws Exception {
        Inscricao novaInscricao = new Inscricao(comboBoxAtleta.getValue(), comboBoxCompeticao.getValue());
        servico.criarInscricao(novaInscricao);
    }

    // ação do botão submit: tenta salvar e exibe feedback
    @FXML private void onSubmit() {
        try {
            salvarInscricao();
            MainController.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Inscrição Realizada", "A inscrição foi salva com sucesso!");
            // limpa os combos para uma nova inscrição
            comboBoxAtleta.setValue(null);
            comboBoxCompeticao.setValue(null);
        } catch(Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de validação", "Não foi possível salvar", e.getMessage());
        }
    }
}
