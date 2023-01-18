import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngxs/store';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { LoadLicitacaoDetalhada } from 'src/app/model/licitacao/licitacao.actions';
import { LicitacaoDetalhadaModel } from 'src/app/model/licitacao/licitacao.state.model';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { SituacaoDaLicitacao } from 'src/app/model/quadroresumo/quadroresumo.state.model';

@Component({
  selector: 'vrpl-detalhar',
  templateUrl: './detalhar.component.html',
  styleUrls: ['./detalhar.component.scss']
})
export class DetalharComponent extends BaseComponent {

  licitacaoDetalhada: LicitacaoDetalhadaModel;

  constructor(
    private route: ActivatedRoute,
    store: Store,
    private readonly auth: UserAuthorizerService
  ) {
    super(store);
  }

  init() {
    this.store.select(LicitacaoDetalhadaState)
      .pipe(this.takeUntilDestroyed())
      .subscribe( licitacao => this.licitacaoDetalhada = licitacao);
  }

  loadActions() {
    const id = this.pathId;
    return new LoadLicitacaoDetalhada(id);
  }

  get pathId() {
    const id = this.route.snapshot.paramMap.get('id');
    return id ? Number(id) : null;
  }

  get licitacaoDetalhadaCarregada() {
    // considerando alguns valores da licitação pois objeto vazio é o padrão
    return this.licitacaoDetalhada
      && this.licitacaoDetalhada.id
      && this.licitacaoDetalhada.lotes.length > 0;
  }

  get podeEditar() {
    return this.auth.podeEditarLicitacao();
  }

  get exibirAbaAssociacao() {
    return this.auth.podeEditar();
  }

  // 567920: SICONV-DocumentosOrcamentarios-PareceresVRPL-RN-PoliticaEdicaoVisualizacaoPareceres
  private get exibeParecer(): boolean {
    const deveExibirParecer = this.licitacaoDetalhada.existeParecer;

    return deveExibirParecer;
  }

  get exibeParecerDeEngenharia(): boolean {
    const parecerEhEngenharia = !this.licitacaoDetalhada.inSocial;

    return this.exibeParecer && parecerEhEngenharia;
  }

  get exibeParecerSocial(): boolean {
    const parecerEhSocial = this.licitacaoDetalhada.inSocial;

    return this.exibeParecer && parecerEhSocial;
  }

}
