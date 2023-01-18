import { PoMacrosservicoServicoModel, ServicoModel } from './../../../../../../../model/pocff/po-cff.state.model';
import { Component } from '@angular/core';
import { MacrosservicoModel } from '../../../../../../../model/pocff/po-cff.state.model';
import { Store, Actions, Select } from '@ngxs/store';
import { PoLicitacaoState } from '../../../../../../../model/licitacao/pocff/po-licitacao.state';
import { Observable, Subscription } from 'rxjs';
import {
  LoadMacrosservicosPO,
  UpdateReferenciaPo,
  LoadPoCffLicitacao,
  LoadPoDetalhada
} from '../../../../../../../model/pocff/po-cff.actions';
import { take, map, filter } from 'rxjs/operators';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { LicitacaoDetalhadaModel } from 'src/app/model/licitacao/licitacao.state.model';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { DataExport } from 'src/app/model/data-export';
import { UserStateModel } from 'src/app/model/user/user.state.model';
import { UserState } from 'src/app/model/user/user.state';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';
import { BaseComponent } from 'src/app/siconv/util/base.component';

@Component({
  selector: 'vrpl-listagem-po',
  templateUrl: './listagem-po.component.html',
  styleUrls: ['./listagem-po.component.scss']
})
export class ListagemPoComponent extends BaseComponent {

  form: FormGroup;

  actionSubscription$: Subscription;
  licitacaoDetalhadaSubscription: Subscription;
  licitacaoDetalhada: LicitacaoDetalhadaModel;

  macrosservicosObservable: Observable<MacrosservicoModel[]>;
  @Select(PoLicitacaoState.poMacrosservicosServicosPoDetalhada) poMacrosservicoServicosObservable: Observable<PoMacrosservicoServicoModel>;

  macrosservicos: MacrosservicoModel[];
  poMacrosservicoServicos: PoMacrosservicoServicoModel;

  totalGeralLicitado: number;
  totalGeralAceitoNaAnalise: number;
  totalGeralNaDataBaseDaLicitacao: number;

  visualizar = false;
  possuiValorDatabase = false;
  algumValorLicitadoMaiorQueFaseAnalise = false;
  algumValorLicitadoMaiorQueDatabase = false;
  algumServicoUtilizandoValorAnalise = false;

  referencia = 'analise';
  travarReferencia = false;  

  idPo: number;
  versaoPo: number;

  lista: any[];

  export: DataExport;

  usuario: UserStateModel;

  constructor(
    private readonly fb: FormBuilder,
    protected store: Store,
    private authService: UserAuthorizerService,
    private readonly actions$: Actions,
    private alertMessageService: AlertMessageService,
    private readonly route: ActivatedRoute
  ) {
    super(store);
  }

  init() {
    return;
  }

  onLoad() {

    this.usuario = this.store.selectSnapshot(UserState);
    this.store.select(PoLicitacaoState.poDetalhada).pipe(
      filter(po => po != null),
      take(1)
    ).subscribe(po => {
      this.visualizar = po.apenasVisualizar || false;
      this.referencia = po.referencia;
      this.idPo = po.id;
      this.versaoPo = po.versao;
    });

    this.initForm();
  }

  loadActions() {
    this.store.select(PoLicitacaoState.poDetalhada).pipe(
      filter(po => po != null),
      take(1)
    ).subscribe(po => {
      this.idPo = po.id;
    });

    return [new LoadPoDetalhada(this.idPo)];
  }

  initForm() {
    this.store.dispatch(new LoadMacrosservicosPO())
      .subscribe(() => {
        this.macrosservicosObservable = this.store
          .select(PoLicitacaoState.macrosservicosPoDetalhada)
          .pipe(
            map(macrosservicos => {
              return macrosservicos.map(macrosservico => {
                macrosservico.servicos.forEach(servico => {
                  
                  this.verificarSituacaoServico(servico);                 
                });

                return { ...macrosservico };
              });
            })
          );

        this.macrosservicosObservable.subscribe(macrosservicos => {
          if (!this.possuiValorDatabase) {
            this.referencia = 'analise';
          }

          this.form = this.fb.group({
            referencia: [{ value: this.referencia, disabled: !this.possuiValorDatabase }]
          });

          this.getExport(macrosservicos);
        });

        this.poMacrosservicoServicosObservable.subscribe(poMacrosservicoServicos => {
          this.poMacrosservicoServicos = poMacrosservicoServicos;
          this.totalGeralLicitado = poMacrosservicoServicos.totalGeralLicitado;
          this.totalGeralAceitoNaAnalise = poMacrosservicoServicos.totalGeralAceitoNaAnalise;
          this.totalGeralNaDataBaseDaLicitacao = poMacrosservicoServicos.totalGeralNaDataBaseDaLicitacao;
        });

      });

    this.licitacaoDetalhadaSubscription = this.store.select(LicitacaoDetalhadaState)
      .pipe(
        filter(licitacaoDetalhada => licitacaoDetalhada != null),
        filter(licitacaoDetalhada => licitacaoDetalhada.po != null),
        filter(licitacaoDetalhada => licitacaoDetalhada.po.poCff != null),
        map(licitacaoDetalhada => <LicitacaoDetalhadaModel>licitacaoDetalhada),
        take(1)
      ).subscribe((licitacaoDetalhada) => {
        this.licitacaoDetalhada = licitacaoDetalhada;
      });

  }

