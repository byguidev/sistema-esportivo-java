package br.edu.ifba.saj.ads.poo.presentation;

import br.edu.ifba.saj.ads.poo.business.FabricaDeServicos;
import br.edu.ifba.saj.ads.poo.business.GenericService;
import br.edu.ifba.saj.ads.poo.business.InscricaoServiceI;
import br.edu.ifba.saj.ads.poo.business.ResultadoServiceI;
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
import java.util.function.Consumer;

public class MainController {

    // a tela recebe os serviços prontos e só enxerga as interfaces
    private final FabricaDeServicos fabrica = new FabricaDeServicos();

    // botões de ação discretos, no mesmo cinza do resto da janela
    private static final String ESTILO_ACAO = "-fx-background-color: #ebebeb; -fx-border-color: #c6c6c6;"
        + " -fx-background-radius: 3; -fx-border-radius: 3;";
    private static final String ESTILO_EDITAR = ESTILO_ACAO + " -fx-text-fill: #44526b;";
    private static final String ESTILO_EXCLUIR = ESTILO_ACAO + " -fx-text-fill: #7d4a4a;";

    GenericService<Atleta, Long> atletaService = fabrica.getAtletaService();
    GenericService<Competicao, Long> competicaoService = fabrica.getCompeticaoService();
    InscricaoServiceI inscricaoService = fabrica.getInscricaoService();
    ResultadoServiceI resultadoService = fabrica.getResultadoService();

    private final ObservableList<Atleta> listaAtletas = FXCollections.observableArrayList();
    private final ObservableList<Competicao> listaCompeticoes = FXCollections.observableArrayList();
    private final ObservableList<Inscricao> listaInscricoes = FXCollections.observableArrayList();
    private final ObservableList<Resultado> listaResultados = FXCollections.observableArrayList();

    @FXML private TableView<Atleta> tabelaAtleta;
    @FXML private TableColumn<Atleta, String> colAtletaNome;
    @FXML private TableColumn<Atleta, String> colAtletaCategoria;
    @FXML private TableColumn<Atleta, Void> colAtletaAcoes;

    @FXML private TableView<Competicao> tabelaCompeticao;
    @FXML private TableColumn<Competicao, String> colCompeticaoNome;
    @FXML private TableColumn<Competicao, LocalDate> colCompeticaoData;
    @FXML private TableColumn<Competicao, Integer> colCompeticaoLimite;
    @FXML private TableColumn<Competicao, Void> colCompeticaoAcoes;

    @FXML private TableView<Inscricao> tabelaInscricao;
    @FXML private TableColumn<Inscricao, String> colInscricaoAtleta;
    @FXML private TableColumn<Inscricao, String> colInscricaoCompeticao;
    @FXML private TableColumn<Inscricao, Void> colInscricaoAcoes;

    @FXML private TableView<Resultado> tabelaResultado;
    @FXML private TableColumn<Resultado, String> colResultadoCompeticao;
    @FXML private TableColumn<Resultado, String> colResultadoPrimeiro;
    @FXML private TableColumn<Resultado, String> colResultadoSegundo;
    @FXML private TableColumn<Resultado, String> colResultadoTerceiro;
    @FXML private TableColumn<Resultado, Void> colResultadoAcoes;

