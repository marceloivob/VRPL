import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { Select } from '@ngxs/store';
import { PropostaState } from '../../model/proposta/proposta.state';
import { PropostaStateModel } from '../../model/proposta/proposta.state.model';

@Component({
  selector: 'vrpl-dados-basicos',
  templateUrl: './dados-basicos.component.html'
})
export class DadosBasicosComponent implements OnInit, OnDestroy {

  @Select(PropostaState) propostaState$: Observable<PropostaStateModel>;

  private stateSubscription: Subscription;

  proposta: PropostaStateModel;

  constructor() { }

  ngOnInit() {
    this.stateSubscription =
      this.propostaState$.subscribe((proposta) => this.proposta = proposta);
  }

  // Convênio == 1
  public isPropostaDoTipoConvenio(): boolean {
    const CODIGO_CONVENIO = 1;

    return this.proposta.modalidade === CODIGO_CONVENIO;
  }

  // Contrato de Repasse == 2
  public isPropostaDoTipoContratoDeRepasse(): boolean {
    const CODIGO_CONTRATO_DE_REPASSE = 2;

    return this.proposta.modalidade === CODIGO_CONTRATO_DE_REPASSE;
  }

  public isPropostaDoTipoTermoDeCompromisso(): boolean {
    const CODIGO_TERMO_DE_COMPROMISSO = 11;

    return this.proposta.modalidade === CODIGO_TERMO_DE_COMPROMISSO;
  }

  public isPropostaDoTipoTermoDeCompromissoComMandataria(): boolean {
    return this.isPropostaDoTipoTermoDeCompromisso() && this.proposta.termoCompromissoTemMandatar;
  }

  // Conforme RN: isExibeModalidade
  // Campo Mandatária só será considerado quando a modalidade da proposta for "Contrato de Repasse" ou "Termo de Compromisso com Mandatária"
  public isExibeModalidade(): boolean {
    return this.isPropostaDoTipoContratoDeRepasse() || this.isPropostaDoTipoTermoDeCompromissoComMandataria();
  }

  /*
  * Conforme RN: 509600 - SICONV-DocumentosOrcamentarios-ManterDadosBasicos-RN-DadosBasicos
  *
  * A informação existe no SICONV apenas quando, ou o convênio, ou o contrato de repasse, ou o termo de compromisso for celebrado.
  * */
  public isDeveExibirNumero(): boolean {
    let deveExibir: boolean = this.proposta.numeroConvenio != null;
    deveExibir = deveExibir && this.proposta.anoConvenio != null;
    deveExibir = deveExibir && this.proposta.dataAssinaturaConvenio != null;

    return deveExibir;
  }
  
  ngOnDestroy() {
    this.stateSubscription.unsubscribe();
  }

}
