package br.edu.ifba.saj.ads.poo.util;

// testes de unidade para o IdGenerator: geração de Long sequencial e UUID aleatório

import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdGeneratorTest {

    @Test
    void deveGerarLongsSequenciaisCrescentes() {
        IdGenerator idGenerator = new IdGenerator();

        Long primeiro = idGenerator.gerarNovoId(Long.class);
        Long segundo = idGenerator.gerarNovoId(Long.class);

        assertEquals(primeiro + 1, segundo);
    }

    @Test
    void deveGerarIntegersSequenciaisCrescentes() {
        IdGenerator idGenerator = new IdGenerator();

        Integer primeiro = idGenerator.gerarNovoId(Integer.class);
        Integer segundo = idGenerator.gerarNovoId(Integer.class);

        assertEquals(primeiro + 1, segundo);
    }

    @Test
    void deveGerarUuidsAleatoriosDiferentes() {
        IdGenerator idGenerator = new IdGenerator();

        UUID primeiro = idGenerator.gerarNovoId(UUID.class);
        UUID segundo = idGenerator.gerarNovoId(UUID.class);

        assertNotNull(primeiro);
        assertNotEquals(primeiro, segundo);
    }

    @Test
    void deveLancarExcecaoParaTipoNaoSuportado() {
        IdGenerator idGenerator = new IdGenerator();

        assertThrows(IllegalArgumentException.class, () -> idGenerator.gerarNovoId(String.class));
    }
}