    public static void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML public void initialize() {
        tabelaAtleta.setItems(listaAtletas);
        tabelaCompeticao.setItems(listaCompeticoes);
        tabelaInscricao.setItems(listaInscricoes);
        tabelaResultado.setItems(listaResultados);

        // colunas simples
        colAtletaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAtletaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        colCompeticaoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCompeticaoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCompeticaoLimite.setCellValueFactory(new PropertyValueFactory<>("limite"));

        // inscrição precisa navegar até o atleta e a competição
        colInscricaoAtleta.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getAtleta().getNome()));
        colInscricaoCompeticao.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getCompeticao().getNome()));

        // resultado precisa navegar até os nomes dos atletas
        colResultadoCompeticao.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getCompeticao().getNome()));
        colResultadoPrimeiro.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getPrimeiroLugar().getNome()));
        colResultadoSegundo.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getSegundoLugar().getNome()));
        colResultadoTerceiro.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getTerceiroLugar().getNome()));

        configurarColunaAcoes(colAtletaAcoes, "atleta", this::abrirEdicaoAtleta, this::excluirAtleta);
        configurarColunaAcoes(colCompeticaoAcoes, "competição", this::abrirEdicaoCompeticao, this::excluirCompeticao);
        configurarColunaAcoes(colInscricaoAcoes, "inscrição", this::abrirEdicaoInscricao, this::excluirInscricao);
        configurarColunaAcoes(colResultadoAcoes, "resultado", this::abrirEdicaoResultado, this::excluirResultado);

        atualizarTabela();
    }

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

    // monta a coluna de ações (editar/excluir) de qualquer uma das tabelas
    private <S> void configurarColunaAcoes(TableColumn<S, Void> coluna, String entidade,
                                           Consumer<S> aoEditar, Consumer<S> aoExcluir) {
        coluna.setCellFactory(column -> new TableCell<>() {
            private final Button editar = criarBotaoAcao("✎", "Editar " + entidade, ESTILO_EDITAR,
                () -> aoEditar.accept(getTableView().getItems().get(getIndex())));
            private final Button excluir = criarBotaoAcao("🗑", "Excluir " + entidade, ESTILO_EXCLUIR,
                () -> aoExcluir.accept(getTableView().getItems().get(getIndex())));
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

    private void atualizarTabela() {
        listaAtletas.setAll(atletaService.buscarTodos());
        listaCompeticoes.setAll(competicaoService.buscarTodos());
        listaInscricoes.setAll(inscricaoService.buscarTodos());
        listaResultados.setAll(resultadoService.buscarTodos());

        tabelaAtleta.refresh();
        tabelaCompeticao.refresh();
        tabelaInscricao.refresh();
        tabelaResultado.refresh();
    }

    // abre a janela de cadastro de competição como modal
    @FXML private void abrirCadastroCompeticao() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroCompeticao.fxml"));
        Parent root = loader.load();

        CadastroCompeticaoController controller = loader.getController();
        controller.setServico(this.competicaoService);

        abrirModal(root, "Cadastro de Competição");
    }

    // abre a janela de cadastro de atleta como modal
    @FXML private void abrirCadastroAtleta() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroAtleta.fxml"));
        Parent root = loader.load();

        CadastroAtletaController controller = loader.getController();
        controller.setServico(this.atletaService);

        abrirModal(root, "Cadastro de Atleta");
    }

    // abre a janela de inscrição de atleta em competição
    @FXML private void abrirCadastroInscricao() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroInscricao.fxml"));
        Parent root = loader.load();

        CadastroInscricaoController controller = loader.getController();
        controller.setAtletaService(atletaService);
        controller.setCompeticaoService(competicaoService);
        controller.setInscricaoService(inscricaoService);

        abrirModal(root, "Inscrição de Atleta");
    }

    // abre a janela de cadastro de resultado (pódio da competição)
    @FXML private void abrirCadastroResultado() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroResultado.fxml"));
        Parent root = loader.load();

        CadastroResultadoController controller = loader.getController();
        controller.setCompeticaoService(competicaoService);
        controller.setInscricaoService(inscricaoService);
        controller.setResultadoService(resultadoService);

        abrirModal(root, "Cadastro de Resultado");
    }

    // abre a tela de edição de atleta reaproveitando o controller de cadastro
    private void abrirEdicaoAtleta(Atleta atleta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroAtleta.fxml"));
            Parent root = loader.load();

            CadastroAtletaController controller = loader.getController();
            controller.setServico(atletaService);
            controller.setAtletaEditando(atleta);

            abrirModal(root, "Editar Atleta");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a edição", e.getMessage());
        }
    }

    // abre a tela de edição de competição
    private void abrirEdicaoCompeticao(Competicao competicao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroCompeticao.fxml"));
            Parent root = loader.load();

            CadastroCompeticaoController controller = loader.getController();
            controller.setServico(competicaoService);
            controller.setCompeticaoEditando(competicao);

            abrirModal(root, "Editar Competição");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a edição", e.getMessage());
        }
    }

    // abre a tela de edição de inscrição
    private void abrirEdicaoInscricao(Inscricao inscricao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroInscricao.fxml"));
            Parent root = loader.load();

            CadastroInscricaoController controller = loader.getController();
            controller.setAtletaService(atletaService);
            controller.setCompeticaoService(competicaoService);
            controller.setInscricaoService(inscricaoService);
            controller.setInscricaoEditando(inscricao);

            abrirModal(root, "Editar Inscrição");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a edição", e.getMessage());
        }
    }

    // abre a tela de edição de resultado
    private void abrirEdicaoResultado(Resultado resultado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroResultado.fxml"));
            Parent root = loader.load();

            CadastroResultadoController controller = loader.getController();
            controller.setCompeticaoService(competicaoService);
            controller.setInscricaoService(inscricaoService);
            controller.setResultadoService(resultadoService);
            controller.setResultadoEditando(resultado);

            abrirModal(root, "Editar Resultado");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a edição", e.getMessage());
        }
    }

    // abre a tela como modal e recarrega as tabelas quando ela fecha
    private void abrirModal(Parent root, String titulo) {
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();

        atualizarTabela();
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

    // remove um atleta após confirmação, junto do que depende dele
    private void excluirAtleta(Atleta atleta) {
        if (!confirmarExclusao("Excluir atleta",
            "Excluir o atleta também remove suas inscrições e os resultados em que ele aparece. Continuar?")) {
            return;
        }

        try {
            atletaService.deletar(atleta.getId());
            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir", e.getMessage());
        }
    }

    // remove uma competição após confirmação, junto do que depende dela
    private void excluirCompeticao(Competicao competicao) {
        if (!confirmarExclusao("Excluir competição",
            "Excluir a competição também remove suas inscrições e seu resultado. Continuar?")) {
            return;
        }

        try {
            competicaoService.deletar(competicao.getId());
            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir", e.getMessage());
        }
    }

    // remove uma inscrição após confirmação
    private void excluirInscricao(Inscricao inscricao) {
        if (!confirmarExclusao("Excluir inscrição", "Deseja excluir a inscrição selecionada?")) {
            return;
        }

        try {
            inscricaoService.deletar(inscricao.getId());
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
            resultadoService.deletar(resultado.getId());
            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir", e.getMessage());
        }
    }
}
