
import { Component, TemplateRef } from '@angular/core';
import { LaudoState } from 'src/app/model/laudo/laudo.state';
import { Observable, Subscription } from 'rxjs';
import { TemplateLaudoModel, LaudoModel, PendenciaModel, TextoModel, RespostaModel } from 'src/app/model/laudo/laudo.state.model';
import { AnexoModel } from 'src/app/model/anexo/anexo.state.model';
import { LicitacaoDetalhadaModel, LoteModel } from 'src/app/model/licitacao/licitacao.state.model';
import { QciStateModel, SubmetaModel } from 'src/app/model/qci/qci.state.model';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { DataExport } from 'src/app/model/data-export';
import { Select, Store } from '@ngxs/store';
import { CnpjPipe } from 'src/app/siconv/pipes/cnpj.pipe';
import { CpfPipe } from 'src/app/siconv/pipes/cpf.pipe';
import { DatePipe } from '@angular/common';
import { LoadPendencias, LoadPerguntas, LoadLaudo, SaveLaudo, QuemEmitiuLaudo } from 'src/app/model/laudo/laudo.actions';
import { LoadAnexosPorTipo, LoadAnexosLicitacao } from 'src/app/model/anexo/anexo.actions';
import { LoadQciLicitacao } from 'src/app/model/qci/qci.actions';
import { PropostaState } from 'src/app/model/proposta/proposta.state';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { InscricaoGenericaPipe } from 'src/app/siconv/pipes/inscricaogenerica.pipe';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { SituacaoDaLicitacao, HistoricoLicitacaoModel, EventoGerador } from 'src/app/model/quadroresumo/quadroresumo.state.model';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';
import { SaveNovoEstadoDocumentacao } from 'src/app/model/quadroresumo/quadroresumo.actions';
import { LoadProposta } from 'src/app/model/proposta/proposta.actions';
import { LoadLicitacoesAtivasProposta, LoadLicitacoesRejeitadasProposta } from 'src/app/model/licitacao/licitacao.actions';

@Component({
  selector: 'vrpl-vrpls',
  templateUrl: './vrpls.component.html',
  styleUrls: ['./vrpls.component.scss']
})
export class VrplsComponent extends BaseComponent {

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

  habilitarComentariosGrupo2 = false;

  habilitarJustificativa = false;

  // Grid
  exportAnexos: DataExport;
  exportQuadroResumo: DataExport;
  lista: any[];

  @Select(LaudoState.templateLaudo) templateObservable: Observable<TemplateLaudoModel>;
  @Select(LicitacaoDetalhadaState.anexosComponente) anexosParecerObservable: Observable<AnexoModel[]>;
  @Select(LicitacaoDetalhadaState.anexosLicitacaoDetalhada) anexosObservable: Observable<AnexoModel[]>;
  @Select(LaudoState.parecer) laudoObservable: Observable<LaudoModel>;
  @Select(LaudoState.pendencias) pendenciasObservable: Observable<PendenciaModel[]>;

  licitacaoDetalhada: LicitacaoDetalhadaModel;
  qciObservable: Observable<QciStateModel>;
  fornecedores: Map<number, string>;

  submetas: SubmetaModel[];
  laudoId: number;

  novoLaudo: LaudoModel;

  grupo2resposta0: string;
  grupo2resposta1: string;
  comentariosGrupo2: string;
  grupo3resposta0 = new Map<string, TextoModel>();
  grupo3resposta1 = new Map<string, TextoModel>();
  grupo3resposta2 = new Map<string, TextoModel>();
  grupo3resposta3 = new Map<string, TextoModel>();
  grupo3resposta4 = new Map<string, TextoModel>();
  grupo3resposta5 = new Map<string, TextoModel>();
  grupo3resposta6 = new Map<string, TextoModel>();
  contadorTituloLote: Map<number, number> = new Map();
  habilitarComentariosGrupo3 = new Map<string, TextoModel>();
  habilitarGrupo3Pergunta3 = new Map<string, TextoModel>();
  comentariosGrupo3 = new Map<string, TextoModel>();

  justicativaConclusao: string;
  parecerViavel: string;
  naoSeAplicaCronograma = false;
  mensagemSucesso = '';

  quemEmitiuLaudo = '';

  private subscription: Subscription = new Subscription();

