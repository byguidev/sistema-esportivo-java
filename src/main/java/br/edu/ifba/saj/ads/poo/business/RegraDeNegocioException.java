package br.edu.ifba.saj.ads.poo.business;

public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String message) {
        super(message);
    }

    public RegraDeNegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
