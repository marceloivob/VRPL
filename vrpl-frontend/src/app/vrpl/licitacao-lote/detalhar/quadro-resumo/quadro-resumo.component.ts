import { Component, TemplateRef } from '@angular/core';
import { LicitacaoDetalhadaState } from '../../../../model/licitacao/licitacao.detalhada.state';
import { HistoricoLicitacaoModel, QuadroResumoModel, EventoGerador } from '../../../../model/quadroresumo/quadroresumo.state.model';
import { Observable } from 'rxjs';
import { Select, Store } from '@ngxs/store';
import { BaseComponent } from '../../../../siconv/util/base.component';
import { LoadQuadroResumo, SaveNovoEstadoDocumentacao } from '../../../../model/quadroresumo/quadroresumo.actions';
import { AnexoModel } from '../../../../model/anexo/anexo.state.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { map, filter } from 'rxjs/operators';
import { LoadAnexosPorTipo } from '../../../../model/anexo/anexo.actions';
import { UserState } from '../../../../model/user/user.state';
import { UserStateModel, PapelDoUsuarioModel } from '../../../../model/user/user.state.model';
import { LicitacaoDetalhadaModel } from '../../../../model/licitacao/licitacao.state.model';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { DataExport } from 'src/app/model/data-export';
import { DatePipe } from '@angular/common';
import {
  LoadLicitacaoDetalhada,
  LoadLicitacoesAtivasProposta,
  LoadLicitacoesRejeitadasProposta
} from 'src/app/model/licitacao/licitacao.actions';
import { PropostaState } from 'src/app/model/proposta/proposta.state';
import { Navigate } from '@ngxs/router-plugin';
import { LoadProposta } from 'src/app/model/proposta/proposta.actions';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'vrpl-quadro-resumo',
  templateUrl: './quadro-resumo.component.html'
})
export class QuadroResumoComponent extends BaseComponent {

  @Select(LicitacaoDetalhadaState.quadroResumoLicitacaoDetalhada) quadroResumoObservable: Observable<QuadroResumoModel>;
  @Select(LicitacaoDetalhadaState.anexosComponente) anexosObservable: Observable<AnexoModel[]>;

  listaHistoricoObservable: Observable<HistoricoLicitacaoModel[]>;
  licitacaoDetalhada: LicitacaoDetalhadaModel;

  listaAnexos: Observable<AnexoModel[]>;
  quadroResumo: QuadroResumoModel;
  historicoForm: FormGroup;
  historicoEdicao: HistoricoLicitacaoModel;
  mensagemSucesso: string;
  titulo: string;
  mensagemConfirmacao: string;
  modalRef: BsModalRef;
  eventoGerador: string;
  caracteresRestantesTxConsideracoes: number;
  podeEditar = false;
  idLicitacao: number;
  versaoSelecionada: number;

  MAX_CONSIDERACOES = 1500;

  usuario: UserStateModel;
  licitacao: LicitacaoDetalhadaModel;

  papelSelecionado: PapelDoUsuarioModel;

  papeisDoUsuario: PapelDoUsuarioModel[];

  exibirErroPapelVazio: boolean;

  // Grid
  export: DataExport;
  lista: any[];

  constructor(
    readonly store: Store,
    private alertMessageService: AlertMessageService,
    private fb: FormBuilder,
    private userService: UserAuthorizerService,
    private readonly modalService: BsModalService,
    private route: ActivatedRoute
  ) { super(store); }