  constructor(
    protected readonly store: Store,
    private readonly cnpjPipe: CnpjPipe,
    private readonly cpfPipe: CpfPipe,
    private readonly inscricaoGenericaPipe: InscricaoGenericaPipe,
    private readonly auth: UserAuthorizerService,
    private alertMessageService: AlertMessageService,
    private readonly modalService: BsModalService,
    private route: ActivatedRoute) {
    super(store);
  }

  init() {
    this.grupo2resposta0 = '';
    this.grupo2resposta1 = '';
    this.comentariosGrupo2 = '';
    this.justicativaConclusao = '';
    this.fornecedores = new Map();
    this.comentariosGrupo3.clear();
    this.grupo3resposta0.clear();
    this.grupo3resposta1.clear();
    this.grupo3resposta2.clear();
    this.grupo3resposta3.clear();
    this.grupo3resposta4.clear();
    this.grupo3resposta5.clear();
    this.grupo3resposta6.clear();

    let indice = 1;
    this.licitacaoDetalhada.lotes.forEach(lote => {

      this.contadorTituloLote.set(lote.numero, indice);
      indice++;
      // dummies para as respostas de cada lote serão setados em preencheRespostas(laudo: LaudoModel)
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
      let text = new TextoModel();
      text.texto = '';
      this.comentariosGrupo3.set(lote.numero + '', text);
      text = new TextoModel();
      text.texto = '';
      this.grupo3resposta0.set(lote.numero + '', text);
      text = new TextoModel();
      text.texto = '';
      this.grupo3resposta1.set(lote.numero + '', text);
      text = new TextoModel();
      text.texto = '';
      this.grupo3resposta2.set(lote.numero + '', text);
      text = new TextoModel();
      text.texto = '';
      this.grupo3resposta3.set(lote.numero + '', text);

      text = new TextoModel();

      text.texto = lote.vlTotalLicitadoMenorOuIgualRef ? 'Sim' : 'Não';
      this.grupo3resposta4.set(lote.numero + '', text);

      text = new TextoModel();

      text.texto = lote.precoUnitarioMenorOuIgualRef ? 'Sim' : 'Não';
      this.grupo3resposta5.set(lote.numero + '', text);

      text = new TextoModel();
      text.texto = '';
      this.grupo3resposta6.set(lote.numero + '', text);
      text = new TextoModel();
      text.texto = false;
      this.habilitarComentariosGrupo3.set(lote.numero + '', text);
      text = new TextoModel();
      text.texto = 'Não';
      this.habilitarGrupo3Pergunta3.set(lote.numero + '', text);
    });
  }

