package br.edu.ifba.saj.ads.poo.presentation;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import br.edu.ifba.saj.ads.poo.model.*;
import br.edu.ifba.saj.ads.poo.business.GenericService;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class CadastroInscricaoController {

    GenericService<Atleta, Long> atletaService;
    GenericService<Competicao, Long> competicaoService;
    GenericService<Inscricao, Long> inscricaoService;
    private Inscricao inscricaoEditando;

    @FXML ComboBox<Atleta> comboBoxAtleta;
    @FXML ComboBox<Competicao> comboBoxCompeticao;
    @FXML private Button submitBtn;

    public void setAtletaService(GenericService<Atleta, Long> atletaService) {
        this.atletaService = atletaService;
        carregarDados();
    }

    public void setCompeticaoService(GenericService<Competicao, Long> competicaoService) {
        this.competicaoService = competicaoService;
        carregarDados();
    }

    public void setInscricaoService(GenericService<Inscricao, Long> inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    public void setInscricaoEditando(Inscricao inscricao) {
        this.inscricaoEditando = inscricao;
        if (inscricao != null) {
            comboBoxAtleta.setValue(inscricao.getAtleta());
            comboBoxCompeticao.setValue(inscricao.getCompeticao());
            submitBtn.setText("Salvar Alterações");
        }
    }

    public void carregarDados() {
        if (atletaService == null || competicaoService == null) {
            return;
        }

        ObservableList<Atleta> obsAtleta = FXCollections.observableArrayList(
            atletaService.buscarTodos());
        ObservableList<Competicao> obsCompeticao = FXCollections.observableArrayList(
            competicaoService.buscarTodos());

        comboBoxAtleta.setItems(obsAtleta);
        comboBoxCompeticao.setItems(obsCompeticao);
    }

    private void salvarInscricao() throws Exception {
        if (inscricaoEditando == null) {
            // nova inscrição
            Inscricao novaInscricao = new Inscricao(comboBoxAtleta.getValue(),
                comboBoxCompeticao.getValue());
            inscricaoService.salvar(novaInscricao);
            return;
        }

        // atualizar existente
        inscricaoEditando.setAtleta(comboBoxAtleta.getValue());
        inscricaoEditando.setCompeticao(comboBoxCompeticao.getValue());
        inscricaoService.atualizar(inscricaoEditando);
    }

    @FXML private void onSubmit() {
        try {
            salvarInscricao();
            MainController.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso",
                "Inscrição Realizada", "A inscrição foi salva com sucesso!");

            if (inscricaoEditando != null) {
                fecharJanela();
            } else {
                comboBoxAtleta.setValue(null);
                comboBoxCompeticao.setValue(null);
            }
            inscricaoEditando = null;
        } catch(Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de validação",
                "Não foi possível salvar", e.getMessage());
        }
    }

    private void fecharJanela() {
        ((Node) submitBtn).getScene().getWindow().hide();
    }
}
