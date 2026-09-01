package br.edu.ifba.saj.ads.poo.presentation;

import br.edu.ifba.saj.ads.poo.business.FabricaDeServicos;
import br.edu.ifba.saj.ads.poo.business.GenericService;
import br.edu.ifba.saj.ads.poo.business.InscricaoServiceI;
import br.edu.ifba.saj.ads.poo.business.ResultadoServiceI;
import br.edu.ifba.saj.ads.poo.business.UsuarioServiceI;
import br.edu.ifba.saj.ads.poo.model.*;
import br.edu.ifba.saj.ads.poo.util.SessaoUsuario;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

public class MainController {

    // a tela recebe os serviços prontos e só enxerga as interfaces
    // usa a instância única da fábrica, senão o login enxergaria um "banco" diferente do resto do sistema
    private final FabricaDeServicos fabrica = FabricaDeServicos.getInstance();

    // botões de ação discretos, em neutro claro pra não competir com o verde principal
    private static final String ESTILO_ACAO = "-fx-background-color: #EEF3F0; -fx-border-color: #C7D3CC;"
        + " -fx-background-radius: 5; -fx-border-radius: 5;";
    private static final String ESTILO_EDITAR = ESTILO_ACAO + " -fx-text-fill: #3C4C44;";
    private static final String ESTILO_EXCLUIR = ESTILO_ACAO + " -fx-text-fill: #A34A4A;";
    private static final String COR_ICONE_EXCLUIR = "#A34A4A";

    // ícone de lixeira (excluir) — vetor, não depende de fonte com emoji
    private static final String SVG_LIXEIRA = "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12Z "
        + "M19 4h-3.5l-1-1h-5l-1 1H5v2h14V4Z";

    GenericService<Atleta, Long> atletaService = fabrica.getAtletaService();
    GenericService<Competicao, Long> competicaoService = fabrica.getCompeticaoService();
    InscricaoServiceI inscricaoService = fabrica.getInscricaoService();
    ResultadoServiceI resultadoService = fabrica.getResultadoService();
    UsuarioServiceI usuarioService = fabrica.getUsuarioService();

    private final ObservableList<Atleta> listaAtletas = FXCollections.observableArrayList();
    private final ObservableList<Competicao> listaCompeticoes = FXCollections.observableArrayList();
    private final ObservableList<Inscricao> listaInscricoes = FXCollections.observableArrayList();
    private final ObservableList<Resultado> listaResultados = FXCollections.observableArrayList();
    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

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

    // seção administrativa: só aparece pra quem loga como ADMIN (ver aplicarPermissoes)
    @FXML private TabPane tabPanePrincipal;
    @FXML private Tab tabUsuarios;
    @FXML private HBox auditoriaRow;
    @FXML private Button auditoriaBtn;
    @FXML private TableView<Usuario> tabelaUsuario;
    @FXML private TableColumn<Usuario, String> colUsuarioLogin;
    @FXML private TableColumn<Usuario, Perfil> colUsuarioPerfil;
    @FXML private TableColumn<Usuario, Void> colUsuarioAcoes;

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
        tabelaUsuario.setItems(listaUsuarios);

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

        colUsuarioLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colUsuarioPerfil.setCellValueFactory(new PropertyValueFactory<>("perfil"));

        configurarColunaAcoes(colAtletaAcoes, "atleta", this::abrirEdicaoAtleta, this::excluirAtleta);
        configurarColunaAcoes(colCompeticaoAcoes, "competição", this::abrirEdicaoCompeticao, this::excluirCompeticao);
        configurarColunaAcoes(colInscricaoAcoes, "inscrição", this::abrirEdicaoInscricao, this::excluirInscricao);
        configurarColunaAcoes(colResultadoAcoes, "resultado", this::abrirEdicaoResultado, this::excluirResultado);
        configurarColunaAcoes(colUsuarioAcoes, "usuário", this::abrirEdicaoUsuario, this::excluirUsuario);

