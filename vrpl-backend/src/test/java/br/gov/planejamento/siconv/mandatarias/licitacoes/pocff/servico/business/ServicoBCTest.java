package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;

@DisplayName(value = "ServicoBC")
public class ServicoBCTest {

	@Spy
	private ServicoBC servicoBC;
	
	@Mock
	private UserCanEditVerifier checkPermission;

	@Spy
	private ConstraintBeanValidation<ServicoFrenteObraBD> constraintBeanValidation;
	
	@BeforeEach
	public void setUp() {

		MockitoAnnotations.initMocks(this);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		Validator beanValidator = factory.getValidator();

		constraintBeanValidation.setBeanValidator(beanValidator);

		servicoBC.setBeanValidator(constraintBeanValidation);
		servicoBC.setCheckPermission(checkPermission);
	}
	
	
	@Test
	@DisplayName(value = "Atualizar Valores Submeta - Contrapartida permanecendo positiva")
	public void atualizarValoresSubmetaDeAcordoComSomaServicosPOSemZerarContrapartida() {

		SubmetaBD submeta = new SubmetaBD();

		BigDecimal valorInicialRepasse = new BigDecimal("70000");
		BigDecimal valorInicialOutros = new BigDecimal("20000");

		submeta.setVlRepasse(valorInicialRepasse);
		submeta.setVlOutros(valorInicialOutros);

		BigDecimal somaValoresServicosPO = new BigDecimal("100000");
		
		
		/* RN 755232 SICONV-VRPL-ManterQCI-RN-ValoresSubmetaAjuda
		 * 
		 * contrapartida = 100.000 - 70.000 - 20.000 = 10.000
		 * 
		 * */
		submeta = servicoBC.atualizarValoresSubmetaDeAcordoComSomaServicosPO(submeta, somaValoresServicosPO);
		
		BigDecimal valorEsperadoContrapartida = new BigDecimal("10000");
		BigDecimal valorEsperadoOutros = valorInicialOutros;
		BigDecimal valorEsperadoRepasse = valorInicialRepasse;
		
		assertEquals(somaValoresServicosPO, submeta.getVlTotalLicitado());
		assertEquals(valorEsperadoContrapartida, submeta.getVlContrapartida());
		assertEquals(valorEsperadoOutros, submeta.getVlOutros());
		assertEquals(valorEsperadoRepasse, submeta.getVlRepasse());
	}

	
	
	@Test
	@DisplayName(value = "Atualizar Valores Submeta - Contrapartida ficaria negativa, mas se limita a zero")
	public void atualizarValoresSubmetaDeAcordoComSomaServicosPOZerandoVlContrapartida() {

		SubmetaBD submeta = new SubmetaBD();

		BigDecimal valorInicialRepasse = new BigDecimal("70000");
		BigDecimal valorInicialOutros = new BigDecimal("31000");

		submeta.setVlRepasse(valorInicialRepasse);
		submeta.setVlOutros(valorInicialOutros);

		BigDecimal somaValoresServicosPO = new BigDecimal("100000");
		
		
		/* RN 755232 SICONV-VRPL-ManterQCI-RN-ValoresSubmetaAjuda
		 * 
		 * contrapartida = 100.000 - 70.000 - 31.000 = -1.000
		 * 
		 * contrapartida = 0
		 * 
		 * outros = 100.000 - 70.000 = 30.000
		 * 
		 * repasse inalterado
		 * 
		 * */
		submeta = servicoBC.atualizarValoresSubmetaDeAcordoComSomaServicosPO(submeta, somaValoresServicosPO);
		
		BigDecimal valorEsperadoContrapartida = BigDecimal.ZERO;
		BigDecimal valorEsperadoOutros = new BigDecimal("30000");
		BigDecimal valorEsperadoRepasse = valorInicialRepasse;
		
		assertEquals(somaValoresServicosPO, submeta.getVlTotalLicitado());
		assertEquals(valorEsperadoContrapartida, submeta.getVlContrapartida());
		assertEquals(valorEsperadoOutros, submeta.getVlOutros());
		assertEquals(valorEsperadoRepasse, submeta.getVlRepasse());
	}
	
