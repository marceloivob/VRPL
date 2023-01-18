import { Component } from '@angular/core';
import { Store } from '@ngxs/store';
import { Observable } from 'rxjs';
import { QciStateModel } from '../../../../../model/qci/qci.state.model';
import { map, filter, distinctUntilChanged, take } from 'rxjs/operators';
import { LoadQciLicitacao } from 'src/app/model/qci/qci.actions';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { DataExport } from 'src/app/model/data-export';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';


interface LoteListagem {
  numero: number;
  rowspan: number;
  metas: MetaListagem[];
}

interface MetaListagem {
  descricao: string;
  numero: number;
  rowspan: number;
  submetas: SubmetaListagem[];
}

interface SubmetaListagem {
  descricao: string;
  numero: number;
  descricaoSituacao: string;
  situacaoDoProcessoLicitatorio: string;
  idSubmeta: number;
  repasseLicitado: number;
  contrapartidaLicitada: number;
  totalLicitado: number;
  vlOutros: number;
  exibeLote: boolean;
  exibeMeta: boolean;
}

interface QciListagem {
  repasseLicitado: number;
  contrapartidaLicitada: number;
  totalLicitado: number;
  difSocialContrapartidaLicitada: number;
  difSocialTotalLicitado: number;
  difMetasContrapartidaLicitada: number;
  difMetasTotalLicitado: number;
  difValorOrcadoRepasseLicitado: number;
  difValorOrcadoContrapartidaLicitada: number;
  difValorOrcadoTotalLicitado: number;
  modalidade: string;
  lotes: LoteListagem[];
}

@Component({
  selector: 'vrpl-listagem-qci',
  templateUrl: './listagem-qci.component.html'
})
export class ListagemQciComponent extends BaseComponent {

  qciObservable: Observable<QciListagem>;
  possuiValorInformadoParaOutros = false;

  // Grid
  export: DataExport;
  lista: any[];

  constructor(
    protected readonly store: Store,
    private readonly auth: UserAuthorizerService,
    private route: ActivatedRoute
  ) {
    super(store);
  }

  init() {
    this.qciObservable = this.store.select(LicitacaoDetalhadaState.qciLicitacaoDetalhada)
      .pipe(
        filter(metas => metas != null),
        distinctUntilChanged(),
        map(qci => this.mapToRow(qci)),
        map(qciListagem => this.getExport(qciListagem))
      );
  }

  loadActions() {
    return new LoadQciLicitacao();
  }

  mapToRow(qci: QciStateModel): QciListagem {
    this.possuiValorInformadoParaOutros = false;

    const lotes: LoteListagem[] = [];

    qci.metas.forEach(meta => {

      meta.submetas.forEach(submeta => {
        let lote = lotes.find(loteL => loteL.numero === submeta.lote);
        if (!lote) {
          lote = {
            numero: submeta.lote,
            rowspan: 0,
            metas: []
          };
          lotes.push(lote);
        }

        let metaListagem = lote.metas.find(metaL => metaL.numero === meta.numero);
        if (!metaListagem) {
          metaListagem = {
            descricao: `${meta.numero} - ${meta.descricao}`,
            numero: meta.numero,
            rowspan: 0,
            submetas: []
          };
          lote.metas.push(metaListagem);
        }



        const submetaListagem: SubmetaListagem = {
          descricao: `${meta.numero}.${submeta.numero} - ${submeta.descricao}`,
          numero: submeta.numero,
          descricaoSituacao: submeta.descricaoSituacao,
          situacaoDoProcessoLicitatorio: submeta.descricaoSituacaoVerificacaoLicitacao,
          idSubmeta: submeta.id,
          repasseLicitado: submeta.vlRepasse,
          contrapartidaLicitada: submeta.vlContrapartida,
          totalLicitado: submeta.vlTotal,
          vlOutros: submeta.vlOutros,
          exibeLote: true,
          exibeMeta: true
        };

        if (submeta.vlOutros > 0) {
          this.possuiValorInformadoParaOutros = true;
        }

        metaListagem.submetas.push(submetaListagem);
        metaListagem.rowspan++;
        lote.rowspan++;

      });
    });

    lotes.forEach(lote => {
      let exibeLote = true;
      lote.metas.forEach(meta => {
        let exibeMeta = true;

        meta.submetas.forEach(submeta => {
          submeta.exibeLote = exibeLote;
          submeta.exibeMeta = exibeMeta;
          exibeLote = false;
          exibeMeta = false;
        });
      });
    });

    lotes.forEach(lote => {
      lote.metas.forEach(meta => {
        meta.submetas.sort(function ordenarSubmeta (a, b) {
          if (a.numero < b.numero) {
            return -1;
          }
          if (a.numero > b.numero) {
            return 1;
          }
          return 0;
        });
      });
    });

    lotes.forEach(lote => {
      lote.metas.sort(function ordenarMeta (a, b) {
          if (a.numero < b.numero) {
            return -1;
          }
          if (a.numero > b.numero) {
            return 1;
          }
          return 0;
        });
    });

    lotes.sort(
      function ordenarLote(a,b){
        if (a.numero < b.numero) {
          return -1;
        }
        if (a.numero > b.numero) {
          return 1;
        }
        return 0;
      }
    );

    const listagemQci = {
      repasseLicitado: qci.vlRepasse,
      contrapartidaLicitada: qci.vlContrapartida,
      totalLicitado: qci.vlTotal,
      difSocialContrapartidaLicitada: qci.difSocialContrapartidaLicitada,
      difSocialTotalLicitado: qci.difSocialTotalLicitado,
      difMetasContrapartidaLicitada: qci.difMetasContrapartidaLicitada,
      difMetasTotalLicitado: qci.difMetasTotalLicitado,
      difValorOrcadoRepasseLicitado: qci.difValorOrcadoRepasseLicitado,
      difValorOrcadoContrapartidaLicitada: qci.difValorOrcadoContrapartidaLicitada,
      difValorOrcadoTotalLicitado: qci.difValorOrcadoTotalLicitado,
      modalidade: qci.modalidade,
      lotes: lotes
    };

    return listagemQci;
  }

  get podeEditar() {
    return this.auth.podeEditarLicitacao();
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  getExport(listagem: QciListagem): QciListagem {
    const data = [];
    const columns = [
      'Lote de Licitação', 'Meta', 'Submeta',
      'Situação', 'Repasse', 'Contrapartida', 'Total'
    ];

    listagem.lotes.forEach(lote => {
      lote.metas.forEach(meta => {
        meta.submetas.forEach(submeta => {
          const linha = [];

          linha.push(lote.numero);
          linha.push(meta.descricao);
          linha.push(submeta.descricao);
          linha.push(submeta.situacaoDoProcessoLicitatorio);
          linha.push(submeta.repasseLicitado);
          linha.push(submeta.contrapartidaLicitada);
          linha.push(submeta.totalLicitado);

          data.push(linha);
        });
      });
    });

    this.export = new DataExport(columns, data);

    return listagem;
  }

  get pathId() {
    const id = this.route.parent.snapshot.paramMap.get('id');
    return id ? Number(id) : null;
  }

  voltar() {
    const url = this.pathId ? '../../../../../listagem' : '../../../../listagem';
    this.dispatch(new Navigate([url], {}, { relativeTo: this.route }));
  }

}
