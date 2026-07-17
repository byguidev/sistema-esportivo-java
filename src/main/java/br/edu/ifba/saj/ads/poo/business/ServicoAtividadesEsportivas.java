package br.edu.ifba.saj.ads.poo.business;

// camada de negócio: concentra validações e regras antes de acessar o repositório

import br.edu.ifba.saj.ads.poo.data.RepositorioAtividades;
import br.edu.ifba.saj.ads.poo.model.*;
import java.util.List;

// serviço que orquestra as regras de negócio para competições, atletas, inscrições e resultados
public class ServicoAtividadesEsportivas {
    // referência ao repositório em memória
    public RepositorioAtividades repositorio;

    // recebe o repositório por injeção para facilitar testes
    public ServicoAtividadesEsportivas(RepositorioAtividades repositorio) {
        this.repositorio = repositorio;
    }

    // lista todas as competições
    public List<Competicao> listarCompeticoes() {
        return repositorio.listarCompeticoes();
    }

    // lista todos os atletas
    public List<Atleta> listarAtletas() {
        return repositorio.listarAtletas();
    }

    // lista todas as inscrições
    public List<Inscricao> listarInscricoes() {
        return repositorio.listarInscricoes();
    }

    // lista todos os resultados
    public List<Resultado> listarResultados() {
        return repositorio.listarResultados();
    }

    // retorna apenas as inscrições da competição informada (ou lista vazia)
    public List<Inscricao> listarInscricoesDaCompeticao(Competicao competicao) {
        if (competicao == null) {
            return List.of();
        }

        long idCompeticao = competicao.getId();
        return repositorio.listarInscricoes().stream()
            .filter(inscricao -> inscricao.getCompeticao().getId() == idCompeticao)
            .toList();
    }

    // atualiza uma competição ou dispara exceção se não existir
    public void atualizarCompeticao(Competicao competicao) throws Exception {
        if (competicao == null || !repositorio.atualizarCompeticao(competicao)) {
            throw new Exception("Competição não encontrada para atualização.");
        }
    }

    // remove uma competição pelo id
    public void removerCompeticao(long id) throws Exception {
        if (!repositorio.removerCompeticao(id)) {
            throw new Exception("Competição não encontrada para remoção.");
        }
    }

    // atualiza um atleta ou dispara exceção se não existir
    public void atualizarAtleta(Atleta atleta) throws Exception {
        if (atleta == null || !repositorio.atualizarAtleta(atleta)) {
            throw new Exception("Atleta não encontrado para atualização.");
        }
    }

    // remove um atleta pelo id
    public void removerAtleta(long id) throws Exception {
        if (!repositorio.removerAtleta(id)) {
            throw new Exception("Atleta não encontrado para remoção.");
        }
    }

    // atualiza uma inscrição ou dispara exceção se não existir
    public void atualizarInscricao(Inscricao inscricao) throws Exception {
        if (inscricao == null || !repositorio.atualizarInscricao(inscricao)) {
            throw new Exception("Inscrição não encontrada para atualização.");
        }
    }

    // remove uma inscrição pelo id
    public void removerInscricao(long id) throws Exception {
        if (!repositorio.removerInscricao(id)) {
            throw new Exception("Inscrição não encontrada para remoção.");
        }
    }

    // atualiza um resultado ou dispara exceção se não existir
    public void atualizarResultado(Resultado resultado) throws Exception {
        if (resultado == null || !repositorio.atualizarResultado(resultado)) {
            throw new Exception("Resultado não encontrado para atualização.");
        }
    }

    // remove um resultado pelo id
    public void removerResultado(long id) throws Exception {
        if (!repositorio.removerResultado(id)) {
            throw new Exception("Resultado não encontrado para remoção.");
        }
    }

    // verifica se a competição ainda tem vagas disponíveis
    public boolean validarLimiteParticipantes(Competicao c) {
        long idCompeticao = c.getId();
        int limiteCompeticao = c.getLimite();
        long totalParticipantes = repositorio.listarInscricoes().stream().filter(ins -> ins.getCompeticao().getId() == idCompeticao).count();

        return totalParticipantes < limiteCompeticao;
    }

    // verifica se o atleta já está inscrito na competição informada
    public boolean validarInscricaoUnica(Competicao c, Atleta a) {
        long idCompeticao = c.getId();
        List<Inscricao> inscricoesC = repositorio.listarInscricoes().stream().filter(ins -> ins.getCompeticao().getId() == idCompeticao).toList();
        long incricoesRepetidas = inscricoesC.stream().filter(ins -> ins.getAtleta().getId() == a.getId()).count();

        return incricoesRepetidas > 0;
    }

    // verifica se já existe resultado publicado para a competição do resultado
    public boolean validarResultadoUnico(Resultado r) {
        long resultadosRepetidos = repositorio.listarResultados().stream().filter(result -> result.getCompeticao().getId() == r.getCompeticao().getId()).count();

        return resultadosRepetidos > 0;
    }

    // valida e cria uma competição nova
    public void criarCompeticao(Competicao competicao) throws Exception {
        if (competicao == null) {
            throw new Exception("Dados da competição são obrigatórios.");
        }

        if (competicao.getNome() == null || competicao.getNome().trim().isEmpty()) {
            throw new Exception("Nome da competição é obrigatório.");
        }

        if (competicao.getData() == null) {
            throw new Exception("Data da competição é obrigatória.");
        }

        if (competicao.getLimite() <= 0) {
            throw new Exception("Limite de participantes deve ser maior que 0.");
        }

        repositorio.salvarCompeticao(competicao);
    }

    // valida e cria um atleta novo
    public void criarAtleta(Atleta atleta) throws Exception {
        if (atleta == null) {
            throw new Exception("Dados do atleta são obrigatórios.");
        }

        if (atleta.getNome() == null || atleta.getNome().trim().isEmpty()) {
            throw new Exception("Nome do atleta é obrigatório.");
        }

        if (atleta.getCategoria() == null || atleta.getCategoria().trim().isEmpty()) {
            throw new Exception("Categoria do atleta é obrigatória.");
        }

        repositorio.salvarAtleta(atleta);
    }

    // valida regras (limite e unicidade) antes de criar a inscrição
    public void criarInscricao(Inscricao inscricao) throws Exception {
        if (inscricao == null || inscricao.getAtleta() == null || inscricao.getCompeticao() == null) {
            throw new Exception("Sem dados válidos para inscrição.");
        }

        if (!validarLimiteParticipantes(inscricao.getCompeticao())) {
            throw new Exception("Competição completa. Não é possível realizar novas inscrições.");
        }

        if (validarInscricaoUnica(inscricao.getCompeticao(), inscricao.getAtleta())) {
            throw new Exception("Atleta já inscrito nesta competição.");
        }

        repositorio.salvarInscricao(inscricao);
    }

    // valida e cria um resultado novo (somente um por competição)
    public void criarResultado(Resultado resultado) throws Exception {
        if (resultado == null || resultado.getCompeticao() == null) {
            throw new Exception("É obrigatório selecionar uma competição.");
        }

        if (validarResultadoUnico(resultado)) {
            throw new Exception("Já existe um resultado cadastrado para essa competição.");
        }

        repositorio.salvarResultado(resultado);
    }
}
