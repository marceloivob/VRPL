package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.bc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto.PLQDTO;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@DisplayName(value = "PLQ BC - Teste integrado")
@TestInstance(Lifecycle.PER_CLASS)
public class PLQBCIT {
    @InjectMocks
    private JDBIFactory jdbiFactory;

    @InjectMocks
    private ConcreteDAOFactory daoFactory;
    
    @Spy
    private PLQBC plqBC;
    
	private final static String usuarioLogado = "00000000000";

	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		// https://github.com/weld/weld-junit/blob/master/junit5/README.md
		return new MockSiconvPrincipal(Profile.MANDATARIA, "1123213");
	}

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        daoFactory.setJdbi(jdbiFactory.createJDBI());

		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

            handle.execute(DataFactory.dependenciasPLQ());
        });
        plqBC.setDao(daoFactory);
    }
    
    @Test
    @Order(1)
    public void recuperarPLQ () {
        //com eventos
        PLQDTO dto = plqBC.recuperarPLQ(1L);
        assertNotNull(dto);
        assertTrue(dto.isPorEvento());
        assertNotNull(dto.getMacroservicos());
        assertTrue(dto.getMacroservicos().size() == 1);
        assertNotNull(dto.getMacroservicos().get(0).getServicos());
        assertTrue(dto.getMacroservicos().get(0).getServicos().size() == 3);
        
        //testar valores
        assertTrue(dto.getMacroservicos().get(0).getPrecoTotalLicitado().longValue() == 1110000);
        
        
        dto = plqBC.recuperarPLQ(2L);
        assertNotNull(dto);
        assertTrue(!dto.isPorEvento());
        assertNotNull(dto.getMacroservicos());
        assertTrue(dto.getMacroservicos().size() == 1);
        assertNotNull(dto.getMacroservicos().get(0).getServicos());
        assertTrue(dto.getMacroservicos().get(0).getServicos().size() == 3);
        
      //testar valores
        assertTrue(dto.getMacroservicos().get(0).getPrecoTotalLicitado().longValue() == 1110000);
        
       
    }
                    
}
