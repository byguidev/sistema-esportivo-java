package br.edu.ifba.saj.ads.poo.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// testa a identidade comum a todas as entidades
class AbstractModelTest {

    @Test
    void entidadesDeTiposDiferentesNuncaSaoIguais() {
        Atleta atleta = new Atleta("Ana", "Adulto");
        Competicao competicao = new Competicao("Corrida", LocalDate.now(), 10);

        // as duas ainda estão sem id, mas continuam sendo coisas diferentes
        assertNotEquals(atleta, competicao);
        assertNotEquals(competicao, atleta);
    }

    @Test
    void entidadesDoMesmoTipoComMesmoIdSaoIguais() {
        Atleta primeiro = new Atleta("Ana", "Adulto");
        Atleta segundo = new Atleta("Ana Paula", "Juvenil");
        primeiro.setId(1L);
        segundo.setId(1L);

        assertEquals(primeiro, segundo);
        assertEquals(primeiro.hashCode(), segundo.hashCode());
    }

    @Test
    void entidadesDoMesmoTipoComIdsDiferentesNaoSaoIguais() {
        Atleta primeiro = new Atleta("Ana", "Adulto");
        Atleta segundo = new Atleta("Ana", "Adulto");
        primeiro.setId(1L);
        segundo.setId(2L);

        assertNotEquals(primeiro, segundo);
    }

    @Test
    void toStringDeveMostrarNomeDaClasseEId() {
        // Resultado não sobrescreve toString, então cai no formato herdado
        Resultado resultado = new Resultado(null, null, null, null);
        resultado.setId(7L);

        assertEquals("Resultado{id=7}", resultado.toString());
    }

    @Test
    void datasDeCriacaoEAtualizacaoSaoPreenchidasNoConstrutor() {
        Atleta atleta = new Atleta("Ana", "Adulto");

        assertNotNull(atleta.getCreatedAt());
        assertNotNull(atleta.getUpdatedAt());
    }
}
