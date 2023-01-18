package br.gov.serpro.vrpl.grpc.submeta;

import static java.util.stream.Collectors.toList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

import br.gov.serpro.vrpl.grpc.Lote;
import br.gov.serpro.vrpl.grpc.Lote.Submeta;
import br.gov.serpro.vrpl.grpc.PropostaLote;

public class LoteSubmetaReducer implements RowReducer<Map<Integer, Lote.Builder>, PropostaLote> {

	private PropostaLote.Builder propostaLote = null;

	@Override
	public void accumulate(Map<Integer, Lote.Builder> container, RowView rowView) {

		if (propostaLote == null) {
			propostaLote = PropostaLote.newBuilder();

			propostaLote.setAnoConvenio(rowView.getColumn("ano_convenio", Integer.class));
			propostaLote.setNumeroConvenio(rowView.getColumn("numero_convenio", Integer.class));
			propostaLote.setNomeObjeto(rowView.getColumn("nome_objeto", String.class));
			propostaLote.setUf(rowView.getColumn("uf", String.class));
			propostaLote.setModalidade(rowView.getColumn("modalidade", Integer.class));
			propostaLote
					.setPossuiInstituicaoMandataria(rowView.getColumn("possui_instituicao_mandataria", Boolean.class));
			propostaLote.setNomeProponente(rowView.getColumn("nome_proponente", String.class));

		}

		Lote.Builder lote = container.computeIfAbsent(rowView.getColumn("numero_lote", Integer.class),
				numeroLote -> Lote.newBuilder().setNumero(numeroLote));

		Submeta.Builder submeta = Submeta.newBuilder().setId(rowView.getColumn("id_submeta", Long.class))
				.setNumero(rowView.getColumn("numero_submeta", String.class))
				.setDescricao(rowView.getColumn("descricao_submeta", String.class))
				.setRegimeExecucao (getRegimeExecucao(rowView))
				.setValorTotal (getValorTotalSubmeta(rowView))
				.setSituacao(getSituacaoSubmeta(rowView));

		lote.addSubmetas(submeta.build());
	}

	private String getValorTotalSubmeta(RowView rowView) {
		if (rowView.getColumn("id_licitacao", Long.class) != null) {
			return rowView.getColumn("vl_total_licitado", String.class);
		} else {
			return rowView.getColumn("vl_total_analise", String.class);
		}
	}

	private String getRegimeExecucao(RowView rowView) {
		if (rowView.getColumn("id_licitacao", Long.class) != null) {
			return rowView.getColumn("in_regime_execucao", RegimeExecucaoEnum.class).getDescricao();
		} else {
			return rowView.getColumn("in_regime_execucao_analise", RegimeExecucaoEnum.class).getDescricao();
		}
	}

	private String getSituacaoSubmeta(RowView rowView) {
		if (rowView.getColumn("id_licitacao", Long.class) != null) {
			return rowView.getColumn("situacao_submeta", SituacaoSubmetaVRPL.class).getDescricao();
		} else {
			return rowView.getColumn("situacao_analise_submeta", SituacaoSubmetaProjetoBasico.class).getDescricao();
		}
	}

	@Override
	public Map<Integer, Lote.Builder> container() {
		return new LinkedHashMap<>();
	}

	@Override
	public Stream<PropostaLote> stream(Map<Integer, Lote.Builder> container) {

		if (propostaLote != null) {
			List<Lote> lotes = container.values().stream().map(Lote.Builder::build).collect(toList());

			propostaLote.addAllLotes(lotes);

			return Stream.of(propostaLote.build());
		}

		return Stream.empty();
	}
}
