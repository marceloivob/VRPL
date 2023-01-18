import { Component } from '@angular/core';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { Store, Select } from '@ngxs/store';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { PoLicitacaoState } from 'src/app/model/licitacao/pocff/po-licitacao.state';
import { Observable } from 'rxjs';
import { CFFComEventosModel, QuadroCFFPorEventoPorMes, EventoCFFcomEventosModel } from 'src/app/model/pocff/po-cff.state.model';
import { LoadCffComEventosPO } from 'src/app/model/pocff/po-cff.actions';
import { filter, map } from 'rxjs/operators';
import { DataExport } from 'src/app/model/data-export';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';

@Component({
  selector: 'vrpl-listagem-cff-com-eventos',
  templateUrl: './listagem-cff-com-eventos.component.html'
})
export class ListagemCffComEventosComponent extends BaseComponent {

  cffComEventosObservable: Observable<CFFComEventosModel>;
  eventosObservable: Observable<EventoCFFcomEventosModel[]>;
  listaValoresMensais: QuadroCFFPorEventoPorMes[];

  visualizar = false;

  listaVisaoPorEventos: any[];
  listaVisaoPorCFF: any[];

  exportVisaoPorEventos: DataExport;
  exportVisaoPorCFF: DataExport;

  constructor(
    readonly store: Store,
    private readonly authorizer: UserAuthorizerService,
    private readonly route: ActivatedRoute
  ) { super(store); }

  init() {
    this.store.select(PoLicitacaoState.poDetalhada).pipe(
      filter(po => po != null),
      this.takeUntilDestroyed()
    ).subscribe(po => this.visualizar = po.apenasVisualizar || false);

    this.eventosObservable = this.store.select(PoLicitacaoState.cffComEventos).pipe(
      filter(cff => cff != null),
      map(cff => cff.listaDeEventoCFF)
    );

    this.store.select(PoLicitacaoState.cffComEventos).pipe(
      filter(cff => cff != null),
      this.takeUntilDestroyed()
    ).subscribe(cff => {
      this.listaValoresMensais = this.converterMapListaCFFVisaoMensal(cff);
    });
  }

  loadActions() {
    return new LoadCffComEventosPO();
  }

  onLoad() {
    this.eventosObservable.subscribe(eventos => {
      this.getExportVisaoEventos(eventos);
      this.getExportVisaoCff(this.listaValoresMensais);
    });
  }

  get podeEditar() {
    return this.authorizer.podeEditarLicitacao() && !this.visualizar;
  }


  converterMapListaCFFVisaoMensal(objCFF: CFFComEventosModel): QuadroCFFPorEventoPorMes[] {
    const retorno: QuadroCFFPorEventoPorMes[] = [];
    const totalPO = objCFF.totalPO;
    let acumulado = 0;
    let percentualAcumulado = 0;

    for (let i = 1; i <= objCFF.qtdMeses; i++)  {
      let value = objCFF.mapaMesValorParcela[i];
      if (!value){
        value = 0;
      }
      acumulado = acumulado + value;
      percentualAcumulado = (acumulado / totalPO);
      retorno.push({
        mes: i,
        percentualParcela: (value / totalPO),
        valorParcela: value,
        percentualAcumulado: percentualAcumulado,
        valorAculumado: acumulado
      });
    }

    return retorno;
  }

  getListaPaginadaVisaoPorEventos(listap) {
    this.listaVisaoPorEventos = listap;
  }

  getListaPaginadaVisaoPorCFF(listap) {
    this.listaVisaoPorCFF = listap;
  }

  getExportVisaoEventos(eventos: EventoCFFcomEventosModel[]): EventoCFFcomEventosModel[] {
    const data = [];
    const columns = [
      'Nº do Evento', 'Título do Evento', 'Nº da Frente de Obra',
      'Frente de Obra', 'Nº do Período de Conclusão do Evento'
    ];

    eventos.forEach(evento => {
      evento.listaFrenteObras.forEach(frente => {
        const linha = [];

        linha.push(evento.numeroEvento);
        linha.push(evento.tituloEvento);
        linha.push(frente.numeroFrenteObra);
        linha.push(frente.nomeFrenteObra);
        if (frente.mesConclusao === 0) {
          linha.push('Não Definido');
        } else {
          linha.push(frente.mesConclusao);
        }

        data.push(linha);
      });
    });

    this.exportVisaoPorEventos = new DataExport(columns, data);

    return eventos;
  }

  getExportVisaoCff(quadros: QuadroCFFPorEventoPorMes[]): QuadroCFFPorEventoPorMes[] {
    const data = [];
    const columns = [
      'Nº do Período de Conclusão do Evento', 'Percentual da Parcela', 'Valor da Parcela',
      'Percentual Acumulado', 'Valor Acumulado'
    ];

    const currencyPipe = new CurrencyPipe('PT-BR');
    const decimalPipe = new DecimalPipe('PT-BR');
    quadros.forEach(quadro => {
      const linha = [];

      linha.push(quadro.mes);
      linha.push(decimalPipe.transform((quadro.percentualParcela * 100), '1.2-2'));
      linha.push(currencyPipe.transform(quadro.valorParcela, 'BRL', ''));
      linha.push(decimalPipe.transform((quadro.percentualAcumulado * 100), '1.2-2'));
      linha.push(currencyPipe.transform(quadro.valorAculumado, 'BRL', ''));

      data.push(linha);
    });

    this.exportVisaoPorCFF = new DataExport(columns, data);

    return quadros;
  }

  voltar() {
    this.store.dispatch(new Navigate(['../../../../listagem'], {}, { relativeTo: this.route }));
  }

}