        aplicarPermissoes();
        atualizarTabela();
    }

    // esconde os recursos administrativos (cadastro de usuário e auditoria) de quem não logou como ADMIN
    private void aplicarPermissoes() {
        Usuario logado = SessaoUsuario.getUsuarioLogado();
        boolean admin = logado != null && logado.getPerfil() == Perfil.ADMIN;

        auditoriaBtn.setVisible(admin);
        auditoriaBtn.setManaged(admin);
        auditoriaRow.setVisible(admin);
        auditoriaRow.setManaged(admin);
        if (!admin) {
            tabPanePrincipal.getTabs().remove(tabUsuarios);
        }
    }

    private Button criarBotaoAcao(String texto, String tooltip, double largura, String estilo, Runnable acao) {
        Button botao = new Button(texto);
        botao.setMnemonicParsing(false);
        botao.setPrefWidth(largura);
        botao.setPrefHeight(28);
        botao.setStyle(estilo);
        botao.setTooltip(new javafx.scene.control.Tooltip(tooltip));
        botao.setOnAction(event -> acao.run());
        return botao;
    }

    // ícone de lixeira desenhado em vetor (SVG), na cor de exclusão
    private Node criarIconeLixeira() {
        SVGPath forma = new SVGPath();
        forma.setContent(SVG_LIXEIRA);
        forma.setStyle("-fx-fill: " + COR_ICONE_EXCLUIR + ";");
        forma.setScaleX(0.5);
        forma.setScaleY(0.5);
        return new Group(forma);
    }

    private Button criarBotaoAcaoIcone(Node icone, String tooltip, double largura, String estilo, Runnable acao) {
        Button botao = new Button();
        botao.setGraphic(icone);
        botao.setMnemonicParsing(false);
        botao.setPrefWidth(largura);
        botao.setPrefHeight(28);
        botao.setStyle(estilo);
        botao.setTooltip(new javafx.scene.control.Tooltip(tooltip));
        botao.setOnAction(event -> acao.run());
        return botao;
    }

    // largura da coluna: conteúdo (2 botões de 32px + espaçamento de 6px = 70px) mais uma folga pequena
    private static final double LARGURA_COLUNA_ACOES = 78;

    // o corpo das linhas da tabela renderiza ~9px deslocado pra direita em relação ao cabeçalho da
    // coluna (o JavaFX reserva espaço pra uma scrollbar vertical no total das linhas, mas não no
    // cabeçalho — então a largura total do corpo fica maior que a do cabeçalho). Sem essa correção,
    // o conteúdo desta coluna, embora corretamente centralizado dentro da própria célula, aparece
    // deslocado pra direita em relação ao título "AÇÕES" acima dela.
    private static final double CORRECAO_DESLOCAMENTO_LINHA = -9;

    private <S> void configurarColunaAcoes(TableColumn<S, Void> coluna, String entidade,
                                           Consumer<S> aoEditar, Consumer<S> aoExcluir) {
        coluna.setCellFactory(column -> new TableCell<>() {
            private final Button editar = criarBotaoAcao("...", "Editar " + entidade, 32, ESTILO_EDITAR,
                () -> aoEditar.accept(getTableView().getItems().get(getIndex())));
            private final Button excluir = criarBotaoAcaoIcone(criarIconeLixeira(), "Excluir " + entidade, 32,
                ESTILO_EXCLUIR, () -> aoExcluir.accept(getTableView().getItems().get(getIndex())));
            private final HBox box = new HBox(6, editar, excluir);

            {
                box.setAlignment(Pos.CENTER);
                box.prefWidthProperty().bind(coluna.widthProperty());
                box.setMaxWidth(Region.USE_PREF_SIZE);
                setTranslateX(CORRECAO_DESLOCAMENTO_LINHA);
                setStyle("-fx-padding: 0;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
        coluna.setResizable(false);
        coluna.setMinWidth(LARGURA_COLUNA_ACOES);
        coluna.setMaxWidth(LARGURA_COLUNA_ACOES);
        coluna.setPrefWidth(LARGURA_COLUNA_ACOES);
    }

    private void atualizarTabela() {
        listaAtletas.setAll(atletaService.buscarTodos());
        listaCompeticoes.setAll(competicaoService.buscarTodos());
        listaInscricoes.setAll(inscricaoService.buscarTodos());
        listaResultados.setAll(resultadoService.buscarTodos());
        listaUsuarios.setAll(usuarioService.buscarTodos());

        tabelaAtleta.refresh();
        tabelaCompeticao.refresh();
        tabelaInscricao.refresh();
        tabelaResultado.refresh();
        tabelaUsuario.refresh();
    }

    // abre a janela de cadastro de competição como modal
    @FXML private void abrirCadastroCompeticao() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroCompeticao.fxml"));
        Parent root = loader.load();

        CadastroCompeticaoController controller = loader.getController();
        controller.setServico(this.competicaoService);

        abrirModal(root, "Cadastro de Competição");
    }

    // abre a janela de cadastro de usuário como modal (só o botão do admin chama isso)
    @FXML private void abrirCadastroUsuario() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroUsuario.fxml"));
        Parent root = loader.load();

        CadastroUsuarioController controller = loader.getController();
        controller.setServico(this.usuarioService);

        abrirModal(root, "Cadastro de Usuário");
    }

    // abre a tela de auditoria como modal
    @FXML private void abrirAuditoria() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/Auditoria.fxml"));
        Parent root = loader.load();

        abrirModal(root, "Auditoria");
    }

    // desloga o usuário atual e volta pra tela de login
    @FXML private void onSair() {
        try {
            SessaoUsuario.deslogar();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tabelaAtleta.getScene().getWindow();
            Scene scene = new Scene(root);
            Theme.aplicar(scene);
            stage.setTitle("Login");
            stage.setScene(scene);
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível sair", e.getMessage());
        }
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

    // abre a tela de edição de usuário
    private void abrirEdicaoUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/CadastroUsuario.fxml"));
            Parent root = loader.load();

            CadastroUsuarioController controller = loader.getController();
            controller.setServico(usuarioService);
            controller.setUsuarioEditando(usuario);

            abrirModal(root, "Editar Usuário");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a edição", e.getMessage());
        }
    }

    // abre a tela como modal e recarrega as tabelas quando ela fecha
    private void abrirModal(Parent root, String titulo) {
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        Theme.aplicar(scene);
        stage.setTitle(titulo);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
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

    // remove um usuário após confirmação
    private void excluirUsuario(Usuario usuario) {
        if (!confirmarExclusao("Excluir usuário", "Deseja excluir o usuário selecionado?")) {
            return;
        }

        try {
            usuarioService.deletar(usuario.getId());
            atualizarTabela();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir", e.getMessage());
        }
    }
}
