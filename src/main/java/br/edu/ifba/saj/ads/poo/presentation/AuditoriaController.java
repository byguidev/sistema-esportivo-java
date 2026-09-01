package br.edu.ifba.saj.ads.poo.presentation;

import br.edu.ifba.saj.ads.poo.business.FabricaDeServicos;
import br.edu.ifba.saj.ads.poo.model.AbstractModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// tela só de leitura (acesso restrito a ADMIN, controlado lá no MainController) com quem criou/alterou cada registro
public class AuditoriaController {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final FabricaDeServicos fabrica = FabricaDeServicos.getInstance();

    @FXML private TableView<LinhaAuditoria> tabelaAuditoria;
    @FXML private TableColumn<LinhaAuditoria, String> colTipo;
    @FXML private TableColumn<LinhaAuditoria, String> colRegistro;
    @FXML private TableColumn<LinhaAuditoria, String> colCriadoPor;
    @FXML private TableColumn<LinhaAuditoria, String> colCriadoEm;
    @FXML private TableColumn<LinhaAuditoria, String> colAtualizadoPor;
    @FXML private TableColumn<LinhaAuditoria, String> colAtualizadoEm;

    @FXML public void initialize() {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colRegistro.setCellValueFactory(new PropertyValueFactory<>("registro"));
        colCriadoPor.setCellValueFactory(new PropertyValueFactory<>("criadoPor"));
        colCriadoEm.setCellValueFactory(new PropertyValueFactory<>("criadoEm"));
        colAtualizadoPor.setCellValueFactory(new PropertyValueFactory<>("atualizadoPor"));
        colAtualizadoEm.setCellValueFactory(new PropertyValueFactory<>("atualizadoEm"));

        ObservableList<LinhaAuditoria> linhas = FXCollections.observableArrayList(montarLinhas());
        tabelaAuditoria.setItems(linhas);
    }

    // junta os registros de todas as entidades numa lista só de linhas de auditoria
    private List<LinhaAuditoria> montarLinhas() {
        List<LinhaAuditoria> linhas = new ArrayList<>();

        adicionarLinhas(linhas, "Atleta", fabrica.getAtletaService().buscarTodos());
        adicionarLinhas(linhas, "Competição", fabrica.getCompeticaoService().buscarTodos());
        adicionarLinhas(linhas, "Inscrição", fabrica.getInscricaoService().buscarTodos());
        adicionarLinhas(linhas, "Resultado", fabrica.getResultadoService().buscarTodos());
        adicionarLinhas(linhas, "Usuário", fabrica.getUsuarioService().buscarTodos());

        return linhas;
    }

    private void adicionarLinhas(List<LinhaAuditoria> linhas, String tipo, List<? extends AbstractModel<?>> registros) {
        for (AbstractModel<?> registro : registros) {
            linhas.add(new LinhaAuditoria(
                tipo,
                registro.toString(),
                registro.getCriadoPor(),
                formatar(registro.getCreatedAt()),
                registro.getAtualizadoPor(),
                formatar(registro.getUpdatedAt())
            ));
        }
    }

    private String formatar(LocalDateTime data) {
        return data == null ? "-" : data.format(FORMATO_DATA);
    }
}
