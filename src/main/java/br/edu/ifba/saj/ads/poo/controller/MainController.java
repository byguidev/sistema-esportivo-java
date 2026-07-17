package br.edu.ifba.saj.ads.poo.controller;

// controller principal: tela inicial com tabelas e botões de ação

import br.edu.ifba.saj.ads.poo.business.ServicoAtividadesEsportivas;
import br.edu.ifba.saj.ads.poo.data.RepositorioAtividades;
import br.edu.ifba.saj.ads.poo.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.time.LocalDate;
import java.util.Optional;

// classe que controla a tela principal e coordena as janelas auxiliares
public class MainController {
    // repositório criado em memória (será substituído por camada persistente no futuro)
    RepositorioAtividades repositorio = new RepositorioAtividades();
    // serviço de regras de negócio usado pelas telas
    ServicoAtividadesEsportivas servico = new ServicoAtividadesEsportivas(repositorio);

    // listas observáveis que alimentam as tabelas da interface
    private final ObservableList<Atleta> listaAtletas = FXCollections.observableArrayList();
    private final ObservableList<Competicao> listaCompeticoes = FXCollections.observableArrayList();
    private final ObservableList<Resultado> listaResultados = FXCollections.observableArrayList();

    // componentes da tabela de atletas
    @FXML private TableView<Atleta> tabelaAtleta;
    @FXML private TableColumn<Atleta, String> colAtletaNome;
    @FXML private TableColumn<Atleta, String> colAtletaCategoria;
    @FXML private TableColumn<Atleta, Void> colAtletaAcoes;

    // componentes da tabela de competições
    @FXML private TableView<Competicao> tabelaCompeticao;
    @FXML private TableColumn<Competicao, String> colCompeticaoNome;
    @FXML private TableColumn<Competicao, LocalDate> colCompeticaoData;
    @FXML private TableColumn<Competicao, Integer> colCompeticaoLimite;
    @FXML private TableColumn<Competicao, Void> colCompeticaoAcoes;

    // componentes da tabela de resultados
    @FXML private TableView<Resultado> tabelaResultado;
    @FXML private TableColumn<Resultado, String> colResultadoCompeticao;
    @FXML private TableColumn<Resultado, String> colResultadoPrimeiro;
    @FXML private TableColumn<Resultado, String> colResultadoSegundo;
    @FXML private TableColumn<Resultado, String> colResultadoTerceiro;
    @FXML private TableColumn<Resultado, Void> colResultadoAcoes;

