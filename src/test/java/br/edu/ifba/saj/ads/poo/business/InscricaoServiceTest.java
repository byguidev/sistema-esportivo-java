package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import br.edu.ifba.saj.ads.poo.model.Inscricao;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// testa o limite de vagas e validação de inscrição única
class InscricaoServiceTest {

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

    private Competicao criarCompeticao(int limite) {
        Competicao competicao = new Competicao("Corrida", LocalDate.now().plusDays(10), limite);
        competicaoService.salvar(competicao);
        return competicao;
    }

    private Atleta criarAtleta(String nome) {
        Atleta atleta = new Atleta(nome, "Adulto");
        atletaService.salvar(atleta);
        return atleta;
    }

    @Test
    void devePermitirInscricaoQuandoHaVagaEAtletaAindaNaoInscrito() {
        Competicao competicao = criarCompeticao(2);
        Atleta atleta = criarAtleta("Ana");

        inscricaoService.salvar(new Inscricao(atleta, competicao));
        assertEquals(1, inscricaoService.buscarTodos().size());
    }

    @Test
    void deveFalharAoInscreverQuandoCompeticaoEstiverCompleta() {
        // limite é 1, então segunda inscrição deve falhar
        Competicao competicao = criarCompeticao(1);
        Atleta primeiroAtleta = criarAtleta("Bruno");
        Atleta segundoAtleta = criarAtleta("Carla");

        inscricaoService.salvar(new Inscricao(primeiroAtleta, competicao));

        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> inscricaoService.salvar(new Inscricao(segundoAtleta, competicao)));

        assertTrue(exception.getMessage().contains("completa"));
        assertEquals(1, inscricaoService.buscarTodos().size());
    }

    @Test
    void deveFalharAoInscreverMesmoAtletaDuasVezesNaMesmaCompeticao() {
        Competicao competicao = criarCompeticao(2);
        Atleta atleta = criarAtleta("Daniel");

        // primeira inscrição funciona
        inscricaoService.salvar(new Inscricao(atleta, competicao));

        // segunda inscrição do mesmo atleta deve falhar
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> inscricaoService.salvar(new Inscricao(atleta, competicao)));

        assertTrue(exception.getMessage().contains("já inscrito"));
        assertEquals(1, inscricaoService.buscarTodos().size());
    }

    @Test
    void deveAtualizarInscricaoSemAcusarDuplicidadeDelaMesma() {
        // competição lotada: a própria inscrição não pode contar contra ela mesma
        Competicao competicao = criarCompeticao(1);
        Atleta ana = criarAtleta("Ana");
        Atleta bruno = criarAtleta("Bruno");

        Inscricao inscricao = new Inscricao(ana, competicao);
        inscricaoService.salvar(inscricao);

        inscricao.setAtleta(bruno);
        assertDoesNotThrow(() -> inscricaoService.atualizar(inscricao));

        assertEquals(bruno, inscricaoService.buscarPorId(inscricao.getId()).getAtleta());
    }

    @Test
    void buscarPorCompeticaoDeveRetornarApenasInscricoesDaquelaCompeticao() {
        Competicao corrida = criarCompeticao(5);
        Competicao natacao = criarCompeticao(5);
        Atleta ana = criarAtleta("Ana");
        Atleta bruno = criarAtleta("Bruno");

        inscricaoService.salvar(new Inscricao(ana, corrida));
        inscricaoService.salvar(new Inscricao(bruno, natacao));

        // verifica que só retorna inscrições da corrida
        assertEquals(1, inscricaoService.buscarPorCompeticao(corrida).size());
        assertEquals(ana, inscricaoService.buscarPorCompeticao(corrida).get(0).getAtleta());
    }

    @Test
    void buscarPorAtletaDeveRetornarApenasInscricoesDaqueleAtleta() {
        Competicao corrida = criarCompeticao(5);
        Competicao natacao = criarCompeticao(5);
        Atleta ana = criarAtleta("Ana");
        Atleta bruno = criarAtleta("Bruno");

        inscricaoService.salvar(new Inscricao(ana, corrida));
        inscricaoService.salvar(new Inscricao(ana, natacao));
        inscricaoService.salvar(new Inscricao(bruno, corrida));

        assertEquals(2, inscricaoService.buscarPorAtleta(ana).size());
    }
}
