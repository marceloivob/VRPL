package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento;

import java.util.ArrayList;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Alteracao;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;

public class ObjectDiffer {

	private List<Alteracao<?>> changesFound = new ArrayList<>();

	public List<Alteracao<?>> extractChanges(Object working, Object base) {
		DiffNode diff = ObjectDifferBuilder.buildDefault().compare(working, base);

		diff.visit(new DiffNode.Visitor() {
			@Override
			public void node(DiffNode node, Visit visit) {
				final Object baseValue = node.canonicalGet(working);
				final Object workingValue = node.canonicalGet(base);
				String fieldName = node.getPath().toString().replace("/", "");

				Alteracao change = new Alteracao().oCampo(fieldName).mudouDe(baseValue).para(workingValue);

				if (!"".equals(fieldName)) {
					changesFound.add(change);

				}
			}

		});

		return changesFound;
	}

}
