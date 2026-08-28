package br.edu.ifba.saj.ads.poo.business;

// unchecked: GenericService.salvar/atualizar declaram "throws RegraDeNegocioException",
// mas sobrescrevem métodos de GenericDAO que não declaram throws — só compila se a
// exceção for unchecked (regra de override não restringe RuntimeException)
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String message) {
        super(message);
    }

    public RegraDeNegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