    // exibe um alerta genérico reaproveitado por todos os controllers
    public static void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // inicializa tabelas, colunas e dados ao abrir a tela
    @FXML public void initialize() {
        // vincula as listas observáveis às tabelas
        tabelaAtleta.setItems(listaAtletas);
        tabelaCompeticao.setItems(listaCompeticoes);
        tabelaResultado.setItems(listaResultados);

        // mapeia as colunas simples via reflexão de propriedades
        colAtletaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAtletaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        colCompeticaoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCompeticaoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCompeticaoLimite.setCellValueFactory(new PropertyValueFactory<>("limite"));

        // colunas de resultado precisam navegar nos objetos relacionados
        colResultadoCompeticao.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCompeticao().getNome()));
        colResultadoPrimeiro.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrimeiroLugar().getNome()));
        colResultadoSegundo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSegundoLugar().getNome()));
        colResultadoTerceiro.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTerceiroLugar().getNome()));

        // configura os botões de ação (editar/excluir) em cada tabela
        configurarColunaAcoesAtleta();
        configurarColunaAcoesCompeticao();
        configurarColunaAcoesResultado();

        // carrega os dados atuais do repositório nas tabelas
        atualizarTabela();
    }

    // cria um botão pequeno usado nas colunas de ação
    private Button criarBotaoAcao(String texto, String tooltip, String estilo, Runnable acao) {
        Button botao = new Button(texto);
        botao.setMnemonicParsing(false);
        botao.setPrefWidth(32);
        botao.setPrefHeight(28);
        botao.setStyle(estilo);
        botao.setTooltip(new javafx.scene.control.Tooltip(tooltip));
        botao.setOnAction(event -> acao.run());
        return botao;
    }

    // monta a célula de ações para a tabela de atletas (editar/excluir)
    private void configurarColunaAcoesAtleta() {
        colAtletaAcoes.setCellFactory(column -> new TableCell<>() {
            // botões de editar (azul) e excluir (vermelho) compartilhados
            private final Button editar = criarBotaoAcao("✎", "Editar atleta", "-fx-background-color: #2f80ed; -fx-text-fill: white; -fx-font-weight: bold;", () -> abrirEdicaoAtleta(getTableView().getItems().get(getIndex())));
            private final Button excluir = criarBotaoAcao("🗑", "Excluir atleta", "-fx-background-color: #d64545; -fx-text-fill: white; -fx-font-weight: bold;", () -> excluirAtleta(getTableView().getItems().get(getIndex())));
            private final HBox box = new HBox(6, editar, excluir);

            {
                box.setStyle("-fx-alignment: CENTER;");
            }

            // mostra os botões apenas nas células preenchidas
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    // monta a célula de ações para a tabela de competições
    private void configurarColunaAcoesCompeticao() {
        colCompeticaoAcoes.setCellFactory(column -> new TableCell<>() {
            // botões de editar e excluir para competições
            private final Button editar = criarBotaoAcao("✎", "Editar competição", "-fx-background-color: #2f80ed; -fx-text-fill: white; -fx-font-weight: bold;", () -> abrirEdicaoCompeticao(getTableView().getItems().get(getIndex())));
            private final Button excluir = criarBotaoAcao("🗑", "Excluir competição", "-fx-background-color: #d64545; -fx-text-fill: white; -fx-font-weight: bold;", () -> excluirCompeticao(getTableView().getItems().get(getIndex())));
            private final HBox box = new HBox(6, editar, excluir);

            {
                box.setStyle("-fx-alignment: CENTER;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    // monta a célula de ações para a tabela de resultados
    private void configurarColunaAcoesResultado() {
        colResultadoAcoes.setCellFactory(column -> new TableCell<>() {
            // botões de editar e excluir para resultados
            private final Button editar = criarBotaoAcao("✎", "Editar resultado", "-fx-background-color: #2f80ed; -fx-text-fill: white; -fx-font-weight: bold;", () -> abrirEdicaoResultado(getTableView().getItems().get(getIndex())));
            private final Button excluir = criarBotaoAcao("🗑", "Excluir resultado", "-fx-background-color: #d64545; -fx-text-fill: white; -fx-font-weight: bold;", () -> excluirResultado(getTableView().getItems().get(getIndex())));
            private final HBox box = new HBox(6, editar, excluir);

            {
                box.setStyle("-fx-alignment: CENTER;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    // recarrega as listas observáveis a partir do serviço
    private void atualizarTabela() {
        listaAtletas.setAll(servico.listarAtletas());
        listaCompeticoes.setAll(servico.listarCompeticoes());
        listaResultados.setAll(servico.listarResultados());

        tabelaAtleta.refresh();
        tabelaCompeticao.refresh();
        tabelaResultado.refresh();
    }

    // abre a janela de cadastro de competição como modal
    @FXML private void abrirCadastroCompeticao() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroCompeticao.fxml"));
        Parent root = loader.load();

        CadastroCompeticaoController controller = loader.getController();
        controller.setServico(this.servico);

        Stage novoStage = new Stage();
        novoStage.setTitle("Cadastro de Competição");
        novoStage.initModality(Modality.APPLICATION_MODAL);
        novoStage.setScene(new Scene(root));
        novoStage.setResizable(false);
        novoStage.showAndWait();

        atualizarTabela();
    }

    // abre a janela de cadastro de atleta como modal
    @FXML private void abrirCadastroAtleta() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroAtleta.fxml"));
        Parent root = loader.load();

        CadastroAtletaController controller = loader.getController();
        controller.setServico(this.servico);

        Stage novoStage = new Stage();
        novoStage.setTitle("Cadastro de Atleta");
        novoStage.initModality(Modality.APPLICATION_MODAL);
        novoStage.setScene(new Scene(root));
        novoStage.setResizable(false);
        novoStage.showAndWait();

        atualizarTabela();
    }

    // abre a janela de inscrição de atleta em competição
    @FXML private void abrirCadastroInscricao() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroInscricao.fxml"));
        Parent root = loader.load();

        CadastroInscricaoController controller = loader.getController();
        controller.setServico(servico);

        Stage novoStage = new Stage();
        novoStage.setTitle("Inscrição de Atleta");
        novoStage.initModality(Modality.APPLICATION_MODAL);
        novoStage.setScene(new Scene(root));
        novoStage.setResizable(false);
        novoStage.showAndWait();

        atualizarTabela();
    }

    // abre a janela de cadastro de resultado (pódio da competição)
    @FXML private void abrirCadastroResultado() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroResultado.fxml"));
        Parent root = loader.load();

        CadastroResultadoController controller = loader.getController();
        controller.setServico(servico);

        Stage novoStage = new Stage();
        novoStage.setTitle("Cadastro de Resultado");
        novoStage.initModality(Modality.APPLICATION_MODAL);
        novoStage.setScene(new Scene(root));
        novoStage.setResizable(false);
        novoStage.showAndWait();

        atualizarTabela();
    }

    // abre a tela de edição de atleta reaproveitando o controller de cadastro
    @FXML private void abrirEdicaoAtleta(Atleta atleta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroAtleta.fxml"));
            Parent root = loader.load();

            CadastroAtletaController controller = loader.getController();
            controller.setServico(servico);
            controller.setAtletaEditando(atleta);

            Stage stage = new Stage();
            stage.setTitle("Editar Atleta");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a edição", e.getMessage());
        }
    }

    // abre a tela de edição de competição
    @FXML private void abrirEdicaoCompeticao(Competicao competicao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroCompeticao.fxml"));
            Parent root = loader.load();

            CadastroCompeticaoController controller = loader.getController();
            controller.setServico(servico);
            controller.setCompeticaoEditando(competicao);

            Stage stage = new Stage();
            stage.setTitle("Editar Competição");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a edição", e.getMessage());
        }
    }

    // abre a tela de edição de resultado
    @FXML private void abrirEdicaoResultado(Resultado resultado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroResultado.fxml"));
            Parent root = loader.load();

            CadastroResultadoController controller = loader.getController();
            controller.setServico(servico);
            controller.setResultadoEditando(resultado);

            Stage stage = new Stage();
            stage.setTitle("Editar Resultado");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a edição", e.getMessage());
        }
    }

    // diálogo de confirmação genérico para exclusões
    private boolean confirmarExclusao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText("Confirmação necessária");
        alert.setContentText(mensagem);

        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    // remove um atleta após confirmação
    private void excluirAtleta(Atleta atleta) {
        if (!confirmarExclusao("Excluir atleta", "Deseja excluir o atleta selecionado?")) {
            return;
        }

        try {
            servico.removerAtleta(atleta.getId());
            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir", e.getMessage());
        }
    }

    // remove uma competição após confirmação
    private void excluirCompeticao(Competicao competicao) {
        if (!confirmarExclusao("Excluir competição", "Deseja excluir a competição selecionada?")) {
            return;
        }

        try {
            servico.removerCompeticao(competicao.getId());
            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir", e.getMessage());
        }
    }

    // remove um resultado após confirmação
    private void excluirResultado(Resultado resultado) {
        if (!confirmarExclusao("Excluir resultado", "Deseja excluir o resultado selecionado?")) {
            return;
        }

        try {
            servico.removerResultado(resultado.getId());
            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir", e.getMessage());
        }
    }
}
