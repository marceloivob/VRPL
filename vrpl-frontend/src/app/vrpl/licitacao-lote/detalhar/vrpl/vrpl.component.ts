import { Component, TemplateRef } from '@angular/core';
import { LicitacaoDetalhadaModel, LoteModel } from '../../../../model/licitacao/licitacao.state.model';
import { Store, Select } from '@ngxs/store';
import { LicitacaoDetalhadaState } from '../../../../model/licitacao/licitacao.detalhada.state';
import { LoadPerguntas, LoadLaudo, LoadPendencias, SaveLaudo, QuemEmitiuLaudo } from '../../../../model/laudo/laudo.actions';
import { LaudoState } from '../../../../model/laudo/laudo.state';
import { Observable, Subscription } from 'rxjs';
import {
  TemplateLaudoModel,
  PendenciaModel,
  LaudoModel,
  RespostaModel,
  TextoModel
} from '../../../../model/laudo/laudo.state.model';
import { LoadAnexosLicitacao, LoadAnexosPorTipo } from '../../../../model/anexo/anexo.actions';
import { AnexoModel } from '../../../../model/anexo/anexo.state.model';
import { QciStateModel, SubmetaModel } from 'src/app/model/qci/qci.state.model';
import { LoadQciLicitacao } from 'src/app/model/qci/qci.actions';
import { CpfPipe } from 'src/app/siconv/pipes/cpf.pipe';
import { CnpjPipe } from 'src/app/siconv/pipes/cnpj.pipe';
import { DataExport } from 'src/app/model/data-export';
import { DatePipe } from '@angular/common';
import { PropostaState } from 'src/app/model/proposta/proposta.state';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { SimNaoPipe } from 'src/app/siconv/pipes/simnao.pipe';
import { InscricaoGenericaPipe } from 'src/app/siconv/pipes/inscricaogenerica.pipe';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { SituacaoDaLicitacao, EventoGerador, HistoricoLicitacaoModel } from 'src/app/model/quadroresumo/quadroresumo.state.model';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';
import { SaveNovoEstadoDocumentacao } from 'src/app/model/quadroresumo/quadroresumo.actions';
import { LoadLicitacoesAtivasProposta, LoadLicitacoesRejeitadasProposta } from 'src/app/model/licitacao/licitacao.actions';
import { LoadProposta } from 'src/app/model/proposta/proposta.actions';

@Component({
  selector: 'vrpl-vrpl',
  templateUrl: './vrpl.component.html'
})
export class VrplComponent extends BaseComponent {

  RASCUNHO = 1;
  EMITIDO = 2;
  ASSINADO = 3;
  CANCELADO = 4;

  MAX_CONSIDERACOES = 1500;

  msgModal = '';
  modalRef: BsModalRef;

  podeEditar = false;
  temPermissaoParaEmitir = false;
  temPermissaoParaSalvarRascunho = false;
  temPermissaoParaCancelar = false;
  temPermissaoParaAssinar = false;

  // Grid
  exportAnexos: DataExport;
  exportQuadroResumo: DataExport;
  lista: any[];

  @Select(LaudoState.templateLaudo) templateObservable: Observable<TemplateLaudoModel>;
  @Select(LicitacaoDetalhadaState.anexosComponente) anexosParecerObservable: Observable<AnexoModel[]>;
  @Select(LicitacaoDetalhadaState.anexosLicitacaoDetalhada) anexosObservable: Observable<AnexoModel[]>;
  @Select(LaudoState.parecer) laudoObservable: Observable<LaudoModel>;
  @Select(LaudoState.pendencias) pendenciasObservable: Observable<PendenciaModel[]>;

  private subscription: Subscription = new Subscription();

  licitacaoDetalhada: LicitacaoDetalhadaModel;
  qciObservable: Observable<QciStateModel>;
  fornecedores: Map<number, string>;

  submetas: SubmetaModel[];
  laudoId: number;

  novoLaudo: LaudoModel;

  grupo8resposta0: Map<number, TextoModel> = new Map();
  comentariosOrcamentoLote: Map<number, TextoModel> = new Map();
  comentarioCronogramaLote: Map<number, TextoModel> = new Map();
  contadorTitulo: Map<number, number> = new Map();

