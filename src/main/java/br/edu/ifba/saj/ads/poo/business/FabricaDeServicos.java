package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.AtletaDAO;
import br.edu.ifba.saj.ads.poo.data.CompeticaoDAO;
import br.edu.ifba.saj.ads.poo.data.InscricaoDAO;
import br.edu.ifba.saj.ads.poo.data.ResultadoDAO;
import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;

// junta os serviços com seus DAOs num lugar só, assim a tela não precisa conhecer o pacote data
public class FabricaDeServicos {

    private final InscricaoServiceI inscricaoService;
    private final ResultadoServiceI resultadoService;
    private final GenericService<Atleta, Long> atletaService;
    private final GenericService<Competicao, Long> competicaoService;

    public FabricaDeServicos() {
        // inscrição e resultado vêm primeiro porque atleta e competição dependem deles
        inscricaoService = new InscricaoService(new InscricaoDAO());
        resultadoService = new ResultadoService(new ResultadoDAO());
        atletaService = new AtletaService(new AtletaDAO(), inscricaoService, resultadoService);
        competicaoService = new CompeticaoService(new CompeticaoDAO(), inscricaoService, resultadoService);
    }

    public GenericService<Atleta, Long> getAtletaService() {
        return atletaService;
    }

    public GenericService<Competicao, Long> getCompeticaoService() {
        return competicaoService;
    }

    public InscricaoServiceI getInscricaoService() {
        return inscricaoService;
    }

    public ResultadoServiceI getResultadoService() {
        return resultadoService;
    }
}
