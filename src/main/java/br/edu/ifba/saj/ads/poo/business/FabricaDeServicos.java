package br.edu.ifba.saj.ads.poo.business;

import br.edu.ifba.saj.ads.poo.data.AtletaDAO;
import br.edu.ifba.saj.ads.poo.data.CompeticaoDAO;
import br.edu.ifba.saj.ads.poo.data.InscricaoDAO;
import br.edu.ifba.saj.ads.poo.data.ResultadoDAO;
import br.edu.ifba.saj.ads.poo.data.UsuarioDAO;
import br.edu.ifba.saj.ads.poo.model.Atleta;
import br.edu.ifba.saj.ads.poo.model.Competicao;
import br.edu.ifba.saj.ads.poo.model.Perfil;
import br.edu.ifba.saj.ads.poo.model.Usuario;

// junta os serviços com seus DAOs num lugar só, assim a tela não precisa conhecer o pacote data
public class FabricaDeServicos {

    // instância única usada pelas telas, pra login e cadastro enxergarem sempre os mesmos dados
    private static FabricaDeServicos instancia;

    private final InscricaoServiceI inscricaoService;
    private final ResultadoServiceI resultadoService;
    private final GenericService<Atleta, Long> atletaService;
    private final GenericService<Competicao, Long> competicaoService;
    private final UsuarioServiceI usuarioService;

    public FabricaDeServicos() {
        // inscrição e resultado vêm primeiro porque atleta e competição dependem deles
        inscricaoService = new InscricaoService(new InscricaoDAO());
        resultadoService = new ResultadoService(new ResultadoDAO());
        atletaService = new AtletaService(new AtletaDAO(), inscricaoService, resultadoService);
        competicaoService = new CompeticaoService(new CompeticaoDAO(), inscricaoService, resultadoService);
        usuarioService = new UsuarioService(new UsuarioDAO());

        criarUsuarioAdminPadrao();
    }

    // continua devolvendo a mesma fábrica pra quem pedir, ao invés de criar um "banco" novo do zero
    public static FabricaDeServicos getInstance() {
        if (instancia == null) {
            instancia = new FabricaDeServicos();
        }

        return instancia;
    }

    // o "banco" é em memória e some ao fechar o app, então sem isso nunca ia existir um admin pra logar
    private void criarUsuarioAdminPadrao() {
        if (usuarioService.buscarTodos().isEmpty()) {
            usuarioService.salvar(new Usuario("admin", "admin", Perfil.ADMIN));
        }
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

    public UsuarioServiceI getUsuarioService() {
        return usuarioService;
    }
}
