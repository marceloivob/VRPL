package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

public enum Role {

    // Perfis de Concedentes
    ADMINISTRADOR_INTEGRACAO_PROPONENTE,
    ADMINISTRADOR_SISTEMA,
    ANALISTA_JURIDICO_CONCEDENTE,
    ANALISTA_TECNICO_CONCEDENTE,
    AUDITOR_CONTROLE,
    CADASTRADOR_LOCAL,
    CADASTRADOR_PARCIAL,
    CADASTRADOR_PROGRAMA,
    CADASTRADOR_PROJETOS_BANCO_PROJETOS,
    CADASTRADOR_PROPOSTA_CONCEDENTE,
    CADASTRADOR_USUARIO_ORGAO_CONTROLE_CONCEDENTE,
    CONSULTAS_BASICAS_CONCEDENTE,
    FINANCEIRO_CONCEDENTE,
    FISCAL_CONCEDENTE,
    GESTOR_AMBIENTE_TREINAMENTO,
    GESTOR_CONVENIO_CONCEDENTE,
    GESTOR_FINANCEIRO_CONCEDENTE,
    OPERACIONAL_CONCEDENTE,
    OPERACIONAL_FINANCEIRO_CONCEDENTE,
    ORGAO_CONTROLE_CONCEDENTE,
    REGISTRADOR_EMPENHO_SIAFI,
    REPRESENTANTE_COMISSAO_SELECAO,
    RESPONSAVEL_INSTAURAR_TCE,
    TECNICO_CONCEDENTE,
    TOMADOR_TCE,
    WEB_SERVICE_CONSULTA,
    WEB_SERVICE_INSERCAO_EDICAO,

    // Perfis de Proponente/Convenente
    CADASTRADOR_PRESTACAO_CONTAS,
    CADASTRADOR_PROPOSTA,
    CADASTRADOR_USUARIO_ENTE_ENTIDADE,
    CADASTRADOR_USUARIO_ORGAO_CONTROLE_CONVENENTE,
    COMISSAO_LICITACAO,
    CONSULTAS_BASICAS_PROPONENTE,
    DIRIGENTE_REPRESENTANTE,
    FISCAL_CONVENENTE,
    GESTOR_CONVENIO_CONVENENTE,
    GESTOR_FINANCEIRO_CONVENENTE,
    OPERADOR_FINANCEIRO_CONVENENTE,
    ORDENADOR_DESPESA_CONVENENTE,
    ORDENADOR_DESPESA_OBTV,
    ORGAO_CONTROLE_CONVENENTE,
    RESPONSAVEL_CREDENCIAMENTO,
    RESPONSAVEL_EXERCICIO_PROPONENTE,
    RESPONSAVEL_PROPONENTE,
    
    // Perfis de Mandatárias
    ADMINISTRADOR_DOMICILIO_BANCARIO_INST_MANDATARIA,
    AGENTE_ACOMPANHAMENTO_INSTITUICAO_MANDATARIA,
    ANALISTA_JURIDICO_INSTITUICAO_MANDATARIA,
    ANALISTA_TECNICO_INSTITUICAO_MANDATARIA,
    CADASTRADOR_AGENTE_FINANCEIRO_INST_MANDATARIA,
    CADASTRADOR_LOCAL_INSTITUICAO_MANDATARIA,
    CADASTRADOR_PARCIAL_INSTITUICAO_MANDATARIA,
    CADASTRADOR_TCE_INSTITUICAO_MANDATARIA,
    CONSULTAS_AGENTE_FINANCEIRO,
    FINANCEIRO_INSTITUICAO_MANDATARIA,
    GESTOR_CONVENIO_INSTITUICAO_MANDATARIA,
    GESTOR_FINANCEIRO_INSTITUICAO_MANDATARIA,
    OPERACIONAL_FINANCEIRO_INSTITUICAO_MANDATARIA,
    OPERACIONAL_INSTITUICAO_MANDATARIA,
    REGISTRADOR_EMPENHO_SIAFI_INSTITUICAO_MANDATARIA,
    RESPONSAVEL_INSTAURAR_TCE_INSTITUICAO_MANDATARIA,
    TECNICO_INSTITUICAO_MANDATARIA,
    WEB_SERVICE_CONSULTA_INST_MANDATARIA
    
}
