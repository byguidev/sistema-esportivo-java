package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.GenericDAO;
import br.edu.ifba.saj.ads.poo.model.Atleta;

public class AtletaService extends GenericServiceImpl<Atleta, Long> {

    private final InscricaoServiceI inscricaoService;
    private final ResultadoServiceI resultadoService;

    public AtletaService(GenericDAO<Atleta, Long> dao,
                         InscricaoServiceI inscricaoService,
                         ResultadoServiceI resultadoService) {
        super(dao);
        this.inscricaoService = inscricaoService;
        this.resultadoService = resultadoService;
    }

    @Override
    public void validar(Atleta atleta) throws RegraDeNegocioException {
        // verifica se o atleta não é null
        if (atleta == null) {
            throw new RegraDeNegocioException("Dados do atleta são obrigatórios.");
        }

        // nome não pode ser vazio
        if (atleta.getNome() == null || atleta.getNome().trim().isEmpty()) {
            throw new RegraDeNegocioException("Nome do atleta é obrigatório.");
        }

        // categoria também é obrigatória
        if (atleta.getCategoria() == null || atleta.getCategoria().trim().isEmpty()) {
            throw new RegraDeNegocioException("Categoria do atleta é obrigatória.");
        }
    }

    @Override
    public void deletar(Long id) {
        Atleta atleta = buscarPorId(id);

        // limpa o que depende do atleta pra não sobrar inscrição nem pódio órfão
        if (atleta != null) {
            resultadoService.removerPorAtleta(atleta);
            inscricaoService.removerPorAtleta(atleta);
        }

        super.deletar(id);
    }
}