  grupo2resposta0 = '';
  justicativaConclusao = '';
  parecerViavel = '';
  contadorDeLote = 1;
  mensagemSucesso = '';

  quemEmitiuLaudo = '';

  constructor(
    protected readonly store: Store,
    private readonly cnpjPipe: CnpjPipe,
    private readonly cpfPipe: CpfPipe,
    private readonly inscricaoGenericaPipe: InscricaoGenericaPipe,
    private readonly auth: UserAuthorizerService,
    private readonly simNaoPipe: SimNaoPipe,
    private alertMessageService: AlertMessageService,
    private readonly modalService: BsModalService,
    private route: ActivatedRoute) {
    super(store);
  }

  init() {
    this.licitacaoDetalhada = this.store.selectSnapshot(LicitacaoDetalhadaState);

    this.fornecedores = new Map();
    this.licitacaoDetalhada.lotes.forEach(lote => {

      // dummies para as respostas de cada lote serão setados em preencheRespostas(laudo: LaudoModel)
      this.grupo8resposta0.set(lote.numero, new TextoModel());
      this.comentariosOrcamentoLote.set(lote.numero, new TextoModel());
      this.comentarioCronogramaLote.set(lote.numero, new TextoModel());
      this.contadorTitulo.set(lote.numero, this.contadorDeLote);
      this.contadorDeLote++;

      if (lote.fornecedor) {
        let tipoDaIdentificacao = lote.fornecedor.identificacao.tipo;

        let identificacao = '';

        if (tipoDaIdentificacao === 'CNPJ') {
          identificacao = this.cnpjPipe.transform(lote.fornecedor.identificacao.valor);
        } else if (tipoDaIdentificacao === 'IG') {
          identificacao = this.inscricaoGenericaPipe.transform(lote.fornecedor.identificacao.valor);
        } else {
          tipoDaIdentificacao = 'CPF';
          identificacao = this.cpfPipe.transform(lote.fornecedor.identificacao);
        }

        this.fornecedores.set(lote.numero, identificacao + ' - ' + lote.fornecedor.nome);
      }
    });
  }

  loadActions() {
    this.licitacaoDetalhada = this.store.selectSnapshot(LicitacaoDetalhadaState);

    const idLicitacao = this.licitacaoDetalhada.id;
    const idVersaoDaLicitacao = this.licitacaoDetalhada.versao;

    this.subscription.add(this.store.dispatch(new QuemEmitiuLaudo(idLicitacao, 'VRPL'))
      .subscribe(() => {
        this.store.select(LaudoState.quemEmitiu)
          .subscribe(quemEmitiu => {
            this.quemEmitiuLaudo = quemEmitiu;
        });
    }));

    return [new LoadAnexosPorTipo('VRPL'),
    new LoadPerguntas('VRPL', idLicitacao, idVersaoDaLicitacao),
    new LoadAnexosLicitacao(),
    new LoadLaudo(this.licitacaoDetalhada.id, 'VRPL')];
  }

  onLoad() {
    this.anexosObservable.subscribe(anexos => {
      this.getExportAnexos(anexos);
    });

    this.subscription.add(this.laudoObservable.subscribe(laudo => {
      this.laudoId = laudo.id;
      this.subscription.add(this.store.dispatch(new LoadPendencias(laudo.id)).subscribe());

      this.definePermissoesDoUsuario(laudo);

      this.preencheRespostas(laudo);
    }));

    this.subscription.add(this.store.select(PropostaState)
      .subscribe(proposta => {
        this.subscription.add(this.store.dispatch(new LoadQciLicitacao())
          .subscribe(() => {
            this.qciObservable = this.store.select(LicitacaoDetalhadaState.qciLicitacaoDetalhada);
            this.qciObservable.subscribe(qci => {
              this.submetas = this.getSubmetas(qci);
              this.getExportQuadroResumo(this.licitacaoDetalhada.lotes);
            });
          }));
      }));
  }

