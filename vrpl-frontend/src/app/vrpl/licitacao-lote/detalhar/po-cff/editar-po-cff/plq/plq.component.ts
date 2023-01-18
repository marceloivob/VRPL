import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { Observable } from 'rxjs';
import { PLQModel } from 'src/app/model/pocff/po-cff.state.model';
import { Store } from '@ngxs/store';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { PoLicitacaoState } from 'src/app/model/licitacao/pocff/po-licitacao.state';
import { filter, map, distinctUntilChanged } from 'rxjs/operators';
import { LoadPLQ } from 'src/app/model/pocff/po-cff.actions';
import { DataExport } from 'src/app/model/data-export';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { Navigate } from '@ngxs/router-plugin';
import { ActivatedRoute } from '@angular/router';


interface FrenteObraPLQListagem {
  id: number;
  servicoFK: number;
  numero: number;
  descricao: string;
  quantidade: number;
  valorLicitado: number;
  exibeServico: boolean;
}

interface ServicoPLQListagem {
  id: number;
  numero: number;
  descricao: string;
  quantidade: number;
  unidade: string;
  numeroEvento: number;
  descricaoEvento: string;
  precoTotalServico: number;
  precoUnitarioServico: number;
  macroServicoFk: number;
  rowspan: number
  frentesObra: FrenteObraPLQListagem[] ;
}

interface MacroServicoPLQListagem {
  id: number;
  numero: number;
  descricao: string;
  precoTotalLicitado: number;
  servicos: ServicoPLQListagem[];
}

interface PLQListagem {
  porEvento: boolean;
  macroservicos: MacroServicoPLQListagem[];
}



@Component( {
  selector: 'vrpl-plq',
  templateUrl: './plq.component.html',
  styleUrls: ['./plq.component.scss']
})
export class PlqComponent extends BaseComponent {

  // Grid
  lista: any[];
  export: DataExport;

  plqObservable: Observable<PLQModel>;
  visualizar = false;
  plq: PLQModel;

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

    this.plqObservable = this.store.select(PoLicitacaoState.plq).pipe(
      filter(plq => plq != null),
      distinctUntilChanged(),
      map(plq => this.mapToRow(plq)),
      map(plqListagem => this.getExport(plqListagem))
    );
  }

  get podeEditar() {
    return this.authorizer.podeEditarLicitacao() && !this.visualizar;
  }

  loadActions() {
    return new LoadPLQ();
  }

  getExport(plq: PLQListagem): PLQListagem {
    const data = [];
    let columns = [];
    const currencyPipe = new CurrencyPipe('PT-BR');
    const decimalPipe = new DecimalPipe('PT-BR');

    if (plq.porEvento) {
      columns = ['Macrosserviço', 'Preço Total Licitado', 'Serviço', 'Qtd.', 'Und.', 'No. Evento', 'Evento', 'Preço Serviço Licitado',
      'No. Frente de Obra', 'Frente de Obra', 'Qtd.', 'Valor Licitado'];
    } else {
      columns = ['Macrosserviço', 'Preço Total Licitado', 'Serviço', 'Qtd.', 'Und.', 'Preço Serviço Licitado',
            'No. Frente de Obra', 'Frente de Obra', 'Qtd.', 'Valor Licitado'];
    }
    for (const macroservico of plq.macroservicos) {
      for (const servico of macroservico.servicos) {
        for (const frenteObra of servico.frentesObra) {
          const linha = [];
          linha.push(macroservico.numero + ' ' + macroservico.descricao);
          linha.push(currencyPipe.transform(macroservico.precoTotalLicitado, 'BRL', ''));
          linha.push(macroservico.numero + '.' + servico.numero + ' ' + servico.descricao);
          linha.push(decimalPipe.transform(servico.quantidade, '1.2-2'));
          linha.push(servico.unidade);
          if (plq.porEvento) {
            linha.push(servico.numeroEvento);
            linha.push(servico.descricaoEvento);
          }
          linha.push(currencyPipe.transform(servico.precoTotalServico, 'BRL', ''));
          linha.push(frenteObra.numero);
          linha.push(frenteObra.descricao);
          linha.push(decimalPipe.transform(frenteObra.quantidade, '1.2-2'));
          linha.push(currencyPipe.transform(frenteObra.valorLicitado, 'BRL', ''));
          data.push(linha);
        }
      }
    }
    this.export = new DataExport(columns, data);

    return plq;
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  voltar() {
    this.store.dispatch(new Navigate(['../../../listagem'], {}, { relativeTo: this.route }));
  }

  mapToRow(plq: PLQModel): PLQListagem {

    const plqListagem: PLQListagem = {
      porEvento: plq.porEvento,
      macroservicos: []
    };
    
    plq.macroservicos.forEach(ms => {
      
      let macro = {
        id: ms.id,
        numero: ms.numero,
        descricao: ms.descricao,
        precoTotalLicitado: ms.precoTotalLicitado,
        servicos: []
      }
      plqListagem.macroservicos.push(macro);
      
      ms.servicos.forEach(servico => {

        let servicoListagem = {
          id: servico.id,
          numero: servico.numero,
          descricao: servico.descricao,
          quantidade: servico.quantidade,
          unidade: servico.unidade,
          numeroEvento: servico.numeroEvento,
          descricaoEvento: servico.descricaoEvento,
          precoTotalServico: servico.precoTotalServico,
          precoUnitarioServico: servico.precoUnitarioServico,
          macroServicoFk: servico.macroServicoFk,
          rowspan: 0,
          frentesObra: [] 
        };
        macro.servicos.push(servicoListagem);

        servico.frentesObra.forEach(frente => {
          
          const frenteObraListagem = {
            id: frente.id,
            servicoFK: frente.servicoFK,
            numero: frente.numero,
            descricao: frente.descricao,
            quantidade: frente.quantidade,
            valorLicitado: frente.valorLicitado,
            exibeServico: (servicoListagem.rowspan === 0 ? true : false)
          };
          
          // para cada frente adicionada, aumenta em 1 o rowspan do servico
          servicoListagem.frentesObra.push(frenteObraListagem);
          servicoListagem.rowspan++;

        });
      });
    });

    return plqListagem;
  }

}
