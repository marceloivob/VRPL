package br.gov.planejamento.siconv.mandatarias.architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public class GarantiaDeCoberturaDosDAOs {

	@Test
	public void verifica() {
		JavaClasses classes = new ClassFileImporter().importClasspath();

		List<JavaClass> listaDeDAOs = classes.stream().filter(dao -> dao.getName().endsWith("DAO"))
				.collect(Collectors.toList());

		List<JavaClass> listaDeTestesIntegradosDosDAOS = classes.stream().filter(dao -> dao.getName().endsWith("DAOIT"))
				.collect(Collectors.toList());

		Map<JavaClass, JavaClass> associacao = associaODaoAoRespectivoTesteIntegrado(listaDeDAOs,
				listaDeTestesIntegradosDosDAOS);

		associacao.forEach((dao, testeIntegrado) -> analisa(dao, testeIntegrado));
		
		daoSemTeste(listaDeDAOs, associacao);

	}

	private void daoSemTeste(List<JavaClass> listaDeDAOs, Map<JavaClass, JavaClass> associacao) {
		//DAOs que não têm teste ainda
		for (JavaClass dao : listaDeDAOs) {
			if(!associacao.containsKey(dao)) {
				System.out.println(String.format("%s | Total de Métodos | %d | Total de Métodos criados | %d",
						dao.getSimpleName(), dao.getMethods().size(), 0));
			}
		}
	}

	private List<String> analisa(JavaClass dao, JavaClass testeIntegrado) {

		Set<JavaMethod> listaDeMetodosDosDAOs = dao.getMethods();

		Set<JavaMethod> listaDeMetodosDosTestesIntegradosDosDAOS = testeIntegrado.getMethods();

		List<String> metodosDosDAOsSemParametros = listaDeMetodosDosDAOs.stream().map(metodo -> metodo.getName())
				.collect(Collectors.toList());

		List<String> metodosDosTestesIntegradosSemParametros = listaDeMetodosDosTestesIntegradosDosDAOS.stream()
				.map(metodo -> metodo.getName()).collect(Collectors.toList());

		int totalDeMetodos = metodosDosDAOsSemParametros.size();

		int totalCriado = 0;

		for (String nomeDoMetodoDoDAO : metodosDosDAOsSemParametros) {

			for (String nomeDoMetodoDoTesteIntegrado : metodosDosTestesIntegradosSemParametros) {

				if (nomeDoMetodoDoDAO.equals(nomeDoMetodoDoTesteIntegrado)) {
					totalCriado++;
				}
			}
		}

		System.out.println(String.format("%s | Total de Métodos | %d | Total de Métodos criados | %d",
				dao.getSimpleName(), totalDeMetodos, totalCriado));

		return new ArrayList<>();
	}

	private Map<JavaClass, JavaClass> associaODaoAoRespectivoTesteIntegrado(List<JavaClass> listaDeDAOs,
			List<JavaClass> listaDeTestesIntegradosDosDAOS) {
		Map<JavaClass, JavaClass> associacao = new HashMap<>();

		listaDeDAOs.forEach(dao -> {
			Optional<JavaClass> classe = listaDeTestesIntegradosDosDAOS.stream()
					.filter(testeIntegrado -> testeIntegrado.getName().contains(dao.getName())).findFirst();
			classe.ifPresent(testeIntegrado -> associacao.put(dao, testeIntegrado));
		});

		return associacao;
	}

}
