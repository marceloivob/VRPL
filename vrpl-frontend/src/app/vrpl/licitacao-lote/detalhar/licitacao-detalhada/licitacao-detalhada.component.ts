import { Component, OnInit, OnDestroy } from '@angular/core';
import { Select, Store } from '@ngxs/store';
import { Observable, Subscription } from 'rxjs';
import { LicitacaoDetalhadaModel, LoteModel } from '../../../../model/licitacao/licitacao.state.model';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { LoadLicitacaoDetalhada } from 'src/app/model/licitacao/licitacao.actions';
import { ActivatedRoute } from '@angular/router';
import { DataExport } from 'src/app/model/data-export';
import { DatePipe } from '@angular/common';
import { Navigate } from '@ngxs/router-plugin';

@Component({
  selector: 'vrpl-licitacao-detalhada',
  templateUrl: './licitacao-detalhada.component.html'
})
export class LicitacaoDetalhadaComponent extends BaseComponent {

  // Grid
  exports: Map <Number, DataExport> = new Map<Number, DataExport>();
  lista: any[];

  @Select(LicitacaoDetalhadaState) licitacaoState: Observable<LicitacaoDetalhadaModel>;

  licitacaoDetalhada: LicitacaoDetalhadaModel;

  licitacao = [];

  constructor(protected store: Store, private route: ActivatedRoute) {
    super(store);
   }

  init() {
    this.dispatch(new LoadLicitacaoDetalhada(this.pathId))
      .subscribe(() => {
        this.licitacaoState
          .pipe(this.takeUntilDestroyed())
          .subscribe(
          (licitacaoDetalhadaState) => {
            this.licitacaoDetalhada = licitacaoDetalhadaState;
            for (const lote of this.licitacaoDetalhada.lotes ) {
              this.adicionarExport(lote);
            }
          });
      });
  }

  get descontoLicitacao() {
    const valorAprovadoAnalise = this.licitacaoDetalhada.valorAprovadoFaseAnalise;
    const valorCtef = this.licitacaoDetalhada.ctef ? this.licitacaoDetalhada.ctef.valorTotal : 0;

    if (valorAprovadoAnalise > 0) {
      return ((valorAprovadoAnalise - valorCtef) / valorAprovadoAnalise);
    } else {
      return null;
    }
  }

  get licitacaoDetalhadaCarregada() {
    // considerando alguns valores da licitação pois objeto vazio é o padrão
    return this.licitacaoDetalhada
      && this.licitacaoDetalhada.id;
  }

  onLoad() {
  
  }

  adicionarExport(lote: LoteModel) {
    const data = [];
    const columns = [
      'Meta',
      'Submeta',
      'Regime de Execução',
      'Valor Licitado'
    ];
    const datepipe = new DatePipe('PT-BR');
    lote.submetas.forEach(submeta => {
      const linha = [];

      linha.push(submeta.meta);
      linha.push(submeta.submeta);
      linha.push(submeta.regimeExecucao);
      linha.push(submeta.valorLicitado);
      data.push(linha);
    });

    this.exports.set(lote.id, new DataExport(columns, data));
  }

  recuperarExport(id: number): DataExport {
    return this.exports.get(id);
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  get pathId() {
    const id = this.route.parent.snapshot.paramMap.get('id');
    return id ? Number(id) : null;
  }

  voltar() {
    const url = this.pathId ? '../../../listagem' : '../../listagem';
    this.dispatch(new Navigate([url], {}, { relativeTo: this.route }));
  }


}
