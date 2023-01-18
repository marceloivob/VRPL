import { Component, OnInit } from '@angular/core';
import { Store } from '@ngxs/store';
import { Observable } from 'rxjs';
import { QciStateModel, QciExternoStateModel } from '../../model/qci/qci.state.model';
import { QciState } from '../../model/qci/qci.state';
import { filter, take, map } from 'rxjs/operators';
import { LoadQciProposta } from 'src/app/model/qci/qci.actions';
import { DataExport } from 'src/app/model/data-export';
import { PropostaState } from 'src/app/model/proposta/proposta.state';

@Component({
  selector: 'vrpl-qci-externo',
  templateUrl: './qci-externo.component.html'
})
export class QciExternoComponent implements OnInit {

  qciObservable: Observable<QciExternoStateModel>;

  // Grid
  lista: any[];
  export: DataExport;

  constructor(private readonly store: Store) {
  }

  ngOnInit() {
    this.store.select(PropostaState).subscribe(proposta => {
      this.store.dispatch(new LoadQciProposta(proposta.versaoSelecionada));
      this.qciObservable = this.store.select(QciState)
        .pipe(
          filter(qci => qci.metas != null),
          map(qci => this.getExport(qci))
        );
    });
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  getExport(qci: QciExternoStateModel): QciExternoStateModel {
    const data = [];
    const columns = [
      'Número da Meta', 'Item de Investimento', 'Descrição da Meta',
      'Quantidade', 'Unidade', 'Repasse da Meta', 'Contrapartida da Meta', 'Total da Meta',
      'Submeta', 'Situação', 'Lote de Licitação', 'Repasse da Submeta', 'Contrapartida da Submeta', 'Outros da Submeta', 'Total da Submeta'
    ];

    qci.metas.forEach(meta => {
      meta.submetas.forEach(submeta => {
        const linha = [];

        linha.push(meta.numero);
        linha.push(meta.itemInvestimento);
        linha.push(meta.descricao);
        linha.push(meta.quantidade);
        linha.push(meta.unidade);
        linha.push(qci.vlRepasse);
        linha.push(qci.vlContrapartida);
        linha.push(qci.vlTotal);

        linha.push(meta.numero + '.' + submeta.numero + ' - ' + submeta.descricao);

        if (submeta.descricaoSituacao) {
          linha.push(submeta.descricaoSituacao);
        }

        if (submeta.descricaoSituacaoVerificacaoLicitacao) {
          linha.push(submeta.descricaoSituacaoVerificacaoLicitacao);
        }

        linha.push(submeta.lote);
        linha.push(submeta.vlRepasse);
        linha.push(submeta.vlContrapartida);
        linha.push(submeta.vlOutros);
        linha.push(submeta.vlTotal);

        data.push(linha);
      });
    });

    this.export = new DataExport(columns, data);

    return qci;
  }

}
