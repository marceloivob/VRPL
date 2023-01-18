import { LoadPoCffLicitacao } from './../../../../../model/pocff/po-cff.actions';
import { PoModel } from './../../../../../model/pocff/po-cff.state.model';
import { Observable } from 'rxjs';
import { Store } from '@ngxs/store';
import { Component } from '@angular/core';
import { map } from 'rxjs/operators';
import { PoLicitacaoState } from 'src/app/model/licitacao/pocff/po-licitacao.state';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { DatePipe } from '@angular/common';
import { DataExport } from 'src/app/model/data-export';
import { formatDate } from 'ngx-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';

@Component({
  selector: 'vrpl-listagem-po-cff',
  templateUrl: './listagem-po-cff.component.html'
})
export class ListagemPoCffComponent extends BaseComponent {

  poCffsObservable: Observable<PoModel[]>;

  // Grid
  lista: any[];
  export: DataExport;

  constructor(
    readonly store: Store,
    private readonly auth: UserAuthorizerService,
    private route: ActivatedRoute
  ) {
    super(store);
  }

  init() {

    this.poCffsObservable = this.store.select(PoLicitacaoState.poCffs).pipe(
      map(pos => {
        if (pos) {
          let ultimaPo: any = {
            numeroMeta: -1
          };
          return pos.map(po => {
            if (po.numeroMeta === ultimaPo.numeroMeta) {
              ultimaPo.rowspan++;

              return { ...po, exibe: false, rowspan: 1 };
            } else {
              ultimaPo = { ...po, exibe: true, rowspan: 1 };

              return ultimaPo;
            }
          });
        } else {
          return [];
        }
      }),
      map(pos => this.getExport(pos))
    );
  }

  loadActions() {
    const idLicitacao = this.store.selectSnapshot(LicitacaoDetalhadaState).id;
    return new LoadPoCffLicitacao(idLicitacao);
  }

  onLoad() {
    this.poCffsObservable.subscribe(poCffs => {
      this.getExport(poCffs);
    });
  }

  get podeEditar() {
    return this.auth.podeEditarLicitacao();
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  getExport(pos: PoModel[]): PoModel[] {
    const data = [];
    const columns = [
      'Meta', 'Submeta', 'Database',
      'Localidade', 'Previsão Definida de Início da Obra',
      'Duração Definida da Obra (meses)', 'Obra Acompanhada por Evento?',
      'Preço Total Aceito na Análise', 'Preço Total Licitado'
    ];

    const datepipe = new DatePipe('PT-BR');
    pos.forEach(po => {
      const linha = [];

      linha.push(po.numeroMeta + ' - ' + po.descricaoMeta);
      linha.push(po.numeroMeta + '.' + po.numeroSubmeta + ' - ' + po.descricao);
      linha.push(datepipe.transform(po.dataBaseVrpl, 'MM/yyyy'));
      linha.push(po.localidade);
      linha.push(datepipe.transform(po.previsaoInicioVRPL, 'MM/yyyy'));
      linha.push(po.duracao);
      if (po.acompanhamentoEvento) {
        linha.push('Sim');
      } else {
        linha.push('Não');
      }

      linha.push(po.precoTotalAnalise);
      linha.push(po.precoTotalLicitacao);

      data.push(linha);
    });

    this.export = new DataExport(columns, data);

    return pos;
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
