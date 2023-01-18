package br.gov.planejamento.siconv.mandatarias.test.core;

import java.util.List;

import br.gov.serpro.siconv.grpc.ClientLicitacoesInterface;
import br.gov.serpro.siconv.grpc.GenericResponse;
import br.gov.serpro.siconv.grpc.LicitacoesResponse;
import br.gov.serpro.siconv.grpc.ListaProcessoExecucaoResponse;
import br.gov.serpro.siconv.grpc.ListaProcessoExecucaoResponse.ProcessoExecucao;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;

public class MockClientLicitacoesInterface implements ClientLicitacoesInterface {

	@Override
	public ProcessoExecucaoResponse detalharProcessosExecucao(Long idLicitacao) {

		if (idLicitacao == null) {
			return null;
		}

		if (idLicitacao.equals(637290L)) {
			ProcessoExecucaoResponse.Builder pe1 = ProcessoExecucaoResponse.newBuilder();
			pe1.setId(637290);
			pe1.setNumeroAno("012019");
			pe1.setObjeto(
					"itens 1.1 - 1.2 - 1.3 - 1.4 - 1.5 - Data de Homologação atualizada de 2019-02-17 para 2019-09-01");
			pe1.setStatus("Concluído");
			pe1.setDataHomologacao("2019-09-01");
			pe1.setDataPublicacaoEdital("2019-02-15");
			pe1.setDescricaoProcesso("Licitação");
			pe1.setRegimeContratacao("Lei 10.520/2002 - Pregão");
			pe1.setModalidade("Pregão");
			pe1.setValor(32500.00);

			return pe1.build();
		} else if (idLicitacao.equals(615289L)) {
			ProcessoExecucaoResponse.Builder pe2 = ProcessoExecucaoResponse.newBuilder();
			pe2.setId(615289L);
			pe2.setNumeroAno("006/2018");
			pe2.setObjeto("Contratação de empresa para a prestação de...");
			pe2.setStatus("Concluído");
			pe2.setDataHomologacao("2018-08-29");
			pe2.setDataPublicacaoEdital("2018-08-01");
			pe2.setDescricaoProcesso("Licitação");
			pe2.setRegimeContratacao("Lei 8.666/1993");
			pe2.setModalidade("Tomada de Preços");
			pe2.setValor(251395.44);

			return pe2.build();

		} else if (idLicitacao.equals(637291L)) {
			ProcessoExecucaoResponse.Builder pe2 = ProcessoExecucaoResponse.newBuilder();
			pe2.setId(637291L);
			pe2.setNumeroAno("022019");
			pe2.setObjeto("ITENS 1.6, 1.7 E 1.8 - atualizado o valor de 378099.23 para 38000.00");
			pe2.setStatus("Concluído");
			pe2.setDataHomologacao("2019-02-22");
			pe2.setDataPublicacaoEdital("2019-02-16");
			pe2.setDescricaoProcesso("Licitação");
			pe2.setRegimeContratacao("Lei 8.666/1993");
			pe2.setModalidade("Tomada de Preços");
			pe2.setValor(38000.00);

			return pe2.build();
		} else if (idLicitacao.equals(3325L)) {
			ProcessoExecucaoResponse.Builder pe1 = ProcessoExecucaoResponse.newBuilder();
			pe1.setId(3325L);
			pe1.setNumeroAno("052019");
			pe1.setObjeto("Licitação 3325");
			pe1.setStatus("Concluído");
			pe1.setDataHomologacao("");
			pe1.setDataPublicacaoEdital("");
			pe1.setDescricaoProcesso("Dispensa de Licitação");
			pe1.setRegimeContratacao("");
			pe1.setModalidade("");
			pe1.setValor(22000.0);

			return pe1.build();
		} else if (idLicitacao.equals(4321L)) {
			ProcessoExecucaoResponse.Builder pe1 = ProcessoExecucaoResponse.newBuilder();
			pe1.setId(4321L);
			pe1.setNumeroAno("052019");
			pe1.setObjeto("Licitação 4321");
			pe1.setStatus("Concluído");
			pe1.setDataHomologacao("");
			pe1.setDataPublicacaoEdital("");
			pe1.setDescricaoProcesso("Dispensa de Licitação");
			pe1.setRegimeContratacao("");
			pe1.setModalidade("");
			pe1.setValor(22000.0);

			return pe1.build();
		} else if (idLicitacao.equals(98173L)) {
			ProcessoExecucaoResponse.Builder pe1 = ProcessoExecucaoResponse.newBuilder();
			pe1.setId(98173L);
			pe1.setNumeroAno("052019");
			pe1.setObjeto("Licitação 98173");
			pe1.setStatus("Concluído");
			pe1.setDataHomologacao("");
			pe1.setDataPublicacaoEdital("");
			pe1.setDescricaoProcesso("Dispensa de Licitação");
			pe1.setRegimeContratacao("");
			pe1.setModalidade("");
			pe1.setValor(22000.0);

			return pe1.build();
		} else if (idLicitacao.equals(6475L)) {
			ProcessoExecucaoResponse.Builder pe1 = ProcessoExecucaoResponse.newBuilder();
			pe1.setId(6475L);
			pe1.setNumeroAno("052019");
			pe1.setObjeto("Licitação 6475");
			pe1.setStatus("Concluído");
			pe1.setDataHomologacao("");
			pe1.setDataPublicacaoEdital("");
			pe1.setDescricaoProcesso("Dispensa de Licitação");
			pe1.setRegimeContratacao("");
			pe1.setModalidade("");
			pe1.setValor(22000.0);

			return pe1.build();

		} else if (idLicitacao.equals(1L)) {
			ProcessoExecucaoResponse.Builder pe1 = ProcessoExecucaoResponse.newBuilder();
			pe1.setId(1);
			pe1.setNumeroAno("012019D");
			pe1.setObjeto("Dispensa para acompanhamento dos projetos sociais");
			pe1.setStatus("Concluído");
			pe1.setDataHomologacao("");
			pe1.setDataPublicacaoEdital("");
			pe1.setDescricaoProcesso("Dispensa de Licitação");
			pe1.setRegimeContratacao("");
			pe1.setModalidade("");
			pe1.setValor(22000.0);

			return pe1.build();

		} else if (idLicitacao.equals(2L)) {
			ProcessoExecucaoResponse.Builder pe3 = ProcessoExecucaoResponse.newBuilder();
			pe3.setId(2);
			pe3.setNumeroAno("032019");
			pe3.setObjeto("ITENS 1.9 a 1.13  - Licitação com Registro de preço para colocar mais de  um vencedor");
			pe3.setStatus("Concluído");
			pe3.setDataHomologacao("");
			pe3.setDataPublicacaoEdital("");
			pe3.setDescricaoProcesso("Licitação");
			pe3.setRegimeContratacao("");
			pe3.setModalidade("");

			return pe3.build();

		} else if (idLicitacao.equals(3L)) {

			ProcessoExecucaoResponse.Builder pe3 = ProcessoExecucaoResponse.newBuilder();
			pe3.setId(3);
			pe3.setNumeroAno("032019");
			pe3.setObjeto("ITENS 1.9 a 1.13  - Licitação com Registro de preço para colocar mais de  um vencedor");
			pe3.setStatus("Concluído");
			pe3.setDataHomologacao("");
			pe3.setDataPublicacaoEdital("");
			pe3.setDescricaoProcesso("Licitação");
			pe3.setRegimeContratacao("");
			pe3.setModalidade("");

			return pe3.build();

		} else if (idLicitacao.equals(4L)) {

			ProcessoExecucaoResponse.Builder pe4 = ProcessoExecucaoResponse.newBuilder();
			pe4.setId(4);
			pe4.setNumeroAno("022019");
			pe4.setObjeto("ITENS 1.6, 1.7 E 1.8");
			pe4.setStatus("Concluído");
			pe4.setDataHomologacao("");
			pe4.setDataPublicacaoEdital("");
			pe4.setDescricaoProcesso("Licitação");
			pe4.setRegimeContratacao("");
			pe4.setModalidade("");

			return pe4.build();
		} else if (idLicitacao.equals(5L)) {
			ProcessoExecucaoResponse.Builder pe = ProcessoExecucaoResponse.newBuilder();
			pe.setStatus("Em Preenchimento");
			pe.setNumeroAno("022019");

			return pe.build();
		}


		return null;
	}