	@Test
	@DisplayName(value = "Atualizar Valores Submeta - Contrapartida e Outros ficariam negativos, mas se limitam a zero")
	public void atualizarValoresSubmetaDeAcordoComSomaServicosPOZerandoVlContrapartidaEOutros() {

		SubmetaBD submeta = new SubmetaBD();

		BigDecimal valorInicialRepasse = new BigDecimal("70000");
		BigDecimal valorInicialOutros = new BigDecimal("31000");

		submeta.setVlRepasse(valorInicialRepasse);
		submeta.setVlOutros(valorInicialOutros);

		BigDecimal somaValoresServicosPO = new BigDecimal("60000");
		
		
		/* RN 755232 SICONV-VRPL-ManterQCI-RN-ValoresSubmetaAjuda
		 * 
		 * contrapartida = 60.000 - 70.000 - 31.000 = -41.000
		 * 
		 * contrapartida = 0
		 * 
		 * outros = 60.000 - 70.000 = -10.000
		 * 
		 * outros = 0
		 * 
		 * repasse = 60.000
		 * 
		 * */
		submeta = servicoBC.atualizarValoresSubmetaDeAcordoComSomaServicosPO(submeta, somaValoresServicosPO);
		
		BigDecimal valorEsperadoContrapartida = BigDecimal.ZERO;
		BigDecimal valorEsperadoOutros = BigDecimal.ZERO;
		BigDecimal valorEsperadoRepasse = somaValoresServicosPO;
		
		assertEquals(somaValoresServicosPO, submeta.getVlTotalLicitado());
		assertEquals(valorEsperadoContrapartida, submeta.getVlContrapartida());
		assertEquals(valorEsperadoOutros, submeta.getVlOutros());
		assertEquals(valorEsperadoRepasse, submeta.getVlRepasse());
	}

	@Test
	@DisplayName(value = "Atualizar Valores Submeta - Contrapartida fica exatamente zero")
	public void atualizarValoresSubmetaDeAcordoComSomaServicosPOVlContrapartidaValorZero() {

		SubmetaBD submeta = new SubmetaBD();

		BigDecimal valorInicialRepasse = new BigDecimal("70000");
		BigDecimal valorInicialOutros = new BigDecimal("30000");

		submeta.setVlRepasse(valorInicialRepasse);
		submeta.setVlOutros(valorInicialOutros);

		BigDecimal somaValoresServicosPO = new BigDecimal("100000");
		
		
		/* RN 755232 SICONV-VRPL-ManterQCI-RN-ValoresSubmetaAjuda
		 * 
		 * contrapartida = 100.000 - 70.000 - 30.000 = 0
		 * 
		 * contrapartida = 0
		 * 
		 * outros = 100.000 - 70.000 = 30.000
		 * 
		 * repasse inalterado
		 * 
		 * */
		submeta = servicoBC.atualizarValoresSubmetaDeAcordoComSomaServicosPO(submeta, somaValoresServicosPO);
		
		BigDecimal valorEsperadoContrapartida = BigDecimal.ZERO;
		BigDecimal valorEsperadoOutros = new BigDecimal("30000");
		BigDecimal valorEsperadoRepasse = valorInicialRepasse;
		
		assertEquals(somaValoresServicosPO, submeta.getVlTotalLicitado());
		assertEquals(valorEsperadoContrapartida, submeta.getVlContrapartida());
		assertEquals(valorEsperadoOutros, submeta.getVlOutros());
		assertEquals(valorEsperadoRepasse, submeta.getVlRepasse());
	}
	
	@Test
	@DisplayName(value = "Atualizar Valores Submeta - Outros fica exatamente zero")
	public void atualizarValoresSubmetaDeAcordoComSomaServicosPOVlOutrosValorZero() {

		SubmetaBD submeta = new SubmetaBD();

		BigDecimal valorInicialRepasse = new BigDecimal("60000");
		BigDecimal valorInicialOutros = new BigDecimal("31000");

		submeta.setVlRepasse(valorInicialRepasse);
		submeta.setVlOutros(valorInicialOutros);

		BigDecimal somaValoresServicosPO = new BigDecimal("60000");
		
		
		/* RN 755232 SICONV-VRPL-ManterQCI-RN-ValoresSubmetaAjuda
		 * 
		 * contrapartida = 60.000 - 60.000 - 31.000 = -31.000
		 * 
		 * contrapartida = 0
		 * 
		 * outros = 60.000 - 60.000 = 0
		 * 
		 * outros = 0
		 * 
		 * repasse = 60.000
		 * 
		 * */
		submeta = servicoBC.atualizarValoresSubmetaDeAcordoComSomaServicosPO(submeta, somaValoresServicosPO);
		
		BigDecimal valorEsperadoContrapartida = BigDecimal.ZERO;
		BigDecimal valorEsperadoOutros = BigDecimal.ZERO;
		BigDecimal valorEsperadoRepasse = somaValoresServicosPO;
		
		assertEquals(somaValoresServicosPO, submeta.getVlTotalLicitado());
		assertEquals(valorEsperadoContrapartida, submeta.getVlContrapartida());
		assertEquals(valorEsperadoOutros, submeta.getVlOutros());
		assertEquals(valorEsperadoRepasse, submeta.getVlRepasse());
	}
}

