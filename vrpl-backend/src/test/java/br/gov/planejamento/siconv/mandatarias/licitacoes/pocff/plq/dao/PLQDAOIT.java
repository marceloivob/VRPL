package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.database.FrenteObraComDetalhesBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.database.ServicoComEventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PLQDAOIT {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Configuração dos testes
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @InjectMocks
    private JDBIFactory jdbiFactory;

    @InjectMocks
    private ConcreteDAOFactory daoFactory;

	private final static String usuarioLogado = "00000000000";

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        daoFactory.setJdbi(jdbiFactory.createJDBI());
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.execute(DataFactory.dependenciasPLQ());
        });
    }
    
    @Test
    @Order(1)
    public void recuperarMacroServicoPorIdPO() {
        List<MacroServicoBD> macroServicos = daoFactory.get(PLQDAO.class).recuperarMacroServicoPorIdPO(1L);
        assertNotNull(macroServicos);
        assertTrue(macroServicos.size() == 1);
        assertTrue(macroServicos.get(0).getId() == 1);
    }
    
    @Test
    @Order(2)
    public void recuperarServicosPorPo() {
        List<ServicoBD> servicos = daoFactory.get(PLQDAO.class).recuperarServicosPorPo(2L);
        assertNotNull(servicos);
        assertTrue(servicos.size() == 3);
        assertTrue(servicos.get(0).getVlPrecoTotal().compareTo(new BigDecimal(10000)) == 0);
    }
    
    @Test
    @Order(3)
    public void recuperarServicosComEventoPorPo() {
        List<ServicoComEventoBD> servicos = daoFactory.get(PLQDAO.class).recuperarServicosComEventoPorPo(1L);
        assertNotNull(servicos);
        assertTrue(servicos.size() == 3);
        assertTrue(servicos.get(0).getEventoNome() != null);
        assertTrue(servicos.get(0).getNumeroEvento() != null);
    }
    
    @Test
    @Order(4)
    public void recuperarFrentesDeObraComDetalhesPorPo() {
        List<FrenteObraComDetalhesBD> frentes = daoFactory.get(PLQDAO.class).recuperarFrentesDeObraComDetalhesPorPo(1L);
        assertNotNull(frentes);
        assertTrue(frentes.size() == 9);
        assertTrue(frentes.get(0).getNomeFrenteObra().equals("nm_frente_obra15"));
        assertTrue(frentes.get(0).getNumeroFrenteObra().equals(15));
        assertTrue(frentes.get(0).getQuantidadeItens().longValue() == 32);
    }
    
}
