package br.edu.ifba.saj.ads.poo.presentation;

// controller da tela de cadastro/edição de resultados (pódio)

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import br.edu.ifba.saj.ads.poo.model.*;
import br.edu.ifba.saj.ads.poo.business.ServicoAtividadesEsportivas;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

// controla o formulário de resultado com a escolha de competição e três colocados
public class CadastroResultadoController {
    // serviço de regras de negócio
    ServicoAtividadesEsportivas servico;
    // resultado em edição (null quando é um cadastro novo)
    private Resultado resultadoEditando;

    // combos do formulário
    @FXML ComboBox<Competicao> comboBoxCompeticao;
    @FXML ComboBox<Inscricao> primeiroLugar;
    @FXML ComboBox<Inscricao> segundoLugar;
    @FXML ComboBox<Inscricao> terceiroLugar;

    // observa mudanças no combo de competição para recarregar as inscrições
    @FXML public void initialize() {
        comboBoxCompeticao.valueProperty().addListener((observable, oldValue, newValue) -> carregarInscricoes(newValue));
    }

    // injeta o serviço e carrega a lista de competições
    public void setServico(ServicoAtividadesEsportivas servico) {
        this.servico = servico;
        carregarDados();
    }

    // prepara o controller para editar um resultado existente
    public void setResultadoEditando(Resultado resultado) {
        this.resultadoEditando = resultado;
        if (resultado != null) {
            // seleciona a competição e tenta selecionar os atletas já definidos
            comboBoxCompeticao.setValue(resultado.getCompeticao());
            carregarInscricoes(resultado.getCompeticao());
            primeiroLugar.setValue(servico.listarInscricoesDaCompeticao(resultado.getCompeticao()).stream().filter(i -> i.getAtleta().getId() == resultado.getPrimeiroLugar().getId()).findFirst().orElse(null));
            segundoLugar.setValue(servico.listarInscricoesDaCompeticao(resultado.getCompeticao()).stream().filter(i -> i.getAtleta().getId() == resultado.getSegundoLugar().getId()).findFirst().orElse(null));
            terceiroLugar.setValue(servico.listarInscricoesDaCompeticao(resultado.getCompeticao()).stream().filter(i -> i.getAtleta().getId() == resultado.getTerceiroLugar().getId()).findFirst().orElse(null));
        }
    }

    // popula o combo de competições e dispara o carregamento das inscrições
    public void carregarDados() {
        if (servico == null) {
            return;
        }

        ObservableList<Competicao> obsCompeticao = FXCollections.observableArrayList(servico.listarCompeticoes());

        comboBoxCompeticao.setItems(obsCompeticao);

        carregarInscricoes(comboBoxCompeticao.getValue());
    }

    // recarrega os combos dos colocados com base na competição selecionada
    private void carregarInscricoes(Competicao competicao) {
        if (servico == null || competicao == null) {
            // limpa os combos quando não há competição selecionada
            primeiroLugar.setItems(FXCollections.observableArrayList());
            segundoLugar.setItems(FXCollections.observableArrayList());
            terceiroLugar.setItems(FXCollections.observableArrayList());
            primeiroLugar.setValue(null);
            segundoLugar.setValue(null);
            terceiroLugar.setValue(null);
            return;
        }

        // mostra apenas inscrições da competição escolhida
        ObservableList<Inscricao> inscricoesDaCompeticao = FXCollections.observableArrayList(servico.listarInscricoesDaCompeticao(competicao));

        primeiroLugar.setItems(inscricoesDaCompeticao);
        segundoLugar.setItems(inscricoesDaCompeticao);
        terceiroLugar.setItems(inscricoesDaCompeticao);

        primeiroLugar.setValue(null);
        segundoLugar.setValue(null);
        terceiroLugar.setValue(null);
    }

    // validações e salvamento do resultado (criação ou edição)
    private void salvarResultado() throws Exception {
        // exige todos os campos preenchidos
        if (comboBoxCompeticao.getValue() == null || primeiroLugar.getValue() == null || segundoLugar.getValue() == null || terceiroLugar.getValue() == null) {
            throw new Exception("Selecione a competição e os três colocados.");
        }

        // garante que os três colocados sejam atletas distintos
        long primeiroId = primeiroLugar.getValue().getAtleta().getId();
        long segundoId = segundoLugar.getValue().getAtleta().getId();
        long terceiroId = terceiroLugar.getValue().getAtleta().getId();

        if (primeiroId == segundoId || primeiroId == terceiroId || segundoId == terceiroId) {
            throw new Exception("Os três colocados precisam ser atletas diferentes.");
        }

        if (resultadoEditando == null) {
            // fluxo de criação
            Resultado novoResultado = new Resultado(
                comboBoxCompeticao.getValue(),
                primeiroLugar.getValue().getAtleta(),
                segundoLugar.getValue().getAtleta(),
                terceiroLugar.getValue().getAtleta());
            servico.criarResultado(novoResultado);
            return;
        }

        // fluxo de edição
        resultadoEditando.setCompeticao(comboBoxCompeticao.getValue());
        resultadoEditando.setPrimeiroLugar(primeiroLugar.getValue().getAtleta());
        resultadoEditando.setSegundoLugar(segundoLugar.getValue().getAtleta());
        resultadoEditando.setTerceiroLugar(terceiroLugar.getValue().getAtleta());
        servico.atualizarResultado(resultadoEditando);
    }

    // ação do botão submit: valida, salva e limpa ou fecha a tela
    @FXML private void onSubmit() {
        try {
            salvarResultado();
            MainController.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Resultado Realizado", "O resultado foi salvo com sucesso!");
            if (resultadoEditando != null) {
                fecharJanela();
            } else {
                // reseta os combos para permitir novo cadastro
                comboBoxCompeticao.setValue(null);
                primeiroLugar.setValue(null);
                segundoLugar.setValue(null);
                terceiroLugar.setValue(null);
            }
            resultadoEditando = null;
        } catch(Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de validação", "Não foi possível salvar", e.getMessage());
        }
    }

    // fecha a janela atual reaproveitando o combo de competição
    private void fecharJanela() {
        ((Node) comboBoxCompeticao).getScene().getWindow().hide();
    }
}
