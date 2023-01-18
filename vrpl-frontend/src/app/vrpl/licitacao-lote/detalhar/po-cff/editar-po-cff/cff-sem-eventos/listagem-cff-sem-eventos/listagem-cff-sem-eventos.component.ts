import { Component } from '@angular/core';
import { BaseComponent } from '../../../../../../../siconv/util/base.component';
import { Store, Select } from '@ngxs/store';
import { UserAuthorizerService } from '../../../../../../../model/user/user-authorizer.service';
import { PoLicitacaoState } from '../../../../../../../model/licitacao/pocff/po-licitacao.state';
import { Observable } from 'rxjs';
import { VisaoParcelasPorMacroServicoModel } from '../../../../../../../model/pocff/po-cff.state.model';
import { LoadCffSemEventosPO } from '../../../../../../../model/pocff/po-cff.actions';
import { map, filter } from 'rxjs/operators';
import { DataExport } from 'src/app/model/data-export';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';

interface CffListagem {
  numeroParcela: number;
  mesAnoParcela: string;
  percentualParcela: number;
  percentualAcumulado: number;
  totalAcumulado: number;
  valorParcela: number;
}

@Component({
  selector: 'vrpl-listagem-cff-sem-eventos',
  templateUrl: './listagem-cff-sem-eventos.component.html'
})
export class ListagemCffSemEventosComponent extends BaseComponent {

  @Select(PoLicitacaoState.listaParcelasPorMacroServico) visaoParcelasObservable: Observable<VisaoParcelasPorMacroServicoModel[]>;

  visualizar = false;

  listaVisaoParcelas: any[];
  listaCFF: any[];

  exportVisaoParcelas: DataExport;
  exportCFF: DataExport;

  cffObservable: Observable<CffListagem[]>;

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
  }

  onLoad() {
    this.cffObservable = this.visaoParcelasObservable.pipe(
      map( visoes => this.converterCffListagem(visoes) )
    );

    this.visaoParcelasObservable.subscribe(visaoParcelas => {
      this.getExportVisaoEventos(visaoParcelas);
    });

    this.cffObservable.subscribe(cffs => {
      this.getExportVisaoCff(cffs);
    });
  }

  loadActions() {
    return new LoadCffSemEventosPO();
  }

  get podeEditar() {
    return this.authorizer.podeEditarLicitacao() && !this.visualizar;
  }

  converterCffListagem (visoes: VisaoParcelasPorMacroServicoModel[]): CffListagem[] {

    const retorno: CffListagem[] = [];
    const valorTotalCff = visoes.reduce((acc, visao) => acc + visao.precoTotal, 0);

    for (const visao of visoes) {
      for (const parcela of visao.parcelas) {
        const cff = retorno.find(c => c.numeroParcela === parcela.numero);
        if (cff) {
          cff.valorParcela = cff.valorParcela + parcela.valorParcela;
        } else {
          retorno.push({
            numeroParcela: parcela.numero,
            mesAnoParcela: parcela.mesAnoParcela,
            percentualParcela: parcela.percentualParcela,
            percentualAcumulado: parcela.percentualAcumulado,
            totalAcumulado: parcela.totalAcumulado,
            valorParcela: parcela.valorParcela
          });
        }
      }
    }

    let totalAcumulado = 0;
    for (const cff of retorno) {
      totalAcumulado += cff.valorParcela;
      cff.totalAcumulado = totalAcumulado;
      cff.percentualParcela = (cff.valorParcela / valorTotalCff);
      cff.percentualAcumulado = (cff.totalAcumulado / valorTotalCff);
    }

    this.sortParcelas(retorno);
    return retorno;
  }

  getListaPaginadaVisaoParcelas(listap) {
    this.listaVisaoParcelas = listap;
  }

  getListaPaginadaCFF(listap) {
    this.listaCFF = listap;
  }

  getExportVisaoEventos(macrosservicos: VisaoParcelasPorMacroServicoModel[]): VisaoParcelasPorMacroServicoModel[] {
    const data = [];
    const columns = [
      'Nº do Macrosserviço', 'Macrosserviço', 'Preço Total',
      'Nº da Parcela', 'Parcela', 'Percentual Parcela',
      'Percentual Acumulado', 'Valor Acumulado'
    ];

    const currencyPipe = new CurrencyPipe('PT-BR');
    const decimalPipe = new DecimalPipe('PT-BR');
    macrosservicos.forEach(macrosservico => {
      macrosservico.parcelas.forEach(parcela => {
        const linha = [];

        linha.push(macrosservico.numeroMacroServico);
        linha.push(macrosservico.nomeMacroServico);
        linha.push(currencyPipe.transform(macrosservico.precoTotal, 'BRL', ''));
        linha.push(parcela.numero);
        linha.push(parcela.mesAnoParcela);
        linha.push(decimalPipe.transform(parcela.percentualParcela, '1.2-2'));
        linha.push(decimalPipe.transform(parcela.percentualAcumulado, '1.2-2'));
        linha.push(decimalPipe.transform(parcela.totalAcumulado, '1.2-2'));

        data.push(linha);
      });
    });

    this.exportVisaoParcelas = new DataExport(columns, data);

    return macrosservicos;
  }

  getExportVisaoCff(cffs: CffListagem[]): CffListagem[] {
    const data = [];
    const columns = [
      'Nº da Parcela', 'Parcela', 'Percentual Parcela',
      'Percentual Acumulado', 'Valor Acumulado'
    ];

    const decimalPipe = new DecimalPipe('PT-BR');
    cffs.forEach(cff => {
      const linha = [];

      linha.push(cff.numeroParcela);
      linha.push(cff.mesAnoParcela);
      linha.push(decimalPipe.transform(cff.percentualParcela, '1.2-2'));
      linha.push(decimalPipe.transform(cff.percentualAcumulado, '1.2-2'));
      linha.push(decimalPipe.transform(cff.totalAcumulado, '1.2-2'));

      data.push(linha);
    });

    this.exportCFF = new DataExport(columns, data);

    return cffs;
  }

  private sortParcelas(cffs: CffListagem[]) {
    for (let i = 0; i < (cffs.length - 1); i += 1) {
      for (let j = i + 1; j < cffs.length; j += 1) {
        if ( cffs[i].numeroParcela > cffs[j].numeroParcela ) {
          const temp = cffs[i];
          cffs[i] = cffs[j];
          cffs[j] = temp;
        }
      }
    }
  }

  voltar() {
    this.store.dispatch(new Navigate(['../../../../listagem'], {}, { relativeTo: this.route }));
  }

}
