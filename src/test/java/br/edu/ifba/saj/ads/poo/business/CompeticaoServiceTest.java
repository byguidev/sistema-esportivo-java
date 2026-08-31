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

// testa as validações de competição
class CompeticaoServiceTest {

    private GenericService<Competicao, Long> competicaoService;
    private GenericService<Atleta, Long> atletaService;
    private InscricaoServiceI inscricaoService;

    @BeforeEach
    void setUp() {
        FabricaDeServicos fabrica = new FabricaDeServicos();
        competicaoService = fabrica.getCompeticaoService();
        atletaService = fabrica.getAtletaService();
        inscricaoService = fabrica.getInscricaoService();
    }

    @Test
    void deveSalvarCompeticaoValidaEGerarId() {
        Competicao competicao = new Competicao("Corrida", LocalDate.now().plusDays(10), 20);
        Long id = competicaoService.salvar(competicao);

        assertNotNull(id);
        assertEquals(competicao, competicaoService.buscarPorId(id));
    }

    @Test
    void deveFalharAoSalvarSemNome() {
        Competicao competicao = new Competicao(" ", LocalDate.now().plusDays(10), 20);
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> competicaoService.salvar(competicao));

        assertTrue(exception.getMessage().contains("Nome"));
    }

    @Test
    void deveFalharAoSalvarSemData() {
        // sem data deve falhar
        Competicao competicao = new Competicao("Corrida", null, 20);
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> competicaoService.salvar(competicao));

        assertTrue(exception.getMessage().contains("Data"));
    }

    @Test
    void deveFalharAoSalvarComLimiteZeroOuNegativo() {
        Competicao competicao = new Competicao("Corrida", LocalDate.now().plusDays(10), 0);
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> competicaoService.salvar(competicao));

        assertTrue(exception.getMessage().contains("Limite"));
    }

    @Test
    void deveAtualizarDadosDaCompeticao() {
        Competicao competicao = new Competicao("Corrida", LocalDate.now().plusDays(10), 20);
        competicaoService.salvar(competicao);

        // aumenta o limite
        competicao.setLimite(30);
        competicaoService.atualizar(competicao);

        assertEquals(30, competicaoService.buscarPorId(competicao.getId()).getLimite());
    }

    @Test
    void deveFalharAoReduzirLimiteAbaixoDoTotalDeInscritos() {
        Competicao competicao = new Competicao("Corrida", LocalDate.now().plusDays(10), 5);
        competicaoService.salvar(competicao);

        Atleta ana = new Atleta("Ana", "Adulto");
        Atleta bruno = new Atleta("Bruno", "Adulto");
        atletaService.salvar(ana);
        atletaService.salvar(bruno);

        inscricaoService.salvar(new Inscricao(ana, competicao));
        inscricaoService.salvar(new Inscricao(bruno, competicao));

        // já há 2 inscritos, então o limite não pode cair para 1
        competicao.setLimite(1);
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> competicaoService.atualizar(competicao));

        assertTrue(exception.getMessage().contains("Limite menor"));
    }

    @Test
    void deveRemoverAsInscricoesDaCompeticaoExcluida() {
        Competicao competicao = new Competicao("Corrida", LocalDate.now().plusDays(10), 5);
        competicaoService.salvar(competicao);

        Atleta ana = new Atleta("Ana", "Adulto");
        atletaService.salvar(ana);
        inscricaoService.salvar(new Inscricao(ana, competicao));

        competicaoService.deletar(competicao.getId());

        // a inscrição não pode sobreviver à competição
        assertTrue(inscricaoService.buscarTodos().isEmpty());
    }
}
