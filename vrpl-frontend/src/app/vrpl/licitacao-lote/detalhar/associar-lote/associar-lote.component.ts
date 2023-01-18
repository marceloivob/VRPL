import { Component } from '@angular/core';
import { Store } from '@ngxs/store';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { LicitacaoModel, FornecedorModel, LoteModel } from 'src/app/model/licitacao/licitacao.state.model';
import { LicitacaoState } from 'src/app/model/licitacao/licitacao.state';
import {
  AssociarLotes,
  LoadLicitacoesAtivasProposta,
  LoadLicitacoesRejeitadasProposta,
  LoadLotesAtivosProposta,
  LoadLotesRejeitadosProposta,
  LoadLicitacaoDetalhada,
  SincronizaLicitacoesProposta
} from 'src/app/model/licitacao/licitacao.actions';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { Navigate } from '@ngxs/router-plugin';
import { ActivatedRoute } from '@angular/router';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { CnpjPipe } from 'src/app/siconv/pipes/cnpj.pipe';
import { CpfPipe } from 'src/app/siconv/pipes/cpf.pipe';
import { PropostaState } from 'src/app/model/proposta/proposta.state';
import { InscricaoGenericaPipe } from 'src/app/siconv/pipes/inscricaogenerica.pipe';
import { SituacaoDaLicitacao } from 'src/app/model/quadroresumo/quadroresumo.state.model';

@Component({
  selector: 'vrpl-associar-lote',
  templateUrl: './associar-lote.component.html'
})
export class AssociarLoteComponent extends BaseComponent {

  licitacoes: LicitacaoModel[];
  licitacaoSelecionada: LicitacaoModel;
  fornecedoresDisponiveis: any;
  fornecedorSelecionado: any;
  lotes: LotesHelper;
  exibeEditarSubmetas = false;

  exibeLicitacaoVazio = false;
  exibeFornecedorVazio = false;

  versaoSelecionadaDaProposta: number;

  podeSelecionarLicitacao = true;

  podeAlterarLicitacao = true;

  labelLotesAssociar = 'Lote(s) a Associar';
  labelLotesAssociados = 'Lote(s) Associado(s)';

  fornecedorObsoletoSelecionado: FornecedorModel;

  constructor(
    protected store: Store,
    private alertMessageService: AlertMessageService,
    private readonly route: ActivatedRoute,
    private readonly cnpjPipe: CnpjPipe,
    private readonly cpfPipe: CpfPipe,
    private readonly inscricaoGenericaPipe: InscricaoGenericaPipe,
    public readonly auth: UserAuthorizerService
  ) {
    super(store);
  }

  init() {

  }

  onLoad() {

    if ((this.route.parent.snapshot.pathFromRoot[4].url[0].path === 'detalhar-licitacao-ativa') && (this.auth.podeEditar())) {
      this.store.select(LicitacaoState.licitacoesAtivasProposta)
        .pipe(this.takeUntilDestroyed())
        .subscribe(licitacoes => {
          this.licitacoes = licitacoes;
          this.initLotes();
          this.initLicitacaoSelecionada();
        });
    } else if ((this.route.parent.snapshot.pathFromRoot[4].url[0].path === 'detalhar-licitacao-rejeitada') && (this.auth.isProponente)) {
      this.store.select(LicitacaoState.licitacoesRejeitadasProposta)
        .pipe(this.takeUntilDestroyed())
        .subscribe(licitacoes => {
          this.licitacoes = licitacoes;
          this.initLotes();
          this.initLicitacaoSelecionada();
        });
    } else {
      this.dispatch(new Navigate(['../licitacao'], {}, { relativeTo: this.route }));
    }
  }

