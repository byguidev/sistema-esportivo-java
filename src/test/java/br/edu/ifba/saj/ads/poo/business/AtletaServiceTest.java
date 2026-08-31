package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import br.edu.ifba.saj.ads.poo.model.Inscricao;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// testa as validações e operações básicas de atleta
class AtletaServiceTest {

    private GenericService<Atleta, Long> atletaService;
    private GenericService<Competicao, Long> competicaoService;
    private InscricaoServiceI inscricaoService;

    @BeforeEach
    void setUp() {
        FabricaDeServicos fabrica = new FabricaDeServicos();
        atletaService = fabrica.getAtletaService();
        competicaoService = fabrica.getCompeticaoService();
        inscricaoService = fabrica.getInscricaoService();
    }

    @Test
    void deveSalvarAtletaValidoEGerarId() {
        Atleta atleta = new Atleta("Ana", "Adulto");
        Long id = atletaService.salvar(atleta);

        assertNotNull(id);
        assertEquals(atleta, atletaService.buscarPorId(id));
    }

    @Test
    void deveFalharAoSalvarSemNome() {
        // testa quando nome é vazio
        Atleta atleta = new Atleta(" ", "Adulto");
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> atletaService.salvar(atleta));

        assertTrue(exception.getMessage().contains("Nome"));
    }

    @Test
    void deveFalharAoSalvarSemCategoria() {
        Atleta atleta = new Atleta("Ana", " ");
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> atletaService.salvar(atleta));

        assertTrue(exception.getMessage().contains("Categoria"));
    }

    @Test
    void deveAtualizarDadosDoAtleta() {
        Atleta atleta = new Atleta("Ana", "Adulto");
        atletaService.salvar(atleta);

        // atualiza o nome
        atleta.setNome("Ana Paula");
        atletaService.atualizar(atleta);

        assertEquals("Ana Paula", atletaService.buscarPorId(atleta.getId()).getNome());
    }

    @Test
    void deveRemoverAtleta() {
        Atleta atleta = new Atleta("Ana", "Adulto");
        atletaService.salvar(atleta);

        atletaService.deletar(atleta.getId());
        assertTrue(atletaService.buscarTodos().isEmpty());
    }

    @Test
    void deveRemoverAsInscricoesDoAtletaExcluido() {
        Atleta atleta = new Atleta("Ana", "Adulto");
        atletaService.salvar(atleta);

        Competicao competicao = new Competicao("Corrida", LocalDate.now().plusDays(10), 10);
        competicaoService.salvar(competicao);

        inscricaoService.salvar(new Inscricao(atleta, competicao));
        atletaService.deletar(atleta.getId());

        // a inscrição não pode sobreviver ao atleta
        assertTrue(inscricaoService.buscarTodos().isEmpty());
    }
}
