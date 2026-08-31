package br.edu.ifba.saj.ads.poo.data;

import br.edu.ifba.saj.ads.poo.model.Atleta;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

// testa o CRUD em memória compartilhado por todos os DAOs
class GenericDAOImplTest {

    private AtletaDAO dao;

    @BeforeEach
    void setUp() {
        dao = new AtletaDAO();
    }

    @Test
    void deveGerarIdSequencialAPartirDeUm() {
        assertEquals(1L, dao.salvar(new Atleta("Ana", "Adulto")));
        assertEquals(2L, dao.salvar(new Atleta("Bruno", "Adulto")));
    }

    @Test
    void salvarEntidadeJaPersistidaNaoDeveDuplicarNemTrocarOId() {
        Atleta atleta = new Atleta("Ana", "Adulto");
        Long primeiroId = dao.salvar(atleta);
        LocalDateTime criadoEm = atleta.getCreatedAt();

        Long segundoId = dao.salvar(atleta);

        assertEquals(primeiroId, segundoId);
        assertEquals(1, dao.buscarTodos().size());
        assertEquals(criadoEm, atleta.getCreatedAt());
    }

    @Test
    void deveAtualizarADataDeAtualizacao() {
        Atleta atleta = new Atleta("Ana", "Adulto");
        dao.salvar(atleta);

        atleta.setNome("Ana Paula");
        dao.atualizar(atleta);

        assertFalse(atleta.getUpdatedAt().isBefore(atleta.getCreatedAt()));
    }

    @Test
    void deveFalharAoAtualizarRegistroInexistente() {
        Atleta atleta = new Atleta("Ana", "Adulto");
        atleta.setId(99L);

        assertThrows(NoSuchElementException.class, () -> dao.atualizar(atleta));
    }

    @Test
    void deveFalharAoDeletarRegistroInexistente() {
        assertThrows(NoSuchElementException.class, () -> dao.deletar(99L));
    }
}
