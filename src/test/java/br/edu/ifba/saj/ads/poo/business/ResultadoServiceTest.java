package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import br.edu.ifba.saj.ads.poo.model.Resultado;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// testa a validação de resultados: obrigatoriedade de competição e unicidade
class ResultadoServiceTest {

    private GenericService<Atleta, Long> atletaService;
    private GenericService<Competicao, Long> competicaoService;
    private ResultadoServiceI resultadoService;

    @BeforeEach
    void setUp() {
        FabricaDeServicos fabrica = new FabricaDeServicos();
        atletaService = fabrica.getAtletaService();
        competicaoService = fabrica.getCompeticaoService();
        resultadoService = fabrica.getResultadoService();
    }

    private Competicao criarCompeticao() {
        Competicao competicao = new Competicao("Corrida", LocalDate.now().plusDays(10), 10);
        competicaoService.salvar(competicao);
        return competicao;
    }

    private Atleta criarAtleta(String nome) {
        Atleta atleta = new Atleta(nome, "Adulto");
        atletaService.salvar(atleta);
        return atleta;
    }

    @Test
    void deveSalvarResultadoValido() {
        Competicao competicao = criarCompeticao();
        Atleta primeiro = criarAtleta("Ana");
        Atleta segundo = criarAtleta("Bruno");
        Atleta terceiro = criarAtleta("Carla");

        // salva resultado com top 3
        resultadoService.salvar(new Resultado(competicao, primeiro, segundo, terceiro));
        assertEquals(1, resultadoService.buscarTodos().size());
    }

    @Test
    void deveFalharAoSalvarSemCompeticao() {
        Atleta primeiro = criarAtleta("Ana");
        Atleta segundo = criarAtleta("Bruno");
        Atleta terceiro = criarAtleta("Carla");

        // resultado sem competição deve falhar
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> resultadoService.salvar(new Resultado(null, primeiro, segundo, terceiro)));

        assertTrue(exception.getMessage().contains("competição"));
    }

    @Test
    void deveFalharAoSalvarSegundoResultadoParaMesmaCompeticao() {
        Competicao competicao = criarCompeticao();
        Atleta primeiro = criarAtleta("Ana");
        Atleta segundo = criarAtleta("Bruno");
        Atleta terceiro = criarAtleta("Carla");

        resultadoService.salvar(new Resultado(competicao, primeiro, segundo, terceiro));

        // tentar salvar resultado novamente para mesma competição deve falhar
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
            () -> resultadoService.salvar(new Resultado(competicao, terceiro, segundo, primeiro)));

        assertTrue(exception.getMessage().contains("Já existe"));
    }

    @Test
    void deveAtualizarResultadoSemAcusarDuplicidadeDeleMesmo() {
        Competicao competicao = criarCompeticao();
        Atleta primeiro = criarAtleta("Ana");
        Atleta segundo = criarAtleta("Bruno");
        Atleta terceiro = criarAtleta("Carla");

        Resultado resultado = new Resultado(competicao, primeiro, segundo, terceiro);
        resultadoService.salvar(resultado);

        // trocar o pódio do próprio registro não é duplicata
        resultado.setPrimeiroLugar(segundo);
        resultado.setSegundoLugar(primeiro);
        assertDoesNotThrow(() -> resultadoService.atualizar(resultado));

        assertEquals(segundo, resultadoService.buscarPorId(resultado.getId()).getPrimeiroLugar());
        assertEquals(1, resultadoService.buscarTodos().size());
    }

    @Test
    void deveRemoverOResultadoQuandoUmDosColocadosEExcluido() {
        Competicao competicao = criarCompeticao();
        Atleta primeiro = criarAtleta("Ana");
        Atleta segundo = criarAtleta("Bruno");
        Atleta terceiro = criarAtleta("Carla");

        resultadoService.salvar(new Resultado(competicao, primeiro, segundo, terceiro));
        atletaService.deletar(segundo.getId());

        // o pódio não faz sentido sem um dos colocados
        assertTrue(resultadoService.buscarTodos().isEmpty());
    }
}