  onLoad() {
    this.anexosObservable.subscribe(anexos => {
      this.getExportAnexos(anexos);
    });

    this.subscription.add(this.store.dispatch(new LoadLaudo(this.licitacaoDetalhada.id, 'VRPLS'))
      .subscribe(() => {
        this.subscription.add(this.laudoObservable.subscribe(laudo => {
          this.laudoId = laudo.id;
          this.subscription.add(this.store.dispatch(new LoadPendencias(laudo.id)).subscribe());

          this.definePermissoesDoUsuario(laudo);

          this.preencheRespostas(laudo);
        }));
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

      this.subscription.add(this.store.dispatch(new QuemEmitiuLaudo(this.licitacaoDetalhada.id, 'VRPLS'))
      .subscribe(() => {
        this.store.select(LaudoState.quemEmitiu)
          .subscribe(quemEmitiu => {
            this.quemEmitiuLaudo = quemEmitiu;
          });
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

  destroy() {
    this.subscription.unsubscribe();
  }

  getColor(variavel: string): string {
    if (this[variavel] !== 'Sim') {
      return '#dc3545'; // Vermelho
    } else {
      return '#666666'; // Cinza
    }
  }

  getColorGrupo3(variavel: string, lote: string): string {
    if (this[variavel].get(lote).texto !== 'Sim') {
      return '#dc3545'; // Vermelho
    } else {
      return '#666666'; // Cinza
    }
  }

  getColorTextArea(required: boolean) {
    if (required) {
      return '#dc3545'; // Vermelho #D32F2F'
    } else {
      return '#666666'; // Cinza
    }
  }

  getOrdemDoLote(numeroLote: number): string {
    return this.contadorTituloLote.get(numeroLote) + '';
  }

  loadActions() {
    this.licitacaoDetalhada = this.store.selectSnapshot(LicitacaoDetalhadaState);

    const idLicitacao = this.licitacaoDetalhada.id;
    const idVersaoDaLicitacao = this.licitacaoDetalhada.versao;

    return [new LoadAnexosPorTipo('VRPLS'),
    new LoadPerguntas('VRPLS', idLicitacao, idVersaoDaLicitacao),
    new LoadAnexosLicitacao(),
    new LoadLaudo(this.licitacaoDetalhada.id, 'VRPLS')];
  }
  onAnexosChanged() {
    this.subscription.add(this.store.dispatch(new LoadAnexosPorTipo('VRPLS')).subscribe());
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
    return 'Lote ' + numero;
  }

  referencia(reflote: string): string {
    let retorno = 'Aceito na Análise';
    if (reflote === 'database') {
      retorno = 'Atualizado na Data Base da Licitação';
    }

    return retorno;
  }

  preencheRespostas(laudo: LaudoModel) {
    laudo.respostas.forEach(res => {
      // O processo licitatório, para Trabalho Social, foi específico e ocorreu em separado do das obras?
      if (res.pergunta.grupo.numero === 3 && res.pergunta.numero === 1) {
        this.grupo2resposta0 = res.resposta;
      }

      // O objeto licitado é compatível com o objeto aprovado?
      if (res.pergunta.grupo.numero === 3 && res.pergunta.numero === 2) {
        this.grupo2resposta1 = res.resposta;
      }

      // Comentários
      if (res.pergunta.grupo.numero === 3 && res.pergunta.numero === 3) {
        this.comentariosGrupo2 = res.resposta;
      }

      // A equipe técnica responsável pelo Trabalho Social está compatível com o projeto aprovado?
      if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 1) {
        this.grupo3resposta0.get(res.lote.numero + '').texto = res.resposta;
      }

      // O cronograma físico-financeiro é compatível com o aprovado no Projeto ou TR ?
      if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 2) {
        this.grupo3resposta1.get(res.lote.numero + '').texto = res.resposta;
      }

      // O percentual global das Despesas Indiretas atende aos parâmetros estabelecidos ?
      if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 3) {
        this.grupo3resposta2.get(res.lote.numero + '').texto = res.resposta;
      }

      // Em caso negativo, foi apresentado relatório técnico circunstanciado e aceito ?
      if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 4) {
        this.grupo3resposta3.get(res.lote.numero + '').texto = res.resposta;
      }

      // // O valor total do lote de %s é menor ou igual ao orçamento utilizado para comparação?
      // if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 5) {
      //   this.grupo3resposta4.get(res.lote.numero + '').texto = res.lote.vlTotalLicitadoMenorOuIgualRef ? 'Sim' : 'Não';
      // }

      // // O preço unitário de cada item do orçamento da empresa vencedora da licitação é
      // // menor ou igual ao orçamento utilizado para comparação?
      // if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 6) {
      //   this.grupo3resposta5.get(res.lote.numero + '').texto = res.lote.precoUnitarioMenorOuIgualRef ? 'Sim' : 'Não';
      // }

      // O cronograma está compatível com o orçamento apresentado ?
      if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 7) {
        this.grupo3resposta6.get(res.lote.numero + '').texto = res.resposta;
      }

      // comentarios
      if (res.lote && res.pergunta.grupo.numero === 4 && res.pergunta.numero === 8) {
        this.comentariosGrupo3.get(res.lote.numero + '').texto = res.resposta;
      }

      // viavel?
      if (res.pergunta.grupo.numero === 8 && res.pergunta.numero === 1) {
        this.parecerViavel = res.resposta;
      }

      // justificativa
      if (res.pergunta.grupo.numero === 8 && res.pergunta.numero === 2) {
        this.justicativaConclusao = res.resposta;
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

    const r1: RespostaModel = {
      respostaId: null,
      resposta: this.grupo2resposta0,
      grupo: template.grupos[2].grupoId,
      pergunta: template.grupos[2].perguntas[0],
      laudoFk: laudo.id,
      versao: 0
    };
    this.novoLaudo.respostas.push(r1);

    const r2: RespostaModel = {
      respostaId: null,
      resposta: this.grupo2resposta1,
      grupo: template.grupos[2].grupoId,
      pergunta: template.grupos[2].perguntas[1],
      laudoFk: laudo.id,
      versao: 0
    };
    this.novoLaudo.respostas.push(r2);

    const r3: RespostaModel = {
      respostaId: null,
      resposta: this.comentariosGrupo2,
      grupo: template.grupos[2].grupoId,
      pergunta: template.grupos[2].perguntas[2],
      laudoFk: laudo.id,
      versao: 0
    };
    this.novoLaudo.respostas.push(r3);

    const r4: RespostaModel = {
      respostaId: null,
      resposta: this.parecerViavel,
      grupo: template.grupos[7].grupoId,
      pergunta: template.grupos[7].perguntas[0],
      laudoFk: laudo.id,
      versao: 0
    };
    this.novoLaudo.respostas.push(r4);

    const r5: RespostaModel = {
      respostaId: null,
      resposta: this.justicativaConclusao,
      grupo: template.grupos[7].grupoId,
      pergunta: template.grupos[7].perguntas[1],
      laudoFk: laudo.id,
      versao: 0
    };
    this.novoLaudo.respostas.push(r5);

    this.licitacaoDetalhada.lotes.forEach(lote => {
      const loteCopia = Object.assign({}, lote);
      loteCopia.fornecedor = null;
      loteCopia.submetas = null;
      delete loteCopia.valorAceitoAnalise;
      delete loteCopia.valorVerificadoLicitado;

      const pergunta0grupo3clone = Object.assign({}, template.grupos[3].perguntas[0]);
      pergunta0grupo3clone.loteId = laudo.id;
      const rl0: RespostaModel = {
        respostaId: null,
        resposta: this.grupo3resposta0.get(lote.numero + '').texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta0grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(rl0);

      const pergunta1grupo3clone = Object.assign({}, template.grupos[3].perguntas[1]);
      pergunta1grupo3clone.loteId = laudo.id;
      const rl1: RespostaModel = {
        respostaId: null,
        resposta: this.grupo3resposta1.get(lote.numero + '').texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta1grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(rl1);

      const pergunta2grupo3clone = Object.assign({}, template.grupos[3].perguntas[2]);
      pergunta2grupo3clone.loteId = laudo.id;
      const rl2: RespostaModel = {
        respostaId: null,
        resposta: this.grupo3resposta2.get(lote.numero + '').texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta2grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(rl2);

      const pergunta3grupo3clone = Object.assign({}, template.grupos[3].perguntas[3]);
      pergunta3grupo3clone.loteId = laudo.id;
      const rl3: RespostaModel = {
        respostaId: null,
        resposta: this.grupo3resposta3.get(lote.numero + '').texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta3grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(rl3);

      const pergunta4grupo3clone = Object.assign({}, template.grupos[3].perguntas[4]);
      pergunta4grupo3clone.loteId = laudo.id;

      const rl4: RespostaModel = {
        respostaId: null,
        resposta: this.grupo3resposta4.get(lote.numero + '').texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta4grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(rl4);

      const pergunta5grupo3clone = Object.assign({}, template.grupos[3].perguntas[5]);
      pergunta5grupo3clone.loteId = laudo.id;

      const rl5: RespostaModel = {
        respostaId: null,
        resposta: this.grupo3resposta5.get(lote.numero + '').texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta5grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(rl5);

      const pergunta6grupo3clone = Object.assign({}, template.grupos[3].perguntas[6]);
      pergunta6grupo3clone.loteId = laudo.id;
      const rl6: RespostaModel = {
        respostaId: null,
        resposta: this.grupo3resposta6.get(lote.numero + '').texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta6grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(rl6);

      const pergunta7grupo3clone = Object.assign({}, template.grupos[3].perguntas[7]);
      pergunta7grupo3clone.loteId = laudo.id;
      const rl7: RespostaModel = {
        respostaId: null,
        resposta: this.comentariosGrupo3.get(lote.numero + '').texto,
        grupo: template.grupos[3].grupoId,
        pergunta: pergunta7grupo3clone,
        laudoFk: laudo.id,
        lote: loteCopia,
        versao: 0
      };
      this.novoLaudo.respostas.push(rl7);
    });

    this.modalRef = this.modalService.show(templateModal);

  }

  salvarRascunho(template: TemplateLaudoModel, laudo: LaudoModel, templateModal: TemplateRef<any>) {
    this.msgModal = 'Tem certeza que deseja salvar rascunho para este Parecer?';
    this.mensagemSucesso = 'Alteração do Parecer Técnico de Trabalho Social realizada com sucesso.';
    this.salvar(template, laudo, this.RASCUNHO, templateModal);
  }

  emitir(template: TemplateLaudoModel, laudo: LaudoModel, templateModal: TemplateRef<any>) {
      this.msgModal = 'Após a emissão do Parecer Técnico de Trabalho Social,' +
        ' só será possível solicitar complementação dos dados' +
        ' se houver o cancelamento da emissão do Parecer Técnico de Trabalho Social. ' +
        ' Tem certeza que deseja prosseguir com a emissão do Parecer Técnico de Trabalho Social?';
      this.mensagemSucesso = 'Parecer Técnico de Trabalho Social emitido com sucesso!';
      this.salvar(template, laudo, this.EMITIDO, templateModal);
  }

  cancelarEmissao(template: TemplateLaudoModel, laudo: LaudoModel, templateModal: TemplateRef<any>) {
    this.msgModal = 'Tem certeza que deseja cancelar a emissão para este Parecer?';
    this.mensagemSucesso = 'Cancelamento de emissão do Parecer Técnico de Trabalho Social realizado com sucesso!';
    this.salvar(template, laudo, this.CANCELADO, templateModal);
  }

  assinar(template: TemplateLaudoModel, laudo: LaudoModel, templateModal: TemplateRef<any>) {
    if (!this.validarParecerSocial()) {
      this.msgModal = 'Tem certeza que deseja assinar este Parecer?';
      this.mensagemSucesso = 'Parecer Técnico de Trabalho Social assinado com Sucesso!';
      this.salvar(template, laudo, this.ASSINADO, templateModal);
    }
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
            if ( evento === EventoGerador.CANCELAR_EMISSAO_PARECER_SOCIAL  ) {

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
      evento = EventoGerador.CANCELAR_EMISSAO_PARECER_SOCIAL;
    } else if (estado === this.EMITIDO) {
      evento = EventoGerador.EMITIR_PARECER_SOCIAL;
    } else if (estado === this.ASSINADO) {
      evento = EventoGerador.ASSINAR_PARECER_SOCIAL;
    }
    return evento;
  }

  cancelaModal() {
    this.modalRef.hide();
  }

  onChangeGrupo3Pergunta2($event: any, variavel: string, numeroLote: string) {
    this.grupo3resposta3.get(numeroLote).texto = '';
  }

  verificarObrigatoriedadeComentarioGrupo3(numeroLote: string): boolean {
    if (this.grupo3resposta0.get(numeroLote).texto === 'Não' ||
      this.grupo3resposta1.get(numeroLote).texto === 'Não' ||
      this.grupo3resposta3.get(numeroLote).texto === 'Não' ||
      this.grupo3resposta6.get(numeroLote).texto === 'Não') {
      return (this.comentariosGrupo3.get(numeroLote).texto === ''
        || this.comentariosGrupo3.get(numeroLote).texto === undefined
        || this.comentariosGrupo3.get(numeroLote).texto === null) ? true : false;
    }

    return false;
  }

  verificarAusenciaRespostaGrupo3(numeroLote: string): boolean {
    if (this.grupo3resposta0.get(numeroLote).texto === ''
     || this.grupo3resposta0.get(numeroLote).texto === null
     || this.grupo3resposta0.get(numeroLote).texto === undefined ||

      this.grupo3resposta1.get(numeroLote).texto === ''
    || this.grupo3resposta1.get(numeroLote).texto === null
    || this.grupo3resposta1.get(numeroLote).texto === undefined ||
    // resposta condicionante
      (  this.grupo3resposta2.get(numeroLote).texto === 'Não'
      && (this.grupo3resposta3.get(numeroLote).texto === ''
      || this.grupo3resposta3.get(numeroLote).texto === null
      || this.grupo3resposta3.get(numeroLote).texto === undefined)) ||

        this.grupo3resposta6.get(numeroLote).texto === ''
      || this.grupo3resposta6.get(numeroLote).texto === null ||
         this.grupo3resposta6.get(numeroLote).texto === undefined ) {

      return true;
    }
    return false;
  }

  verificarObrigatoriedadeComentarioGrupo2() {
    return (this.grupo2resposta0 === 'Não' || this.grupo2resposta1 === 'Não')
        && (this.comentariosGrupo2 === ''
            || this.comentariosGrupo2 === undefined
            || this.comentariosGrupo2 === null);
  }

  validarParecerSocial(): boolean {
    this.alertMessageService.dismissAll();
    let erro = false;

    // respostas lotes
    this.licitacaoDetalhada.lotes.forEach(lote => {

      if (this.verificarAusenciaRespostaGrupo3(lote.numero + '')) {
        this.alertMessageService.error('Respostas são obrigatórias na seção Dados Gerais do Lote ' + lote.numero + '.');
        erro = true;
      }

      if (this.verificarObrigatoriedadeComentarioGrupo3(lote.numero + '')) {
        this.alertMessageService.error('Comentários são obrigatórios no Lote ' + lote.numero + '!');
        erro = true;
      }
      if ((!lote.vlTotalLicitadoMenorOuIgualRef || !lote.precoUnitarioMenorOuIgualRef) &&
      (this.comentariosGrupo3.get(lote.numero + '').texto === '' || this.comentariosGrupo3.get(lote.numero + '').texto === undefined) ) {
         this.alertMessageService.error('Comentários do Orcamento são obrigatórios no Lote ' + lote.numero + '!');
         erro = true;
     }
    });

    // respostas gerais
    if (this.verificarObrigatoriedadeComentarioGrupo2()) {
      this.alertMessageService.error('Comentários são obrigatórios na seção "Aspectos Gerais da Licitação"!');
      erro = true;
    }

    if (this.grupo2resposta0 === '' || this.grupo2resposta0 === null || this.grupo2resposta0 === undefined ||
        this.grupo2resposta1 === '' || this.grupo2resposta1 === null || this.grupo2resposta1 === undefined) {
          this.alertMessageService.error('Respostas são obrigatórias na seção Aspectos Gerais da Licitação.');
    }

    // respostas conclusao
    if (this.parecerViavel === '' ||
               this.parecerViavel === null ||
               this.parecerViavel === undefined) {
        this.alertMessageService.error('O campo "Considera-se a verificação do resultado do processo licitatório" deve ser preenchido!');
      erro = true;
    }

    if ((this.parecerViavel === 'Inviável') && (this.justicativaConclusao === null ||  this.justicativaConclusao === '') ) {
      this.alertMessageService.error('Justificativa é Obrigatória!');
      erro = true;
    }
    return erro;
  }


  verificarNecessidadeMudancaMensagemModalNaEmissao(): boolean {
    let necessario = false;

    // nao, com comentarios
    this.licitacaoDetalhada.lotes.forEach(lote => {
      necessario = necessario ||
      ((this.grupo3resposta0.get(lote.numero + '').texto === 'Não' ||
      this.grupo3resposta1.get(lote.numero + '').texto === 'Não' ||
      this.grupo3resposta3.get(lote.numero + '').texto === 'Não' ||
      this.grupo3resposta6.get(lote.numero + '').texto === 'Não' ||
      !lote.vlTotalLicitadoMenorOuIgualRef || !lote.precoUnitarioMenorOuIgualRef)
        && this.comentariosGrupo3.get(lote.numero + '').texto !== ''
        && this.comentariosGrupo3.get(lote.numero + '').texto !== undefined
        && this.comentariosGrupo3.get(lote.numero + '').texto !== null);
    });

    necessario = necessario || ((this.grupo2resposta0 === 'Não' || this.grupo2resposta1 === 'Não')
      && this.comentariosGrupo2 !== '' && this.comentariosGrupo2 !== null && this.comentariosGrupo2 !== undefined);

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


  podeCancelarEmissao(): boolean {
    return this.temPermissaoParaCancelar;
  }

  podeAssinar(): boolean {
    return this.temPermissaoParaAssinar;
  }

  calcularCaracteresRestantesGrupo2() {
    const textoDoTextArea = this.comentariosGrupo2;

    const caracteresRestantesJustificativa = this.MAX_CONSIDERACOES -
      (textoDoTextArea ? textoDoTextArea.length : 0);

    return caracteresRestantesJustificativa;

  }

  calcularCaracteresRestantesPorLote(lotenumero: string): number {
    const textoDoTextArea = this.comentariosGrupo3.get(lotenumero).texto;

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

  verificarJustificativaRequired() {
    return this.parecerViavel === 'Inviável' && this.justicativaConclusao === '';
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
