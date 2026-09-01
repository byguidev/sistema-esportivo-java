package br.edu.ifba.saj.ads.poo.presentation;

// linha usada só pra exibir os dados de auditoria na tabela; não representa nada que é salvo no "banco"
public class LinhaAuditoria {
    private final String tipo;
    private final String registro;
    private final String criadoPor;
    private final String criadoEm;
    private final String atualizadoPor;
    private final String atualizadoEm;

    public LinhaAuditoria(String tipo, String registro, String criadoPor, String criadoEm,
                          String atualizadoPor, String atualizadoEm) {
        this.tipo = tipo;
        this.registro = registro;
        this.criadoPor = criadoPor;
        this.criadoEm = criadoEm;
        this.atualizadoPor = atualizadoPor;
        this.atualizadoEm = atualizadoEm;
    }

    public String getTipo() {
        return tipo;
    }

    public String getRegistro() {
        return registro;
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public String getCriadoEm() {
        return criadoEm;
    }

    public String getAtualizadoPor() {
        return atualizadoPor;
    }

    public String getAtualizadoEm() {
        return atualizadoEm;
    }
}