  // 	566892:	SICONV-DocumentosOrcamentarios-Pareceres-RN-Opcoes
  definePermissoesDoUsuario(laudo: LaudoModel) {

    const ehVersaoAtualDaLicitacao = this.licitacaoDetalhada && this.licitacaoDetalhada.versaoAtual;

    if (   !(this.auth.isMandataria || this.auth.isConcedente)
        || !ehVersaoAtualDaLicitacao) {
      this.temPermissaoParaSalvarRascunho = false;
      this.temPermissaoParaEmitir = false;
      this.temPermissaoParaAssinar = false;
      this.temPermissaoParaCancelar = false;

      this.podeEditar = this.getPodeEditar(laudo);

      return;
    }

    // As opções a seguir devem ser exibidas habilitadas apenas se:

    // O parecer ( Técnico de Engenharia ou de Trabalho Social ) ainda NÃO tiver sido emitido;
    const laudoNaoEmitido = laudo.inStatus !== this.EMITIDO && laudo.inStatus !== this.ASSINADO;

    // A situação da Licitação for "Em Análise – Fase de Licitação".
    const licitacaoEmAnalise = this.licitacaoDetalhada.situacaoDaLicitacao === SituacaoDaLicitacao.EM_ANALISE;

    const usuarioPodeEmitirOuDesemitirLaudo = this.usuarioPodeEmitirOuDesemitirLaudo();

    this.temPermissaoParaSalvarRascunho = laudoNaoEmitido && licitacaoEmAnalise && usuarioPodeEmitirOuDesemitirLaudo;

    this.temPermissaoParaEmitir = laudoNaoEmitido && licitacaoEmAnalise && usuarioPodeEmitirOuDesemitirLaudo;

    // A opção abaixo deve ser exibida habilitada apenas se:

    // O parecer ( Técnico de Engenharia ou de Trabalho Social ) JÁ estiver emitido.
    const laudoEmitido = laudo.inStatus === this.EMITIDO || laudo.inStatus === this.ASSINADO;

    // A situação da Licitação for "Em Análise – Fase de Licitação" ou "Aceita – Fase de Licitação".
    const licitacaoAceita = this.licitacaoDetalhada.situacaoDaLicitacao === SituacaoDaLicitacao.ACEITA_FASE_LICITACAO;

    this.temPermissaoParaAssinar = laudoEmitido && (licitacaoEmAnalise)
                                 && (this.auth.isMandataria || this.auth.isConcedente)
                                     && (this.auth.user.cpf !== this.quemEmitiuLaudo);

    if (laudoEmitido && licitacaoEmAnalise && this.auth.isConcedente && this.auth.isContratoRepasse) {
      this.temPermissaoParaAssinar = false;
    }

    // A opção abaixo deve ser exibida habilitada apenas se:

    // O parecer ( Técnico de Engenharia ou de Trabalho Social ) JÁ estiver emitido.
    // A situação da Licitação for "Em Análise – Fase de Licitação.
    this.temPermissaoParaCancelar = (laudo.inStatus === this.EMITIDO || laudo.inStatus === this.ASSINADO)
                                    && licitacaoEmAnalise && usuarioPodeEmitirOuDesemitirLaudo;

    this.podeEditar = this.getPodeEditar(laudo);
  }

  // 567920:	SICONV-DocumentosOrcamentarios-PareceresVRPL-RN-PoliticaEdicaoVisualizacaoPareceres
  private getPodeEditar(laudo: LaudoModel): boolean {

    const ehVersaoAtualDaLicitacao = this.licitacaoDetalhada && this.licitacaoDetalhada.versaoAtual;

    if (!ehVersaoAtualDaLicitacao) {
      this.podeEditar = false;

      return this.podeEditar;
    }

    const laudoNaoEmitido = laudo.inStatus !== this.EMITIDO;

    const laudoNaoAssinado = laudo.inStatus !== this.ASSINADO;

    const licitacaoEmAnalise = this.licitacaoDetalhada.situacaoDaLicitacao === SituacaoDaLicitacao.EM_ANALISE;

    const usuarioEhMandatariaOuConcedente = this.auth.isConcedente || this.auth.isMandataria;

    this.podeEditar = laudoNaoEmitido && laudoNaoAssinado && licitacaoEmAnalise && usuarioEhMandatariaOuConcedente;

    if (laudoNaoEmitido && licitacaoEmAnalise && this.auth.isConcedente && this.auth.isContratoRepasse()) {
      this.podeEditar = false;

      return this.podeEditar;
    }

    return this.podeEditar;
  }

