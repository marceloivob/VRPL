import { Injectable, OnDestroy } from '@angular/core';
import { Store } from '@ngxs/store';
import { UserStateModel, PapelDoUsuarioModel } from './user.state.model';
import { UserState } from './user.state';
import { Subject, Observable, of } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { LicitacaoDetalhadaModel } from '../licitacao/licitacao.state.model';
import { LicitacaoDetalhadaState } from '../licitacao/licitacao.detalhada.state';
import { SituacaoDaLicitacao } from '../quadroresumo/quadroresumo.state.model';
import { PropostaState } from '../proposta/proposta.state';

@Injectable({
  providedIn: 'root'
})
export class UserAuthorizerService implements OnDestroy {

  private user$: UserStateModel;
  private versaoAtual: boolean;

  private unsubscribe$ = new Subject();
  private licitacaoDetalhada$: LicitacaoDetalhadaModel;

  private modalidade : Number;
  private termoCompromissoTemMandatar : boolean;

  constructor(store: Store) {
    this.user$ = store.selectSnapshot(UserState);

    store.select(LicitacaoDetalhadaState)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(licitacao => this.licitacaoDetalhada$ = licitacao);

    store.select(UserState)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(user => this.user$ = user);

    store.select(PropostaState)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(proposta => {
        if (proposta) {
          this.modalidade = proposta.modalidade;
          this.termoCompromissoTemMandatar = proposta.termoCompromissoTemMandatar;
        }
        this.versaoAtual = proposta && proposta.versaoAtual;
      });

  }

  get user() {
    return this.user$;
  }

  get isMandataria() {
    return this.user$ && this.user$.profile === 'MANDATARIA' && this.naoEhAdministrador();
  }

  get isProponente() {
    return this.user$ && this.user$.profile === 'PROPONENTE';
  }

  get isConcedente() {
    return this.user$ && this.user$.profile === 'CONCEDENTE' && this.naoEhAdministrador();
  }

  naoEhAdministrador(): boolean{
    return !(this.user$ && this.user$.roles && 
    this.user$.roles.length === 1 && 
    this.user$.roles.includes('ADMINISTRADOR_SISTEMA_ORGAO_EXTERNO'));
  }

  podeEditar(): boolean {
    return this.isProponente && this.versaoAtual;
  }

  podeEditarLicitacao(): boolean {
    const documentoEstaEmPreenchimento = this.licitacaoDetalhada$ &&
      this.licitacaoDetalhada$.situacaoDaLicitacao === SituacaoDaLicitacao.EM_PREENCHIMENTO;
    const documentoEstaEmComplementacao = this.licitacaoDetalhada$ &&
      this.licitacaoDetalhada$.situacaoDaLicitacao === SituacaoDaLicitacao.EM_COMPLEMENTACAO;

    let resultado: boolean = this.podeEditar();
    resultado = resultado && this.licitacaoDetalhada$ && this.licitacaoDetalhada$.versaoAtual;
    resultado = resultado && (documentoEstaEmPreenchimento || documentoEstaEmComplementacao);

    return resultado;
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  recuperarPapelDoUsuario(role: string) {
    const papel: PapelDoUsuarioModel = {
      label: 'Desconhecido',
      numero: '?'
    };

    switch (role) {
      case 'ANALISTA_TECNICO_CONCEDENTE': {
        papel.label = 'Analista Técnico do Concedente';
        papel.numero = '01';
        break;
      }
      case 'ANALISTA_TECNICO_INSTITUICAO_MANDATARIA': {
        papel.label = 'Analista Técnico da Instituição Mandatária';
        papel.numero = '02';
        break;
      }
      case 'FISCAL_CONCEDENTE': {
        papel.label = 'Fiscal do Concedente';
        papel.numero = '03';
        break;
      }
      case 'GESTOR_CONVENIO_CONCEDENTE': {
        papel.label = 'Gestor de Convênio do Concedente';
        papel.numero = '04';
        break;
      }
      case 'GESTOR_CONVENIO_INSTITUICAO_MANDATARIA': {
        papel.label = 'Gestor de Convênio da Instituição Mandatária';
        papel.numero = '05';
        break;
      }
      case 'GESTOR_FINANCEIRO_CONCEDENTE': {
        papel.label = 'Gestor Financeiro do Concedente';
        papel.numero = '06';
        break;
      }
      case 'GESTOR_FINANCEIRO_INSTITUICAO_MANDATARIA': {
        papel.label = 'Gestor Financeiro da Instituição Mandatária';
        papel.numero = '07';
        break;
      }
      case 'OPERACIONAL_FINANCEIRO_CONCEDENTE': {
        papel.label = 'Operacional Financeiro do Concedente';
        papel.numero = '08';
        break;
      }
      case 'OPERACIONAL_FINANCEIRO_INSTITUICAO_MANDATARIA': {
        papel.label = 'Operacional Financeiro da Instituição Mandatária';
        papel.numero = '09';
        break;
      }

    }

    return papel;
  }

  public isContratoRepasse(): boolean {
    return this.modalidade === 2;
  }

  public isConvenio(): boolean {
    return this.modalidade === 1;
  }

  public isConvenioEContratoRepasse(): boolean {
    return this.modalidade === 6;
  }

  public isTermoCompromissoMandataria(): boolean {
    return this.modalidade === 11 && this.termoCompromissoTemMandatar;
  }

  public isTermoCompromissoConcedente(): boolean {
    return this.modalidade === 11 && !this.termoCompromissoTemMandatar;
  }
}