  loadActions() {
    this.store.select(PropostaState)
      .pipe(this.takeUntilDestroyed())
      .subscribe(proposta => {
        this.versaoSelecionadaDaProposta = proposta.versaoSelecionada;
      });

    if (this.route.parent.snapshot.pathFromRoot[4].url[0].path === 'detalhar-licitacao-ativa') {
      if (this.auth.podeEditar()) {
        return [
          new SincronizaLicitacoesProposta(),
          new LoadLotesAtivosProposta(this.versaoSelecionadaDaProposta)
        ];
      } else {
        return [
          new LoadLicitacoesAtivasProposta(this.versaoSelecionadaDaProposta),
          new LoadLotesAtivosProposta(this.versaoSelecionadaDaProposta)
        ];
      }
    } else if (this.route.parent.snapshot.pathFromRoot[4].url[0].path === 'detalhar-licitacao-rejeitada') {
      return [
        new LoadLicitacoesRejeitadasProposta(this.versaoSelecionadaDaProposta),
        new LoadLotesRejeitadosProposta(this.versaoSelecionadaDaProposta)
      ];
    }
  }

  initLicitacaoSelecionada() {
    const id = this.pathId;
    if (id) {
      this.licitacaoSelecionada = this.licitacoes
        .find(licitacao => licitacao.id === id);

      this.podeSelecionarLicitacao = false;
    } else if (this.licitacoes.length === 1) {
      this.licitacaoSelecionada = this.licitacoes[0];
    }

    if (this.licitacaoSelecionada) {
      this.licitacaoSelecionadaChanged(this.licitacaoSelecionada);
    }
  }

  initLotes() {
    this.lotes = new LotesHelper(
      this.store.selectSnapshot(LicitacaoState.lotesProposta)
    );
  }

  licitacaoSelecionadaChanged(licitacao: LicitacaoModel) {

    if (!licitacao) {
      this.podeAlterarLicitacao = false;
      this.fornecedorSelecionado = null;
      this.lotes.updateLotes([]);
      
      return;
    }

    this.fornecedoresDisponiveis = null;
    if (licitacao && licitacao.fornecedores.length === 1) {
      this.fornecedorSelecionado = this.fornecedorLabel(licitacao.fornecedores[0]);
      this.fornecedorSelecionadoChanged(this.fornecedorSelecionado);
    } else if (licitacao && licitacao.fornecedores.length > 1) {
      let temFornecedorObsoleto = false;
      licitacao.fornecedores.forEach(fornecedor => {
        if (fornecedor.obsoleto) {
          this.fornecedorSelecionado = this.fornecedorLabel(fornecedor);
          this.fornecedorSelecionadoChanged(this.fornecedorSelecionado);
          temFornecedorObsoleto = true;
        }
      });
      if (!temFornecedorObsoleto) {
        let guardaFornecedorSelecionado = this.escolheFornecedorParaApresentarNoCombo(licitacao.fornecedores);
        this.fornecedorSelecionado = this.fornecedorLabel(guardaFornecedorSelecionado);

        this.fornecedorSelecionadoChanged(this.fornecedorSelecionado);

        this.lotes.updateLotes(
          this.licitacaoSelecionada.lotes.filter(lote => lote.idFornecedor === this.fornecedorSelecionado.id,)
        );
      }
    } else {
      this.fornecedorSelecionado = null;
      this.lotes.updateLotes([]);
    }

    if (licitacao && licitacao.fornecedores) {
      let temFornecedorNaoObsoleto = false;
      licitacao.fornecedores.forEach(fornecedor => {
        if (!fornecedor.obsoleto) {
          temFornecedorNaoObsoleto = true;
        }
      });

      if (!temFornecedorNaoObsoleto && this.auth.podeEditar()) {
        this.alertMessageService.error('Não existe um fornecedor vencedor cadastrado no módulo SICONV para a Licitação '
          + licitacao.numeroAno + '.');
      }
    }

    this.podeAlterarLicitacao =
      (licitacao.situacaoDaLicitacao === 'COM' ||
        licitacao.situacaoDaLicitacao === 'EPE');
  }


  // Ver RN: 543140: SICONV-DocumentosOrcamentarios-ManterAssociacaodeLotesaLicitacao-RN-FormularioEdicao
  // Se não existir fornecedor obsoleto, deve ser selecionado o primeiro fornecedor que tenha algum lote associado.
  // Caso não tenha nenhum fornecedor com lote associado, selecionar o primeiro fornecedor."
  escolheFornecedorParaApresentarNoCombo(fornecedores: FornecedorModel[]): any {
    if (this.licitacaoSelecionada) {
      if (this.licitacaoSelecionada.lotes.length == 0) {
        return this.selecionaPrimeiroFornecedor(fornecedores);
      } else {
        return this.selecionaPrimeiroFornecedorComLoteAssociado(fornecedores);
      }
    }
  }
  selecionaPrimeiroFornecedorComLoteAssociado(fornecedores: FornecedorModel[]): any {
    return this.licitacaoSelecionada.lotes[0].fornecedor;
  }