  private usuarioPodeEmitirOuDesemitirLaudo(): boolean {
    const usuarioEhMandataria = this.auth.isMandataria;
    const usuarioEhConcedente = this.auth.isConcedente;

    const ehContratoRepasse = this.auth.isContratoRepasse();
    const ehTermoDeCompromissoMandataria = this.auth.isTermoCompromissoMandataria();
    const ehTermoDeCompromissoConcedente = this.auth.isTermoCompromissoConcedente();

    const ehConvenio = (this.auth.isConvenio() || this.auth.isConvenioEContratoRepasse());

    const usuarioPodeEmitirOuDesemitirLaudo = (
      (usuarioEhMandataria && (ehContratoRepasse || ehTermoDeCompromissoMandataria))
        || (usuarioEhConcedente && (ehConvenio || ehTermoDeCompromissoConcedente))
     );

    return usuarioPodeEmitirOuDesemitirLaudo;
  }

  contadorDeGrupo(valor: number) {
    if (this.exibeAspectosGeraisDaLicitacao()) {
      return valor;
    } else {
      return valor - 1;
    }
  }

  destroy() {
    this.subscription.unsubscribe();
  }

  onAnexosChanged() {
    this.subscription.add(this.store.dispatch(new LoadAnexosPorTipo('VRPL')).subscribe());
  }

  onPendenciasChanged() {
    this.subscription.add(this.store.dispatch(new LoadPendencias(this.laudoId)).subscribe());
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  downloadFile(linkToDownload: string) {
    window.open(linkToDownload);
  }

  getColor(valor: string): string {

    if (valor === 'Não') {
      return '#dc3545'; // Vermelho
    } else {
      return '#666666'; // Cinza
    }
  }

  exibeAspectosGeraisDaLicitacao(): boolean {
    // R02 - Só exibir se o "Regime de contratação" for RDC ou 13.303, a partir do obtido no Siconv.
    let exibe = this.licitacaoDetalhada.regimeContratacao === 'Lei 13.303/2016';
    exibe = exibe || this.licitacaoDetalhada.regimeContratacao === 'Lei 12.462/2011 - RDC';

    return exibe;
  }

  getExportAnexos(anexos: AnexoModel[]): AnexoModel[] {
    const data = [];
    const columns = [
      'Descrição', 'Tipo', 'Data de Envio',
      'Enviado por', 'Perfil'
    ];

    const datepipe = new DatePipe('PT-BR');
    if (anexos) {
      anexos.forEach(anexo => {
        const linha = [];

        linha.push(anexo.descricao);
        linha.push(anexo.tipoDoAnexoAsString);
        linha.push(datepipe.transform(anexo.dataUpload, 'dd/MM/yyyy'));
        linha.push(anexo.nomeDoUsuarioQueEnviou);
        linha.push(anexo.perfilUsuarioEnvio);

        data.push(linha);
      });
    }

    this.exportAnexos = new DataExport(columns, data);

    return anexos;
  }

  getExportQuadroResumo(lotes: LoteModel[]): LoteModel[] {
    const data = [];
    const columns = [
      'Lote de Licitação', 'Meta', 'Submeta',
      'Empresa Vencedora', 'Repasse', 'Contrapartida', 'Valor'
    ];

    if (lotes) {
      lotes.forEach(lote => {

        lote.submetas.forEach(submeta => {
          const linha = [];

          linha.push(lote.numero);
          linha.push(submeta.meta);
          linha.push(submeta.submeta);
          linha.push(this.fornecedores.get(lote.numero));
          linha.push(submeta.vlRepasse);
          linha.push(submeta.vlContrapartida);
          linha.push(submeta.valorLicitado);

          data.push(linha);
        });
      });
    }

    this.exportQuadroResumo = new DataExport(columns, data);

    return lotes;
  }

  getSubmetas(qci: QciStateModel): SubmetaModel[] {
    const subs: SubmetaModel[] = [];
    if (qci.metas) {
      qci.metas.forEach(meta => {
        if (meta.submetas) {
          meta.submetas.forEach(submeta => {
            const submetaLabel = {
              ...submeta,
              label: `${meta.numero}.${submeta.numero} - ${submeta.descricao}`
            };
            subs.push(submetaLabel);
          });
        }
      });
    }

    return subs;
  }

  loteNumero(numero: number): string {
    return '1.2.' + this.contadorTitulo.get(numero) + ' Dados Gerais do Lote ' + numero;
  }

  referencia(reflote: string): string {
    let retorno = 'Aceito na Análise';
    if (reflote === 'database') {
      retorno = 'Atualizado na Data Base da Licitação';
    }

    return retorno;
  }

  calcularCaracteresRestantesOrcamentoLote(lotenumero: number): number {
    const textoDoTextArea = this.comentariosOrcamentoLote.get(lotenumero).texto;

    // const formValues = this.historicoForm.value;
    const caracteresRestantesTxConsideracoes = this.MAX_CONSIDERACOES -
      (textoDoTextArea ? textoDoTextArea.length : 0);

    return caracteresRestantesTxConsideracoes;
  }

  calcularCaracteresRestantesCronogramaLote(lotenumero: number): number {
    const textoDoTextArea = this.comentarioCronogramaLote.get(lotenumero).texto;

    // const formValues = this.historicoForm.value;
    const caracteresRestantesTxConsideracoes = this.MAX_CONSIDERACOES -
      (textoDoTextArea ? textoDoTextArea.length : 0);

    return caracteresRestantesTxConsideracoes;
  }

  calcularCaracteresRestantesJustificativa() {
    const textoDoTextArea = this.justicativaConclusao;

    const caracteresRestantesJustificativa = this.MAX_CONSIDERACOES -
      (textoDoTextArea ? textoDoTextArea.length : 0);

    return caracteresRestantesJustificativa;

  }

  preencheRespostas(laudo: LaudoModel) {
    laudo.respostas.forEach(res => {

      // Foi apresentada declaração...
      if (res.pergunta.grupo.numero === 3 && res.pergunta.numero === 1) {
        this.grupo2resposta0 = res.resposta;
      }

      // Justificativa parecer
      if (res.pergunta.grupo.numero === 8 && res.pergunta.numero === 2) {
        this.justicativaConclusao = res.resposta;
      }

      // Parecer viavel e inviavel
      if (res.pergunta.grupo.numero === 8 && res.pergunta.numero === 1) {
        this.parecerViavel = res.resposta;
      }

      // O cronograma cumpre as exigencias...
      if (res.lote && res.pergunta.grupo.numero === 9 && res.pergunta.numero === 6) {
        this.grupo8resposta0.get(res.lote.numero).texto = res.resposta;
      }

      // Comentarios de orçamento do lote
      if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 4) {
        this.comentariosOrcamentoLote.get(res.lote.numero).texto = res.resposta;
      }

      // Comentarios de cff do lote
      if (res.lote && res.pergunta.grupo.numero === 9 && res.pergunta.numero === 7) {
        this.comentarioCronogramaLote.get(res.lote.numero).texto = res.resposta;
      }
    });
  }