  verificarSituacaoServico(servico: ServicoModel) {
    if (servico.precoUnitarioLicitado > servico.precoUnitarioAnalise) {
      this.algumValorLicitadoMaiorQueFaseAnalise = true;
    }

    if (servico.precoUnitarioLicitado > servico.precoUnitarioDataBase) {
      this.algumValorLicitadoMaiorQueDatabase = true;
    }

    if (this.servicoNaDatabaseEstaUtilizandoValorAnalise(servico)) {
      this.algumServicoUtilizandoValorAnalise = true;
    }

    if (servico.precoUnitarioDataBase) {
      this.possuiValorDatabase = true;
    }
  }

  get podeEditar() {
    return this.authService.podeEditarLicitacao() && !this.visualizar;
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  radioReferenciaChange() {
    this.referencia = this.form.get('referencia').value;    
  }

  salvar() {
    if (this.idPo) {
      this.store.dispatch(new UpdateReferenciaPo(this.idPo, this.referencia, this.versaoPo))
        .subscribe(() => {
          this.store.dispatch(new LoadPoDetalhada(this.idPo))
            .subscribe(() => {
              this.onLoad();
              this.alertMessageService.success('Alteração da opção de Orçamento de Referência realizada com sucesso.');
              this.store.dispatch(new LoadPoCffLicitacao(this.licitacaoDetalhada.id));
              this.versaoPo += 1;
            });
        });
    }
  }

  exibirAvisoUtilizandoValorAnalise(referencia: string, servico: ServicoModel) {
    if (referencia == 'analise') {    
      return false;

    } else {
      return this.servicoEstaUtilizandoValorAnalise(referencia, servico);
    }
  }  

  getExport(macrosservicos: MacrosservicoModel[]): MacrosservicoModel[] {
    const data = [];
    const columns = [
      'Item', 'Macrosserviço', 'Serviço', 'Fonte - Código',
      'Qtd.', 'Und.', 'Preço Unitário Licitado',
      'Preço Total Licitado', 'Preço Unitário Aceito na Análise',
      'Preço Total Aceito na Análise', 'Preço Unitário na Data Base de Licitação',
      'Preço Total na Data Base de Licitação'
    ];

    const currencyPipe = new CurrencyPipe('PT-BR');
    const decimalPipe = new DecimalPipe('PT-BR');
    macrosservicos.forEach(macrosservico => {
      macrosservico.servicos.forEach(servico => {
        const linha = [];

        linha.push(macrosservico.item + '.' + servico.item);
        linha.push(macrosservico.nome);
        linha.push(servico.nome);
        linha.push(servico.fonte + ' - ' + servico.codigo);
        linha.push(decimalPipe.transform(servico.quantidade, '1.2-2'));
        linha.push(servico.unidade);
        linha.push(currencyPipe.transform(servico.precoUnitarioLicitado, 'BRL', ''));
        linha.push(currencyPipe.transform((servico.precoUnitarioLicitado * servico.quantidade), 'BRL', ''));
        linha.push(currencyPipe.transform(servico.precoUnitarioAnalise, 'BRL', ''));
        linha.push(currencyPipe.transform((servico.precoUnitarioAnalise * servico.quantidade), 'BRL', ''));

        if (servico.precoUnitarioDataBase) {
          linha.push(currencyPipe.transform(servico.precoUnitarioDataBase, 'BRL', ''));
          linha.push(currencyPipe.transform((servico.precoUnitarioDataBase * servico.quantidade), 'BRL', ''));
        } else {
          linha.push('-');
          linha.push('-');
        }

        data.push(linha);
      });
    });

    this.export = new DataExport(columns, data);

    return macrosservicos;
  }

  voltar() {
    this.store.dispatch(new Navigate(['../../../../listagem'], {}, { relativeTo: this.route }));
  }

  servicoEstaUtilizandoValorAnalise(referencia: string, servico: ServicoModel): boolean {
    if (referencia == 'analise') {      
      return true;

    } else if (referencia == 'database') {      
      return this.servicoNaDatabaseEstaUtilizandoValorAnalise(servico);

    } else {
      return false;
    }
  }

  servicoNaDatabaseEstaUtilizandoValorAnalise(servico: ServicoModel): boolean {
    return !servico.sinapiPossuiOcorrenciaNaDataBaseDeReferencia && servico.fonte === 'SINAPI';
  }

  servicoPrecoUnitarioLicitadoMaiorQueReferencia(referencia: string, servico: ServicoModel): boolean {
    if (referencia == 'analise') {
      return servico.precoUnitarioLicitado > servico.precoUnitarioAnalise;      

    } else if (referencia == 'database') {
      return servico.precoUnitarioLicitado > servico.precoUnitarioDataBase;

    } else {
      return false;
    }
  }

  exibirLegendaAvisoValorLicitadoMaiorQueReferencia(referencia: string): boolean {
    if (referencia == 'database') {
      return this.algumValorLicitadoMaiorQueDatabase;
      
    } else if (referencia == 'analise') {
      return this.algumValorLicitadoMaiorQueFaseAnalise;

    } else {
      return false;
    }
  }

  exibirLegendaAvisoUtilizandoValorAnalise(referencia: string): boolean {
    if (referencia == 'analise') {  
      return false;

    } else if (referencia == 'database') {
      return this.algumServicoUtilizandoValorAnalise;

    } else {
      return false;
    }
  }

  getDescricaoReferencia(referencia: string): string {
    if (referencia == 'analise') {
      return "Aceito na Análise";

    } else if (referencia == 'database') {
      return "na Data Base da Licitação";

    } else {
      return "de Referência";
    }
  }

  getMacrosservicoPrecoTotalReferencia(referencia: string, macrosservico: MacrosservicoModel): number {
    if (referencia == 'analise') {
      return macrosservico.precoTotalAnalise;

    } else if (referencia == 'database') {
      return macrosservico.precoTotalNaDataBaseDaLicitacao;

    } else {
      return 0;
    }
  }

  getServicoPrecoUnitarioReferencia(referencia: string, servico: ServicoModel): number {
    if (referencia == 'analise') {
      return servico.precoUnitarioAnalise;

    } else if (referencia == 'database') {
      return servico.precoUnitarioDataBase;

    } else {
      return 0;
    }
  }   

  getServicoPrecoTotalReferencia(referencia: string, servico: ServicoModel): number {
    if (referencia == 'analise') {
      return servico.precoTotalAnalise;

    } else if (referencia == 'database') {
      return servico.precoTotalDataBase;

    } else {
      return 0;
    }                                   
  }

  getTotalGeralReferencia(referencia: string): number {
    if (referencia == 'analise') {
      return this.totalGeralAceitoNaAnalise;

    } else if (referencia == 'database') {
      return this.totalGeralNaDataBaseDaLicitacao;

    } else {
      return 0;
    } 
  }

  getDescricaoServicoPrecoUnitarioMaior(referencia: string, servico: ServicoModel): string {
    var descricaoReferencia = "";

    if (this.servicoEstaUtilizandoValorAnalise(referencia, servico)) {
      descricaoReferencia = this.getDescricaoReferencia('analise');

    } else {
      descricaoReferencia = this.getDescricaoReferencia(referencia);    
    }

    return `O preço informado é maior que o preço unitário ${descricaoReferencia}`;
  }

  getDescricaoAvisoUtilizandoValorAnalise(servico: ServicoModel): string {
    var descricaoMotivo = '';

    if (servico.fonte !== 'SINAPI') {
      descricaoMotivo = 'fonte é diferente de SINAPI';

    } else if (!servico.sinapiPossuiOcorrenciaNaDataBaseDeReferencia) {
      descricaoMotivo = 'não possui ocorrência na data base de referência';
    }

    return `Preço unitário não contemplado no SINAPI, ${descricaoMotivo}, foi adotado o preço unitário da Fase da Análise.`
  }
}
