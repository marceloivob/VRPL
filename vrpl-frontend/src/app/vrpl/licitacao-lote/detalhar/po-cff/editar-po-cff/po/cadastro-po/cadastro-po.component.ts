import { Component, OnInit, OnDestroy } from '@angular/core';
import { Store, Select, Actions, ofActionSuccessful } from '@ngxs/store';
import { PoLicitacaoState } from '../../../../../../../model/licitacao/pocff/po-licitacao.state';
import { filter, take } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { LoadEventosServico,
         LoadFrentesDeObraServico,
         LoadFrentesDeObraServicoAnalise,
         SaveServicoPo,
         LoadPoDetalhada, 
         LoadMacrosservicosPO, 
         LoadPoCffLicitacao } from '../../../../../../../model/pocff/po-cff.actions';
import { Observable, Subscription } from 'rxjs';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Navigate } from '@ngxs/router-plugin';

import {
  MacrosservicoModel,
  ServicoModel,
  EventoModel,
  FrenteDeObraQuantidadeModel,
  ServicoCadastroModel,
  ServicoFrenteDeObraAnaliseModel,
  FrenteDeObraCadastroModel
} from '../../../../../../../model/pocff/po-cff.state.model';
import { CurrencyHelperService } from '../../../../../../../siconv/services/currency-helper.service';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { LoadQciProposta, LoadQciLicitacao } from 'src/app/model/qci/qci.actions';
import { DataExport } from 'src/app/model/data-export';
import { PropostaState } from 'src/app/model/proposta/proposta.state';

@Component({
  selector: 'vrpl-cadastro-po',
  templateUrl: './cadastro-po.component.html',
  styleUrls: ['./cadastro-po.component.scss']
})
export class CadastroPoComponent implements OnInit, OnDestroy {

  actionSubscription$: Subscription;

  @Select(PoLicitacaoState.eventosServicoPoDetalhada) eventosObservable: Observable<EventoModel[]>;
  @Select(PoLicitacaoState.frenteDeObraServicoPoDetalhada) frentesObservable: Observable<FrenteDeObraQuantidadeModel[]>;
  @Select(PoLicitacaoState.servicoFrenteDeObraAnalise) servicosFrenteDeObraAnaliseObservable: Observable<ServicoFrenteDeObraAnaliseModel[]>;

  frentes: FrenteDeObraQuantidadeModel[];
  servicosFrenteObraAnalise: ServicoFrenteDeObraAnaliseModel[];
  eventos: EventoModel[];

  eventoSelecionado: EventoModel;
  eventoSelecionadoId: number;

  macrosservico: MacrosservicoModel;
  servico: ServicoModel;

  idPo: number;

  referencia: string;

  form: FormGroup;

  visualizar = false;
  acompanhaEventos = false;

  somaValorLicitado = 0.0;
  somaValorAnalise = 0.0;
  somaQtAnalise = 0.0;

  // Export
  lista: any[];
  lista2: any[];
  export: DataExport;
  export2: DataExport;

