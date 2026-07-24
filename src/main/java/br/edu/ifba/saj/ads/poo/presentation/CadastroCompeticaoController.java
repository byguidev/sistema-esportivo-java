package br.edu.ifba.saj.ads.poo.presentation;

// controller da tela de cadastro/edição de competições

import br.edu.ifba.saj.ads.poo.business.ServicoAtividadesEsportivas;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

// controla o formulário de competição (criação e edição)
public class CadastroCompeticaoController {
    // serviço de regras de negócio
    ServicoAtividadesEsportivas servico;
    // competição em edição (null quando é um cadastro novo)
    private Competicao competicaoEditando;

    // componentes do formulário
    @FXML private TextField nome;
    @FXML private DatePicker data;
    @FXML private Spinner<Integer> limiteParticipantes;
    @FXML private Button submitBtn;

    // configura o spinner de limite (1 a 100, começando em 1)
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        limiteParticipantes.setValueFactory(valueFactory);
    }

    // injeta o serviço vindo do controller principal
    public void setServico(ServicoAtividadesEsportivas servico) {
        this.servico = servico;
    }

    // prepara o controller para editar uma competição existente
    public void setCompeticaoEditando(Competicao competicao) {
        this.competicaoEditando = competicao;
        if (competicao != null) {
            // preenche os campos com os dados atuais
            nome.setText(competicao.getNome());
            data.setValue(competicao.getData());
            limiteParticipantes.getValueFactory().setValue(competicao.getLimite());
            submitBtn.setText("Salvar Alterações");
        }
    }

    // salva uma competição nova ou atualiza a competição em edição
    public void salvarCompeticao() throws Exception {
        if (competicaoEditando == null) {
            // fluxo de criação
            Competicao novaCompeticao = new Competicao(nome.getText(), data.getValue(), limiteParticipantes.getValue());
            servico.criarCompeticao(novaCompeticao);
            return;
        }

        // fluxo de edição
        competicaoEditando.setNome(nome.getText());
        competicaoEditando.setData(data.getValue());
        competicaoEditando.setLimite(limiteParticipantes.getValue());
        servico.atualizarCompeticao(competicaoEditando);
    }

    // ação do botão submit: valida, salva e limpa ou fecha a tela
    public void onSubmit() {
        try {
            salvarCompeticao();
            MainController.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Operação Realizada", "A competição foi salva com sucesso!");
            if (competicaoEditando != null) {
                fecharJanela();
            } else {
                // reseta os campos para permitir novo cadastro em sequência
                nome.clear();
                data.setValue(null);
                limiteParticipantes.getValueFactory().setValue(1);
            }
            competicaoEditando = null;
        } catch(Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de validação", "Não foi possível salvar", e.getMessage());
        }
    }

    // fecha a janela atual
    private void fecharJanela() {
        ((Node) submitBtn).getScene().getWindow().hide();
    }
}