  selecionaPrimeiroFornecedor(fornecedores: FornecedorModel[]): any {

    if (fornecedores) {
      return fornecedores[0];
    } else {
      return null;
    }
  }

  fornecedorSelecionadoChanged(fornecedor: FornecedorModel) {

    if (fornecedor && this.fornecedorObsoletoSelecionado && fornecedor.id === this.fornecedorObsoletoSelecionado.id) {
      this.lotes.updateLotes(
        this.licitacaoSelecionada.lotes.filter(lote => lote.idFornecedor === fornecedor.id)
      );
      
      return;
    }

    if (fornecedor) {
      const fornecedorAnteriorEraObsoleto = this.fornecedorObsoletoSelecionado;
      this.fornecedorObsoletoSelecionado = fornecedor;
      if (!fornecedorAnteriorEraObsoleto ||
        (fornecedorAnteriorEraObsoleto.obsoleto && fornecedor.id !== fornecedorAnteriorEraObsoleto.id)) {

        let lotesSelecionados = this.licitacaoSelecionada.lotes.filter(lote => lote.idFornecedor === fornecedor.id);
        this.lotes.selecionados.forEach(lote => {
          lotesSelecionados.push(lote);
        });
        this.lotes.updateLotes(lotesSelecionados);
        return;
      }

      this.lotes.updateLotes(
        this.licitacaoSelecionada.lotes.filter(lote => lote.idFornecedor === fornecedor.id)
      );
    } else {
      this.lotes.updateLotes([]);
    }
  }

  onEditarLotes() {
    this.exibeEditarSubmetas = true;
  }

  onSalvarEdicaoLotes(event: SubmetaAlterada[]) {
    this.exibeEditarSubmetas = false;
    this.lotes.alterarSubmetas(event);
    this.fornecedorSelecionadoChanged(this.fornecedorSelecionado);
  }

  onCancelarEdicaoLotes() {
    this.exibeEditarSubmetas = false;
    this.lotes.alterarSubmetas([]);
    this.fornecedorSelecionadoChanged(this.fornecedorSelecionado);
  }

  salvar() {
    this.exibeLicitacaoVazio = !this.licitacaoSelecionada;
    this.exibeFornecedorVazio = !this.fornecedorSelecionado;

    if (this.exibeLicitacaoVazio || this.exibeFornecedorVazio) {
      this.alertMessageService.error('Por favor, verifique os campos obrigatórios.');
      return;
    }

    const dadosAssociacao = {
      idLicitacao: this.licitacaoSelecionada.id,
      idFornecedor: this.fornecedorSelecionado.id,
      lotes: this.lotes.selecionados.map(lote => {
        return { id: lote.id, numero: lote.numero, versao: lote.versao };
      }),
      submetasAlteradas: this.lotes.submetasAlteradas.map(submeta => {
        return {
          idSubmeta: submeta.idSubmeta,
          versaoSubmeta: submeta.versaoSubmeta,
          idNovoLote: this.lotes.lotePropostaPorNumero(submeta.novoLote).id,
          numeroNovoLote: submeta.novoLote,
          idAntigoLote: this.lotes.lotePropostaPorNumero(submeta.antigoLote).id
        };
      })
    };

    if (dadosAssociacao.lotes && dadosAssociacao.lotes.length === 0) {
      this.alertMessageService.error('Pelo menos um Lote deve ser associado!');
      return;

    } else {
      this.dispatch(new AssociarLotes(dadosAssociacao))
        .subscribe(() => {
          this.alertMessageService.success('Licitação salva com sucesso!');

          this.dispatch(new LoadLicitacaoDetalhada(this.licitacaoSelecionada.id));

          if (!this.pathId) {
            this.dispatch(
              new Navigate(['../../detalhar-licitacao-ativa/', this.licitacaoSelecionada.id], {}, { relativeTo: this.route })
            );
          } else {
            let guardaFornecedorSelecionado = this.fornecedorSelecionado;

            this.store.dispatch(this.loadActions())
              .subscribe(() => {
                this.onLoad();

                this.fornecedorSelecionado = this.fornecedorLabel(guardaFornecedorSelecionado);

                this.fornecedorSelecionadoChanged(this.fornecedorSelecionado);
                this.lotes.updateLotes(
                  this.licitacaoSelecionada.lotes.filter(lote => lote.idFornecedor === this.fornecedorSelecionado.id,)
                );
              });
          }
        });
    }
  }

