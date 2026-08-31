package br.edu.ifba.saj.ads.poo.presentation;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import br.edu.ifba.saj.ads.poo.model.*;
import br.edu.ifba.saj.ads.poo.business.GenericService;
import br.edu.ifba.saj.ads.poo.business.InscricaoServiceI;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class CadastroResultadoController {

    GenericService<Competicao, Long> competicaoService;
    InscricaoServiceI inscricaoService;
    GenericService<Resultado, Long> resultadoService;
    private Resultado resultadoEditando;

    @FXML ComboBox<Competicao> comboBoxCompeticao;
    @FXML ComboBox<Inscricao> primeiroLugar;
    @FXML ComboBox<Inscricao> segundoLugar;
    @FXML ComboBox<Inscricao> terceiroLugar;

    @FXML public void initialize() {
        // recarrega inscrições quando competição muda
        comboBoxCompeticao.valueProperty().addListener((observable, oldValue, newValue) ->
            carregarInscricoes(newValue));
    }

    public void setCompeticaoService(GenericService<Competicao, Long> competicaoService) {
        this.competicaoService = competicaoService;
        carregarDados();
    }

    public void setInscricaoService(InscricaoServiceI inscricaoService) {
        this.inscricaoService = inscricaoService;
        carregarDados();
    }

    public void setResultadoService(GenericService<Resultado, Long> resultadoService) {
        this.resultadoService = resultadoService;
    }

    public void setResultadoEditando(Resultado resultado) {
        this.resultadoEditando = resultado;
        if (resultado != null) {
            comboBoxCompeticao.setValue(resultado.getCompeticao());
            carregarInscricoes(resultado.getCompeticao());

            var inscritos = inscricaoService.buscarPorCompeticao(resultado.getCompeticao());

            primeiroLugar.setValue(inscritos.stream()
                .filter(i -> i.getAtleta().getId().equals(resultado.getPrimeiroLugar().getId()))
                .findFirst().orElse(null));
            segundoLugar.setValue(inscritos.stream()
                .filter(i -> i.getAtleta().getId().equals(resultado.getSegundoLugar().getId()))
                .findFirst().orElse(null));
            terceiroLugar.setValue(inscritos.stream()
                .filter(i -> i.getAtleta().getId().equals(resultado.getTerceiroLugar().getId()))
                .findFirst().orElse(null));
        }
    }

    public void carregarDados() {
        if (competicaoService == null || inscricaoService == null) {
            return;
        }

        ObservableList<Competicao> obsCompeticao = FXCollections.observableArrayList(
            competicaoService.buscarTodos());
        comboBoxCompeticao.setItems(obsCompeticao);
        carregarInscricoes(comboBoxCompeticao.getValue());
    }

    private void carregarInscricoes(Competicao competicao) {
        if (inscricaoService == null || competicao == null) {
            primeiroLugar.setItems(FXCollections.observableArrayList());
            segundoLugar.setItems(FXCollections.observableArrayList());
            terceiroLugar.setItems(FXCollections.observableArrayList());
            primeiroLugar.setValue(null);
            segundoLugar.setValue(null);
            terceiroLugar.setValue(null);
            return;
        }

        ObservableList<Inscricao> inscricoesDaCompeticao = FXCollections.observableArrayList(
            inscricaoService.buscarPorCompeticao(competicao));

        primeiroLugar.setItems(inscricoesDaCompeticao);
        segundoLugar.setItems(inscricoesDaCompeticao);
        terceiroLugar.setItems(inscricoesDaCompeticao);

        primeiroLugar.setValue(null);
        segundoLugar.setValue(null);
        terceiroLugar.setValue(null);
    }

    private void salvarResultado() throws Exception {
        if (comboBoxCompeticao.getValue() == null || primeiroLugar.getValue() == null ||
            segundoLugar.getValue() == null || terceiroLugar.getValue() == null) {
            throw new Exception("Selecione a competição e os três colocados.");
        }

        long primeiroId = primeiroLugar.getValue().getAtleta().getId();
        long segundoId = segundoLugar.getValue().getAtleta().getId();
        long terceiroId = terceiroLugar.getValue().getAtleta().getId();

        // verifica atletas distintos
        if (primeiroId == segundoId || primeiroId == terceiroId || segundoId == terceiroId) {
            throw new Exception("Os três colocados precisam ser atletas diferentes.");
        }

        if (resultadoEditando == null) {
            Resultado novoResultado = new Resultado(
                comboBoxCompeticao.getValue(),
                primeiroLugar.getValue().getAtleta(),
                segundoLugar.getValue().getAtleta(),
                terceiroLugar.getValue().getAtleta());
            resultadoService.salvar(novoResultado);
            return;
        }

        resultadoEditando.setCompeticao(comboBoxCompeticao.getValue());
        resultadoEditando.setPrimeiroLugar(primeiroLugar.getValue().getAtleta());
        resultadoEditando.setSegundoLugar(segundoLugar.getValue().getAtleta());
        resultadoEditando.setTerceiroLugar(terceiroLugar.getValue().getAtleta());
        resultadoService.atualizar(resultadoEditando);
    }

    @FXML private void onSubmit() {
        try {
            salvarResultado();
            MainController.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso",
                "Resultado Realizado", "O resultado foi salvo com sucesso!");

            if (resultadoEditando != null) {
                fecharJanela();
            } else {
                comboBoxCompeticao.setValue(null);
                primeiroLugar.setValue(null);
                segundoLugar.setValue(null);
                terceiroLugar.setValue(null);
            }
            resultadoEditando = null;
        } catch(Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de validação",
                "Não foi possível salvar", e.getMessage());
        }
    }

    private void fecharJanela() {
        ((Node) comboBoxCompeticao).getScene().getWindow().hide();
    }
}
