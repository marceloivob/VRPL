import { Component, Inject, TemplateRef, LOCALE_ID } from '@angular/core';
import { Observable } from 'rxjs';
import { Select, Store } from '@ngxs/store';
import { LicitacaoState } from '../../../model/licitacao/licitacao.state';
import { LicitacaoModel } from '../../../model/licitacao/licitacao.state.model';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { filter, map } from 'rxjs/operators';
import { BsModalService, BsModalRef } from 'ngx-bootstrap';
import { LoadAnexosDaLicitacao, RemoverAssociacaoLotes } from 'src/app/model/licitacao/licitacao.actions';
import { LoadLicitacoesAtivasProposta } from 'src/app/model/licitacao/licitacao.actions';
import { LoadLicitacoesRejeitadasProposta } from 'src/app/model/licitacao/licitacao.actions';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { DataExport } from 'src/app/model/data-export';
import { PropostaState } from 'src/app/model/proposta/proposta.state';
import { formatCurrency } from '@angular/common';
import { AnexoModel } from 'src/app/model/anexo/anexo.state.model';
import { VerificaFornecedorObsoleto } from 'src/app/model/proposta/proposta.actions';
import { PropostaService } from 'src/app/model/proposta/proposta.service';

@Component({
  selector: 'vrpl-licitacao-lote-listagem',
  templateUrl: './licitacao-lote-listagem.component.html'
})
export class LicitacaoLoteListagemComponent extends BaseComponent {

  @Select(LicitacaoState.anexosDaLicitacao) anexosDaLicitacaoObservable: Observable<AnexoModel[]>;
  anexosDaLicitacao: AnexoModel[] = [];


  licitacoesAssociadas: Observable<LicitacaoModel[]>;
  licitacoesRejeitadas: Observable<LicitacaoModel[]>;
  licitacaoExcluir: LicitacaoModel;
  modalRef: BsModalRef;

  listaAtivas: any[];
  listaRejeitadas: any[];
  exportAssociadas: DataExport;
  exportRejeitadas: DataExport;

  versaoSelecionadaDaProposta: number;

  constructor(
    protected readonly  store: Store,
    private readonly modalService: BsModalService,
    private readonly  alertMessageService: AlertMessageService,
    private readonly auth: UserAuthorizerService,
    @Inject(LOCALE_ID) private locale: string
  ) {
    super(store);
  }

  loadActions() {
    this.store.select(PropostaState)
      .pipe(this.takeUntilDestroyed())
      .subscribe(proposta => {
        this.versaoSelecionadaDaProposta = proposta.versaoSelecionada;
    });
  }

  onLoad() {  }

  init() {
    this.licitacoesAssociadas = this.store.select(LicitacaoState.licitacoesAtivasProposta)
      .pipe(
        filter(licitacoes => licitacoes != null),
        map(licitacoes => licitacoes
          .filter(licitacao => {
            return (licitacao.lotes.length);
          }))
      );

    this.licitacoesRejeitadas = this.store.select(LicitacaoState.licitacoesRejeitadasProposta)
      .pipe(
        filter(licitacoes => licitacoes != null),
        map(licitacoes => licitacoes
          .filter(licitacao => {
            return (licitacao.lotes.length);
          }))
      );

      this.licitacoesAssociadas.subscribe(lics => {
        this.getExportAssociadas(lics);
      });
  
      this.licitacoesRejeitadas.subscribe(lics => {
        this.getExportRejeitadas(lics);
      });


    this.store.dispatch( new VerificaFornecedorObsoleto() );
  }

  mostrarBotaoExclusao (licitacaoLista: LicitacaoModel): boolean {
    return this.auth.podeEditar() &&
      (licitacaoLista.situacaoDaLicitacao === 'COM' ||
       licitacaoLista.situacaoDaLicitacao === 'EPE');
  }

  podeEditar() {
    return this.auth.podeEditar();
  }

  deleteLotesLicitacao(licitacao: LicitacaoModel, template: TemplateRef<any>) {
    this.store.dispatch( new LoadAnexosDaLicitacao(licitacao.id) )
      .subscribe( () => {

        this.anexosDaLicitacaoObservable.subscribe(anexos => {
          this.anexosDaLicitacao = anexos;
        });
      });

      this.licitacaoExcluir = licitacao;
      this.modalRef = this.modalService.show(template);
  }

  confirmaExclusao() {

    this.dispatch(new RemoverAssociacaoLotes(this.licitacaoExcluir.id))
      .subscribe( () => {
        this.alertMessageService.success('Lotes removidos com sucesso!');
        this.anexosDaLicitacao = [];
        this.dispatch(
          [
            new LoadLicitacoesAtivasProposta(this.versaoSelecionadaDaProposta),
            new LoadLicitacoesRejeitadasProposta(this.versaoSelecionadaDaProposta)
          ]
        );
      });

    this.modalRef.hide();
    this.licitacaoExcluir = null;
  }

  cancelaExclusao() {
    this.modalRef.hide();
    this.licitacaoExcluir = null;
  }

  getListaPaginada(listap) {
    this.listaAtivas = listap;
  }

  getListaPaginadaRejeitadas(listap) {
    this.listaRejeitadas = listap;
  }


  getExportAssociadas(lics: LicitacaoModel[]): LicitacaoModel[] {

    this.exportAssociadas = this.getDataExport(lics);

    return lics;
  }

  getExportRejeitadas(lics: LicitacaoModel[]): LicitacaoModel[] {
   
    this.exportRejeitadas = this.getDataExport(lics);

    return lics;
  }

  getDataExport(lics: LicitacaoModel[]): DataExport {
    const data = [];
    const columns = [
      'Licitação', 'Processo de Execução', 'Situação',
      'Lote', 'Meta', 'Submeta',
      'Valor Aceito na Análise', 'Valor Verificado Licitado'
    ];

    lics.forEach(lic => {
      lic.lotes.forEach(lote => {
        lote.submetas.forEach(sub => {
          const linha = [];

          linha.push(lic.numeroAno);
          linha.push(lic.processoDeExecucao);
          linha.push(lic.situacaoDaLicitacaoDescricao);
          linha.push(lote.numero);
          linha.push(sub.meta);
          linha.push(sub.submeta);
          linha.push(this.formatarValor(sub.valorAceitoAnalise));
          linha.push(this.formatarValor(sub.valorLicitado));

          data.push(linha);
        });
      });
    });

    return new DataExport(columns, data);
  }

  private formatarValor(valor: number): string {
    return valor ? formatCurrency(valor, this.locale, 'R$', 'BRL') : '';
  }

  mensagemExclusaoLotes(licitacao: LicitacaoModel, anexos: AnexoModel[]) : string {
    let msg = '';

    if (licitacao) {
      licitacao.lotes.forEach(lote => {
        msg += 'Lote ' + lote.numero + ':</br>'

        lote.submetas.forEach(submeta => {
          msg += submeta.submeta + '.</br>'
        });
      });
    }

    return msg;
  }

  mensagemExclusaoAnexos(licitacao: LicitacaoModel, anexos: AnexoModel[]) : string {
    let msg = '';

    if (licitacao) {
      anexos.forEach(anexo => {
        msg += anexo.descricao + '.</br>'
      });
    }

    return msg;
  }

}