  voltar() {
    const url = this.pathId ? '../../../listagem' : '../../listagem';
    this.dispatch(new Navigate([url], {}, { relativeTo: this.route }));
  }

  get pathId() {
    const id = this.route.parent.snapshot.paramMap.get('id');
    return id ? Number(id) : null;
  }

  get podeEditar() {

    const usuarioPodeEditar = this.auth.podeEditar();
    const licitacaoPodeSerEditada = this.auth.podeEditarLicitacao();
    const entrouClicandoNoBotaoAssociarLotes = !this.pathId;
    const temLicitacaoSelecionada = this.licitacaoSelecionada;
    const licitacaoSelecionadaTemSituacao = this.licitacaoSelecionada && this.licitacaoSelecionada.situacaoDaLicitacao;
    const licitacaoSelecionadaPreenOuCompl = licitacaoSelecionadaTemSituacao &&
      (this.licitacaoSelecionada.situacaoDaLicitacao === SituacaoDaLicitacao.EM_PREENCHIMENTO ||
        this.licitacaoSelecionada.situacaoDaLicitacao === SituacaoDaLicitacao.EM_COMPLEMENTACAO);

    return usuarioPodeEditar &&
      (licitacaoPodeSerEditada ||
        (entrouClicandoNoBotaoAssociarLotes && !temLicitacaoSelecionada) ||
        (entrouClicandoNoBotaoAssociarLotes && temLicitacaoSelecionada && licitacaoSelecionadaTemSituacao && licitacaoSelecionadaPreenOuCompl)
      );
  }

  get fornecedores() {
    if (!this.fornecedoresDisponiveis && this.licitacaoSelecionada) {
      this.fornecedoresDisponiveis = this.licitacaoSelecionada.fornecedores
        .map(fornecedor => {
          return this.fornecedorLabel(fornecedor);
        });
    }

    if (this.fornecedoresDisponiveis) {
      this.sortFornecedores(this.fornecedoresDisponiveis);
    }

    return this.fornecedoresDisponiveis;
  }

  fornecedorLabel(fornecedor: FornecedorModel) {
    let tipoDaIdentificacao = fornecedor.identificacao.tipo;

    let identificacao = '';

    if (tipoDaIdentificacao === 'CNPJ') {
      identificacao = this.cnpjPipe.transform(fornecedor.identificacao.valor);
    } else if (tipoDaIdentificacao === 'IG') {
      identificacao = this.inscricaoGenericaPipe.transform(fornecedor.identificacao.valor);
    } else {
      tipoDaIdentificacao = 'CPF';
      identificacao = this.cpfPipe.transform(fornecedor.identificacao);
    }

    const fornecedorLabel = {
      ...fornecedor,
      label: `${tipoDaIdentificacao}: ${identificacao} - ${fornecedor.nome}`
    };

    return fornecedorLabel;
  }

  sortFornecedores(fornecedores: any[]) {
    for (let i = 0; i < (fornecedores.length - 1); i += 1) {
      for (let j = i + 1; j < fornecedores.length; j += 1) {
        if (fornecedores[i].label > fornecedores[j].label) {
          const temp = fornecedores[i];
          fornecedores[i] = fornecedores[j];
          fornecedores[j] = temp;
        }
      }
    }
  }
}

class LotesHelper {
  public target: any;
  public source: any;
  public submetasAlteradas: SubmetaAlterada[] = [];
  public selecionados: LoteModel[] = [];
  public lotesProposta: LoteModel[] = [];

  constructor(
    private lotesPropostaOriginal: LoteModel[] = []
  ) {
    this.lotesProposta = this.cloneLotes(lotesPropostaOriginal);
    this.updateLotes([]);
  }

