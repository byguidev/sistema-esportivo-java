package br.edu.ifba.saj.ads.poo.controller;

// controller da tela de cadastro/edição de atletas

import br.edu.ifba.saj.ads.poo.business.ServicoAtividadesEsportivas;
import br.edu.ifba.saj.ads.poo.model.Atleta;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

// controla o formulário de atleta (criação e edição)
public class CadastroAtletaController {
    // serviço usado para aplicar regras de negócio
    ServicoAtividadesEsportivas servico;
    // atleta que está sendo editado (null quando é um cadastro novo)
    private Atleta atletaEditando;

    // campos do formulário
    @FXML private TextField nome;
    @FXML private TextField categoria;
    @FXML private Button submitBtn;

    // injeta o serviço vindo do controller principal
    public void setServico(ServicoAtividadesEsportivas servico) {
        this.servico = servico;
    }

    // prepara o controller para editar um atleta já existente
    public void setAtletaEditando(Atleta atleta) {
        this.atletaEditando = atleta;
        if (atleta != null) {
            // preenche os campos com os dados atuais
            nome.setText(atleta.getNome());
            categoria.setText(atleta.getCategoria());
            submitBtn.setText("Salvar Alterações");
        }
    }

    // salva um atleta novo ou atualiza o atleta em edição
    public void salvarAtleta() throws Exception {
        if (atletaEditando == null) {
            // fluxo de criação: delega ao serviço após construir o objeto
            Atleta novoAtleta = new Atleta(nome.getText(), categoria.getText());
            servico.criarAtleta(novoAtleta);
            return;
        }

        // fluxo de edição: reaproveita o objeto e pede atualização
        atletaEditando.setNome(nome.getText());
        atletaEditando.setCategoria(categoria.getText());
        servico.atualizarAtleta(atletaEditando);
    }

    // ação do botão submit: tenta salvar e mostra feedback ao usuário
    public void onSubmit() {
        try {
            salvarAtleta();
            MainController.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Operação Realizada", "O atleta foi salvo com sucesso!");
            if (atletaEditando != null) {
                // fecha a janela se era uma edição
                fecharJanela();
            } else {
                // limpa os campos para um novo cadastro
                nome.clear();
                categoria.clear();
            }
            atletaEditando = null;
        } catch(Exception e) {
            MainController.exibirAlerta(Alert.AlertType.ERROR, "Erro de validação", "Não foi possível salvar", e.getMessage());
        }
    }

    // fecha a janela atual reaproveitando a referência do botão
    private void fecharJanela() {
        ((Node) submitBtn).getScene().getWindow().hide();
    }
}