  init() {
    this.exibirErroPapelVazio = false;
    this.podeEditar = false;
    this.store.select(LicitacaoDetalhadaState)
      .pipe(this.takeUntilDestroyed())
      .subscribe(licitacao => {
        this.idLicitacao = licitacao.idIntegracao;
        this.licitacao = licitacao;
        this.usuario = this.store.selectSnapshot(UserState);

        if ((((this.usuario.profile === 'CONCEDENTE' &&
                    (this.userService.isConvenio() || this.userService.isConvenioEContratoRepasse()
                     || this.userService.isTermoCompromissoConcedente()))
           || this.usuario.profile === 'MANDATARIA') && this.naoEhAdministrador())
          && this.licitacao.situacaoDaLicitacao === 'ANL') {

            const ehVersaoAtualDaLicitacao = this.licitacao && this.licitacao.versaoAtual;
            this.podeEditar = ehVersaoAtualDaLicitacao;
        }

        this.papeisDoUsuario = this.usuario.roles
          .map(role => this.userService.recuperarPapelDoUsuario(role))
          .filter(papel => papel.numero !== '?')
          .sort((a, b) => a.label.localeCompare(b.label));

        if (this.papeisDoUsuario.length === 1) {
          this.papelSelecionado = this.papeisDoUsuario[0];
        }
      });

    this.listaHistoricoObservable = this.quadroResumoObservable
      .pipe(
        filter(qr => qr != null),
        map(qr => qr.listaHistorico)
      );
    this.quadroResumoObservable
      .pipe(
        filter(qr => qr != null),
        this.takeUntilDestroyed()
      ).subscribe(qr => {
        this.quadroResumo = qr;
        this.initForm();
        this.getExport(this.quadroResumo.listaHistorico);
      });
  }

  loadActions() {
    this.store.select(LicitacaoDetalhadaState)
      .pipe(this.takeUntilDestroyed())
      .subscribe(licitacao => {
        this.idLicitacao = licitacao.idIntegracao;
        this.licitacao = licitacao;
      });

    this.store.select(PropostaState)
      .pipe(this.takeUntilDestroyed())
      .subscribe(proposta => {
        this.versaoSelecionada = proposta.versaoSelecionada;
      });

    return new LoadQuadroResumo(this.versaoSelecionada);
  }

  onLoad() {
    this.store.dispatch(new LoadLicitacaoDetalhada(this.licitacao.id))
      .subscribe(() => {
        this.store.dispatch(new LoadAnexosPorTipo('QUADRO_RESUMO'))
          .subscribe(() => {
            this.initForm();
          });
      });
  }

  showError(fieldName: string) {
    const field = this.field(fieldName);
    return !field.valid && (!field.pristine);
  }

  field(fieldName: string) {
    return this.historicoForm.get(fieldName);
  }

  calcularCaracteresRestantesTxConsideracoes() {
    const formValues = this.historicoForm.value;
    this.caracteresRestantesTxConsideracoes = this.MAX_CONSIDERACOES -
      (formValues.consideracoes ? formValues.consideracoes.length : 0);
  }

  initForm() {
    const historico: HistoricoLicitacaoModel = {
      identificadorDaLicitacao: this.quadroResumo.
        listaHistorico[this.quadroResumo.listaHistorico.length - 1].identificadorDaLicitacao,
      consideracoes: '',
      versaoDaLicitacao: this.quadroResumo.listaHistorico[this.quadroResumo.listaHistorico.length - 1].versaoDaLicitacao
    };

    this.historicoEdicao = historico;
    this.historicoForm = this.fb.group({
      consideracoes: [{ value: '', disabled: false }, Validators.maxLength(this.MAX_CONSIDERACOES)]
    });

    this.caracteresRestantesTxConsideracoes = this.MAX_CONSIDERACOES;
  }

  submit() {
    const formValues = this.historicoForm.value;

    const novoHistorico: HistoricoLicitacaoModel = {
      identificadorDaLicitacao: this.historicoEdicao.identificadorDaLicitacao,
      eventoGerador: this.eventoGerador,
      consideracoes: formValues.consideracoes,
      versaoDaLicitacao: this.historicoEdicao.versaoDaLicitacao
    };

    if (this.papelSelecionado) {
      novoHistorico.papelDoResponsavel = this.papelSelecionado.numero;
    }

    this.dispatch(new SaveNovoEstadoDocumentacao(novoHistorico))
      .subscribe(() => {
        this.reloadProposta();
        this.alertMessageService.success(this.mensagemSucesso);
        this.historicoForm.reset();
        this.store.dispatch(new Navigate(['./vrpl/licitacao-lote']));
      }, (err) => {
        if (err.error.data.severity === 'WARN') {
          this.reloadProposta();
          this.alertMessageService.success(this.mensagemSucesso);
          this.historicoForm.reset();
          this.store.dispatch(new Navigate(['./vrpl/licitacao-lote']));
        }
      });
  }

