package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.TipoOrgao.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public enum Perfil {

	// ACOMPANHAMENTO
	FISCAL_ACOMPANHAMENTO("Fiscal Acompanhamento e Fiscalização", ACOMPANHAMENTO),
	SUPERVISOR_ACOMPANHAMENTO("Supervisor Acompanhamento", ACOMPANHAMENTO),
	TECNICO_TERCEIRO("Técnico de Terceiro Acompanhamento e Fiscalização", ACOMPANHAMENTO),

	// AGENDA_COMPROMISSOS
	ADMINISTRADOR_AGENDA_COMPROMISSOS("Administrador da Agenda de Compromissos", AGENDA_COMPROMISSOS),
	GUEST_AGENDA_COMPROMISSOS("Guest Agenda", AGENDA_COMPROMISSOS),
	RELATOR_AGENDA_COMPROMISSOS("Relator da Agenda", AGENDA_COMPROMISSOS),

	// CONCEDENTE
	ADMINISTRADOR_SISTEMA("Administrador do Sistema", CONCEDENTE),
	ANALISTA_JURIDICO_CONCEDENTE("Analista Jurí­dico do Concedente", CONCEDENTE),
	ANALISTA_TECNICO_CONCEDENTE("Analista Técnico do Concedente", CONCEDENTE),
	AUDITOR_CONTROLE("Auditor de Controle Interno", CONCEDENTE),
	CADASTRADOR_LOCAL("Cadastrador Local", CONCEDENTE),
	CADASTRADOR_PARCIAL("Cadastrador Parcial", CONCEDENTE),
	CADASTRADOR_PROGRAMA("Cadastrador de Programa", CONCEDENTE),
	CADASTRADOR_PROJETOS_BANCO_PROJETOS("Cadastrador de Projetos do Banco de Projetos", CONCEDENTE),
	CADASTRADOR_PROPOSTA_CONCEDENTE("Cadastrador de Proposta pelo Concedente", CONCEDENTE),
	TOMADOR_TCE("Tomador da TCE", CONCEDENTE),
	CADASTRADOR_TCE_INSTITUICAO_MANDATARIA("Cadastrador de TCE da Instituição Mandatária", INST_MANDATARIA),
	CADASTRADOR_USUARIO_ORGAO_CONTROLE_CONCEDENTE("Cadastrador de Usuário de Órgão de Controle do Concedente", CONCEDENTE),
	CONSULTAS_BASICAS_CONCEDENTE("Consultas Básicas do Concedente", CONCEDENTE),
	FINANCEIRO_CONCEDENTE("Financeiro do Concedente - Prestação de Contas", CONCEDENTE),
	FISCAL_CONCEDENTE("Fiscal de Execução do Concedente", CONCEDENTE),
	GESTOR_AMBIENTE_TREINAMENTO("Gestor do Ambiente de Treinamento", CONCEDENTE),
	GESTOR_CONVENIO_CONCEDENTE("Gestor de Convênio do Concedente", CONCEDENTE),
	GESTOR_FINANCEIRO_CONCEDENTE("Gestor Financeiro do Concedente", CONCEDENTE),
	OPERACIONAL_CONCEDENTE("Operacional do Concedente", CONCEDENTE),
	OPERACIONAL_FINANCEIRO_CONCEDENTE("Operacional Financeiro do Concedente", CONCEDENTE),
	ORGAO_CONTROLE_CONCEDENTE("Órgão de Controle do Concedente", CONCEDENTE),
	REGISTRADOR_EMPENHO_SIAFI("Registrador de Empenho no SIAFI", CONCEDENTE),
	RESPONSAVEL_INSTAURAR_TCE("Responsável Instaurar TCE", CONCEDENTE),
	RESPONSAVEL_INSTAURAR_TCE_INSTITUICAO_MANDATARIA("Responsável Instaurar TCE da Instituição Mandatária", INST_MANDATARIA),
	TECNICO_CONCEDENTE("Técnico do Concedente - Prestação de Contas", CONCEDENTE),
 
	REPRESENTANTE_COMISSAO_SELECAO("Representante Comissão de Seleção", CONCEDENTE),
	WEB_SERVICE_CONSULTA("Web Service (Consulta)", CONCEDENTE),
	WEB_SERVICE_INSERCAO_EDICAO("Web Service (Inserção e Edição)", CONCEDENTE),

	// INSTITUICAO_MANDATARIA
	ADMINISTRADOR_DOMICILIO_BANCARIO_INST_MANDATARIA("Administrador de Domicílio Bancário", INST_MANDATARIA),
	AGENTE_ACOMPANHAMENTO_INSTITUICAO_MANDATARIA("Agente de Acompanhamento da Instituição Mandatária", INST_MANDATARIA),
	ANALISTA_JURIDICO_INSTITUICAO_MANDATARIA("Analista Jurí­dico da Instituição Mandatária", INST_MANDATARIA),
	ANALISTA_TECNICO_INSTITUICAO_MANDATARIA("Analista Técnico da Instituição Mandatária", INST_MANDATARIA),
	CADASTRADOR_AGENTE_FINANCEIRO_INST_MANDATARIA("Cadastrador de Agente Financeiro", INST_MANDATARIA),
	CADASTRADOR_LOCAL_INSTITUICAO_MANDATARIA("Cadastrador Local da Instituição Mandatária", INST_MANDATARIA),
	CADASTRADOR_PARCIAL_INSTITUICAO_MANDATARIA("Cadastrador Parcial da Instituição Mandatária", INST_MANDATARIA),
	CONSULTAS_AGENTE_FINANCEIRO("Consultas Agente Financeiro", INST_MANDATARIA),
	FINANCEIRO_INSTITUICAO_MANDATARIA("Financeiro da Instituição Mandatária - Prestação de Contas", INST_MANDATARIA),
	GESTOR_CONVENIO_INSTITUICAO_MANDATARIA("Gestor de Convênio da Instituição Mandatária", INST_MANDATARIA),
	GESTOR_FINANCEIRO_INSTITUICAO_MANDATARIA("Gestor Financeiro da Instituição Mandatária", INST_MANDATARIA),
	OPERACIONAL_FINANCEIRO_INSTITUICAO_MANDATARIA("Operacional Financeiro da Instituição Mandatária", INST_MANDATARIA),
	OPERACIONAL_INSTITUICAO_MANDATARIA("Operacional da Instituição Mandatária", INST_MANDATARIA),
	REGISTRADOR_EMPENHO_SIAFI_INSTITUICAO_MANDATARIA("Registrador de Empenho no SIAFI da Instituição Mandatária", INST_MANDATARIA),
	TECNICO_INSTITUICAO_MANDATARIA("Técnico da Instituição Mandatária - Prestação de Contas", INST_MANDATARIA),

	// IMPRENSA_NACIONAL
	OPERADOR_IMPRENSA_NACIONAL("Operador da Imprensa Nacional", IMPRENSA_NACIONAL),

	// ORGAO_MAXIMO
	CADASTRADOR_GERAL("Cadastrador Geral", ORGAO_MAXIMO),
	GESTOR_PROJETOS_BANCO_PROJETOS("Gestor de Projetos do Banco de Projetos", ORGAO_MAXIMO),

	// PROPONENTE
	RESPONSAVEL_EXERCICIO_PROPONENTE("Responsável em Exercício do Proponente", PROPONENTE),
	CADASTRADOR_PRESTACAO_CONTAS("Cadastrador de Prestação de Contas", PROPONENTE),
	CADASTRADOR_PROPOSTA("Cadastrador de Proposta", PROPONENTE),
	CADASTRADOR_USUARIO_ENTE_ENTIDADE("Cadastrador de Usuário do Ente/Entidade", PROPONENTE),
	CADASTRADOR_USUARIO_ORGAO_CONTROLE_CONVENENTE("Cadastrador de Usuário de Órgão de Controle do Convenente", PROPONENTE),
	COMISSAO_LICITACAO("Comissão de Licitação", PROPONENTE),
	CONSULTAS_BASICAS_PROPONENTE("Consultas Básicas do Proponente", PROPONENTE),
	DIRIGENTE_REPRESENTANTE("Dirigente/Representante", PROPONENTE),
	FISCAL_CONVENENTE("Fiscal do Convenente", PROPONENTE),
	GESTOR_CONVENIO_CONVENENTE("Gestor de Convênio do Convenente", PROPONENTE),
	GESTOR_FINANCEIRO_CONVENENTE("Gestor Financeiro do Convenente", PROPONENTE),
	OPERADOR_FINANCEIRO_CONVENENTE("Operador Financeiro do Convenente", PROPONENTE),
	ORDENADOR_DESPESA_CONVENENTE("Ordenador de Despesa do Convenente", PROPONENTE),
	ORDENADOR_DESPESA_OBTV("Ordenador de Despesa OBTV", PROPONENTE),
	ORGAO_CONTROLE_CONVENENTE("Órgão de Controle do Convenente", PROPONENTE),
	RESPONSAVEL_CREDENCIAMENTO("Responsável pelo Credenciamento", PROPONENTE),
	RESPONSAVEL_PROPONENTE("Responsável pelo Proponente", PROPONENTE),

	// TRIBUNAL
	//ATENCAO, CASO SURJA UM NOVO PERFIL CRIADO PARA TRIBUNAL DEVE SER COLOCADO NO METODOS QUE VALIDAM A POSSIBILIDADE DE ATRIBUICAO DESTE
	//ParticipeHandlerBeanImpl.perfilPodeAtribuidoATribunal
	//ParticipeHandlerBeanImpl.perfilPodeAtribuidoSomenteAOrgaosQueSaoExclusivamenteTribunal
	CADASTRADOR_IRREGULARIDADE_TRIBUNAL("Cadastrador Irregularidades Tribunal", TRIBUNAL),
	RESPONSAVEL_REGISTRO_IRREGULARIDADE_TRIBUNAL("Responsável Registro Irregularidades Tribunal", TRIBUNAL),

	//ESSES PERFIS PODEM SER DADOS AOS RESPECTIVOS ORGAOS, NAO PODEM SER DADOS A TRIBUNAIS
	GESTOR_REGISTRO_IRREGULARIDADE_TRIBUNAL_MPOG("Gestor Registro Irregularidades Tribunal", TRIBUNAL),
	GESTOR_REGISTRO_IRREGULARIDADE_TRIBUNAL_CGU("Gestor Registro Irregularidades Tribunal", TRIBUNAL),

	ADMINISTRADOR_INTEGRACAO_PROPONENTE("Administrador de Integração pelo proponente", PROPONENTE),

	// sistema
	guest("Guest", sistema),

	// UNIDADE_CADASTRADORA
	CADASTRADOR_PROPONENTE("Cadastrador de Proponente", UNIDADE_CADASTRADORA),
	
	// COMISSAO_SELECAO
	MEMBRO_COMISSAO_SELECAO("Membro de Comissão de Seleção", COMISSAO_SELECAO);

	public static final Collection<Perfil> proponentes = EnumSet.of(GESTOR_CONVENIO_CONVENENTE,
			GESTOR_FINANCEIRO_CONVENENTE, OPERADOR_FINANCEIRO_CONVENENTE, FISCAL_CONVENENTE);

	public static final Collection<Perfil> concedentes = EnumSet.of(ANALISTA_TECNICO_CONCEDENTE,
			GESTOR_CONVENIO_CONCEDENTE);

	public static final Collection<Perfil> mandatarias = EnumSet.of(ANALISTA_TECNICO_INSTITUICAO_MANDATARIA,
			GESTOR_CONVENIO_INSTITUICAO_MANDATARIA);

	public static Perfil getPerfil(String codigo) {
		if (StringUtils.isBlank(codigo)) {
			return null;
		}
		for (Perfil perfil : Perfil.values()) {
			if (perfil.name().equals(codigo)) {
				return perfil;
			}
		}
		return null;
	}

	public static List<Perfil> getPerfisPorTipoOrgao(TipoOrgao... tiposOrgao) {
		List<Perfil> perfis = new ArrayList<Perfil>();
		for (Perfil perfil : Perfil.values()) {
			for (TipoOrgao tipoOrgao : tiposOrgao) {
				if (perfil.getTipoOrgao().equals(tipoOrgao)) {
					perfis.add(perfil);
				}
			}
		}
		return perfis;
	}

	private String descricao;

	private TipoOrgao tipoOrgao;

	private Perfil(String descricao, TipoOrgao tipoOrgao) {
		this.descricao = descricao;
		this.tipoOrgao = tipoOrgao;
	}

	public String getDescricao() {
		return descricao;
	}

	// Este método permite que Perfil seja carregado em combos, fornecendo um
	// getter para a propriedade name
	public String getName() {
		return name();
	}

	public TipoOrgao getTipoOrgao() {
		return tipoOrgao;
	}

	public String toString() {
		return this.name();
	}

}