  salvar(template: TemplateLaudoModel,
    laudo: LaudoModel,
    status: number,
    templateModal: TemplateRef<any>) {

    this.novoLaudo = {
      id: laudo.id,
      templateFk: template.id,
      licitacaoFk: this.licitacaoDetalhada.id,
      respostas: [],
      inStatus: status,
      versao: laudo.versao
    };

    // r1 == Foi apresentada declaração de que o edital da licitação contempla todos os elementos necessários abaixo dispostos?
    const r1: RespostaModel = {
      respostaId: null,
      resposta: this.grupo2resposta0,
      grupo: template.grupos[2].grupoId,
      pergunta: template.grupos[2].perguntas[0],
      laudoFk: laudo.id,
      versao: 0
    };
    this.novoLaudo.respostas.push(r1);

    // r2 == Justificativa
    const r2: RespostaModel = {
      respostaId: null,
      resposta: this.justicativaConclusao,
      grupo: template.grupos[7].grupoId,
      pergunta: template.grupos[7].perguntas[1],
      laudoFk: laudo.id,
      versao: 0
    };
    this.novoLaudo.respostas.push(r2);

    // r4 == Considera-se a verificação do resultado do processo licitatório
    const r4: RespostaModel = {
      respostaId: null,
      resposta: this.parecerViavel,
      grupo: template.grupos[7].grupoId,
      pergunta: template.grupos[7].perguntas[0],
      laudoFk: laudo.id,
      versao: 0
    };
    this.novoLaudo.respostas.push(r4);

    this.licitacaoDetalhada.lotes.forEach(lote => {
      const loteCopia = Object.assign({}, lote);
      loteCopia.fornecedor = null;
      loteCopia.submetas = null;
      delete loteCopia.valorAceitoAnalise;
      delete loteCopia.valorVerificadoLicitado;

      // resposta0Grupo3 == Orçamento utilizado para comparação
      const pergunta0grupo3clone = Object.assign({}, template.grupos[3].perguntas[0]);
      pergunta0grupo3clone.loteId = laudo.id;
      const resposta0Grupo3: RespostaModel = {
        respostaId: null,
        resposta: this.referencia(lote.referencia),
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta0grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(resposta0Grupo3);

      // resposta1Grupo3 == O valor total do lote de %s é menor ou igual ao orçamento utilizado para comparação?
      const pergunta1grupo3clone = Object.assign({}, template.grupos[3].perguntas[1]);
      pergunta1grupo3clone.loteId = laudo.id;
      const resposta1Grupo3: RespostaModel = {
        respostaId: null,
        resposta: this.simNaoPipe.transform(lote.vlTotalLicitadoMenorOuIgualRef),
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta1grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(resposta1Grupo3);

      // resposta2Grupo3 == O preço unitário de cada item do orçamento da empresa vencedora da
      // licitação é menor ou igual ao orçamento utilizado para comparação?
      const pergunta2grupo3clone = Object.assign({}, template.grupos[3].perguntas[2]);
      pergunta2grupo3clone.loteId = laudo.id;
      const resposta2Grupo3: RespostaModel = {
        respostaId: null,
        resposta: this.simNaoPipe.transform(lote.precoUnitarioMenorOuIgualRef),
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta2grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(resposta2Grupo3);

      const datepipe = new DatePipe('PT-BR');
      // r3 == Previsão de término
      const pergunta4grupo3clone = Object.assign({}, template.grupos[3].perguntas[4]);
      pergunta4grupo3clone.loteId = laudo.id;
      const r3: RespostaModel = {
        respostaId: null,
        resposta: datepipe.transform(loteCopia.previsaoTermino, 'dd/MM/yyyy'),
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta4grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };

      this.novoLaudo.respostas.push(r3);

      // r5 == O cronograma cumpre as exigências do Programa (prazo máximo de construção, percentual mínimo nas últimas parcelas, etc.)?
      const pergunta5grupo3clone = Object.assign({}, template.grupos[8].perguntas[0]);
      pergunta5grupo3clone.loteId = laudo.id;
      const r5: RespostaModel = {
        respostaId: null,
        resposta: this.grupo8resposta0.get(lote.numero).texto,
        grupo: template.grupos[8].grupoId,
        pergunta: pergunta5grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(r5);

      // r6 == Comentários do Orçamento
      const pergunta3grupo3clone = Object.assign({}, template.grupos[3].perguntas[3]);
      pergunta3grupo3clone.loteId = laudo.id;
      const r6: RespostaModel = {
        respostaId: null,
        resposta: this.comentariosOrcamentoLote.get(lote.numero).texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta3grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(r6);

      // r7 == Comentários do Cronograma Físico Financeiro
      const pergunta6grupo3clone = Object.assign({}, template.grupos[8].perguntas[1]);
      pergunta6grupo3clone.loteId = laudo.id;
      const r7: RespostaModel = {
        respostaId: null,
        resposta: this.comentarioCronogramaLote.get(lote.numero).texto,
        grupo: template.grupos[8].grupoId,
        pergunta: pergunta6grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };

      this.novoLaudo.respostas.push(r7);
    });

    this.modalRef = this.modalService.show(templateModal);

  }

  salvarRascunho(template: TemplateLaudoModel, laudo: LaudoModel, templateModal: TemplateRef<any>) {
    this.msgModal = 'Tem certeza que deseja salvar rascunho para este Parecer?';
    this.mensagemSucesso = 'Alteração do Parecer Técnico de Engenharia realizada com sucesso.';
    this.salvar(template, laudo, this.RASCUNHO, templateModal);
  }

  emitir(template: TemplateLaudoModel, laudo: LaudoModel, templateModal: TemplateRef<any>) {
    this.msgModal = 'Após a emissão do Parecer Técnico de Engenharia,' +
      ' só será possível solicitar complementação dos dados' +
      ' se houver o cancelamento da emissão do Parecer Técnico de Engenharia. ' +
      ' Tem certeza que deseja prosseguir com a emissão do Parecer Técnico de Engenharia?';
    this.mensagemSucesso = 'Parecer Técnico de Engenharia emitido com sucesso!';
    this.salvar(template, laudo, this.EMITIDO, templateModal);
  }

  cancelarEmissao(template: TemplateLaudoModel, laudo: LaudoModel, templateModal: TemplateRef<any>) {
    this.msgModal = 'Tem certeza que deseja cancelar a emissão para este Parecer?';
    this.mensagemSucesso = 'Cancelamento de emissão do Parecer Técnico de Engenharia realizado com sucesso!';
    this.salvar(template, laudo, this.CANCELADO, templateModal);
  }

  assinar(template: TemplateLaudoModel, laudo: LaudoModel, templateModal: TemplateRef<any>) {
    if (!this.verificarObrigatoriedadePergunbtasLotes()) {
      this.msgModal = 'Tem certeza que deseja assinar este Parecer?';
      this.mensagemSucesso = 'Parecer Técnico de Engenharia assinado com Sucesso!';
      this.salvar(template, laudo, this.ASSINADO, templateModal);
    }
  }

  verificarObrigatoriedadePergunbtasLotes(): boolean {
    this.alertMessageService.dismissAll();
    let obrigatorio = false;
    let erro = false;
    this.licitacaoDetalhada.lotes.forEach(lote => {
      obrigatorio = obrigatorio || this.verificarObrigatoriedadeComentarioCronograma(lote.numero);
      if (obrigatorio) {
        this.alertMessageService.error('Comentários do Cronograma são obrigatórios no Lote ' + lote.numero + '!');
        erro = true;
      }

      if ((!lote.vlTotalLicitadoMenorOuIgualRef || !lote.precoUnitarioMenorOuIgualRef) &&
          (this.comentariosOrcamentoLote.get(lote.numero).texto === ''
          || this.comentariosOrcamentoLote.get(lote.numero).texto === undefined
          || this.comentariosOrcamentoLote.get(lote.numero).texto === null) ) {
        this.alertMessageService.error('Comentários do Orcamento são obrigatórios no Lote ' + lote.numero + '!');
        erro = true;
      }

      // validar se a pergunta esta em branco
      if (this.grupo8resposta0.get(lote.numero).texto === '' ||
         this.grupo8resposta0.get(lote.numero).texto === null ||
         this.grupo8resposta0.get(lote.numero).texto === undefined) {
        this.alertMessageService.error('Respostas são obrigatórias na seção Dados Gerais do Lote ' + lote.numero + '.');
        erro = true;
      }

    });
    if (this.validarJustificativa()) {
      this.alertMessageService.error('A justificativa é obrigatória!');
      erro = true;
    }

    if (this.parecerViavel === '' ||
               this.parecerViavel === null ||
               this.parecerViavel === undefined) {
        this.alertMessageService.error('O campo "Considera-se a verificação do resultado do processo licitatório" deve ser preenchido!');
      erro = true;
    }
    return erro;
  }

  verificarNecessidadeMudancaMensagemModalNaEmissao(): boolean {
    let necessario = false;

    // nao com comentarios
    this.licitacaoDetalhada.lotes.forEach(lote => {
      necessario = necessario ||
      this.verificarSePossuiRespostaNaoComComentarioCronograma(lote.numero) ||
      ( (!lote.vlTotalLicitadoMenorOuIgualRef || !lote.precoUnitarioMenorOuIgualRef)
        && this.comentariosOrcamentoLote.get(lote.numero).texto !== ''
        && this.comentariosOrcamentoLote.get(lote.numero).texto !== undefined
        && this.comentariosOrcamentoLote.get(lote.numero).texto !== null);
    });


    // inviavel com comentario
    necessario = necessario || (this.parecerViavel === 'Inviável' &&
       this.justicativaConclusao !== '' &&
       this.justicativaConclusao !== undefined &&
       this.justicativaConclusao !== null);



    // viavel com pendencia
    necessario = necessario || (this.parecerViavel === 'Viável' && this.contarPendencias() > 0);



    return necessario;
  }

  contarPendencias(): Number {
    let qtd = 0;
    this.pendenciasObservable.subscribe(pendencia => {
        qtd = pendencia.length;
    });

    return qtd;
  }

  validarJustificativa(): boolean {
    if (this.parecerViavel === 'Inviável') {
      if (this.justicativaConclusao === ''
      || this.justicativaConclusao === undefined
      || this.justicativaConclusao === null) {
        return true;
      }
    }
    return false;
  }

  verificarObrigatoriedadeComentarioCronograma(numeroLote: number): boolean {
    if (this.grupo8resposta0.get(numeroLote).texto === 'Não') {
      return (this.comentarioCronogramaLote.get(numeroLote).texto === '' ||
      this.comentarioCronogramaLote.get(numeroLote).texto === undefined ||
      this.comentarioCronogramaLote.get(numeroLote).texto === null ) ? true : false;
    }
    return false;
  }


  verificarSePossuiRespostaNaoComComentarioCronograma(numeroLote: number): boolean {
    if (this.grupo8resposta0.get(numeroLote).texto === 'Não') {
      return (this.comentarioCronogramaLote.get(numeroLote).texto !== '' &&
      this.comentarioCronogramaLote.get(numeroLote).texto !== undefined &&
      this.comentarioCronogramaLote.get(numeroLote).texto !== null ) ? true : false;
    }
    return false;
  }

  confirmaModal() {
    this.subscription.add(this.store.dispatch(new SaveLaudo(this.novoLaudo))
      .subscribe(() => {
        this.alertMessageService.success(this.mensagemSucesso);
        this.dispatch(this.loadActions()).subscribe(() => {
          this.onLoad();
        }
        );
        const evento = this.recuperarEvento(this.novoLaudo.inStatus);

        if (evento !== '') {

          const novoHistorico: HistoricoLicitacaoModel = {
            identificadorDaLicitacao: this.licitacaoDetalhada.id,
            eventoGerador: evento,
            consideracoes: this.auth.user.cpf + ' ' + this.auth.user.profile,
            versaoDaLicitacao: this.licitacaoDetalhada.versao
          };

          this.dispatch(new SaveNovoEstadoDocumentacao(novoHistorico))
            .subscribe(() => {
            if ( evento === EventoGerador.CANCELAR_EMISSAO_PARECER_ENGENHARIA  ) {

              this.store.select(PropostaState)
                .pipe(this.takeUntilDestroyed())
                .subscribe(proposta => {
                  const versaoProposta = proposta.versaoSelecionada + 1;
                  this.store.dispatch([
                    new LoadProposta(Number(versaoProposta)),
                    new LoadLicitacoesAtivasProposta(versaoProposta),
                    new LoadLicitacoesRejeitadasProposta(versaoProposta)
                  ]);
                  this.alertMessageService.success('Nova versão da documentação criada com sucesso.');
                  this.store.dispatch(new Navigate(['./vrpl/licitacao-lote']));
                });
            }
          });
        }

        this.init();
      }));

    this.modalRef.hide();
  }


  recuperarEvento(estado: number): string {

    let evento = '';
    if (estado === this.CANCELADO) {
      evento = EventoGerador.CANCELAR_EMISSAO_PARECER_ENGENHARIA;
    } else if (estado === this.EMITIDO) {
      evento = EventoGerador.EMITIR_PARECER_ENGENHARIA;
    } else if (estado === this.ASSINADO) {
      evento = EventoGerador.ASSINAR_PARECER_ENGENHARIA;
    }
    return evento;
  }

  cancelaModal() {
    this.modalRef.hide();
  }

  get pathId() {
    const id = this.route.parent.snapshot.paramMap.get('id');
    return id ? Number(id) : null;
  }

  voltar() {
    const url = this.pathId ? '../../../listagem' : '../../listagem';
    this.dispatch(new Navigate([url], {}, { relativeTo: this.route }));
  }

  mostrarBotaoDownload(link: string): boolean {
    if (link === null || link === undefined || link === '') {
      return false;
    }
    return true;
  }
}