  private reloadProposta() {
    if (this.eventoGerador === EventoGerador.REJEITAR
     || this.eventoGerador === EventoGerador.CANCELAR_REJEITE
     || this.eventoGerador === EventoGerador.INICIAR_COMPLEMENTACAO_CONVENENTE
     || this.eventoGerador === EventoGerador.CANCELAR_ACEITE) {

      this.versaoSelecionada += 1;
    }

    this.store.dispatch([
      new LoadProposta(Number(this.versaoSelecionada)),
      new LoadLicitacoesAtivasProposta(this.versaoSelecionada),
      new LoadLicitacoesRejeitadasProposta(this.versaoSelecionada)
    ]);
  }

  confirmarAcao() {
    this.submit();
    this.modalRef.hide();
    this.titulo = null;
  }

  cancelarAcao() {
    this.modalRef.hide();
    this.titulo = null;
  }

  enviarParaAnalise() {
    this.mensagemSucesso = 'Documentos da Verificação do Resultado do Processo ' +
      'Licitatório enviados com sucesso para análise pela mandatária/concedente.';
    this.eventoGerador = EventoGerador.ENVIAR_PARA_ANALISE;
    this.submit();
  }

  confirmarCancelarEnvioParaAnalise(template: TemplateRef<any>) {
    this.mensagemConfirmacao = 'Tem certeza que deseja cancelar o envio para análise dos documentos da ' +
      'Verificação do Resultado do Processo Licitatório?';
    this.mensagemSucesso = 'O cancelamento do envio para análise dos documentos da ' +
      'Verificação do Resultado do Processo Licitatório foi realizado com sucesso!';
    this.eventoGerador = EventoGerador.CANCELAR_ENVIO_ANALISE;
    this.modalRef = this.modalService.show(template);
  }

  confirmarCancelarEnvioParaAnaliseComplementacao(template: TemplateRef<any>) {
    this.mensagemConfirmacao = 'Tem certeza que deseja cancelar o envio para análise dos documentos da ' +
      'Verificação do Resultado do Processo Licitatório?';
    this.mensagemSucesso = 'O cancelamento do envio para análise dos documentos da ' +
      'Verificação do Resultado do Processo Licitatório foi realizado com sucesso!';
    this.eventoGerador = EventoGerador.CANCELAR_ENVIO_ANALISE_COMPLEMENTACAO;
    this.modalRef = this.modalService.show(template);
  }

  iniciarAnalise() {
    this.mensagemSucesso = 'Análise dos documentos orçamentários iniciada com sucesso!';
    this.eventoGerador = EventoGerador.INICIAR_ANALISE_PELA_MANDATARIA;
    this.submit();
  }

  solicitarComplementacao() {
    this.mensagemSucesso = 'Solicitação ao Tomador para a complementação dos Documentos da ' +
      'Verificação do Resultado do Processo Licitatório realizada com sucesso.';
    this.eventoGerador = EventoGerador.SOLICITAR_COMPLEMENTACAO_CONVENENTE;
    this.submit();
  }

  confirmarCancelarComplementacao(template: TemplateRef<any>) {
    this.mensagemConfirmacao = 'Tem certeza que deseja cancelar o envio para complementação dos documentos da ' +
      'Verificação do Resultado do Processo Licitatório?';
    this.mensagemSucesso = 'O cancelamento do envio para complementação dos documentos da ' +
      'Verificação do Resultado do Processo Licitatório foi realizado com sucesso!';
    this.eventoGerador = EventoGerador.CANCELAR_COMPLEMENTACAO_CONVENENTE;
    this.modalRef = this.modalService.show(template);
  }

  iniciarComplementacaoConvenente() {
    this.mensagemSucesso = 'Complementação dos Documentos da Verificação do Resultado do Processo Licitatório iniciada com sucesso!';
    this.eventoGerador = EventoGerador.INICIAR_COMPLEMENTACAO_CONVENENTE;
    this.submit();
  }

  confirmarAceitarDocumentacao(template: TemplateRef<any>) {
    if (this.papelSelecionado) {
      this.exibirErroPapelVazio = false;
      this.mensagemConfirmacao =
        'Tem certeza que deseja aceitar os Documentos da Verificação do Resultado do Processo Licitatório da presente licitação ?';
      this.mensagemSucesso = 'Aceite dos Documentos da Verificação do Resultado do Processo Licitatório realizado com sucesso!';
      this.eventoGerador = EventoGerador.ACEITAR_DOCUMENTACAO;
      this.modalRef = this.modalService.show(template);
    } else {
      this.alertMessageService.error('É necessário informar a Atribuição do Responsável para Aceitar ou Rejeitar a licitação.');
      this.exibirErroPapelVazio = true;
    }
  }