  regexpNumber = / \d{1,15}(\.\d{1,2})?$/;

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly store: Store,
    private readonly actions$: Actions,
    private alertMessageService: AlertMessageService,
    public currencyHelper: CurrencyHelperService
  ) { }

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (this.route.snapshot.url[0].path === 'detalhar') {
      this.visualizar = true;
    }

    this.store.select(PoLicitacaoState)
        .pipe(
          filter(po => po != null),
          take(1)
        ).subscribe(po => {
          this.idPo = po.poDetalhada.id;
          this.referencia = po.poDetalhada.referencia;
          this.acompanhaEventos = po.poDetalhada.acompanhamentoEvento;
          po.poDetalhada.listaMacrosservicos.map(macrosservico => {
            macrosservico.servicos.map(servico => {
              if (id === servico.id) {
                this.servico = servico;
                this.macrosservico = macrosservico;
              }
            });
          });

          this.initForm();

          if (po.poDetalhada.acompanhamentoEvento) {
            this.loadEventosServico();            
          }

          this.loadFrentesDeObraServico();         

          this.loadFrentesDeObraServicoAnalise();          
        });

    this.store.select(PropostaState).subscribe(proposta => {
      this.actionSubscription$ = this.actions$
      .pipe(ofActionSuccessful(SaveServicoPo, take(1)))
      .subscribe(() => {
        this.alertMessageService.success('Alteração do Serviço realizada com sucesso.');
        this.store.dispatch([
          new LoadMacrosservicosPO(),
          new LoadQciProposta(proposta.versaoSelecionada),
          new LoadQciLicitacao()
        ]);
        this.voltar();
      });
    });
  }  

  ngOnDestroy() {
    this.actionSubscription$.unsubscribe();
  }

  initForm() {
   
   const bdiLicitado = this.servico.bdiLicitado ? this.converterParaFormatadorSemArredondamento(this.servico.bdiLicitado) : 0;
   const precoUnitarioLicitado = this.servico.precoUnitarioLicitado? this.converterParaFormatadorSemArredondamento(this.servico.precoUnitarioLicitado):0;
   const custoUnitario = this.servico.custoUnitarioDataBase? this.converterParaFormatadorSemArredondamento(this.servico.custoUnitarioDataBase):0;

    this.eventoSelecionadoId = 0;
    if (this.servico.eventoFK) {
      this.eventoSelecionadoId = this.servico.eventoFK;
    }

    this.form = this.fb.group({
      precoUnitarioLicitado: [{value: precoUnitarioLicitado, disabled: this.visualizar}, [Validators.required]],
      custoUnitario: [{value: custoUnitario, disabled: this.visualizar}, [Validators.required]],
      bdiLicitado: [{value: bdiLicitado, disabled: this.visualizar}, Validators.required],
      observacao: [{value: this.servico.observacao, disabled: this.visualizar}],
      evento: [{value: this.eventoSelecionadoId, disabled: this.visualizar}]
    });
  }

  converterParaFormatadorSemArredondamento(valor:number): number{
    
    var numerText = valor + '';
    var indicePonto = numerText.indexOf( "." );
    if (indicePonto == -1) {
      numerText = valor + '00';
    } else {
      var aposVirgula = numerText.substring(indicePonto + 1);
      var antesVirgula = numerText.substring(0,indicePonto);
      if (aposVirgula.length == 0) {
        antesVirgula = antesVirgula + '00'
      } else if (aposVirgula.length == 1) {
        antesVirgula = antesVirgula + aposVirgula + '0'
      } else if (aposVirgula.length == 2){
        antesVirgula = antesVirgula + aposVirgula;
      } else if (aposVirgula.length > 2){
        var digito = aposVirgula.substring(2,3);
        if (parseInt(digito) >= 5) {
          var arredondamento = parseInt(antesVirgula + aposVirgula.substring(0,2)) + 1;
          antesVirgula = arredondamento + '';
        } else {
          antesVirgula = antesVirgula + aposVirgula.substring(0,2);
        }
      }
      numerText = antesVirgula;
    }
    return parseInt(numerText);
  }

  onSubmit() {
    return;
  }

  changeEvento() {
    if (this.eventos != null && this.eventos.length > 0) {
      this.eventos.forEach(evento => {
        if (evento.id === +this.form.get('evento').value) {
          this.eventoSelecionado = evento;
          this.eventoSelecionadoId = evento.id;
        }
      });
    }
  }

  salvar() {
    
    if (!this.frentes || this.frentes.length === 0){
      this.alertMessageService.error('Nenhuma Frente de Obra cadastrada. Favor cadastrar.');

    } else if ( this.acompanhaEventos && (!this.eventos || this.eventos.length === 0)) {
      this.alertMessageService.error('Nenhum Evento cadastrado. Favor cadastrar.');

    } else {

      const frentesCadastro: FrenteDeObraCadastroModel[] = [];
      this.frentes.forEach(frente => {
        const frenteCadastro: FrenteDeObraCadastroModel = {
          frenteObraFk: frente.frenteDeObra.id,
          qtItens: frente.quantidade
        };
        frentesCadastro.push(frenteCadastro);
      });
  
      const precoUnitarioLicitado = this.currencyHelper.rawValue(this.form.get('precoUnitarioLicitado').value);
      const custoUnitario = this.currencyHelper.rawValue(this.form.get('custoUnitario').value);
      const bdiLicitado = this.currencyHelper.rawValue(this.form.get('bdiLicitado').value);
  
      const servicoCadastro: ServicoCadastroModel = {
        id: this.servico.id,
        macroServicoFk: this.macrosservico.id,
        idSubmeta: this.macrosservico.submetaId,
        idPo: this.idPo,
        vlPrecoUnitarioDaLicitacao: precoUnitarioLicitado,
        vlCustoUnitario: custoUnitario,
        pcBdiLicitado: bdiLicitado,
        eventoFk: this.eventoSelecionadoId === 0 ? null : this.eventoSelecionadoId,
        frentesObra: frentesCadastro,
        txObservacao: this.form.get('observacao').value,
        versao: this.servico.versao
      };
  
      let contadorQuantidade = 0;
      frentesCadastro.forEach(frente => {
        contadorQuantidade += frente.qtItens;
      });
  
      contadorQuantidade = Math.round(contadorQuantidade * 100) / 100;
      this.somaQtAnalise = Math.round(this.somaQtAnalise * 100) / 100;
      if (contadorQuantidade !== this.somaQtAnalise) {
        const msg = 'A soma das quantidades redistribuídas por frente de obra tem que ser igual à quantidade total de análise.';
        this.alertMessageService.error(msg);
        return;
      }
  
      this.store.dispatch(new SaveServicoPo(servicoCadastro));
  
    }

  }

  voltar() {
    const idLicitacao = this.store.selectSnapshot(LicitacaoDetalhadaState).id;
    this.store.dispatch(new LoadPoDetalhada(this.idPo))
    .subscribe(() => {
      this.store.dispatch(new LoadPoCffLicitacao(idLicitacao));
      this.store.dispatch(this.voltarListagemAction());
    });
  }

  voltarListagemAction() {
    return new Navigate(['../../listagem'], {}, {relativeTo: this.route});
  }

  getSomaQuantidade(): number {
    let soma = 0.0;
    this.somaValorLicitado = 0.0;

    if (this.frentes != null) {
      this.frentes.forEach(frente => {
        soma += frente.quantidade;        
        this.somaValorLicitado += this.roundToTwo(frente.quantidade *
          this.currencyHelper.rawValue(this.form.get('precoUnitarioLicitado').value));
      });
    }

    return soma;
  }

  getPrecoUnitarioDatabase(): number {
    let preco = 0.0;

    preco = this.roundToTwo((((this.servico.bdiLicitado)*0.01)+1)*
    (this.currencyHelper.rawValue(this.form.get('custoUnitario').value)));
   
    return preco;
  }

  getSomaQuantidadeAnalise(): number {
    let soma = 0.0;
    this.somaValorAnalise = 0.0;

    if (this.servicosFrenteObraAnalise != null) {
      this.servicosFrenteObraAnalise.forEach(analise => {
        soma += analise.qtItens;         
        this.somaValorAnalise += this.roundToTwo(analise.qtItens *
          this.servico.precoUnitarioAnalise);
      });
    }
    this.somaQtAnalise = soma;
    return soma;
  }

  roundToTwo(num) {    
    return Math.round((num + Number.EPSILON) * 100) / 100;
  }

  precoUnitarioLicitadoEhInvalido(): boolean {
    let precoUnitarioLicitado = this.currencyHelper.rawValue(this.form.get('precoUnitarioLicitado').value);

    if (this.referencia == 'analise') {
      return precoUnitarioLicitado > this.servico.precoUnitarioAnalise;

    } else if (this.referencia == 'database') {
      return precoUnitarioLicitado > this.getPrecoUnitarioDatabase();
      
    } else {
      return false;
    }
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  getExport(frentes: FrenteDeObraQuantidadeModel[]): FrenteDeObraQuantidadeModel[] {
    const data = [];
    const columns = [
      'Frente de Obra', 'Quantidade Licitado', 'Valor Licitado'
    ];

    frentes.forEach(frente => {
      const linha = [];

      linha.push(frente.frenteDeObra.nomeFrente);
      linha.push(frente.quantidade);
      linha.push(frente.quantidade *  this.form.get('precoUnitarioLicitado').value);

      data.push(linha);
    });

    this.export = new DataExport(columns, data);

    return frentes;
  }

  getListaPaginada2(listap) {
    this.lista2 = listap;
  }

  getExport2(servicosFrente: ServicoFrenteDeObraAnaliseModel[]): ServicoFrenteDeObraAnaliseModel[] {
    const data = [];
    const columns = [
      'Frente de Obra', 'Quantidade Analise', 'Valor Analise'
    ];

    servicosFrente.forEach(servFrente => {
      const linha = [];

      linha.push(servFrente.nomeFrente);
      linha.push(servFrente.qtItens);
      linha.push(servFrente.qtItens * this.servico.precoUnitarioAnalise);

      data.push(linha);
    });

    this.export2 = new DataExport(columns, data);

    return servicosFrente;
  }
  
  numberOnly(event): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }

  validarQuantidade(frente: FrenteDeObraQuantidadeModel) {

    if (!(' ' + frente.quantidade.toString()).match(this.regexpNumber) ) {
      frente.quantidade = 999999999999999;
    }
  }


  loadFrentesDeObraServicoAnalise() {
    this.store.dispatch(new LoadFrentesDeObraServicoAnalise (this.servico.id));

    this.actionSubscription$ = this.actions$
      .pipe(ofActionSuccessful(LoadFrentesDeObraServicoAnalise, take(1)))
      .subscribe(() => {
        this.servicosFrenteDeObraAnaliseObservable.subscribe(servicoFrenteLista => {
          if (servicoFrenteLista != null) {
            this.servicosFrenteObraAnalise = [];
            servicoFrenteLista.forEach(servicoFrente => {
              const servicoFrenteClone = {
                idPO: servicoFrente.idPO,
                qtItens: servicoFrente.qtItens,
                nomeFrente: servicoFrente.nomeFrente,
                numeroFrente: servicoFrente.numeroFrente,
                servicoFk: servicoFrente.servicoFk,
                versao: servicoFrente.versao
              };
                                  
              this.servicosFrenteObraAnalise.push(servicoFrenteClone);
            });
          }
          
          this.getExport2(this.servicosFrenteObraAnalise);
        });
      });
  }

  loadFrentesDeObraServico() {
    this.store.dispatch(new LoadFrentesDeObraServico(this.macrosservico.id, this.servico.id));

    this.actionSubscription$ = this.actions$
      .pipe(ofActionSuccessful(LoadFrentesDeObraServico, take(1)))
      .subscribe(() => {
        this.frentesObservable.subscribe(frentesLista => {
          if (frentesLista != null) {
            this.frentes = [];
            frentesLista.forEach(frente => {
              const frenteClone = {
                id: frente.id,
                quantidade: frente.quantidade,
                frenteDeObra: frente.frenteDeObra
              };
              this.frentes.push(frenteClone);
            });
          }

          this.getExport(this.frentes);
        });
      });
  }

  loadEventosServico() {    
    this.store.dispatch(new LoadEventosServico(this.macrosservico.id, this.servico.id));

    this.actionSubscription$ = this.actions$
      .pipe(ofActionSuccessful(LoadEventosServico, take(1)))
      .subscribe(() => {
        this.eventosObservable.subscribe(eventosLista => {
          if (eventosLista != null) {
            this.eventos = [];

            eventosLista.forEach(evento => {
              const eventoClone = {
                id: evento.id,
                numeroEvento: evento.numeroEvento,
                tituloEvento: evento.tituloEvento,
                versao: evento.versao
              };

              this.eventos.push(eventoClone);
            });

            if (this.servico.eventoFK != null) {
              this.eventoSelecionadoId = this.servico.eventoFK;
              this.eventos.forEach(evento => {
                if (+evento.id === +this.eventoSelecionadoId) {
                  this.eventoSelecionado = evento;
                }
              });
            }
          }
        });
      });
  }

  getDescricaoReferencia(): string {
    if (this.referencia == 'analise') {
      return "Aceito na Análise";

    } else if (this.referenciaEhDatabaseMasServicoEstaUtilizandoValorAnalise()) {   
      // Apenas a descrição será "aceito na análise", os campos nas comparações/operações ainda
      // serão "data base" pois os valores foram replicados nesses campos.
      return "Aceito na Análise";

    } else if (this.referencia == 'database') {
      return "na Data Base da Licitação";

    } else {
      return "de Referência";
    }
  }

  referenciaEhDatabaseMasServicoEstaUtilizandoValorAnalise(): boolean {
    if (this.referencia == 'database' && this.servicoNaDatabaseEstaUtilizandoValorAnalise()) {
      return true;

    } else {
      return false;
    }
  }
  
  servicoNaDatabaseEstaUtilizandoValorAnalise(): boolean {
    return !this.servico.sinapiPossuiOcorrenciaNaDataBaseDeReferencia && this.servico.fonte === 'SINAPI';
  }

  getServicoPrecoUnitarioReferencia(): number {
    if (this.referencia == 'analise') {
      return this.servico.precoUnitarioAnalise;

    } else if (this.referencia == 'database') {
      return this.getPrecoUnitarioDatabase();

    } else {
      return 0;
    }
  }

  getServicoCustoUnitarioReferencia(): number {
    if (this.referencia == 'analise') {
      return this.servico.custoUnitarioAnalise;

    } else if (this.referencia == 'database') {
           
      if (this.servicoNaDatabaseEstaUtilizandoValorAnalise() && this.servico.custoUnitarioDataBase == null) {
        // Em alguns casos o valor do custo está retornando "nulo" no backend, 
        // vamos exibir o de análise para não confundir
        return this.servico.custoUnitarioAnalise;

      } else {
        return this.servico.custoUnitarioDataBase;
      }

    } else {
      return 0;
    }     
  }

  getDescricaoServicoPrecoUnitarioMaior(): string {
    var descricaoReferencia = this.getDescricaoReferencia();        

    return `O preço informado é maior que o preço unitário ${descricaoReferencia}.`;
  }

  exibirAvisoUtilizandoValorAnalise() {
    if (this.referencia == 'analise') {
      return false;

    } else {
      return this.servicoNaDatabaseEstaUtilizandoValorAnalise();
    }
  }

  podeEditarCustoUnitario() {
    return (this.referencia == 'database' && this.servico.fonte !== 'SINAPI');
  }

  getDescricaoAvisoUtilizandoPrecoAnalise(): string {
    var descricaoMotivo = '';

    if (this.servico.fonte !== 'SINAPI') {
      descricaoMotivo = 'fonte é diferente de SINAPI';

    } else if (!this.servico.sinapiPossuiOcorrenciaNaDataBaseDeReferencia) {
      descricaoMotivo = 'não possui ocorrência na data base de referência';
    }

    return `Preço unitário não contemplado no SINAPI, ${descricaoMotivo}, foi adotado o preço unitário da Fase da Análise.`
  }

  getDescricaoAvisoUtilizandoCustoAnalise(): string {
    var descricaoMotivo = '';

    if (this.servico.fonte !== 'SINAPI') {
      descricaoMotivo = 'fonte é diferente de SINAPI';

    } else if (!this.servico.sinapiPossuiOcorrenciaNaDataBaseDeReferencia) {
      descricaoMotivo = 'não possui ocorrência na data base de referência';
    }

    return `Custo unitário não contemplado no SINAPI, ${descricaoMotivo}, foi adotado o custo unitário da Fase da Análise.`
  }

  exibirLegendaAvisoUtilizandoValorAnalise(): boolean {
    if (this.referencia == 'analise') {
      return false;

    } else if (this.referencia == 'database') {
      return this.servicoNaDatabaseEstaUtilizandoValorAnalise();

    } else {
      return false;
    }
  }
}
