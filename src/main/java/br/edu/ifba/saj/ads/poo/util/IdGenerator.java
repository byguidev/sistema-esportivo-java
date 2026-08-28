package br.edu.ifba.saj.ads.poo.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final AtomicLong sequencial = new AtomicLong(0);

    @SuppressWarnings("unchecked")
    public <T> T gerarNovoId(Class<T> tipoClasse) {
        if (tipoClasse == Long.class) {
            return (T) Long.valueOf(sequencial.incrementAndGet());
        }

        if (tipoClasse == UUID.class) {
            return (T) UUID.randomUUID();
        }

        throw new IllegalArgumentException("Tipo de id não suportado: " + tipoClasse);
    }
}