  confirmarRejeitarDocumentacao(template: TemplateRef<any>) {
    if (this.papelSelecionado) {
      this.exibirErroPapelVazio = false;
      this.titulo = 'Prezado (a), ';

      this.mensagemConfirmacao = '\tNão existe um parecer de análise emitido para esta licitação. ' +
        'Deseja prosseguir com a rejeição da licitação, mesmo sem um parecer emitido?';

      if (this.licitacao.existeParecerEmitido) {
        this.mensagemConfirmacao = '\tTem certeza que deseja rejeitar a licitação?';
      }

      this.mensagemSucesso = 'Rejeite dos Documentos da Verificação do Resultado do Processo Licitatório realizado com sucesso!';
      this.eventoGerador = EventoGerador.REJEITAR;
      this.modalRef = this.modalService.show(template);
    } else {
      this.alertMessageService.error('É necessário informar a Atribuição do Responsável para Aceitar ou Rejeitar a licitação.');
      this.exibirErroPapelVazio = true;
    }
  }

  confirmarCancelarAceite(template: TemplateRef<any>) {
    this.mensagemConfirmacao = 'Tem certeza que deseja realizar o cancelamento do aceite ' +
      'dos Documentos da Verificação do Resultado do Processo Licitatório da presente licitação ?';
    this.mensagemSucesso = 'Cancelamento do Aceite dos Documentos da Verificação do Resultado do ' +
      'Processo Licitatório da presente licitação realizado com sucesso!';
    this.eventoGerador = EventoGerador.CANCELAR_ACEITE;
    this.modalRef = this.modalService.show(template);
  }

  confirmarCancelarRejeite(template: TemplateRef<any>) {
    const lotes = this.licitacao.lotes.map(lote => lote.numero).join(', ').replace(/,([^,]*)$/, ' e$1');

    this.mensagemConfirmacao = 'O cancelamento da rejeição fará com que os valores atuais associados ' +
        'ao(s) lote(s) de número ' + lotes + ' sejam ignorados e sobrepostos com os ' +
        'valores registrados antes da rejeição da licitação. Deseja continuar?';
    this.mensagemSucesso = 'Cancelamento da Rejeição dos Documentos da Verificação do Resultado do ' +
      'Processo Licitatório da presente licitação realizado com sucesso!';
    this.eventoGerador = EventoGerador.CANCELAR_REJEITE;
    this.modalRef = this.modalService.show(template);
  }

  onAnexosChanged() {
    this.store.dispatch(new LoadAnexosPorTipo('QUADRO_RESUMO'));
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  getExport(historicoQuadroResumo: HistoricoLicitacaoModel[]) {
    const data = [];
    const columns = [
      'Data', 'Evento',
      'Responsável', 'Considerações', 'Situação'
    ];

    const datepipe = new DatePipe('PT-BR');
    historicoQuadroResumo.forEach(quadroResumo => {
      const linha = [];

      linha.push(datepipe.transform(quadroResumo.dataDeRegistro, 'dd/MM/yyyy HH:mm:ss'));
      linha.push(quadroResumo.eventoGerador.descricao);
      linha.push(quadroResumo.cpfDoResponsavel + ' ' + quadroResumo.nomeDoResponsavel);
      linha.push((!quadroResumo.consideracoes) ? '' : quadroResumo.consideracoes);
      linha.push(quadroResumo.situacaoDaLicitacao.descricao);

      data.push(linha);
    });

    this.export = new DataExport(columns, data);

  }

  get exibirPapel() {
    return this.historicoEdicao &&
      (this.quadroResumo.podeAceitarDocumentacao ||
        this.quadroResumo.podeRejeitarDocumentacao);
  }

  papelSelecionadoChanged(event: any) {
    if (this.papelSelecionado) {
      this.exibirErroPapelVazio = false;
    }
  }

  naoEhAdministrador(): boolean {
    return !(this.usuario && this.usuario.roles &&
    this.usuario.roles.length === 1 &&
    this.usuario.roles.includes('ADMINISTRADOR_SISTEMA_ORGAO_EXTERNO'));
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