  updateLotesSelecionados(novosLotesSelecionados: LoteModel[]) {
    let lotesSelecionados: LoteModel[] = [];
    novosLotesSelecionados.forEach(lote => {
      if (!this.contem(lotesSelecionados, lote)) {
        lotesSelecionados.push(lote);
      }
    });
    this.selecionados = lotesSelecionados.sort((a, b) => a.numero - b.numero);
  }

  contem(lotes: LoteModel[], lote: LoteModel) {
    let contem = false;
    lotes.forEach(l => {
      if (l.numero === lote.numero) {
        contem = true;
      }
    });
    return contem;
  }

  updateLotes(novosLotesSelecionados: LoteModel[]) {
    this.updateLotesSelecionados(novosLotesSelecionados);
    this.target = novosLotesSelecionados
      .sort((a, b) => a.numero - b.numero)
      .map(lote => {
        return { name: `Lote ${lote.numero}`, data: lote };
      });

    this.source = this.lotesProposta
      .filter(lote => !lote.idLicitacao)
      .filter(lote => !this.selecionados.some(loteSel => lote.id === loteSel.id))
      .filter(lote => lote.submetas.length > 0)
      .sort((a, b) => a.numero - b.numero)
      .map(lote => {
        return { name: `Lote ${lote.numero}`, data: lote };
      });
  }

  alterarSubmetas(novasSubmetasAlteradas: SubmetaAlterada[]) {
    this.lotesProposta = this.cloneLotes(this.lotesPropostaOriginal);
    this.submetasAlteradas = this.submetasAlteradas
      .filter(submeta => {
        return !novasSubmetasAlteradas.some(subAlt => subAlt.idSubmeta === submeta.idSubmeta);
      })
      .concat(novasSubmetasAlteradas);
    this.submetasAlteradas.forEach(submeta => this.alterarSubmeta(submeta));

    this.lotesProposta.forEach(lote => {
      lote.submetas.sort((a, b) => {
        const nomeA = a.submeta.toLowerCase();
        const nomeB = b.submeta.toLowerCase();
        if (nomeA < nomeB) {
          return -1;
        }
        if (nomeA > nomeB) {
          return 1;
        }
        return 0;

      });
    });

    this.updateLotes(
      this.lotesProposta
        .filter(loteP => this.selecionados.some(loteS => loteS.id === loteP.id))
        .filter(loteP => loteP.submetas.length > 0)
    );
  }

  onLotesChanged(event: any[]) {
    this.updateLotesSelecionados(event.map(tLote => tLote.data));
  }

  lotePropostaPorNumero(numeroLote: number) {
    return this.lotesProposta.find(lote => lote.numero === numeroLote);
  }

  lotePropostaPorSubmeta(idSubmeta: number) {
    return this.lotesProposta.find(lote => {
      return lote.submetas.some(submeta => submeta.id === idSubmeta);
    });
  }

  private alterarSubmeta(submetaAlterada: SubmetaAlterada) {
    const loteRemover = this.lotePropostaPorSubmeta(submetaAlterada.idSubmeta);
    let loteAdicionar = this.lotePropostaPorNumero(submetaAlterada.novoLote);

    let iSub = 0;
    let submeta;
    for (; iSub < loteRemover.submetas.length; ++iSub) {
      if (loteRemover.submetas[iSub].id === submetaAlterada.idSubmeta) {
        submeta = loteRemover.submetas[iSub];
        loteRemover.submetas.splice(iSub, 1);
        break;
      }
    }

    if (!loteAdicionar) {
      loteAdicionar = {
        id: null,
        numero: submetaAlterada.novoLote,
        versao: 0,
        submetas: []
      };
      this.lotesProposta = [...this.lotesProposta, loteAdicionar];
    }

    loteAdicionar.submetas = [...loteAdicionar.submetas, submeta];
  }

  private cloneLotes(lotesProposta: LoteModel[]) {
    return lotesProposta.map(lote => {
      const submetas = lote.submetas.map(submeta => {
        return { ...submeta };
      });
      return { ...lote, submetas };
    });
  }
}

interface SubmetaAlterada {
  novoLote: number;
  antigoLote: number;
  idSubmeta: number;
  versaoSubmeta: number;
}