	@Override
	public ListaProcessoExecucaoResponse listarProcessosExecucao(Long identificadorDaProposta) {
		ListaProcessoExecucaoResponse.Builder builder = ListaProcessoExecucaoResponse.newBuilder();

		Long casoDeTeste = 1293027L;

		if (identificadorDaProposta.equals(casoDeTeste)) {
			ProcessoExecucao.Builder pe1 = ProcessoExecucao.newBuilder();
			pe1.setId(637291);

			builder.addListaProcessoExecucao(pe1);

			ProcessoExecucao.Builder pe2 = ProcessoExecucao.newBuilder();
			pe2.setId(637290);

			builder.addListaProcessoExecucao(pe2);

			ProcessoExecucao.Builder pe3 = ProcessoExecucao.newBuilder();
			pe3.setId(1);

			builder.addListaProcessoExecucao(pe3);

			ProcessoExecucao.Builder pe4 = ProcessoExecucao.newBuilder();
			pe4.setId(2);

			builder.addListaProcessoExecucao(pe4);

			ProcessoExecucao.Builder pe5 = ProcessoExecucao.newBuilder();
			pe5.setId(3);

			builder.addListaProcessoExecucao(pe5);

			ProcessoExecucao.Builder pe6 = ProcessoExecucao.newBuilder();
			pe6.setId(4);

			builder.addListaProcessoExecucao(pe6);
		}

		return builder.build();
	}

	public GenericResponse liveness(String arg0) {
		return GenericResponse.getDefaultInstance();
	}

	@Override
	public void shutdown() throws Exception {

	}

	@Override
	public LicitacoesResponse consultaLicitacoesEmApostilamento(List<Long> idLicitacoes) {
		// TODO Auto-generated method stub
		return null;
	}

}
