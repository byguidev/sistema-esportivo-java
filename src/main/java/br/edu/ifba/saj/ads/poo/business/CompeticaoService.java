package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.GenericDAO;
import br.edu.ifba.saj.ads.poo.model.Competicao;

public class CompeticaoService extends GenericServiceImpl<Competicao, Long> {

    private final InscricaoServiceI inscricaoService;
    private final ResultadoServiceI resultadoService;

    public CompeticaoService(GenericDAO<Competicao, Long> dao,
                             InscricaoServiceI inscricaoService,
                             ResultadoServiceI resultadoService) {
        super(dao);
        this.inscricaoService = inscricaoService;
        this.resultadoService = resultadoService;
    }

    @Override
    public void validar(Competicao competicao) throws RegraDeNegocioException {
        if (competicao == null) {
            throw new RegraDeNegocioException("Dados da competição são obrigatórios.");
        }

        // validar nome
        if (competicao.getNome() == null || competicao.getNome().trim().isEmpty()) {
            throw new RegraDeNegocioException("Nome da competição é obrigatório.");
        }

        // validar data
        if (competicao.getData() == null) {
            throw new RegraDeNegocioException("Data da competição é obrigatória.");
        }

        // limite precisa ser positivo
        if (competicao.getLimite() <= 0) {
            throw new RegraDeNegocioException("Limite de participantes deve ser maior que 0.");
        }

        // numa edição o limite não pode cair abaixo de quem já está inscrito
        if (competicao.getId() != null) {
            int inscritos = inscricaoService.buscarPorCompeticao(competicao).size();

            if (competicao.getLimite() < inscritos) {
                throw new RegraDeNegocioException(
                    "Limite menor que o número de atletas já inscritos (" + inscritos + ").");
            }
        }
    }

    @Override
    public void deletar(Long id) {
        Competicao competicao = buscarPorId(id);

        // limpa o que depende da competição pra não sobrar inscrição nem resultado órfão
        if (competicao != null) {
            resultadoService.removerPorCompeticao(competicao);
            inscricaoService.removerPorCompeticao(competicao);
        }

        super.deletar(id);
    }
}
